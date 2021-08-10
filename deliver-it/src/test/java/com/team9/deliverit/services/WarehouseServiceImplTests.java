package com.team9.deliverit.services;

import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.EntityNotFoundException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.Warehouse;
import com.team9.deliverit.models.dtos.WarehouseDisplayDto;
import com.team9.deliverit.repositories.contracts.WarehouseRepository;
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

@ExtendWith(MockitoExtension.class)
public class WarehouseServiceImplTests {

    @Mock
    WarehouseRepository mockRepository;

    @InjectMocks
    WarehouseServiceImpl service;

    @Test
    public void getById_Should_ReturnWarehouse_When_MatchExist() {
        // Arrange
        Warehouse warehouse = createMockWarehouse();
        Mockito.when(mockRepository.getById(1))
                .thenReturn(warehouse);
        // Act
        Warehouse result = service.getById(createMockEmployee(), 1);

        // Assert
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals(warehouse.getAddress(), result.getAddress());
    }

    @Test
    public void getAll_Should_ReturnEmptyList_When_RepositoryEmpty() {
        // Arrange
        List<Warehouse> list = new ArrayList<>();

        Mockito.when(mockRepository.getAll())
                .thenReturn(list);
        // Act
        List<WarehouseDisplayDto> result = service.getAll();

        // Assert
        Assertions.assertEquals(0, result.size());
    }

    @Test
    public void create_Should_Throw_When_UserNotEmployee() {

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.create(createMockWarehouse(), createMockCustomer()));
    }

    @Test
    public void create_Should_Throw_When_DuplicateExits() {

        Warehouse warehouse = createMockWarehouse();

        Mockito.when(mockRepository.getByAddressId(anyInt()))
                .thenReturn(warehouse);


        Assertions.assertThrows(DuplicateEntityException.class,
                () -> service.create(warehouse, createMockEmployee()));
    }

    @Test
    public void create_Should_Call_Repository_When_WarehouseIsValid() {

        var mockWarehouse = createMockWarehouse();

        Mockito.when(mockRepository.getByAddressId(anyInt()))
                .thenThrow(new EntityNotFoundException("Warehouse", "address", mockWarehouse.getAddress().getStreetName()));

        // Act
        service.create(mockWarehouse, createMockEmployee());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(Mockito.any(Warehouse.class));
    }

    @Test
    public void update_Should_Throw_When_UserNotEmployee() {

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.update(createMockWarehouse(), createMockCustomer()));
    }

    @Test
    public void update_Should_Throw_When_DuplicateExits() {

        Warehouse warehouse = createMockWarehouse();
        Warehouse warehouse1 = createMockWarehouse();
        warehouse1.setId(2);

        Mockito.when(mockRepository.getByAddressId(anyInt()))
                .thenReturn(warehouse);


        Assertions.assertThrows(DuplicateEntityException.class,
                () -> service.update(warehouse1, createMockEmployee()));
    }

    @Test
    public void update_Should_Call_Repository_When_WarehouseIsValid() {

        var mockWarehouse = createMockWarehouse();

        Mockito.when(mockRepository.getByAddressId(anyInt()))
                .thenThrow(new EntityNotFoundException("Warehouse", "address", mockWarehouse.getAddress().getStreetName()));

        // Act
        service.update(mockWarehouse, createMockEmployee());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(Mockito.any(Warehouse.class));
    }

    @Test
    public void delete_Should_Throw_When_UserNotEmployee() {

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.delete(1, createMockCustomer()));
    }

    @Test
    public void delete_Should_Call_Repository_When_UserValid() {

        service.delete(1, createMockEmployee());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(anyInt());

    }
}
