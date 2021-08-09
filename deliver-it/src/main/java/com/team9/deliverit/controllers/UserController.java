package com.team9.deliverit.controllers;

import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.dtos.ParcelDisplayDto;
import com.team9.deliverit.models.dtos.UserDisplayDto;
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
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return service.getAll(user);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public User getById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return service.getById(user, id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping
    public UserDisplayDto create(@Valid @RequestBody UserRegistrationDto userDto) {
        try {
            User user = modelMapper.fromDto(userDto);
            service.create(user);
            return UserModelMapper.toUserDto(user);
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

        @PutMapping("/{id}")
        public User update (@RequestHeader HttpHeaders headers,@PathVariable int id,
        @Valid @RequestBody UserRegistrationDto userDto){
            try {
                User userExecuting = authenticationHelper.tryGetUser(headers);
                User user = modelMapper.fromDto(userDto, id);
                service.update(userExecuting, user, id);
                return user;
            } catch (EntityNotFoundException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            } catch (DuplicateEntityException e) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
            } catch (UnauthorizedOperationException e) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
            }
        }

        @DeleteMapping("/{id}")
        public void delete (@RequestHeader HttpHeaders headers,@PathVariable int id){
            try {
                User user = authenticationHelper.tryGetUser(headers);
                service.delete(user, id);
            } catch (EntityNotFoundException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            } catch (UnauthorizedOperationException e) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
            }
        }

        @PutMapping("/{id}/registerEmployee")
        public User registerEmployee (@RequestHeader HttpHeaders headers,@PathVariable int id){
            try {
                User user = authenticationHelper.tryGetUser(headers);
                return service.registerEmployee(id, user);
            } catch (EntityNotFoundException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            } catch (UnauthorizedOperationException e) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
            }
        }

        @GetMapping("/{id}/incoming-parcels")
        public List<ParcelDisplayDto> incomingParcels (@RequestHeader HttpHeaders headers,@PathVariable int id){
            try {
                User user = authenticationHelper.tryGetUser(headers);
                return service.incomingParcels(id, user);
            } catch (UnauthorizedOperationException e) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
            }
        }

        @GetMapping("/optional-search")
        public List<User> search (@RequestHeader HttpHeaders headers,
                @RequestParam(required = false) Optional < String > email,
                Optional < String > firstName,
                Optional < String > lastName){
            try {
                User user = authenticationHelper.tryGetUser(headers);
                return service.search(email, firstName, lastName, user);
            } catch (UnauthorizedOperationException e) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
            }
        }

        @GetMapping("/search")
        public List<User> searchEverywhere (@RequestHeader HttpHeaders headers, @RequestParam String param){
            try {
                User user = authenticationHelper.tryGetUser(headers);
                return service.searchEverywhere(param, user);
            } catch (UnauthorizedOperationException e) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
            }
        }

        @GetMapping("/customers-count")
        public int countCustomers () {
            return service.countCustomers();
        }

    }