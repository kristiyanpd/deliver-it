package com.team9.deliverit.controllers.mvc;

import com.team9.deliverit.controllers.utils.AuthenticationHelper;
import com.team9.deliverit.exceptions.AuthenticationFailureException;
import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.models.City;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.dtos.LoginDto;
import com.team9.deliverit.models.dtos.RegisterDto;
import com.team9.deliverit.services.contracts.CityService;
import com.team9.deliverit.services.contracts.UserService;
import com.team9.deliverit.services.mappers.UserModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.List;

import static com.team9.deliverit.config.ApplicationConstants.CURRENT_USER_SESSION_KEY;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserModelMapper mapper;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final CityService cityService;

    @Autowired
    public AuthenticationController(UserModelMapper mapper, UserService userService, AuthenticationHelper authenticationHelper, CityService cityService) {
        this.mapper = mapper;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.cityService = cityService;
    }

    @ModelAttribute("cities")
    public List<City> populateCities() {
        return cityService.getAll();
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("login", new LoginDto());
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") LoginDto dto,
                              BindingResult bindingResult,
                              HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {
            authenticationHelper.verifyAuthentication(dto.getEmail(), dto.getPassword());
            session.setAttribute(CURRENT_USER_SESSION_KEY, dto.getEmail());
            return "redirect:/";
        } catch (AuthenticationFailureException e) {
            bindingResult.rejectValue("email", "auth_error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.removeAttribute(CURRENT_USER_SESSION_KEY);
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("register", new RegisterDto());
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register") RegisterDto registerDto,
                                 BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        if (!registerDto.getPassword().equals(registerDto.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "password_error", "Password confirmation should match password!");
            return "register";
        }

        try {
            User user = mapper.fromDto(registerDto);
            userService.create(user);
            session.setAttribute(CURRENT_USER_SESSION_KEY, user.getEmail());
            return "redirect:/";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("email", "username_error", e.getMessage());
            return "register";
        } catch (AuthenticationFailureException e) {
            bindingResult.rejectValue("email", "auth_error", e.getMessage());
            return "login";
        }
    }
}
