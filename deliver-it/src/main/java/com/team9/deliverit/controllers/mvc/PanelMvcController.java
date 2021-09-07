package com.team9.deliverit.controllers.mvc;

import com.team9.deliverit.controllers.utils.AuthenticationHelper;
import com.team9.deliverit.exceptions.AuthenticationFailureException;
import com.team9.deliverit.models.User;
import com.team9.deliverit.services.contracts.ParcelService;
import com.team9.deliverit.services.contracts.ShipmentService;
import com.team9.deliverit.services.contracts.UserService;
import com.team9.deliverit.services.contracts.WarehouseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/panel")
public class PanelMvcController {

    private final UserService userService;
    private final WarehouseService warehouseService;
    private final ShipmentService shipmentService;
    private final ParcelService parcelService;
    private final AuthenticationHelper authenticationHelper;

    public PanelMvcController(UserService userService,
                              WarehouseService warehouseService,
                              ShipmentService shipmentService, ParcelService parcelService,
                              AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.warehouseService = warehouseService;
        this.shipmentService = shipmentService;
        this.parcelService = parcelService;
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

    @ModelAttribute("customersCount")
    public String customersCount() {
        return String.valueOf(userService.countCustomers());
    }

    @ModelAttribute("warehousesCount")
    public String warehousesCount() {
        return String.valueOf(warehouseService.warehousesCount());
    }

    @ModelAttribute("parcelsCount")
    public String parcelsCount() {
        return String.valueOf(parcelService.parcelsCount());
    }

    @ModelAttribute("shipmentsCount")
    public String shipmentsCount() {
        return String.valueOf(shipmentService.shipmentsCount());
    }

    @GetMapping
    public String showPanelPage(HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
            return "panel";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }
    }

}
