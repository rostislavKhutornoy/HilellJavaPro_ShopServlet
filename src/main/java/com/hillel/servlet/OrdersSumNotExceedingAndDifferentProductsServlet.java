package com.hillel.servlet;

import com.hillel.entity.Order;
import com.hillel.service.ShopService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/ordersSumNotExceedingAndDifferentProducts")
public class OrdersSumNotExceedingAndDifferentProductsServlet extends HttpServlet {
    private final ShopService shopService;

    public OrdersSumNotExceedingAndDifferentProductsServlet() {
        this.shopService = new ShopService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/ordersSumNotExceedingAndDifferentProducts.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Order> list = shopService.ordersSumNotExceedingAndDifferentProducts(
                Double.parseDouble(req.getParameter("sum")),
                Integer.parseInt(req.getParameter("differentProducts")));
        req.setAttribute("orders", list);
        req.getRequestDispatcher("/jsp/ordersSumNotExceedingAndDifferentProducts.jsp").forward(req, resp);
    }
}
