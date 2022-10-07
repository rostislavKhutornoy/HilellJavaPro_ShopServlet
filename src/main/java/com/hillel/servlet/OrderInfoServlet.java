package com.hillel.servlet;

import com.hillel.entity.Order;
import com.hillel.service.ShopService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.tags.shaded.org.apache.xpath.operations.Or;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@WebServlet("/orderInfo")
public class OrderInfoServlet extends HttpServlet {
    private final ShopService shopService;

    public OrderInfoServlet() {
        this.shopService = new ShopService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/orderInfo.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Order> list = List.of(shopService.orderInfo(Integer.parseInt(req.getParameter("orderNumber"))));
        req.setAttribute("orders", list);
        req.getRequestDispatcher("/jsp/orderInfo.jsp").forward(req, resp);
    }
}
