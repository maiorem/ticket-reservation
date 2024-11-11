package com.hhplus.io.common.support.api.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class TokenAuthenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader("token");
        if (token == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            log.info("token is missing");
            return;
        }
        log.info("token :: {}", token);
        filterChain.doFilter(request, response);
    }
}
