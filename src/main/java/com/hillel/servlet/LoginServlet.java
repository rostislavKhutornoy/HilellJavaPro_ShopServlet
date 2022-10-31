package com.hillel.servlet;

import com.hillel.entity.User;
import com.hillel.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserService userService;

    public LoginServlet() {
        userService = new UserService();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("login");
        String password = request.getParameter("password");

        Optional<User> found = userService.findByNameAndPassword(name, password);

        if (found.isEmpty()) {
            request.setAttribute("errorMessage", true);
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        } else {
            request.getSession().setAttribute("user", found.get());
            response.sendRedirect("/HilellJavaPro_ShopServlet_war_exploded");
        }
    }
}
