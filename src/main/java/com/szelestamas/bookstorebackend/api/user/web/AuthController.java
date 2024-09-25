package com.szelestamas.bookstorebackend.api.user.web;

import com.szelestamas.bookstorebackend.api.user.AuthService;
import com.szelestamas.bookstorebackend.api.user.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<UserResource> signUp(@RequestBody @Valid SignUpDto signUpDto) {
        User user = authService.signUp(signUpDto.convertTo());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /*@PostMapping("/signin")
    public ResponseEntity<UserResource> signIn(@RequestHeader(name = "Authorization") String authorization) {
        UsernamePasswordAuthenticationToken userNamePassword = new UsernamePasswordAuthenticationToken(signInDto.login(), signInDto.password());
        Authentication authUser = authenticationManager.authenticate(authorization);
        if (authUser.isAuthenticated()) {
            User user = (User)authUser.getPrincipal();
            return ResponseEntity.ok(UserResource.of(user));
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }*/
}
