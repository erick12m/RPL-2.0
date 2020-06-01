package com.example.rpl.RPL.service

import com.example.rpl.RPL.exception.EntityAlreadyExistsException
import com.example.rpl.RPL.model.User
import com.example.rpl.RPL.repository.UserRepository
import com.example.rpl.RPL.repository.ValidationTokenRepository
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification
import spock.lang.Unroll

class AuthenticationServiceSpec extends Specification {

    private AuthenticationService authenticationService
    private UserRepository userRepository
    private PasswordEncoder passwordEncoder
    private ValidationTokenRepository validationTokenRepository
    private EmailService emailService

    def setup() {
        userRepository = Mock(UserRepository)
        passwordEncoder = Mock(PasswordEncoder)
        validationTokenRepository = Mock(ValidationTokenRepository)
        emailService = Mock(EmailService)
        authenticationService = new AuthenticationService(userRepository, validationTokenRepository, emailService, passwordEncoder)
    }

    void "should create user successfully"() {
        given:
            String password = "1234"
            String email = "asd@gmail.com"
            String username = "alepox"

        when:
            User newUser = authenticationService.createUser("Ale", "Levinas", "95719", username, email, password, "UBA", "Ing. Informatica")

        then:
            1 * passwordEncoder.encode(password) >> password
            1 * userRepository.existsByEmail(email) >> false
            1 * userRepository.existsByUsername(username) >> false

            1 * userRepository.save(_ as User) >> { User user -> return user }

            assert newUser.username == username
            assert newUser.email == email
            assert newUser.password == password
    }

    @Unroll
    void "should fail to create user if #property exists"() {
        given:
            String password = "1234"
            String email = "asd@gmail.com"
            String username = "alepox"

        when:
            User newUser = authenticationService.createUser("Ale", "Levinas", "95719", username, email, password, "UBA", "Ing. Informatica")

        then:
            1 * passwordEncoder.encode(password) >> password
            userRepository.existsByEmail(email) >> emailExists
            userRepository.existsByUsername(username) >> usernameExists


            thrown(EntityAlreadyExistsException)

        where:
            property   | usernameExists | emailExists
            "username" | true           | false
            "email"    | false          | true
    }
}
