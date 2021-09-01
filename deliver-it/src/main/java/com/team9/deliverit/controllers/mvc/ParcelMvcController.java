package com.team9.deliverit.controllers.mvc;

import com.team9.deliverit.controllers.utils.AuthenticationHelper;
import com.team9.deliverit.exceptions.AuthenticationFailureException;
import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.Role;
import com.team9.deliverit.models.Shipment;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.dtos.ParcelDto;
import com.team9.deliverit.services.contracts.ParcelService;
import com.team9.deliverit.services.contracts.ShipmentService;
import com.team9.deliverit.services.contracts.UserService;
import com.team9.deliverit.services.mappers.ParcelModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/panel/parcels")
public class ParcelMvcController {

    private final ParcelService service;
    private final ParcelModelMapper modelMapper;
    private final UserService userService;
    private final ShipmentService shipmentService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public ParcelMvcController(ParcelService service,
                               ParcelModelMapper modelMapper,
                               UserService userService,
                               ShipmentService shipmentService,
                               AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.shipmentService = shipmentService;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("shipments")
    public List<Shipment> populateShipments(HttpSession session) {
        User user = userService.getByEmail("kristiyanpd02@gmail.com");
        return shipmentService.getAll(user);
    }


    @ModelAttribute("users")
    public List<User> populateUsers(HttpSession session) {
        User user = userService.getByEmail("kristiyanpd02@gmail.com");
        return userService.getAll(user);
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

    @ModelAttribute("isEmployee")
    public boolean isEmployee(HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return false;
        }
        return user.isEmployee();
    }

    //TODO Has parcels, otherwise hide tables and show "You do not have any parcels!"

    @GetMapping
    public String showAllParcels(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            model.addAttribute("parcels", service.getAll(user));
            return "parcels";
        } catch (EntityNotFoundException | UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/{id}")
    public String showSingleParcel(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            Parcel parcel = service.getById(user, id);
            model.addAttribute("parcel", parcel);
            return "parcel";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/new")
    public String showNewParcelPage(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            if (user.isEmployee()) {
                model.addAttribute("parcel", new ParcelDto());
                return "parcel-new";
            } else {
                return "not-found";
            }
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/new")
    public String createParcel(@Valid @ModelAttribute("parcel") ParcelDto parcelDto, BindingResult errors, Model model, HttpSession session) {
        if (errors.hasErrors()) {
            return "parcel-new";
        }

        try {
            User user = authenticationHelper.tryGetUser(session);
            Parcel parcel = modelMapper.fromDto(parcelDto);
            service.create(user, parcel);
            return "redirect:/panel/parcels";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("name", "duplicate_parcel", e.getMessage());
            return "parcel-new";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/{id}/update")
    public String showEditParcelPage(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            if (user.isEmployee()) {
                Parcel parcel = service.getById(user, id);
                ParcelDto parcelDto = modelMapper.toDto(parcel);
                model.addAttribute("parcelId", id);
                model.addAttribute("parcel", parcelDto);
                return "parcel-update";
            } else {
                return "not-found;";
            }
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

    @PostMapping("/{id}/update")
    public String updateParcel(@PathVariable int id, @Valid @ModelAttribute("parcel") ParcelDto parcelDto,
                               BindingResult errors, Model model, HttpSession session) {
        if (errors.hasErrors()) {
            return "parcel-update";
        }

        try {
            User user = authenticationHelper.tryGetUser(session);
            Parcel parcel = modelMapper.fromDto(parcelDto, id);
            service.update(user, parcel);
            return "redirect:/panel/parcels";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("name", "duplicate_parcel", e.getMessage());
            return "parcel-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteParcel(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            service.delete(user, id);
            return "redirect:/panel/parcels";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }
}
