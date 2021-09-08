package com.team9.deliverit.controllers.mvc;

import com.team9.deliverit.models.Warehouse;
import com.team9.deliverit.services.contracts.UserService;
import com.team9.deliverit.services.contracts.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeMvcController extends BaseAuthenticationController {

    private final UserService service;
    private final WarehouseService warehouseService;

    @Autowired
    public HomeMvcController(UserService service, WarehouseService warehouseService) {
        this.service = service;
        this.warehouseService = warehouseService;
    }

    @ModelAttribute("warehouses")
    public List<Warehouse> populateWarehouses() {
        return warehouseService.getAll();
    }

    @ModelAttribute("customersCount")
    public String customersCount() {
        return String.valueOf(service.countCustomers());
    }

    @GetMapping
    public String showHomePage() {
        return "index";
    }

}
