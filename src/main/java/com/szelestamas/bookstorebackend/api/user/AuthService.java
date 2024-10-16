package com.szelestamas.bookstorebackend.api.user;

import com.szelestamas.bookstorebackend.api.author.domain.Author;
import com.szelestamas.bookstorebackend.api.book.domain.Book;
import com.szelestamas.bookstorebackend.api.book.persistence.BookEntity;
import com.szelestamas.bookstorebackend.api.category.domain.Category;
import com.szelestamas.bookstorebackend.api.user.domain.User;
import com.szelestamas.bookstorebackend.api.user.persistence.UserEntity;
import com.szelestamas.bookstorebackend.api.user.persistence.UserRepository;
import com.szelestamas.bookstorebackend.core.NotEnoughAuthorization;
import com.szelestamas.bookstorebackend.core.ResourceAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    public List<User> getAllUsers() {
        return userRepository.findAll().stream().map(UserEntity::toUser).toList();
    }

    public void deleteUser(String login) {
        Optional<UserEntity> user = userRepository.findById(login);
        if (user.isPresent() && user.get().getUserRole() == UserRole.ADMIN)
            throw new NotEnoughAuthorization("Delete admin" + user.get().getFirstName());
        userRepository.deleteById(login);
    }

    public User updateUser(String login, User user) {
        UserEntity userEntity = userRepository.findById(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + login));
        userEntity.setPassword(user.password());
        userEntity.setFirstName(user.firstName());
        userEntity.setLastName(user.lastName());
        userEntity.setUserRole(user.role());
        return userRepository.save(userEntity).toUser();
    }

    public List<Book> getBookmarks(String login) {
        UserEntity user = userRepository.findById(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + login));
        return user.getBookmarks().stream().map(BookEntity::toBook).toList();
    }

    public List<Book> addBookmark(String login, Book book, Category category, List<Author> authors) {
        UserEntity user = userRepository.findById(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + login));
        Set<BookEntity> bookmarks = user.getBookmarks();
        bookmarks.add(BookEntity.of(book, category, authors));
        user.setBookmarks(bookmarks);
        userRepository.save(user);
        return bookmarks.stream().map(BookEntity::toBook).toList();
    }

    public void deleteBookmark(String login, long id) {
        UserEntity user = userRepository.findById(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + login));
        BookEntity bookToDelete = user.getBookmarks().stream().filter(book -> book.getId() == id).findFirst().get();
        Set<BookEntity> bookmarks = user.getBookmarks();
        bookmarks.remove(bookToDelete);
        user.setBookmarks(bookmarks);
        userRepository.save(user);
    }
}
