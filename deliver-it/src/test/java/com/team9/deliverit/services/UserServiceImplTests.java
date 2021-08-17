package com.team9.deliverit.services;

import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.User;
import com.team9.deliverit.repositories.contracts.RoleRepository;
import com.team9.deliverit.repositories.contracts.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.team9.deliverit.Helpers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @Mock
    UserRepository mockRepository;

    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    UserServiceImpl service;

    @Test
    public void getById_Should_ReturnUser_When_MatchExist() {
        // Arrange
        var mockUser = createMockEmployee();
        Mockito.when(mockRepository.getById(anyInt()))
                .thenReturn(mockUser);
        // Act
        var result = service.getById(createMockEmployee(), 1);

        // Assert
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals(mockUser.getFirstName(), result.getFirstName());
    }

    @Test
    public void getById_Should_Throw_When_UnauthorizedUser() {
        // Arrange
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.getById(createMockCustomer(), 1));
    }

    @Test
    public void getAll_Should_ReturnEmptyList_When_RepositoryEmpty() {
        // Arrange
        List<User> list = new ArrayList<>();

        Mockito.when(mockRepository.getAll())
                .thenReturn(list);
        // Act
        List<User> result = service.getAll(createMockEmployee());

        // Assert
        Assertions.assertEquals(0, result.size());
    }

    @Test
    public void getAll_Should_Throw_When_UnauthorizedUser() {
        // Arrange
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.getAll(createMockCustomer()));
    }

    @Test
    public void getByEmail_Should_ReturnUser_When_MatchExist() {
        // Arrange

        var mockUser = createMockEmployee();

        Mockito.when(mockRepository.getByEmail(anyString()))
                .thenReturn(mockUser);
        // Act
        var result = service.getByEmail("georgiev.kamen@abv.bg");

        // Assert
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals(mockUser.getFirstName(), result.getFirstName());
    }

    @Test
    public void Create_Should_Throw_When_DuplicateUser() {

        Mockito.when(mockRepository.isDuplicate(anyString()))
                .thenReturn(true);

        Assertions.assertThrows(DuplicateEntityException.class,
                () -> service.create(createMockCustomer()));
    }

    @Test
    public void Create_Should_Call_Repository_When_UserIsValid() {

        Mockito.when(mockRepository.isDuplicate(anyString()))
                .thenReturn(false);

        // Act
        service.create(createMockEmployee());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(Mockito.any(User.class));
    }

    @Test
    public void Update_Should_Throw_When_UserNotEmployeeOrNotOwner() {

        var mockCustomer = createMockCustomer();
        var mockEmployee = createMockEmployee();
        mockEmployee.setId(3);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.update(mockCustomer, mockEmployee, 3));
    }

    @Test
    public void Update_Should_Throw_When_DuplicateExits() {

        var mockCustomer = createMockCustomer();
        var mockEmployee = createMockEmployee();

        Mockito.when(mockRepository.isDuplicate(anyString()))
                .thenReturn(true);

        Mockito.when(mockRepository.getById(anyInt()))
                .thenReturn(mockCustomer);

        Assertions.assertThrows(DuplicateEntityException.class,
                () -> service.update(mockCustomer, mockEmployee, 1));
    }

    @Test
    public void Update_Should_Call_Repository_When_UserValid() {

        var mockCustomer = createMockCustomer();
        var mockEmployee = createMockEmployee();

        Mockito.when(mockRepository.isDuplicate(anyString()))
                .thenReturn(false);

        Mockito.when(mockRepository.getById(anyInt()))
                .thenReturn(mockCustomer);

        service.update(mockEmployee, mockCustomer, 1);

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(Mockito.any(User.class));
    }

    @Test
    public void Delete_Should_Throw_When_UserNotEmployee() {

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.delete(createMockCustomer(), 1));
    }

    @Test
    public void Delete_Should_Call_Repository_When_UserValid() {

        service.delete(createMockEmployee(), 1);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(anyInt());

    }

    @Test
    public void RegisterEmployee_Should_Throw_When_UnauthorizedUser() {

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.registerEmployee(1, createMockCustomer()));
    }

    @Test
    public void registerEmployee_Should_Call_Repository_When_UserIsValid() {

        var mockUser = createMockEmployee();
        var mockCustomer = createMockCustomer();
        var mockRole = createMockEmployeeRole();

        Mockito.when(mockRepository.getById(anyInt()))
                .thenReturn(mockCustomer);

        Mockito.when(roleRepository.getById(anyInt()))
                .thenReturn(mockRole);

        service.registerEmployee(1,mockUser);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockCustomer);
    }

    @Test
    public void CountCustomers_Should_Return_Count() {

        Mockito.when(mockRepository.countCustomers())
                .thenReturn(2);
        // Act
        var result = service.countCustomers();

        // Assert
        Assertions.assertEquals(2, result);

    }

    @Test
    public void Search_Should_Throw_When_UnauthorisedUser() {

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.search(Optional.empty(), Optional.empty(), Optional.empty(), createMockCustomer()));
    }

    @Test
    public void Search_Should_Call_Repository_When_UserIsValid() {

        var mockEmployee = createMockEmployee();

        service.search(Optional.empty(), Optional.empty(), Optional.empty(), mockEmployee);

        Mockito.verify(mockRepository, Mockito.times(1))
                .search(Optional.empty(), Optional.empty(), Optional.empty());
    }

    @Test
    public void SearchEveryWhere_Should_Throw_When_UnauthorisedUser() {

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.searchEverywhere("Search", createMockCustomer()));

    }

    @Test
    public void SearchEveryWhere_Should_Call_Repository_When_UserIsValid() {

        var mockEmployee = createMockEmployee();

        service.searchEverywhere(anyString(), mockEmployee);

        Mockito.verify(mockRepository, Mockito.times(1))
                .searchEverywhere(anyString());

    }
}
