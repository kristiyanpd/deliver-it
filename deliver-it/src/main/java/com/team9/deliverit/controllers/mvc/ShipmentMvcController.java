package com.team9.deliverit.controllers.mvc;

import com.team9.deliverit.controllers.utils.AuthenticationHelper;
import com.team9.deliverit.exceptions.AuthenticationFailureException;
import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.Shipment;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.Warehouse;
import com.team9.deliverit.models.dtos.ShipmentDto;
import com.team9.deliverit.services.contracts.ShipmentService;
import com.team9.deliverit.services.contracts.UserService;
import com.team9.deliverit.services.contracts.WarehouseService;
import com.team9.deliverit.services.mappers.ShipmentModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/panel/shipments")
public class ShipmentMvcController {

    private final ShipmentService service;
    private final ShipmentModelMapper modelMapper;
    private final UserService userService;
    private final WarehouseService warehouseService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public ShipmentMvcController(ShipmentService service,
                                 ShipmentModelMapper modelMapper,
                                 UserService userService,
                                 WarehouseService warehouseService,
                                 AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.warehouseService = warehouseService;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("warehouses")
    public List<Warehouse> populateWarehouses() {
        return warehouseService.getAll();
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

    @GetMapping
    public String showAllShipments(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            model.addAttribute("shipments", service.getAll(user));
            return "shipments";
        } catch (EntityNotFoundException | UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/{id}")
    public String showSingleShipment(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            Shipment shipment = service.getById(user, id);
            model.addAttribute("shipment", shipment);
            return "shipment";
        } catch (EntityNotFoundException | UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/new")
    public String showNewShipmentPage(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            if (user.isEmployee()) {
                model.addAttribute("shipment", new ShipmentDto());
                return "shipment-new";
            } else {
                return "not-found";
            }
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/new")
    public String createShipment(@Valid @ModelAttribute("shipment") ShipmentDto shipmentDto, BindingResult errors, Model model, HttpSession session) {
        if (errors.hasErrors()) {
            return "shipment-new";
        }

        try {
            Shipment shipment = modelMapper.fromDto(shipmentDto);
            User user = authenticationHelper.tryGetUser(session);
            service.create(user, shipment);

            return "redirect:/panel/shipments";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("name", "duplicate_shipment", e.getMessage());
            return "shipment-new";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/{id}/update")
    public String showEditShipmentPage(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            Shipment shipment = service.getById(user, id);
            ShipmentDto shipmentDto = modelMapper.toDto(shipment);
            model.addAttribute("shipmentId", id);
            model.addAttribute("shipment", shipmentDto);
            return "shipment-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @PostMapping("/{id}/update")
    public String updateShipment(@PathVariable int id,
                                 @Valid @ModelAttribute("shipment") ShipmentDto shipmentDto,
                                 BindingResult errors,
                                 Model model,
                                 HttpSession session) {
        if (errors.hasErrors()) {
            return "shipment-update";
        }

        try {
            User user = authenticationHelper.tryGetUser(session);
            Shipment shipment = modelMapper.fromDto(shipmentDto, id);
            service.update(user, shipment);

            return "redirect:/panel/shipments";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("name", "duplicate_shipment", e.getMessage());
            return "shipment-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteShipment(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            service.delete(user, id);

            return "redirect:/panel/shipments";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }
}
