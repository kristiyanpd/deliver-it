package com.team9.deliverit.controllers.mvc;

import com.team9.deliverit.services.contracts.UserService;
import com.team9.deliverit.services.contracts.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

import static com.team9.deliverit.config.ApplicationConstants.CURRENT_USER_SESSION_KEY;

@Controller
@RequestMapping("/")
public class HomeMvcController extends BaseAuthenticationController {

    private final UserService service;

    @Autowired
    public HomeMvcController(UserService service) {
        this.service = service;
    }

    @ModelAttribute("customersCount")
    public String customersCount() {
        return String.valueOf(service.countCustomers());
    }

    @GetMapping
    public String showHomePage() {
        return "index";
    }

}
