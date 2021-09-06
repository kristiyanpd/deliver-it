package com.team9.deliverit.controllers.mvc;

import com.team9.deliverit.controllers.utils.AuthenticationHelper;
import com.team9.deliverit.exceptions.AuthenticationFailureException;
import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.Address;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.Warehouse;
import com.team9.deliverit.models.dtos.WarehouseDto;
import com.team9.deliverit.services.contracts.AddressService;
import com.team9.deliverit.services.contracts.ShipmentService;
import com.team9.deliverit.services.contracts.UserService;
import com.team9.deliverit.services.contracts.WarehouseService;
import com.team9.deliverit.services.mappers.WarehouseModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/panel/warehouses")
public class WarehouseMvcController {

    private final WarehouseService service;
    private final WarehouseModelMapper modelMapper;
    private final UserService userService;
    private final AddressService addressService;
    private final ShipmentService shipmentService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public WarehouseMvcController(WarehouseService service,
                                  WarehouseModelMapper modelMapper,
                                  UserService userService,
                                  AddressService addressService,
                                  ShipmentService shipmentService,
                                  AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.addressService = addressService;
        this.shipmentService = shipmentService;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("addresses")
    public List<Address> populateAddresses() {
        User user = userService.getByEmail("kristiyan.dimitrov@gmail.com");
        return addressService.getAll(user);
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
    public String showAllWarehouses(Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
            model.addAttribute("warehouses", service.getAll());
            return "warehouses";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/{id}")
    public String showSingleWarehouse(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            Warehouse warehouse = service.getById(user, id);
            model.addAttribute("warehouse", warehouse);
            if (user.isEmployee()) {
                try {
                    model.addAttribute("shipmentToArrive", shipmentService.nextShipmentToArrive(id, user));
                    model.addAttribute("shipmentExists", true);
                } catch (EntityNotFoundException e) {
                    model.addAttribute("shipmentExists", false);
                }
            }
            return "warehouse";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/new")
    public String showNewWarehousePage(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            if (user.isEmployee()) {
                model.addAttribute("warehouse", new WarehouseDto());
                return "warehouse-new";
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
    public String createWarehouse(@Valid @ModelAttribute("warehouse") WarehouseDto warehouseDto,
                                  BindingResult errors, Model model, HttpSession session) {
        if (errors.hasErrors()) {
            return "warehouse-new";
        }

        try {
            Warehouse warehouse = modelMapper.fromDto(warehouseDto);
            User user = authenticationHelper.tryGetUser(session);
            service.create(warehouse, user);
            return "redirect:/panel/warehouses";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("addressId", "duplicate_address", e.getMessage());
            return "warehouse-new";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/{id}/update")
    public String showEditWarehousePage(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            if (user.isEmployee()) {
                Warehouse warehouse = service.getById(user, id);
                WarehouseDto warehouseDto = modelMapper.toDto(warehouse);
                model.addAttribute("warehouseId", id);
                model.addAttribute("warehouse", warehouseDto);
                return "warehouse-update";
            } else {
                return "not-found";
            }
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @PostMapping("/{id}/update")
    public String updateWarehouse(@PathVariable int id,
                                  @Valid @ModelAttribute("warehouse") WarehouseDto warehouseDto,
                                  BindingResult errors,
                                  Model model,
                                  HttpSession session) {
        if (errors.hasErrors()) {
            return "warehouse-update";
        }

        try {
            User user = authenticationHelper.tryGetUser(session);
            Warehouse warehouse = modelMapper.fromDto(warehouseDto, id);
            service.update(warehouse, user);

            return "redirect:/panel/warehouses";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("addressId", "duplicate_address", e.getMessage());
            return "warehouse-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteAddress(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            service.delete(id, user);

            return "redirect:/panel/warehouses";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e) {
            return "not-found";
        }
    }
}
