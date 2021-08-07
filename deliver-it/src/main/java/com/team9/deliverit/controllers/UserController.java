package com.team9.deliverit.controllers;

import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.dtos.UserRegistrationDto;
import com.team9.deliverit.services.contracts.UserService;
import com.team9.deliverit.services.mappers.UserModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserModelMapper userModelMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public UserController(UserService userService, UserModelMapper userModelMapper, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.userModelMapper = userModelMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable int id) {
        try {
            return userService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage()
            );
        }
    }

    @PostMapping
    public User create(@Valid @RequestBody UserRegistrationDto userDto) {
        try {
            User user = userModelMapper.fromDto(userDto);
            userService.create(user);
            return user;
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public User update(@PathVariable int id, @Valid @RequestBody UserRegistrationDto userDto) {
        try {
            User user = userModelMapper.fromDto(userDto, id);
            userService.update(user);
            return user;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {

        try {
            userService.delete(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}/registerEmployee")
    public User registerEmployee(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return userService.registerEmployee(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

/*    @GetMapping("/search")
    public List<User> search(
            @RequestParam(required = false) Optional<String> email, Optional<String> firstName, Optional<String> lastName) {
        return userService.search(email, firstName, lastName);
    }

    @GetMapping("/{id}/incoming-parcels")
    public List<Parcel> incomingParcels(@PathVariable int id) {
        return userService.incomingParcels(id);
    }

    @GetMapping("search?everywhere")
    public List<Customer> searchEverywhere(@RequestParam String value){
        return userService.searchEverywhere(value);
    }*/

}