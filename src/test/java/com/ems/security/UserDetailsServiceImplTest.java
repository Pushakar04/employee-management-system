package com.ems.security;

import com.ems.entity.Role;
import com.ems.entity.AppUser;
import com.ems.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsername_shouldReturnUserDetailsWithCorrectAuthority_whenUserExists() {
    	AppUser mockUser = AppUser
    			.builder()
                .id(1L)
                .username("admin1")
                .password("admin@123")
                .role(Role.ADMIN)
                .build();

        when(userRepository.findByUsername("admin1")).thenReturn(Optional.of(mockUser));

        UserDetails result = userDetailsService.loadUserByUsername("admin1");

        assertEquals("admin1", result.getUsername());
        assertEquals("admin@123", result.getPassword());
        assertTrue(result.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenUserDoesNotExist() {
    	
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
        		() -> userDetailsService.loadUserByUsername("ghost"));
    }
}