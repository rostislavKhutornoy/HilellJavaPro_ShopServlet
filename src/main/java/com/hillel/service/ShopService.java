package com.hillel.service;

import com.hillel.entity.Order;
import com.hillel.repository.OrderRepository;

import java.util.List;

public class ShopService {
    private final OrderRepository orderRepository;

    public ShopService() {
        this.orderRepository = new OrderRepository();
    }

    public Order orderInfo(int orderNumber) {
        return orderRepository.getOrderByNumber(orderNumber);
    }

    public List<Order> ordersSumNotExceedingAndDifferentProducts(double maxTotalCost,
                                                                    int quantityDifferentProducts) {
        return orderNumbersToOrder(orderRepository.orderNumbersSumNotExceedingAndDifferentProducts(maxTotalCost,
                quantityDifferentProducts));
    }

    public List<Order> ordersHaveDefinedProduct(String productName) {
        return orderNumbersToOrder(orderRepository.orderNumbersHaveDefinedProduct(productName));
    }

    public List<Order> ordersHaventDefinedProductIncomingToday(String productName) {
        return orderNumbersToOrder(orderRepository.orderNumbersHaventDefinedProductIncomingToday(productName));
    }

    public List<Order> newOrderFromOrderedToday() {
        return orderNumbersToOrder(orderRepository.newOrderFromOrderedToday());
    }

    public boolean deleteOrderWithDefinedProduct(String productName, int amount) {
        return orderRepository.deleteOrderWithDefinedProduct(productName, amount);
    }

    private List<Order> orderNumbersToOrder(List<Integer> orderNumbers) {
        return orderNumbers.stream().map(orderRepository::getOrderByNumber).toList();
    }
}
