package com.example.StockStick.CustomAcessFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UserPlanAccessFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
           String uri=request.getRequestURI();
           if("/app/UserPlan".equals(uri) && request.getSession().getAttribute("registered")==null)
           {
               response.sendRedirect("/app/Register");
           }
           filterChain.doFilter(request,response);
    }
}
