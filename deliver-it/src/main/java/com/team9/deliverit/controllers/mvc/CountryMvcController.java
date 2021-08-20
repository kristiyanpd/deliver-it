package com.team9.deliverit.controllers.mvc;

import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.EntityNotFoundException;
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

import javax.validation.Valid;

@Controller
@RequestMapping("/countries")
public class CountryMvcController {

    private final CountryService service;
    private final CountryModelMapper modelMapper;
    private final UserService userService;

    @Autowired
    public CountryMvcController(CountryService service, CountryModelMapper modelMapper, UserService userService) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @GetMapping
    public String showAllCountries(Model model) {
        model.addAttribute("countries", service.getAll());
        return "countries";
    }

    @GetMapping("/{id}")
    public String showSingleCountry(@PathVariable int id, Model model) {
        try {
            Country country = service.getById(id);
            model.addAttribute("country", country);
            return "country";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/new")
    public String showNewCountryPage(Model model) {
        model.addAttribute("country", new CountryDto());
        return "country-new";
    }

    @PostMapping("/new")
    public String createCountry(@Valid @ModelAttribute("country") CountryDto countryDto, BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            return "country-new";
        }

        try {
            //ToDo Rework with current user in MVC authentication session.
            Country country = modelMapper.fromDto(countryDto);
            User user = userService.getByEmail("kristiyan.dimitrov@gmail.com");
            service.create(user, country);

            return "redirect:/countries";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("name", "duplicate_country", e.getMessage());
            return "country-new";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }


    @GetMapping("/{id}/update")
    public String showEditCountryPage(@PathVariable int id, Model model) {
        try {
            Country country = service.getById(id);
            CountryDto countryDto = modelMapper.toDto(country);
            model.addAttribute("countryId", id);
            model.addAttribute("country", countryDto);
            return "country-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @PostMapping("/{id}/update")
    public String updateCountry(@PathVariable int id,
                                @Valid @ModelAttribute("country") CountryDto dto,
                                BindingResult errors,
                                Model model) {
        if (errors.hasErrors()) {
            return "country-update";
        }

        try {
            User user = userService.getByEmail("kristiyan.dimitrov@gmail.com");
            Country country = modelMapper.fromDto(dto, id);
            service.update(user, country);

            return "redirect:/countries";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("name", "duplicate_country", e.getMessage());
            return "country-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteCountry(@PathVariable int id, Model model) {
        try {
            User user = userService.getByEmail("kristiyan.dimitrov@gmail.com");
            service.delete(user, id);

            return "redirect:/countries";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }
}
