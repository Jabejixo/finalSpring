package com.example.finalproject.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class SessionExpirationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            long lastAccessedTime = session.getLastAccessedTime();
            long currentTime = System.currentTimeMillis();
            long idleTime = currentTime - lastAccessedTime;

            if (idleTime > 10 * 60 * 1000) {
                session.invalidate();
                response.sendRedirect("/login?timeout");
                return;
            }

            long creationTime = session.getCreationTime();
            if (currentTime - creationTime > 60 * 60 * 1000) {
                session.invalidate();
                response.sendRedirect("/login?session-expired");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
