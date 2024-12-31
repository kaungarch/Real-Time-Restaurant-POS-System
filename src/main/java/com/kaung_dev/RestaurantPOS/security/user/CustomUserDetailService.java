package com.kaung_dev.RestaurantPOS.security.user;

import com.kaung_dev.RestaurantPOS.domain.Staff;
import com.kaung_dev.RestaurantPOS.exception.ResourceNotFoundException;
import com.kaung_dev.RestaurantPOS.repository.StaffRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final StaffRepository staffRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("🔽 trying to search user with name " + username);
        Staff user = Optional.ofNullable(staffRepository.findByName(username)).orElseThrow(() -> new ResourceNotFoundException("User not found."));

        return CustomUserDetails.buildUserDetails(user);
    }
}
