package com.kaung_dev.RestaurantPOS.security.user;

import com.kaung_dev.RestaurantPOS.domain.Staff;
import com.kaung_dev.RestaurantPOS.service.StaffService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomUserDetails implements UserDetails {

    private Long id;
    private String name;
    private String password;

    private Collection<GrantedAuthority> authorities;

    public static CustomUserDetails buildUserDetails(Staff user) {

        List<GrantedAuthority> authority = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().toString())).collect(Collectors.toList());

        return CustomUserDetails.builder()
                .id(user.getId())
                .name(user.getName())
                .password(user.getPassword())
                .authorities(authority)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }
}