package com.team9.deliverit.controllers.mvc;

import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.City;
import com.team9.deliverit.models.Country;
import com.team9.deliverit.services.contracts.CityService;
import com.team9.deliverit.services.contracts.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cities")
public class CityMvcController {

    private final CityService service;

    @Autowired
    public CityMvcController(CityService cityService) {
        this.service = cityService;
    }

    @GetMapping
    public String showAllCities(Model model) {
        model.addAttribute("cities", service.getAll());
        return "cities";
    }

    @GetMapping("/{id}")
    public String showSingleCity(@PathVariable int id, Model model) {
        try {
            City city = service.getById(id);
            model.addAttribute("city", city);
            return "city";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }
}
