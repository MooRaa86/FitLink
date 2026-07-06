package com.project.FitLink.filters;

import com.project.FitLink.service.jwtService;
import com.project.FitLink.utils.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtTokenValidatorFilter extends OncePerRequestFilter {

    private final jwtService jwtService;

    /**
     * This filter is used to validate the JWT token in the request header.
     * If the token is invalid or missing, the filter will return a 401 Unauthorized response.
     * Otherwise, the filter will pass the request to the next filter in the chain.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(Constants.JWT_HEADER);
        if(token == null || token.isBlank() || !token.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = token.substring(7);
        try {
            jwtService.validateTokenForFilter(accessToken);
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("""
                    {
                      "error": "Invalid JWT token"
                    }
                    """);
            return;
        }

        filterChain.doFilter(request, response);

    }
}
