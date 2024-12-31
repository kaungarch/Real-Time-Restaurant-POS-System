package com.kaung_dev.RestaurantPOS.security.jwt;

import com.kaung_dev.RestaurantPOS.security.user.CustomUserDetailService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailService customUserDetailService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = extractJwtFromCookie(request);
            if (StringUtils.hasText(jwt) && jwtUtils.validateToken(jwt)) {
                String username = jwtUtils.getUsernameFromToken(jwt);
                UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
//                logger.info("🪲 user details " + userDetails );
                var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                logger.info("🚀 role and authority " + auth.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (JwtException e) {
//            response.getWriter().write(e.getMessage() + ". Invalid Token or Expired Token.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception e) {
            logger.error("❌ error " + e.getMessage());
            throw e;
        }
        filterChain.doFilter(request, response);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    private String extractJwtFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        logger.info("url: " + request.getRequestURL());
        logger.info("method: " + request.getMethod());
        logger.info("origin: " + request.getHeader("Origin"));
        logger.info("🍪🍪🍪Cookies: " + cookies);

        String cookieHeader = request.getHeader("cookie");
        logger.info("Cookie header value: " + cookieHeader);

        request.getHeaderNames().asIterator().forEachRemaining(headerName -> {
            logger.info("🚨" + headerName + ": " + request.getHeader(headerName));
        });

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    logger.info("🍪Cookies: " + cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
