package com.team9.deliverit.controllers.utils;

import com.team9.deliverit.exceptions.AuthenticationFailureException;
import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.User;
import com.team9.deliverit.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

import static com.team9.deliverit.config.ApplicationConstants.CURRENT_USER_SESSION_KEY;

@Component
public class AuthenticationHelper {

    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    public static final String AUTHENTICATION_FAILURE_MESSAGE = "Wrong username or password.";

    private final UserService userService;

    @Autowired
    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }

    public User tryGetUser(HttpHeaders headers) {
        if (!headers.containsKey(AUTHORIZATION_HEADER_NAME)) {
            throw new UnauthorizedOperationException("The requested resource requires authentication!");
        }
        String email = headers.getFirst(AUTHORIZATION_HEADER_NAME);
        return userService.getByEmail(email);
    }

    public User tryGetUser(HttpSession session) {
        if (session.getAttribute(CURRENT_USER_SESSION_KEY) == null) {
            throw new AuthenticationFailureException("No user logged in!");
        }

        try {
            String currentUser = (String) session.getAttribute("currentUser");
            return userService.getByEmail(currentUser);
        } catch (EntityNotFoundException e) {
            throw new AuthenticationFailureException("No such user.");
        }
    }

    public User verifyAuthentication(String email, String password) {
        try {
            User user = userService.getByEmail(email);
            if (!user.getPassword().equals(password)) {
                throw new AuthenticationFailureException(AUTHENTICATION_FAILURE_MESSAGE);
            }
            return user;
        } catch (EntityNotFoundException e) {
            throw new AuthenticationFailureException(AUTHENTICATION_FAILURE_MESSAGE);

        }
    }
}
