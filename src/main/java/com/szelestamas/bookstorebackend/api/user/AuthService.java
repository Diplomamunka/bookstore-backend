package com.szelestamas.bookstorebackend.api.user;

import com.szelestamas.bookstorebackend.api.user.domain.User;
import com.szelestamas.bookstorebackend.api.user.persistence.UserEntity;
import com.szelestamas.bookstorebackend.api.user.persistence.UserRepository;
import com.szelestamas.bookstorebackend.core.ResourceAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public User signUp(User data) {
        if (userRepository.findByLogin(data.login()).isPresent())
            throw new ResourceAlreadyExistsException("User already exists: " + data.login());
        UserEntity user = userRepository.save(UserEntity.of(data));
        return user.toUser();
    }
}
