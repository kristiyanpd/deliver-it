package com.team9.deliverit.controllers.mvc;


import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;

import static com.team9.deliverit.config.ApplicationConstants.CURRENT_USER_SESSION_KEY;

public abstract class BaseAuthenticationController {

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute(CURRENT_USER_SESSION_KEY) != null;
    }

}
