package com.team9.deliverit.controllers.mvc;

import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.Country;
import com.team9.deliverit.services.contracts.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/countries")
public class CountryMvcController {

    private final CountryService service;

    @Autowired
    public CountryMvcController(CountryService countryService) {
        this.service = countryService;
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
}
