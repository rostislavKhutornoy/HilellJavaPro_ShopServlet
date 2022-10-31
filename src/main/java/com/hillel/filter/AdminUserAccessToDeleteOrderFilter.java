package com.hillel.filter;

import com.hillel.entity.User;
import com.hillel.entity.UserRole;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

import static java.util.Objects.nonNull;

@WebFilter("/deleteOrderWithDefinedProduct")
public class AdminUserAccessToDeleteOrderFilter extends HttpFilter implements Filter {
    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (METHOD_GET.equals(httpRequest.getMethod())) {
            chain.doFilter(request, response);
        } else if (METHOD_POST.equals(httpRequest.getMethod())) {
            User user = (User) httpRequest.getSession().getAttribute("user");
            if (nonNull(user) && UserRole.ADMIN == user.getUserRole()) {
                chain.doFilter(request, response);
            } else {
                httpRequest.setAttribute("errorMessage", true);
                httpRequest.getRequestDispatcher("/jsp/deleteOrderWithDefinedProduct.jsp")
                        .forward(httpRequest, response);
            }
        }
    }
}
