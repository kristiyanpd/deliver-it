package com.team9.deliverit.controllers.rest;

import com.team9.deliverit.controllers.utils.AuthenticationHelper;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.dtos.UserDisplayDto;
import com.team9.deliverit.models.dtos.RegisterDto;
import com.team9.deliverit.services.contracts.UserService;
import com.team9.deliverit.services.mappers.UserModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;
    private final UserModelMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public UserController(UserService service, UserModelMapper modelMapper, AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<User> getAll(@RequestHeader HttpHeaders headers) {
        User user = authenticationHelper.tryGetUser(headers);
        return service.getAll(user);
    }

    @GetMapping("/{id}")
    public User getById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        User user = authenticationHelper.tryGetUser(headers);
        return service.getById(user, id);
    }

    @PostMapping
    public UserDisplayDto create(@Valid @RequestBody RegisterDto userDto) {
        User user = modelMapper.fromDto(userDto);
        service.create(user);
        return UserModelMapper.toUserDisplayDto(user);
    }

    @PutMapping("/{id}")
    public User update(@RequestHeader HttpHeaders headers, @PathVariable int id,
                       @Valid @RequestBody RegisterDto userDto) {
        User userExecuting = authenticationHelper.tryGetUser(headers);
        User user = modelMapper.fromDto(userDto, id);
        service.update(userExecuting, user, id);
        return user;
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        User user = authenticationHelper.tryGetUser(headers);
        service.delete(user, id);
    }

    @PutMapping("/{id}/employee/register")
    public User registerEmployee(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        User user = authenticationHelper.tryGetUser(headers);
        service.registerEmployee(id, user);
        return service.getById(user, id);
    }

    @GetMapping("/search/optional")
    public List<User> search(@RequestHeader HttpHeaders headers,
                             @RequestParam(required = false) Optional<String> email,
                             Optional<String> firstName, Optional<String> lastName) {
        User user = authenticationHelper.tryGetUser(headers);
        return service.search(email, firstName, lastName, user);
    }

    @GetMapping("/search")
    public List<User> searchEverywhere(@RequestHeader HttpHeaders headers, @RequestParam String param) {
        User user = authenticationHelper.tryGetUser(headers);
        return service.searchEverywhere(param, user);
    }

    @GetMapping("/customers/count")
    public int countCustomers() {
        return service.countCustomers();
    }

}