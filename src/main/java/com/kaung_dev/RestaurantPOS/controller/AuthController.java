package com.kaung_dev.RestaurantPOS.controller;

import com.kaung_dev.RestaurantPOS.request.LoginRequest;
import com.kaung_dev.RestaurantPOS.response.ApiResponse;
import com.kaung_dev.RestaurantPOS.response.JwtResponse;
import com.kaung_dev.RestaurantPOS.security.jwt.JwtUtils;
import com.kaung_dev.RestaurantPOS.security.user.CustomUserDetails;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    @PostMapping(path = "/sign-in")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {

        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getName(),
                            request.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateToken(authentication);

            CustomUserDetails useDetails = (CustomUserDetails) authentication.getPrincipal();

            ResponseCookie cookie = ResponseCookie.from("token", jwt)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(Duration.ofDays(1))
                    .sameSite("None")
                    .secure(true)
                    .domain("localhost")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            return new ResponseEntity<>(
                    ApiResponse.builder().message("Success").build(),
                    HttpStatus.OK
            );
        } catch (AuthenticationException e) {

            return new ResponseEntity<>(
                    ApiResponse.builder().message("Invalid email or password").build(),
                    HttpStatus.UNAUTHORIZED
            );
        } catch (Exception e) {

            return new ResponseEntity<>(
                    ApiResponse.builder().message("Invalid email or password").build(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping(path = "/sign-out")
    public ResponseEntity<ApiResponse> logout(HttpServletResponse response) {
        try {
            ResponseCookie cookie = ResponseCookie.from("token", "")
                    .httpOnly(true)
                    .path("/")
                    .maxAge(0)
                    .sameSite("None")
                    .secure(true)
                    .domain("localhost")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            return new ResponseEntity<>(
                    ApiResponse.builder().message("Logout successfully").build(),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ApiResponse.builder().message(e.getMessage()).build(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

}

