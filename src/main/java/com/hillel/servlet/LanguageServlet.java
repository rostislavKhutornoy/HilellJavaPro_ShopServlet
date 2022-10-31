package com.hillel.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.jsp.jstl.core.Config;

import java.io.IOException;

@WebServlet("/language")
public class LanguageServlet extends HttpServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String localeToSet = request.getParameter("locale");

        HttpSession session = request.getSession();
        Config.set(session, "jakarta.servlet.jsp.jstl.fmt.locale", localeToSet);

        response.sendRedirect("/HilellJavaPro_ShopServlet_war_exploded");
    }
}
