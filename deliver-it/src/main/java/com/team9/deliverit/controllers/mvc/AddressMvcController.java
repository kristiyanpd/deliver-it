package com.team9.deliverit.controllers.mvc;

import com.team9.deliverit.controllers.utils.AuthenticationHelper;
import com.team9.deliverit.exceptions.AuthenticationFailureException;
import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
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

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/panel/addresses")
public class AddressMvcController {

    private final AddressService service;
    private final AddressModelMapper modelMapper;
    private final CityService cityService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public AddressMvcController(AddressService service,
                                AddressModelMapper modelMapper,
                                CityService cityService,
                                AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.cityService = cityService;
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

    @ModelAttribute("cities")
    public List<City> populateCities() {
        return cityService.getAll();
    }

    @GetMapping
    public String showAllAddresses(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            model.addAttribute("addresses", service.getAll(user));
            return "addresses";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            return "not-found";
        }
    }

    @GetMapping("/{id}")
    public String showSingleAddress(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            Address address = service.getById(user, id);
            model.addAttribute("address", address);
            return "address";
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
    public String showNewAddressPage(Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
            model.addAttribute("address", new AddressDto());
            return "address-new";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            return "not-found";
        }
    }

    @PostMapping("/new")
    public String createAddress(@Valid @ModelAttribute("address") AddressDto addressDto, BindingResult errors, Model model,
                                HttpSession session) {
        if (errors.hasErrors()) {
            return "address-new";
        }

        try {
            Address address = modelMapper.fromDto(addressDto);
            User user = authenticationHelper.tryGetUser(session);
            service.create(user, address);

            return "redirect:/panel/addresses";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("streetName", "duplicate_address", e.getMessage());
            return "address-new";
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
    public String showEditAddressPage(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            Address address = service.getById(user, id);
            AddressDto addressDto = modelMapper.toDto(address);
            model.addAttribute("addressId", id);
            model.addAttribute("address", addressDto);
            return "address-update";
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
    public String updateAddress(@PathVariable int id,
                                @Valid @ModelAttribute("address") AddressDto addressDto,
                                BindingResult errors,
                                Model model,
                                HttpSession session) {
        if (errors.hasErrors()) {
            return "address-update";
        }

        try {
            User user = authenticationHelper.tryGetUser(session);
            Address address = modelMapper.fromDto(addressDto,id);
            service.update(user, address);

            return "redirect:/panel/addresses";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("streetName", "duplicate_address", e.getMessage());
            return "address-update";
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
    public String deleteAddress(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            service.delete(user, id);

            return "redirect:/panel/addresses";
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
