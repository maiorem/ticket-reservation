package com.hhplus.io.common.support.api.interceptor;

import com.hhplus.io.common.support.utils.TokenValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class TokenAuthorInterceptor implements HandlerInterceptor {

    private final TokenValidator validator;

    public TokenAuthorInterceptor(TokenValidator validator) {
        this.validator = validator;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (token == null) {
            log.warn("token is missing");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        if(!isValidToken(token)){
            log.warn("invalid token");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        if (isExpiredToken(token)) {
            log.warn("expired token");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        request.setAttribute("token", token);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private boolean isValidToken(String token){
        return validator.validate(token);
    }

    private boolean isExpiredToken(String token){
        return validator.isExpired(token);
    }
}
