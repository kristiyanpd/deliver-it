package com.team9.deliverit.controllers.mvc;

import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.Address;
import com.team9.deliverit.models.City;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.dtos.AddressDto;
import com.team9.deliverit.services.contracts.AddressService;
import com.team9.deliverit.services.contracts.CityService;
import com.team9.deliverit.services.contracts.UserService;
import com.team9.deliverit.services.mappers.AddressModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/panel/addresses")
public class AddressMvcController {

    private final AddressService service;
    private final AddressModelMapper modelMapper;
    private final UserService userService;
    private final CityService cityService;

    @Autowired
    public AddressMvcController(AddressService service,
                                AddressModelMapper modelMapper,
                                UserService userService,
                                CityService cityService) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.cityService = cityService;
    }

    @ModelAttribute("cities")
    public List<City> populateCities() {
        return cityService.getAll();
    }

    @GetMapping
    public String showAllAddresses(Model model) {
        User user = userService.getByEmail("kristiyan.dimitrov@gmail.com");
        model.addAttribute("addresses", service.getAll(user));
        return "addresses";
    }

    @GetMapping("/{id}")
    public String showSingleAddress(@PathVariable int id, Model model) {
        try {
            User user = userService.getByEmail("kristiyan.dimitrov@gmail.com");
            Address address = service.getById(user, id);
            model.addAttribute("address", address);
            return "address";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/new")
    public String showNewAddressPage(Model model) {
        model.addAttribute("address", new AddressDto());
        return "address-new";
    }

    @PostMapping("/new")
    public String createAddress(@Valid @ModelAttribute("address") AddressDto addressDto, BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            return "address-new";
        }

        try {
            //ToDo Rework with current user in MVC authentication session.
            Address address = modelMapper.fromDto(addressDto);
            User user = userService.getByEmail("kristiyan.dimitrov@gmail.com");
            service.create(user, address);

            return "redirect:/addresses";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("streetName", "duplicate_address", e.getMessage());
            return "address-new";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/{id}/update")
    public String showEditAddressPage(@PathVariable int id, Model model) {
        try {
            User user = userService.getByEmail("kristiyan.dimitrov@gmail.com");
            Address address = service.getById(user, id);
            AddressDto addressDto = modelMapper.toDto(address);
            model.addAttribute("addressId", id);
            model.addAttribute("address", addressDto);
            return "address-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @PostMapping("/{id}/update")
    public String updateAddress(@PathVariable int id,
                                @Valid @ModelAttribute("address") AddressDto addressDto,
                                BindingResult errors,
                                Model model) {
        if (errors.hasErrors()) {
            return "address-update";
        }

        try {
            User user = userService.getByEmail("kristiyan.dimitrov@gmail.com");
            Address address = modelMapper.fromDto(addressDto,id);
            service.update(user, address);

            return "redirect:/addresses";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("streetName", "duplicate_address", e.getMessage());
            return "address-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteAddress(@PathVariable int id, Model model) {
        try {
            User user = userService.getByEmail("kristiyan.dimitrov@gmail.com");
            service.delete(user, id);

            return "redirect:/addresses";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }
}
