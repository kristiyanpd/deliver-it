package com.team9.deliverit.services;

import com.team9.deliverit.exceptions.EnumAlreadySameException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.enums.PickUpOption;
import com.team9.deliverit.models.enums.Status;
import com.team9.deliverit.repositories.contracts.ParcelRepository;
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
public class ParcelServiceImplTests {

    @Mock
    ParcelRepository mockRepository;

    @Mock
    ShipmentRepository shipmentMockRepository;

    @InjectMocks
    ParcelServiceImpl service;

    @Test
    public void getAll_Should_ReturnEmptyList_When_RepositoryEmpty() {
        // Arrange
        List<Parcel> list = new ArrayList<>();

        Mockito.when(mockRepository.getAll())
                .thenReturn(list);

        // Act
        List<Parcel> result = service.getAll(createMockEmployee());

        // Assert
        Assertions.assertEquals(0, result.size());
    }

    @Test
    public void getAll_Should_ReturnList_When_RepositoryNotEmpty() {
        // Arrange
        List<Parcel> list = new ArrayList<>();
        list.add(createMockParcel());

        Mockito.when(mockRepository.getAll())
                .thenReturn(list);

        // Act
        List<Parcel> result = service.getAll(createMockEmployee());

        // Assert
        Assertions.assertEquals(1, result.size());
    }

