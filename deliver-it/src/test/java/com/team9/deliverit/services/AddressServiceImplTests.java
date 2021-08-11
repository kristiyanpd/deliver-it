package com.team9.deliverit.services;

import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.Address;
import com.team9.deliverit.models.dtos.AddressDisplayDto;
import com.team9.deliverit.repositories.contracts.AddressRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.team9.deliverit.Helpers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class AddressServiceImplTests {

    @Mock
    AddressRepository mockRepository;

    @InjectMocks
    AddressServiceImpl service;

    @Test
    public void getById_Should_ReturnAddress_When_MatchExist() {
        // Arrange
        Address mockAddress = createMockAddress();
        Mockito.when(mockRepository.getById(anyInt()))
                .thenReturn(mockAddress);
        // Act
        var result = service.getById(createMockEmployee(), 1);

        // Assert
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals(mockAddress.getCity(), result.getCity());
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
        List<Address> list = new ArrayList<>();

        Mockito.when(mockRepository.getAll())
                .thenReturn(list);
        // Act
        List<AddressDisplayDto> result = service.getAll(createMockEmployee());

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
    public void getByName_Should_ReturnAddress_When_MatchExist() {
        // Arrange

        List<Address> list = new ArrayList<>();
        Address mockAddress = createMockAddress();
        list.add(mockAddress);

        Mockito.when(mockRepository.getByName(anyString()))
                .thenReturn(list);
        // Act
        List<Address> result = service.getByName(createMockEmployee(), "Tsar Samuil");

        // Assert
        Assertions.assertEquals(1, result.get(0).getId());
        Assertions.assertEquals(mockAddress.getStreetName(), result.get(0).getStreetName());
    }

    @Test
    public void getByName_Should_Throw_When_UnauthorizedUser() {
        // Arrange
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.getByName(createMockCustomer(), "Tsar Samuil"));
    }

    @Test
    public void create_Should_Throw_When_DuplicateAddress() {

        Mockito.when(mockRepository.isDuplicate(anyString(), anyInt()))
                .thenReturn(true);

        Assertions.assertThrows(DuplicateEntityException.class,
                () -> service.create(createMockCustomer(), createMockAddress()));
    }

    @Test
    public void create_Should_Call_Repository_When_AddressIsValid() {

        Mockito.when(mockRepository.isDuplicate(anyString(), anyInt()))
                .thenReturn(false);

        // Act
        service.create(createMockEmployee(), createMockAddress());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(Mockito.any(Address.class));
    }

    @Test
    public void update_Should_Throw_When_UserNotEmployee() {

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.update(createMockCustomer(), createMockAddress()));
    }

    @Test
    public void update_Should_Throw_When_DuplicateExits() {

        Mockito.when(mockRepository.isDuplicate(anyString(), anyInt()))
                .thenReturn(true);


        Assertions.assertThrows(DuplicateEntityException.class,
                () -> service.update(createMockEmployee(), createMockAddress()));
    }

    @Test
    public void update_Should_Call_Repository_When_AddressIsValid() {

        Address mockAddress = createMockAddress();

        Mockito.when(mockRepository.isDuplicate(anyString(), anyInt()))
                .thenReturn(false);

        // Act
        service.update(createMockEmployee(), mockAddress);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(Mockito.any(Address.class));
    }

    @Test
    public void delete_Should_Throw_When_UserNotEmployee() {

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.delete(createMockCustomer(), 1));
    }

    @Test
    public void delete_Should_Call_Repository_When_UserValid() {

        service.delete(createMockEmployee(), 1);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(anyInt());

    }
}
