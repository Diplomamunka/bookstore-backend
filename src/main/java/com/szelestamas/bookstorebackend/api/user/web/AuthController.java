package com.szelestamas.bookstorebackend.api.user.web;

import com.szelestamas.bookstorebackend.api.book.web.BookResource;
import com.szelestamas.bookstorebackend.api.user.AuthService;
import com.szelestamas.bookstorebackend.api.user.UserRole;
import com.szelestamas.bookstorebackend.api.user.domain.User;
import com.szelestamas.bookstorebackend.api.user.persistence.UserEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @RequestMapping(value = "/auth/login", method = { RequestMethod.GET, RequestMethod.POST })
    @PostAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<UserResource> login(@AuthenticationPrincipal UserEntity authenticatedUser) {
        if (!(authenticatedUser == null)) {
            User user = authenticatedUser.toUser();
            return ResponseEntity.ok(UserResource.of(user));
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostAuthorize("returnObject.body.login() == authentication.name")
    public ResponseEntity<UserResource> getUser(@AuthenticationPrincipal UserEntity authenticatedPrincipal) {
        User user = authenticatedPrincipal.toUser();
        return ResponseEntity.ok(UserResource.of(user));
    }

    @GetMapping("/profile/bookmarks")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<BookResource>> getBookmarks(@AuthenticationPrincipal UserEntity authenticatedPrincipal) {
        User user = authenticatedPrincipal.toUser();
        return ResponseEntity.ok(authService.getBookmarks(user.login()).stream().map(BookResource::of).toList());
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResource>> getAllUsers() {
        return ResponseEntity.ok(authService.getAllUsers().stream().map(UserResource::of).toList());
    }

    @GetMapping("/users/{login}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResource> getUser(@PathVariable String login) {
        return ResponseEntity.ok(UserResource.of(((UserEntity)authService.loadUserByUsername(login)).toUser()));
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<UserResource> signUp(@RequestBody @Valid SignUpDto signUpDto) {
        User user;
        try {
            user = ((UserEntity)authService.loadUserByUsername(signUpDto.login())).toUser();
            if (user.firstName().equals(signUpDto.firstName()) && user.lastName().equals(signUpDto.lastName()) && user.password() == null) {
                user = authService.updateUser(user.login(), signUpDto.convertFromUser(user));
            }
        } catch (UsernameNotFoundException ex) {
            user = authService.signUp(signUpDto.convertWithRole(UserRole.CUSTOMER));
        }
        return ResponseEntity.ok(UserResource.of(user));
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResource> addUser(@RequestBody @Valid SignUpDto signUpDto) {
        User user = authService.signUp(signUpDto.convertTo());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResource.of(user));
    }

    @PutMapping("/profile/update")
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostAuthorize("returnObject.body.login() == authentication.name")
    public ResponseEntity<UserResource> updateUser(@RequestBody @Valid SignUpDto signUpDto, @AuthenticationPrincipal UserEntity authenticatedPrincipal) {
        User user = authenticatedPrincipal.toUser();
        User newUser = signUpDto.convertFromUser(user);
        return ResponseEntity.ok(UserResource.of(authService.updateUser(user.login(), newUser)));
    }

    @PutMapping("/users/{login}")
    @PreAuthorize("hasRole('ADMIN')")
    @PostAuthorize("returnObject.body.login() == #l and #l == #u.login()")
    public ResponseEntity<UserResource> updateUser(@PathVariable @P("l") String login, @RequestBody @Valid @P("u") SignUpDto user) {
        return ResponseEntity.ok(UserResource.of(authService.updateUser(login, user.convertTo())));
    }

    @DeleteMapping("/users/{login}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        authService.deleteUser(login);
        return ResponseEntity.noContent().build();
    }
}
