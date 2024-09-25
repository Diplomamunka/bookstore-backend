package com.szelestamas.bookstorebackend.api.user.persistence;

import com.szelestamas.bookstorebackend.api.user.UserRole;
import com.szelestamas.bookstorebackend.api.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Table(name = "users")
public class UserEntity implements UserDetails {
    @Id
    private String login;

    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole userRole;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return switch (userRole) {
            case ADMIN ->
                    List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_STAFF"), new SimpleGrantedAuthority("ROLE_CUSTOMER"));
            case STAFF ->
                    List.of(new SimpleGrantedAuthority("ROLE_STAFF"), new SimpleGrantedAuthority("ROLE_CUSTOMER"));
            case CUSTOMER -> List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        };
    }

    @Override
    public String getUsername() {
        return login;
    }

    public static UserEntity of(User user) {
        return new UserEntity(user.login(), user.password(), user.firstName(), user.lastName(), user.role());
    }

    public User toUser() {
        return new User(login, password, firstName, lastName, userRole);
    }
}