    @Test
    public void getAll_Should_Throw_When_UnauthorizedUser() {
        // Assert
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.getAll(createMockCustomer()));
    }

    @Test
    public void getById_Should_Throw_WhenUnauthorizedUser() {
        // Arrange
        var mockParcel = createMockParcel();
        var mockCustomer = createMockCustomer();
        mockCustomer.setId(2);

        // Act
        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenReturn(mockParcel);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.getById(mockCustomer, 1));
    }

    @Test
    public void getById_Should_ReturnShipment_When_MatchExist() {
        // Arrange
        var mockParcel = createMockParcel();
        Mockito.when(mockRepository.getById(anyInt()))
                .thenReturn(mockParcel);
        // Act
        var result = service.getById(createMockEmployee(), 1);

        // Assert
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals(mockParcel.getWeight(), result.getWeight());
    }

    @Test
    public void create_Should_Call_Repository_When_ParcelIsValid() {

        var mockParcel = createMockParcel();

        // Act
        service.create(createMockEmployee(), mockParcel);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(Mockito.any(Parcel.class));
    }

    @Test
    public void create_Should_Throw_When_UnauthorizedUser() {
        // Assert
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.create(createMockCustomer(), createMockParcel()));
    }

    @Test
    public void create_Should_Throw_When_ShipmentIsFull() {
        var mockParcel = createMockParcel();
        mockParcel.getShipment().setFull(true);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.create(createMockEmployee(), mockParcel));
    }

    @Test
    public void update_Should_Call_Repository_When_ParcelIsValid() {

        var mockParcel = createMockParcel();

        service.update(createMockEmployee(), mockParcel);
        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(Mockito.any(Parcel.class));
    }

    @Test
    public void update_Should_Throw_When_UnauthorizedUser() {
        // Assert
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.update(createMockCustomer(), createMockParcel()));
    }

    @Test
    public void update_Should_Throw_When_ShipmentIsFull() {
        var mockParcel = createMockParcel();
        mockParcel.getShipment().setFull(true);

        var mockParcel2 = createMockParcel();
        var mockShipment = createMockShipment();
        mockShipment.setFull(true);
        mockShipment.setId(14);
        mockParcel2.setShipment(mockShipment);

        Mockito.when(mockRepository
                .getById(anyInt())).thenReturn(mockParcel);


        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.update(createMockEmployee(), mockParcel2));
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
    public void filter_Calls_Repository_When_UserNotEmployee() {
        service.filter(createMockCustomer(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .filter(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(1));
    }

    @Test
    public void filter_Calls_Repository_When_UserIsValid() {

        service.filter(createMockEmployee(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .filter(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    }

    @Test
    public void sort_Should_Call_Repository_When_UserNotEmployee() {
        service.sort(createMockCustomer(), Optional.empty(), Optional.empty(), Optional.empty());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .sort(Optional.empty(), Optional.empty(), Optional.of(1));
    }

    @Test
    public void sort_Should_Call_Repository_When_UserIsEmployee() {

        service.sort(createMockEmployee(), Optional.empty(), Optional.empty(), Optional.empty());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .sort(Optional.empty(), Optional.empty(), Optional.empty());
    }

    @Test
    public void getAllUserParcels_Should_ReturnAll_UserParcels() {

        var mockUser = createMockCustomer();
        var mockParcel = createMockParcel();

        List<Parcel> list = new ArrayList<>();
        list.add(mockParcel);

        Mockito.when(mockRepository.getAllUserParcels(Mockito.anyInt()))
                .thenReturn(list);

        var result = service.getAllUserParcels(mockUser);

        Assertions.assertEquals(result.get(0).getId(), list.get(0).getId());
        Assertions.assertEquals(result.get(0).getCategory(), list.get(0).getCategory());
    }

    @Test
    public void updatePickUpOption_Should_Throw_When_UserNotOwner() {
        var mockParcel = createMockParcel();
        var mockUser = createMockCustomer();
        mockUser.setId(3);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.updatePickUpOption(mockUser, mockParcel, "DELIVER_TO_ADDRESS"));
    }

    @Test
    public void updatePickUpOption_Should_Throw_When_StatusCompleted() {
        var mockParcel = createMockParcel();
        var mockUser = createMockCustomer();
        mockParcel.getShipment().setStatus(Status.COMPLETED);
        mockParcel.setUser(mockUser);

        Assertions.assertThrows(EnumAlreadySameException.class,
                () -> service.updatePickUpOption(mockUser, mockParcel, "DELIVER_TO_ADDRESS"));

    }

    @Test
    public void updatePickUpOption_Call_Repository_When_Valid() {
        var mockParcel = createMockParcel();
        var mockUser = createMockCustomer();
        mockParcel.setUser(mockUser);
        mockParcel.setPickUpOption(PickUpOption.PICK_UP_FROM_WAREHOUSE);

        service.updatePickUpOption(mockUser, mockParcel, "DELIVER_TO_ADDRESS");

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockParcel);
    }

    @Test
    public void IncomingParcels_Should_Call_Repository_When_UserCalls() {

        var mockCustomer = createMockCustomer();

        service.incomingParcels(mockCustomer);

        Mockito.verify(mockRepository, Mockito.times(1))
                .incomingParcels(mockCustomer.getId());

    }

    @Test
    public void PastParcels_Should_Call_Repository_When_UserCalls() {

        var mockCustomer = createMockCustomer();

        service.pastParcels(mockCustomer);

        Mockito.verify(mockRepository, Mockito.times(1))
                .pastParcels(mockCustomer.getId());

    }

    @Test
    public void updateShipment_Should_Throw_When_UserNotEmployee() {
        var mockParcel = createMockParcel();
        var mockUser = createMockCustomer();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.updateShipment(mockUser, mockParcel, 3));
    }

    @Test
    public void updateShipment_Should_Throw_When_Shipment_Is_Full() {
        var mockParcel = createMockParcel();
        var mockParcel1 = createMockParcel();
        mockParcel1.getShipment().setId(4);
        var mockEmployee = createMockEmployee();
        var mockShipment = createMockShipment();
        mockShipment.setFull(true);

        Mockito.when(shipmentMockRepository.getById(anyInt()))
                .thenReturn(mockShipment);

        Mockito.when(mockRepository.getById(anyInt()))
                .thenReturn(mockParcel1);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.updateShipment(mockEmployee, mockParcel, 5));

    }


    @Test
    public void updateShipment_Should_Call_Repository_When_Valid() {
        var mockParcel = createMockParcel();
        var mockShipment = createMockShipment();
        var mockEmployee = createMockEmployee();


        Mockito.when(shipmentMockRepository.getById(anyInt()))
                .thenReturn(mockShipment);

        service.updateShipment(mockEmployee, mockParcel, mockShipment.getId());

        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockParcel);
    }


}
