package com.kaung_dev.RestaurantPOS.security;

import com.kaung_dev.RestaurantPOS.security.jwt.AuthTokenFilter;
import com.kaung_dev.RestaurantPOS.security.jwt.JwtAuthEntryPoint;
import com.kaung_dev.RestaurantPOS.security.jwt.JwtUtils;
import com.kaung_dev.RestaurantPOS.security.user.CustomUserDetailService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebMvc
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;
    private final JwtUtils jwtUtils;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    private final List<String> MANAGER_URLS = List.of("/orders/**", "/staffs/**", "/tables/**", "/menu-items/**");
    private final List<String> WAITER_URLS = List.of("/orders/**", "/tables/**", "/menu-items/**");
    private final List<String> COOK_URLS = List.of("/orders/**", "/tables/**", "/menu-items");

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter(jwtUtils, customUserDetailService);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService(customUserDetailService);
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/**").permitAll() // this is for test
                                .requestMatchers("/auth/**", "/role/**").permitAll()
//                                .requestMatchers(this.COOK_URLS.toArray(new String[0])).hasAnyAuthority("COOK",
//                                        "MANAGER", "WAITER")
                                .requestMatchers(this.WAITER_URLS.toArray(new String[0])).hasAnyAuthority("WAITER",
                                        "COOK",
                                        "MANAGER")
                                .requestMatchers(this.MANAGER_URLS.toArray(new String[0])).hasAnyAuthority("MANAGER")
                                .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

}
