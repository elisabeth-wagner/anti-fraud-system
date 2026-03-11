package com.example.AntiFraudSystemApplication.service;

import com.example.AntiFraudSystemApplication.model.User;
import com.example.AntiFraudSystemApplication.model.UserAdapter;
import com.example.AntiFraudSystemApplication.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));
        return new UserAdapter(user);
    }
}
