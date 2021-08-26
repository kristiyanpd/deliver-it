package com.team9.deliverit.controllers.mvc;

import com.team9.deliverit.services.contracts.UserService;
import com.team9.deliverit.services.contracts.WarehouseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/panel")
public class PanelMvcController {

    private final UserService userService;
    private final WarehouseService warehouseService;

    public PanelMvcController(UserService userService, WarehouseService warehouseService) {
        this.userService = userService;
        this.warehouseService = warehouseService;
    }

    @ModelAttribute("customersCount")
    public String customersCount() {
        return String.valueOf(userService.countCustomers());
    }

    @GetMapping
    public String showPanelPage() {
        return "panel";
    }

}
