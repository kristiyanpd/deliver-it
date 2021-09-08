package com.team9.deliverit.controllers.mvc;

import com.team9.deliverit.controllers.utils.AuthenticationHelper;
import com.team9.deliverit.exceptions.AuthenticationFailureException;
import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.City;
import com.team9.deliverit.models.Country;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.dtos.CityDto;
import com.team9.deliverit.services.contracts.CityService;
import com.team9.deliverit.services.contracts.CountryService;
import com.team9.deliverit.services.contracts.UserService;
import com.team9.deliverit.services.mappers.CityModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/panel/cities")
public class CityMvcController {

    private final CityService service;
    private final CityModelMapper modelMapper;
    private final CountryService countryService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public CityMvcController(CityService service,
                             CityModelMapper modelMapper,
                             CountryService countryService,
                             AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.countryService = countryService;
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

    @ModelAttribute("countries")
    public List<Country> populateCountries() {
        return countryService.getAll();
    }

    @GetMapping
    public String showAllCities(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            if (user.isEmployee()) {
                model.addAttribute("cities", service.getAll());
                return "cities";
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
    public String showSingleCity(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            if (user.isEmployee()) {
                City city = service.getById(id);
                model.addAttribute("city", city);
                return "city";
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
    public String showNewCityPage(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            if (user.isEmployee()) {
                model.addAttribute("city", new CityDto());
                return "city-new";
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
    public String createCity(@Valid @ModelAttribute("city") CityDto cityDto, BindingResult errors, Model model, HttpSession session) {
        if (errors.hasErrors()) {
            return "city-new";
        }

        try {
            City city = modelMapper.fromDto(cityDto);
            User user = authenticationHelper.tryGetUser(session);
            service.create(city, user);

            return "redirect:/panel/cities";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("name", "duplicate_city", e.getMessage());
            return "city-new";
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
    public String showEditCityPage(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            if (user.isEmployee()) {
                City city = service.getById(id);
                CityDto cityDto = modelMapper.toDto(city);
                model.addAttribute("cityId", id);
                model.addAttribute("city", cityDto);
                return "city-update";
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
    public String updateCity(@PathVariable int id,
                             @Valid @ModelAttribute("city") CityDto cityDto,
                             BindingResult errors,
                             Model model,
                             HttpSession session) {
        if (errors.hasErrors()) {
            return "city-update";
        }

        try {
            User user = authenticationHelper.tryGetUser(session);
            City city = modelMapper.fromDto(cityDto, id);
            service.update(city, user);

            return "redirect:/panel/cities";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("name", "duplicate_city", e.getMessage());
            return "city-update";
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
    public String deleteCity(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            service.delete(id, user);

            return "redirect:/panel/cities";
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
