package com.team9.deliverit.controllers.mvc;

import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.models.Address;
import com.team9.deliverit.models.City;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.Warehouse;
import com.team9.deliverit.models.dtos.AddressDto;
import com.team9.deliverit.models.dtos.WarehouseDto;
import com.team9.deliverit.services.contracts.AddressService;
import com.team9.deliverit.services.contracts.CityService;
import com.team9.deliverit.services.contracts.UserService;
import com.team9.deliverit.services.contracts.WarehouseService;
import com.team9.deliverit.services.mappers.AddressModelMapper;
import com.team9.deliverit.services.mappers.WarehouseModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/panel/warehouses")
public class WarehouseMvcController {

    private final WarehouseService service;
    private final WarehouseModelMapper modelMapper;
    private final UserService userService;
    private final AddressService addressService;

    @Autowired
    public WarehouseMvcController(WarehouseService service,
                                  WarehouseModelMapper modelMapper,
                                  UserService userService,
                                  AddressService addressService) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.addressService = addressService;
    }

    @ModelAttribute("addresses")
    public List<Address> populateAddresses() {
        User user = userService.getByEmail("kristiyan.dimitrov@gmail.com");
        return addressService.getAll(user);
    }

    @GetMapping
    public String showAllWarehouses(Model model) {
        model.addAttribute("warehouses", service.getAll());
        return "warehouses";
    }

    @GetMapping("/{id}")
    public String showSingleWarehouse(@PathVariable int id, Model model) {
        try {
            User user = userService.getByEmail("kristiyan.dimitrov@gmail.com");
            Warehouse warehouse = service.getById(user,id);
            model.addAttribute("warehouse", warehouse);
            return "warehouse";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/new")
    public String showNewWarehousePage(Model model) {
        model.addAttribute("warehouse", new WarehouseDto());
        return "warehouse-new";
    }

    @PostMapping("/new")
    public String createWarehouse(@Valid @ModelAttribute("warehouse") WarehouseDto warehouseDto,
                                BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            return "warehouse-new";
        }

        try {
            //ToDo Rework with current user in MVC authentication session.
            Warehouse warehouse = modelMapper.fromDto(warehouseDto);
            User user = userService.getByEmail("kristiyan.dimitrov@gmail.com");
            service.create(warehouse,user);

            return "redirect:/warehouses";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("addressId", "duplicate_address", e.getMessage());
            return "warehouse-new";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/{id}/update")
    public String showEditWarehousePage(@PathVariable int id, Model model) {
        try {
            User user = userService.getByEmail("kristiyan.dimitrov@gmail.com");
            Warehouse warehouse = service.getById(user,id);
            WarehouseDto warehouseDto = modelMapper.toDto(warehouse);

            model.addAttribute("warehouseId", id);
            model.addAttribute("warehouse", warehouseDto);
            return "warehouse-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @PostMapping("/{id}/update")
    public String updateWarehouse(@PathVariable int id,
                                @Valid @ModelAttribute("warehouse") WarehouseDto warehouseDto,
                                BindingResult errors,
                                Model model) {
        if (errors.hasErrors()) {
            return "warehouse-update";
        }

        try {
            User user = userService.getByEmail("kristiyan.dimitrov@gmail.com");
            Warehouse warehouse = modelMapper.fromDto(warehouseDto, id);
            service.update(warehouse,user);

            return "redirect:/warehouses";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("addressId", "duplicate_address", e.getMessage());
            return "warehouse-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteAddress(@PathVariable int id, Model model) {
        try {
            User user = userService.getByEmail("kristiyan.dimitrov@gmail.com");
            service.delete(id,user);

            return "redirect:/warehouses";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }
}
