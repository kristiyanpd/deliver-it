package com.team9.deliverit.services;

import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.Shipment;
import com.team9.deliverit.models.dtos.ShipmentDisplayDto;
import com.team9.deliverit.models.enums.Status;
import com.team9.deliverit.repositories.contracts.ShipmentRepository;
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

@ExtendWith(MockitoExtension.class)
public class ShipmentServiceImplTests {

    @Mock
    ShipmentRepository mockRepository;

    @InjectMocks
    ShipmentServiceImpl service;

    @Test
    public void getById_Should_ReturnShipment_When_MatchExist() {
        // Arrange
        var mockShipment = createMockShipment();
        Mockito.when(mockRepository.getById(anyInt()))
                .thenReturn(mockShipment);
        // Act
        var result = service.getById(createMockEmployee(), 1);

        // Assert
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals(mockShipment.getStatus(), result.getStatus());
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
        List<Shipment> list = new ArrayList<>();

        Mockito.when(mockRepository.getAll())
                .thenReturn(list);
        // Act
        List<Shipment> result = service.getAll(createMockEmployee());

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
    public void create_Should_Throw_When_UnauthorisedUser() {

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.create(createMockCustomer(), createMockShipment()));
    }

    @Test
    public void create_Should_Throw_When_OriginWarehouse_Equals_DestinationWarehouse() {

        var mockShipment = createMockShipment();
        var mockWarehouse = createMockWarehouse();

        mockShipment.setDestinationWarehouse(mockWarehouse);
        mockShipment.setOriginWarehouse(mockWarehouse);


        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.create(createMockEmployee(), mockShipment));
    }

    @Test
    public void create_Should_Call_Repository_When_ShipmentIsValid() {

        var mockShipment = createMockShipment();

        // Act
        service.create(createMockEmployee(), mockShipment);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(Mockito.any(Shipment.class));
    }

    @Test
    public void update_Should_Throw_When_UserNotEmployee() {

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.update(createMockCustomer(), createMockShipment()));
    }

    @Test
    public void update_Should_Throw_When_OriginWarehouse_Equals_DestinationWarehouse() {

        var mockShipment = createMockShipment();
        var mockWarehouse = createMockWarehouse();

        mockShipment.setDestinationWarehouse(mockWarehouse);
        mockShipment.setOriginWarehouse(mockWarehouse);

        Mockito.when(mockRepository.getById(anyInt()))
                .thenReturn(mockShipment);


        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.update(createMockEmployee(), mockShipment));
    }

    @Test
    public void update_Should_Throw_When_Update_Status_Of_Shipment_That_Is_Empty() {

        var mockShipment = createMockShipment();
        mockShipment.setStatus(Status.PREPARING);

        var mockShipment1 = createMockShipment();
        mockShipment.setStatus(Status.COMPLETED);

        Mockito.when(mockRepository.getById(anyInt()))
                .thenReturn(mockShipment);

        Mockito.when(mockRepository.isEmpty(anyInt()))
                .thenReturn(true);


        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.update(createMockEmployee(), mockShipment1));
    }

    @Test
    public void update_Should_Call_Repository_When_ShipmentIsValid() {

        var mockShipment = createMockShipment();


        Mockito.when(mockRepository.getById(anyInt()))
                .thenReturn(mockShipment);

        Mockito.when(mockRepository.isEmpty(anyInt()))
                .thenReturn(true);

        service.update(createMockEmployee(), mockShipment);
        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(Mockito.any(Shipment.class));
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

    @Test
    public void filter_Should_Throw_When_UserNotEmployee() {

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.filter(createMockCustomer(), Optional.empty(), Optional.empty()));
    }

    @Test
    public void filter_Calls_Repository_When_UserIsValid() {

        service.filter(createMockEmployee(), Optional.empty(), Optional.empty());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .filter(Optional.empty(), Optional.empty());
    }

    @Test
    public void countShipmentsOnTheWay_Should_Throw_When_UserNotEmployee() {

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.countShipmentsOnTheWay(createMockCustomer()));
    }

    @Test
    public void countShipmentsOnTheWay_Calls_Repository_When_UserIsValid() {

        service.countShipmentsOnTheWay(createMockEmployee());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .countShipmentsOnTheWay();
    }

    @Test
    public void nextShipmentToArrive_Should_Throw_When_UserNotEmployee() {

        var mockCustomer = createMockCustomer();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.nextShipmentToArrive(1, mockCustomer));
    }

    @Test
    public void nextShipmentToArrive_Calls_Repository_When_UserIsValid() {

        var mockShipment = createMockShipment();

        Mockito.when(mockRepository.nextShipmentToArrive(anyInt()))
                .thenReturn(mockShipment);

        service.nextShipmentToArrive(1, createMockEmployee());


        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .nextShipmentToArrive(anyInt());
    }
}
