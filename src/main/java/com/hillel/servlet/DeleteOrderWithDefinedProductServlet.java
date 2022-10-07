package com.hillel.servlet;

import com.hillel.service.ShopService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/deleteOrderWithDefinedProduct")
public class DeleteOrderWithDefinedProductServlet extends HttpServlet {
    private final ShopService shopService;

    public DeleteOrderWithDefinedProductServlet() {
        this.shopService = new ShopService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/deleteOrderWithDefinedProduct.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        shopService.deleteOrderWithDefinedProduct(req.getParameter("productName"),
                Integer.parseInt(req.getParameter("amount")));
        req.setAttribute("message", "Successful delete");
        req.getRequestDispatcher("/jsp/deleteOrderWithDefinedProduct.jsp").forward(req, resp);
    }
}
