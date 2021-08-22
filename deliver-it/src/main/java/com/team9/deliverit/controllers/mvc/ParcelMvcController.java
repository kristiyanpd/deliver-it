package com.team9.deliverit.controllers.mvc;

import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.Parcel;
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

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/parcels")
public class ParcelMvcController {

    private final ParcelService service;
    private final ParcelModelMapper modelMapper;
    private final UserService userService;
    private final ShipmentService shipmentService;

    @Autowired
    public ParcelMvcController(ParcelService service,
                               ParcelModelMapper modelMapper,
                               UserService userService,
                               ShipmentService shipmentService) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.shipmentService = shipmentService;
    }

    @ModelAttribute("shipments")
    public List<Shipment> populateShipments() {
        User user = userService.getByEmail("kristiyan.dimitrov@gmail.com");
        return shipmentService.getAll(user);
    }

    @ModelAttribute("users")
    public List<User> populateUsers() {
        User user = userService.getByEmail("kristiyan.dimitrov@gmail.com");
        return userService.getAll(user);
    }

    @GetMapping
    public String showAllParcels(Model model) {
        try {
            User user = userService.getByEmail("kristiyan.dimitrov@gmail.com");
            model.addAttribute("parcels", service.getAll(user));
            return "parcels";
        } catch (EntityNotFoundException | UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/{id}")
    public String showSingleParcel(@PathVariable int id, Model model) {
        try {
            User user = userService.getByEmail("kristiyan.dimitrov@gmail.com");
            Parcel parcel = service.getById(user, id);
            model.addAttribute("parcel", parcel);
            return "parcel";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/new")
    public String showNewParcelPage(Model model) {
        model.addAttribute("parcel", new ParcelDto());
        return "parcel-new";
    }

    @PostMapping("/new")
    public String createParcel(@Valid @ModelAttribute("parcel") ParcelDto parcelDto, BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            return "parcel-new";
        }

        try {
            //ToDo Rework with current user in MVC authentication session.
            Parcel parcel = modelMapper.fromDto(parcelDto);
            User user = userService.getByEmail("kristiyan.dimitrov@gmail.com");
            service.create(user, parcel);

            return "redirect:/parcels";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("name", "duplicate_parcel", e.getMessage());
            return "parcel-new";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/{id}/update")
    public String showEditParcelPage(@PathVariable int id, Model model) {
        try {
            User user = userService.getByEmail("kristiyan.dimitrov@gmail.com");
            Parcel parcel = service.getById(user, id);
            ParcelDto parcelDto = modelMapper.toDto(parcel);
            model.addAttribute("parcelId", id);
            model.addAttribute("parcel", parcelDto);
            return "parcel-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @PostMapping("/{id}/update")
    public String updateParcel(@PathVariable int id,
                             @Valid @ModelAttribute("parcel") ParcelDto parcelDto,
                             BindingResult errors,
                             Model model) {
        if (errors.hasErrors()) {
            return "parcel-update";
        }

        try {
            User user = userService.getByEmail("kristiyan.dimitrov@gmail.com");
            Parcel parcel = modelMapper.fromDto(parcelDto, id);
            service.update(user, parcel);

            return "redirect:/parcels";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("name", "duplicate_parcel", e.getMessage());
            return "parcel-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteParcel(@PathVariable int id, Model model) {
        try {
            User user = userService.getByEmail("kristiyan.dimitrov@gmail.com");
            service.delete(user, id);

            return "redirect:/parcels";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }
}
