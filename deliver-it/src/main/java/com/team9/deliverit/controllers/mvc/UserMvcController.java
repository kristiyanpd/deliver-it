package com.team9.deliverit.controllers.mvc;

import com.team9.deliverit.controllers.utils.AuthenticationHelper;
import com.team9.deliverit.exceptions.AuthenticationFailureException;
import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.City;
import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.dtos.UserDto;
import com.team9.deliverit.services.contracts.CityService;
import com.team9.deliverit.services.contracts.ParcelService;
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
    private final ParcelService parcelService;
    private final UserModelMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public UserMvcController(UserService service,
                             CityService cityService,
                             ParcelService parcelService, UserModelMapper modelMapper,
                             AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.cityService = cityService;
        this.parcelService = parcelService;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("cities")
    public List<City> populateCities() {
        return cityService.getAll();
    }

    @ModelAttribute("currentLoggedUser")
    public String populateCurrentLoggedUser(HttpSession session, Model model) {
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
        try {
            User user = authenticationHelper.tryGetUser(session);
            return user.isEmployee();
        } catch (AuthenticationFailureException e) {
            return false;
        }
    }

    @GetMapping
    public String showAllUsers(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            model.addAttribute("users", service.getAll(user));
            return "users";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            return "not-found";
        }
    }

    @GetMapping("/{id}")
    public String showSingleUser(@PathVariable int id, Model model, HttpSession session) {
        try {
            User admin = authenticationHelper.tryGetUser(session);
            User user = service.getById(admin, id);
            List<Parcel> parcels = parcelService.getAllUserParcels(user);

            model.addAttribute("user", user);
            model.addAttribute("hasParcels", !parcels.isEmpty());
            model.addAttribute("parcels", parcels);
            return "user";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            return "not-found";
        }
    }

    @GetMapping("/{id}/update")
    public String showEditUserPage(@PathVariable int id, Model model, HttpSession session) {
        try {
            User userEditing = authenticationHelper.tryGetUser(session);
            User user = service.getById(userEditing, id);
            UserDto userDto = modelMapper.toUserDto(user);
            model.addAttribute("userId", id);
            model.addAttribute("user", userDto);
            return "user-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @PostMapping("/{id}/update")
    public String updateUser(@PathVariable int id,
                             @Valid @ModelAttribute("user") UserDto userDto,
                             BindingResult errors,
                             Model model,
                             HttpSession session) {
        if (errors.hasErrors()) {
            return "user-update";
        }

        try {
            User userExecuting = authenticationHelper.tryGetUser(session);
            User user = modelMapper.fromUserDto(id, userDto);
            service.update(userExecuting, user, id);

            model.addAttribute("userId", id);
            return "user-update";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("email", "duplicate_email", e.getMessage());
            return "user-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            return "not-found";
        }
    }

    @GetMapping("/{id}/employee")
    public String registerEmployee(@PathVariable int id, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            service.registerEmployee(id, user);
            return String.format("redirect:/panel/users/%s", id);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            return "not-found";
        }
    }

    @GetMapping("/{id}/employee/remove")
    public String removeEmployee(@PathVariable int id, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            service.removeEmployee(id, user);
            return String.format("redirect:/panel/users/%s", id);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            return "not-found";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable int id, Model model, HttpSession session) {
        try {
            User admin = authenticationHelper.tryGetUser(session);
            service.delete(admin, id);

            return "redirect:/panel/users";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            return "not-found";
        }
    }
}
