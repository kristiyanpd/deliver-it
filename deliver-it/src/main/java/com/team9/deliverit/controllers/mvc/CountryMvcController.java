package com.team9.deliverit.controllers.mvc;

import com.team9.deliverit.controllers.utils.AuthenticationHelper;
import com.team9.deliverit.exceptions.AuthenticationFailureException;
import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.Country;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.dtos.CountryDto;
import com.team9.deliverit.services.contracts.CountryService;
import com.team9.deliverit.services.contracts.UserService;
import com.team9.deliverit.services.mappers.CountryModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/panel/countries")
public class CountryMvcController {

    private final CountryService service;
    private final CountryModelMapper modelMapper;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public CountryMvcController(CountryService service, CountryModelMapper modelMapper, UserService userService, AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
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
    public String showAllCountries(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            if (user.isEmployee()) {
                model.addAttribute("countries", service.getAll());
                return "countries";
            } else {
                return "not-found";
            }
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            return "not-found";
        }
    }

    @GetMapping("/{id}")
    public String showSingleCountry(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            if (user.isEmployee()) {
                Country country = service.getById(id);
                model.addAttribute("country", country);
                return "country";
            } else {
                return "not-found";
            }
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            return "not-found";
        }
    }

    @GetMapping("/new")
    public String showNewCountryPage(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            if (user.isEmployee()) {
                model.addAttribute("country", new CountryDto());
                return "country-new";
            } else {
                return "not-found";
            }
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            return "not-found";
        }
    }

    @PostMapping("/new")
    public String createCountry(@Valid @ModelAttribute("country") CountryDto countryDto, BindingResult errors, Model model, HttpSession session) {
        if (errors.hasErrors()) {
            return "country-new";
        }

        try {
            Country country = modelMapper.fromDto(countryDto);
            User user = authenticationHelper.tryGetUser(session);
            service.create(user, country);

            return "redirect:/panel/countries";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("name", "duplicate_country", e.getMessage());
            return "country-new";
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
    public String showEditCountryPage(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            if (user.isEmployee()) {
                Country country = service.getById(id);
                CountryDto countryDto = modelMapper.toDto(country);
                model.addAttribute("countryId", id);
                model.addAttribute("country", countryDto);
                return "country-update";
            } else {
                return "not-found";
            }
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            return "not-found";
        }
    }

    @PostMapping("/{id}/update")
    public String updateCountry(@PathVariable int id,
                                @Valid @ModelAttribute("country") CountryDto dto,
                                BindingResult errors,
                                Model model,
                                HttpSession session) {
        if (errors.hasErrors()) {
            return "country-update";
        }

        try {
            User user = authenticationHelper.tryGetUser(session);
            Country country = modelMapper.fromDto(dto, id);
            service.update(user, country);

            return "redirect:/panel/countries";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("name", "duplicate_country", e.getMessage());
            return "country-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            return "not-found";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteCountry(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            service.delete(user, id);

            return "redirect:/panel/countries";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            return "not-found";
        }
    }
}
