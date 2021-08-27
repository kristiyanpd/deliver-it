package com.team9.deliverit.controllers.mvc;

import com.team9.deliverit.controllers.utils.AuthenticationHelper;
import com.team9.deliverit.exceptions.AuthenticationFailureException;
import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.City;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.dtos.RegisterDto;
import com.team9.deliverit.services.contracts.CityService;
import com.team9.deliverit.services.contracts.UserService;
import com.team9.deliverit.services.mappers.UserModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/panel/users")
public class UserMvcController {

    private final UserService service;
    private final CityService cityService;
    private final UserModelMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;
    @Autowired
    public UserMvcController(UserService service,
                             CityService cityService,
                             UserModelMapper modelMapper,
                             AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.cityService = cityService;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("cities")
    public List<City> populateCities() {
        return cityService.getAll();
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


    @GetMapping
    public String showAllUsers(Model model) {
        User user = service.getByEmail("kristiyan.dimitrov@gmail.com");
        model.addAttribute("users", service.getAll(user));
        return "users";
    }

    @GetMapping("/{id}")
    public String showSingleUser(@PathVariable int id, Model model) {
        try {
            User admin = service.getByEmail("kristiyan.dimitrov@gmail.com");
            User user = service.getById(admin,id);
            model.addAttribute("user", user);
            return "user";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/new")
    public String showNewUserPage(Model model) {
        model.addAttribute("user", new RegisterDto());
        return "user-new";
    }

    @PostMapping("/new")
    public String createAddress(@Valid @ModelAttribute("user") RegisterDto registerDto,
                                BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            return "user-new";
        }

        try {
            //ToDo Rework with current user in MVC authentication session.
            User user = modelMapper.fromDto(registerDto);
            service.create(user);

            return "redirect:/";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("email", "duplicate_email", e.getMessage());
            return "user-new";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/{id}/update")
    public String showEditAddressPage(@PathVariable int id, Model model) {
        try {
            User admin = service.getByEmail("kristiyan.dimitrov@gmail.com");
            User user = service.getById(admin, id);
            RegisterDto registerDto = modelMapper.toDto(user);
            model.addAttribute("userId", id);
            model.addAttribute("user", registerDto);
            return "user-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @PostMapping("/{id}/update")
    public String updateAddress(@PathVariable int id,
                                @Valid @ModelAttribute("user") RegisterDto registerDto,
                                BindingResult errors,
                                Model model) {
        if (errors.hasErrors()) {
            return "user-update";
        }

        try {
            User admin = service.getByEmail("kristiyan.dimitrov@gmail.com");
            User user = modelMapper.fromDto(registerDto,id);
            service.update(admin,user,user.getId());

            return "redirect:../";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("email", "duplicate_email", e.getMessage());
            return "user-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteAddress(@PathVariable int id, Model model) {
        try {
            User admin = service.getByEmail("kristiyan.dimitrov@gmail.com");
            User user = service.getById(admin,id);
            service.delete(admin, user.getId());

            return "redirect:../";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }
}
