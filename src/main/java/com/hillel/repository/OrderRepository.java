package com.hillel.repository;

import com.hillel.connection.ConnectionProvider;
import com.hillel.entity.ItemOnOrder;
import com.hillel.entity.Order;
import com.hillel.entity.Product;
import com.hillel.interfaces.ConnectionConsumer;
import com.hillel.interfaces.ConnectionFunction;
import com.hillel.service.ShopService;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hillel.repository.BaseRepository.closeConnection;
import static java.util.Objects.nonNull;

public class OrderRepository {
    private final static String SUM_NOT_EXCEEDING_AND_DIFFERENT_PRODUCTS = """
            SELECT item_on_order.order_number, SUM(cost * amount) AS sum_cost,
            COUNT(DISTINCT item_on_order.product_id) AS product_count
            FROM shop.order
            JOIN item_on_order ON shop.order.order_number = item_on_order.order_number
            JOIN product ON product_id = product.id
            GROUP BY order_number
            HAVING sum_cost <= ? AND product_count = ?
            """;

    private final static String HAVE_DEFINED_PRODUCT = """
            SELECT DISTINCT order_number FROM item_on_order
            JOIN product ON item_on_order.product_id = product.id
            WHERE product.name = ?
            """;

    private final static String HAVENT_DEFINED_PRODUCT_INCOMING_TODAY = """
            SELECT item_on_order.order_number FROM item_on_order
            JOIN shop.order ON item_on_order.order_number = shop.order.order_number
            WHERE item_on_order.order_number NOT IN (SELECT order_number FROM item_on_order
            JOIN product ON item_on_order.product_id = product.id
            WHERE name = ?)
            AND receipt_date BETWEEN Addtime(Curdate(), '00:00:00') AND Addtime(Curdate(), '23:59:59')
            """;

    private final static String DELETE_ORDER_WITH_DEFINED_PRODUCT = """
            SELECT DISTINCT shop.order.order_number FROM shop.order
            JOIN item_on_order ON shop.order.order_number = item_on_order.order_number
            JOIN product ON item_on_order.product_id = product.id
            WHERE product.name = ?
            AND item_on_order.amount = ?
            """;

    private final String DELETE_FROM_ITEMS_ON_ORDER = """
            DELETE FROM shop.item_on_order WHERE order_number = ?
            """;
    private final String DELETE_FROM_ORDER = """
            DELETE FROM shop.order WHERE order_number = ?
            """;

    private final static String GET_ORDER_BY_NUMBER = """
            SELECT o.id, o.order_number, o.receipt_date,
            i.order_number, i.product_id, i.amount,
            p.id, p.name, p.description, p.cost
            FROM shop.order o LEFT JOIN item_on_order i ON o.order_number = i.order_number
            LEFT JOIN product p ON i.product_id = p.id WHERE i.order_number = ?
            """;

    private final static String ORDERS_ORDERED_TODAY = """
            SELECT DISTINCT item_on_order.order_number FROM shop.item_on_order
            JOIN shop.order where item_on_order.order_number = shop.order.order_number AND
            receipt_date = curdate();                                       
            """;

    private final static String INSERT_INTO_ITEMS_ON_ORDER = """
            INSERT INTO shop.item_on_order VALUES(?, ?, ?);
            """;

    private final static String INSERT_INTO_ORDER = """
            INSERT INTO shop.order VALUES(?, ?, ?);
            """;

    public Order getOrderByNumber(int orderNumber) {
        Connection connection = ConnectionProvider.provideConnection();
        if (nonNull(connection)) {
            try (PreparedStatement statement = connection.prepareStatement(GET_ORDER_BY_NUMBER,
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY)) {
                statement.setInt(1, orderNumber);
                ResultSet resultSet = statement.executeQuery();
                List<ItemOnOrder> itemsOnOrder = new ArrayList<>();

                while (resultSet.next()) {
                    itemsOnOrder.add(new ItemOnOrder(resultSet.getInt(2),
                            new Product(resultSet.getInt("product_id"),
                                    resultSet.getString("name"),
                                    resultSet.getString("description"),
                                    resultSet.getDouble("cost")),
                            resultSet.getInt("amount")));
                }
                resultSet.previous();
                return new Order(resultSet.getInt(1),
                        resultSet.getInt("order_number"),
                        itemsOnOrder,
                        resultSet.getDate("receipt_date").toLocalDate());
            } catch (SQLException exception) {
                exception.printStackTrace();
            } finally {
                closeConnection(connection);
            }
        }
        return null;
    }

    public List<Integer> orderNumbersSumNotExceedingAndDifferentProducts(double maxTotalCost,
                                                                         int quantityDifferentProducts) {
        return getListOrderNumbersDataBaseQuery(SUM_NOT_EXCEEDING_AND_DIFFERENT_PRODUCTS,
                (preparedStatement, resultSet) -> {
                    preparedStatement.setDouble(1, maxTotalCost);
                    preparedStatement.setInt(2, quantityDifferentProducts);
                    resultSet = preparedStatement.executeQuery();
                    List<Integer> result = new ArrayList<>();
                    while (resultSet.next()) {
                        result.add(resultSet.getInt("order_number"));
                    }
                    return result;
                });
    }

