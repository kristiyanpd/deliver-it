package com.team9.deliverit.controllers.mvc;

import com.team9.deliverit.controllers.utils.AuthenticationHelper;
import com.team9.deliverit.exceptions.AuthenticationFailureException;
import com.team9.deliverit.models.User;
import com.team9.deliverit.services.contracts.UserService;
import com.team9.deliverit.services.contracts.WarehouseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/panel")
public class PanelMvcController {

    private final UserService userService;
    private final WarehouseService warehouseService;
    private final AuthenticationHelper authenticationHelper;

    public PanelMvcController(UserService userService, WarehouseService warehouseService, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.warehouseService = warehouseService;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("currentUser")
    public String currentUser(HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
        return String.format("%s %s", user.getFirstName(), user.getLastName());
    }

    @ModelAttribute("isEmployee")
    public boolean isEmployee(HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return false;
        }
        return user.isEmployee();
    }

    @ModelAttribute("customersCount")
    public String customersCount() {
        return String.valueOf(userService.countCustomers());
    }

    @GetMapping
    public String showPanelPage(HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
            return "panel";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
    }

}
