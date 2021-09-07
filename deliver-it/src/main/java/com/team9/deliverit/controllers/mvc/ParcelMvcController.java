package com.team9.deliverit.controllers.mvc;

import com.team9.deliverit.controllers.utils.AuthenticationHelper;
import com.team9.deliverit.exceptions.AuthenticationFailureException;
import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.*;
import com.team9.deliverit.models.dtos.FilterParcelDto;
import com.team9.deliverit.models.dtos.ParcelDto;
import com.team9.deliverit.models.enums.Category;
import com.team9.deliverit.models.enums.Status;
import com.team9.deliverit.services.contracts.ParcelService;
import com.team9.deliverit.services.contracts.ShipmentService;
import com.team9.deliverit.services.contracts.UserService;
import com.team9.deliverit.services.contracts.WarehouseService;
import com.team9.deliverit.services.mappers.ParcelModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/panel/parcels")
public class ParcelMvcController {

    private final ParcelService service;
    private final ParcelModelMapper modelMapper;
    private final UserService userService;
    private final ShipmentService shipmentService;
    private final WarehouseService warehouseService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public ParcelMvcController(ParcelService service,
                               ParcelModelMapper modelMapper,
                               UserService userService,
                               ShipmentService shipmentService,
                               WarehouseService warehouseService, AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.shipmentService = shipmentService;
        this.warehouseService = warehouseService;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("warehouses")
    public List<Warehouse> populateWarehouses() {
        return warehouseService.getAll();
    }

    @ModelAttribute("shipments")
    public List<Shipment> populateShipments(HttpSession session) {
        User user = userService.getByEmail("kristiyanpd02@gmail.com");
        return shipmentService.getAll(user);
    }

    @ModelAttribute("categories")
    public List<Category> populateCategories() {
        return Arrays.asList(Category.values());
    }

    @ModelAttribute("statuses")
    public List<Status> populateStatuses() {
        return Arrays.asList(Status.values());
    }

    @ModelAttribute("users")
    public List<User> populateUsers() {
        User user = userService.getByEmail("kristiyanpd02@gmail.com");
        return userService.getAll(user);
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

    //TODO Has parcels, otherwise hide tables and show "You do not have any parcels!"

    @GetMapping
    public String showAllParcels(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            List<Parcel> parcels = service.getAll(user);
            model.addAttribute("parcels", parcels);
            model.addAttribute("filterParcelDto", new FilterParcelDto());
            model.addAttribute("parcelsExist", !parcels.isEmpty());
            model.addAttribute("filtered", false);
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
            Parcel parcel = service.getById(user, id);

            if (user.isEmployee() || parcel.getUser().getId() == user.getId()) {
                if (parcel.getShipment().getStatus() == Status.COMPLETED) {
                    return "redirect:";
                }
                ParcelDto parcelDto = modelMapper.toDto(parcel);
                model.addAttribute("parcelId", id);
                model.addAttribute("parcel", parcelDto);
                return "parcel-update";
            } else {
                return "not-found";
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

    @PostMapping("/{id}/update/pickup")
    public String updateParcel(@PathVariable int id,
                               @RequestParam(name = "pickUpOption", required = false) String pickUpOption,
                               HttpSession session) {

        try {
            User user = authenticationHelper.tryGetUser(session);
            Parcel parcel = service.getById(user, id);

            if (parcel.getUser().getId() == user.getId()) {
                service.updatePickUpOption(user, parcel, pickUpOption);
            }

            return String.format("redirect:/panel/parcels/%s", id);
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (UnauthorizedOperationException e) {
            return "not-found";
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

    @PostMapping("/filter")
    public String filterParcels(@ModelAttribute FilterParcelDto filterParcelDto, HttpSession session, Model model) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            Optional<Double> weight = !filterParcelDto.getWeight().isEmpty() ? Optional.of(Double.valueOf(filterParcelDto.getWeight())) : Optional.empty();
            Optional<Integer> warehouseId = filterParcelDto.getWarehouseId() != -1 ? Optional.of(filterParcelDto.getWarehouseId()) : Optional.empty();
            Optional<Category> category = !filterParcelDto.getCategory().isEmpty() ? Optional.of(Category.getEnum(filterParcelDto.getCategory())) : Optional.empty();
            Optional<Status> status = !filterParcelDto.getStatus().isEmpty() ? Optional.of(Status.getEnum(filterParcelDto.getStatus())) : Optional.empty();
            Optional<Integer> userId;
            if (!user.isEmployee()) {
                userId = Optional.empty();
            } else {
                userId = filterParcelDto.getUserId() != -1 ? Optional.of(filterParcelDto.getUserId()) : Optional.empty();
            }

            var filtered = service.filter(user, weight, warehouseId, category, status, userId);
            model.addAttribute("parcels", filtered);
            model.addAttribute("parcelsExist", !filtered.isEmpty());
            model.addAttribute("filtered", true);
            return "parcels";
        } catch (EntityNotFoundException | UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

    }
}
