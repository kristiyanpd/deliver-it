package com.team9.deliverit.controllers.mvc;

import com.team9.deliverit.controllers.utils.AuthenticationHelper;
import com.team9.deliverit.exceptions.AuthenticationFailureException;
import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.City;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.dtos.UserDto;
import com.team9.deliverit.services.contracts.CityService;
import com.team9.deliverit.services.contracts.UserService;
import com.team9.deliverit.services.mappers.UserModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class ProfileMvcController {

    private final UserService service;
    private final UserModelMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;
    private final CityService cityService;

    public ProfileMvcController(UserService service, UserModelMapper modelMapper, AuthenticationHelper authenticationHelper, CityService cityService) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
        this.cityService = cityService;
    }

    @ModelAttribute("cities")
    public List<City> populateCities() {
        return cityService.getAll();
    }

    @ModelAttribute("currentLoggedUser")
    public String testFunction(HttpSession session, Model model) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            model.addAttribute("currentLoggedUser", user);
            return "";
        } catch (AuthenticationFailureException e) {
            return "";
        }
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

    @GetMapping("/panel/account-profile")
    public String showUserProfile(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            UserDto userDto = modelMapper.toUserDto(user);
            model.addAttribute("user", userDto);
            model.addAttribute("userId", user.getId());
            return "account-profile";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/panel/account-profile")
    public String updateProfile(@Valid @ModelAttribute("user") UserDto userDto,
                                BindingResult errors, HttpSession session) {
        if (errors.hasErrors()) {
            return "account-profile";
        }

        try {
            User userExecuting = authenticationHelper.tryGetUser(session);
            User user = modelMapper.fromUserDto(userExecuting.getId(), userDto);
            service.update(userExecuting, user, user.getId());
            return "account-profile";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("email", "duplicate_email", e.getMessage());
            return "account-profile";
        } catch (UnauthorizedOperationException e) {
            return "error";
        }
    }
}