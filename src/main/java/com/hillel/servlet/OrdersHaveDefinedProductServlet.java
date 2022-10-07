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

@WebServlet("/ordersHaveDefinedProduct")
public class OrdersHaveDefinedProductServlet extends HttpServlet {
    private final ShopService shopService;

    public OrdersHaveDefinedProductServlet() {
        this.shopService = new ShopService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/ordersHaveDefinedProduct.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Order> list = shopService.ordersHaveDefinedProduct(req.getParameter("productName"));
        req.setAttribute("orders", list);
        req.getRequestDispatcher("/jsp/ordersHaveDefinedProduct.jsp").forward(req, resp);
    }
}