    public List<Integer> orderNumbersHaveDefinedProduct(String productName) {
        return getListOrderNumbersByProductNamePattern(productName, HAVE_DEFINED_PRODUCT);
    }

    public List<Integer> orderNumbersHaventDefinedProductIncomingToday(String productName) {
        return getListOrderNumbersByProductNamePattern(productName, HAVENT_DEFINED_PRODUCT_INCOMING_TODAY);
    }

    public List<Integer> newOrderFromOrderedToday() {
        List<Order> todayOrders = orderNumbersToOrder(getTodayOrders());
        Map<Integer, Integer> orderList = new HashMap<>();
        int newOrderNumber = 0;
        for (Order order : todayOrders) {
            if (order.getOrderNumber() > newOrderNumber) {
                newOrderNumber = order.getOrderNumber();
            }
            for (ItemOnOrder itemOnOrder : order.getItemsOnOrder()) {
                if (orderList.containsKey(itemOnOrder.getProduct().getId())) {
                    Integer id = itemOnOrder.getProduct().getId();
                    Integer oldAmount = orderList.get(id);
                    Integer amount = itemOnOrder.getAmount();
                    orderList.put(id, oldAmount + amount);
                } else {
                    orderList.put(itemOnOrder.getProduct().getId(),
                            itemOnOrder.getAmount());
                }
            }
        }
        int finalNewOrderNumber = newOrderNumber + 1;
        for (Map.Entry<Integer, Integer> entry : orderList.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            dataBaseUpdateQuery(INSERT_INTO_ITEMS_ON_ORDER, preparedStatement -> {
                preparedStatement.setInt(1, finalNewOrderNumber);
                preparedStatement.setInt(2, key);
                preparedStatement.setInt(3, value);
            });
        }
        dataBaseUpdateQuery(INSERT_INTO_ORDER, preparedStatement -> {
            preparedStatement.setInt(1, finalNewOrderNumber);
            preparedStatement.setInt(2, finalNewOrderNumber);
            preparedStatement.setDate(3, Date.valueOf(LocalDate.now()));
        });
        return List.of(finalNewOrderNumber);
    }

    public boolean deleteOrderWithDefinedProduct(String productName, int amount) {
        List<Integer> ordersNumberToDelete = getListOrderNumbersDataBaseQuery(DELETE_ORDER_WITH_DEFINED_PRODUCT,
                (preparedStatement, resultSet) -> {
                    preparedStatement.setString(1, productName);
                    preparedStatement.setInt(2, amount);
                    resultSet = preparedStatement.executeQuery();
                    List<Integer> result = new ArrayList<>();
                    while (resultSet.next()) {
                        result.add(resultSet.getInt("order_number"));
                    }
                    return result;
                });
        for (Integer orderNumbers : ordersNumberToDelete) {
            dataBaseUpdateQuery(DELETE_FROM_ITEMS_ON_ORDER, preparedStatement -> {
                preparedStatement.setInt(1, orderNumbers);
            });
            dataBaseUpdateQuery(DELETE_FROM_ORDER, preparedStatement -> {
                preparedStatement.setInt(1, orderNumbers);
            });
        }
        return ordersNumberToDelete.size() != 0;
    }

    public List<Integer> getTodayOrders() {
        return getListOrderNumbersDataBaseQuery(ORDERS_ORDERED_TODAY,
                (preparedStatement, resultSet) -> {
                    resultSet = preparedStatement.executeQuery();
                    List<Integer> result = new ArrayList<>();
                    while (resultSet.next()) {
                        result.add(resultSet.getInt("order_number"));
                    }
                    return result;
                });
    }

    private List<Integer> getListOrderNumbersDataBaseQuery(String statement,
                                                           ConnectionFunction<List<Integer>> connectionFunction) {
        Connection connection = ConnectionProvider.provideConnection();
        if (nonNull(connection)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
                ResultSet resultSet = null;
                return connectionFunction.apply(preparedStatement, resultSet);
            } catch (SQLException exception) {
                exception.printStackTrace();
            } finally {
                closeConnection(connection);
            }
        }
        return List.of();
    }

    private List<Integer> getListOrderNumbersByProductNamePattern(String productName, String statement) {
        return getListOrderNumbersDataBaseQuery(statement,
                (preparedStatement, resultSet) -> {
                    preparedStatement.setString(1, productName);
                    resultSet = preparedStatement.executeQuery();
                    List<Integer> result = new ArrayList<>();
                    while (resultSet.next()) {
                        result.add(resultSet.getInt("order_number"));
                    }
                    return result;
                });
    }

    private void dataBaseUpdateQuery(String statement, ConnectionConsumer connectionConsumer) {
        Connection connection = ConnectionProvider.provideConnection();
        if (nonNull(connection)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
                connectionConsumer.accept(preparedStatement);
                preparedStatement.executeUpdate();
            } catch (SQLException exception) {
                exception.printStackTrace();
            } finally {
                closeConnection(connection);
            }
        }
    }

    private List<Order> orderNumbersToOrder(List<Integer> orderNumbers) {
        return orderNumbers.stream().map(this::getOrderByNumber).toList();
    }
}
