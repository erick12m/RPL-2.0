package com.example.rpl.RPL.service;

import com.example.rpl.RPL.exception.EntityAlreadyExistsException;
import com.example.rpl.RPL.exception.NotFoundException;
import com.example.rpl.RPL.model.User;
import com.example.rpl.RPL.repository.UserRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates a new User.
     *
     * @return a new saved User
     * @throws EntityAlreadyExistsException if email or username exists ValidationException declared
     * on the User class
     */
    @Transactional
    public User createUser(String name, String surname, String studentId, String username,
        String email, String password, String university, String degree) {
        User user = new User(name, surname, studentId, username, email,
            passwordEncoder.encode(password), university, degree);

        if (userRepository.existsByEmail(email)) {
            throw new EntityAlreadyExistsException(String.format("Email '%s' already used", email),
                "ERROR_EMAIL_USED");
        }

        if (userRepository.existsByUsername(username)) {
            throw new EntityAlreadyExistsException(
                String.format("Username '%s' already used", username), "ERROR_USERNAME_USED");
        }

        log.info("[process:create_user][username:{}] Creating new user", username);

        //TODO: Send confirmation e-mail

        return userRepository.save(user);
    }

    @Transactional
    public User getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException(String.format("User with id '%d' does not exist", userId));
        }

        return user.get();
    }
}
