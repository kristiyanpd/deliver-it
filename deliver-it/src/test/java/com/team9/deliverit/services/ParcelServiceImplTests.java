package com.team9.deliverit.services;

import com.team9.deliverit.exceptions.EnumAlreadySameException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.Parcel;
import com.team9.deliverit.models.enums.PickUpOption;
import com.team9.deliverit.models.enums.Status;
import com.team9.deliverit.repositories.contracts.ParcelRepository;
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
                () -> service.getById(1, mockCustomer));
    }

    @Test
    public void getById_Should_ReturnShipment_When_MatchExist() {
        // Arrange
        var mockParcel = createMockParcel();
        Mockito.when(mockRepository.getById(anyInt()))
                .thenReturn(mockParcel);
        // Act
        var result = service.getById(1, createMockEmployee());

        // Assert
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals(mockParcel.getWeight(), result.getWeight());
    }

    @Test
    public void create_Should_Call_Repository_When_ParcelIsValid() {

        var mockParcel = createMockParcel();

        // Act
        service.create(mockParcel, createMockEmployee());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(Mockito.any(Parcel.class));
    }

    @Test
    public void create_Should_Throw_When_UnauthorizedUser() {
        // Assert
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.create(createMockParcel(), createMockCustomer()));
    }

    @Test
    public void create_Should_Throw_When_ShipmentIsFull() {
        var mockParcel = createMockParcel();
        mockParcel.getShipment().setFull(true);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.create(mockParcel, createMockEmployee()));
    }

    @Test
    public void update_Should_Call_Repository_When_ParcelIsValid() {

        var mockParcel = createMockParcel();

        service.update(mockParcel, createMockEmployee());
        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(Mockito.any(Parcel.class));
    }

    @Test
    public void update_Should_Throw_When_UnauthorizedUser() {
        // Assert
        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.update(createMockParcel(), createMockCustomer()));
    }

    @Test
    public void update_Should_Throw_When_ShipmentIsFull() {
        var mockParcel = createMockParcel();
        mockParcel.getShipment().setFull(true);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.update(mockParcel, createMockEmployee()));
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

    @Test
    public void filter_Calls_Repository_When_UserNotEmployee() {
        service.filter(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), createMockCustomer());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .filter(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(1));
    }

    @Test
    public void filter_Calls_Repository_When_UserIsValid() {

        service.filter(Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), createMockEmployee());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .filter(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    }

    @Test
    public void sort_Should_Call_Repository_When_UserNotEmployee() {
        service.sort(Optional.empty(), Optional.empty(), Optional.empty(), createMockCustomer());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .sort(Optional.empty(), Optional.empty(), Optional.of(1));
    }

    @Test
    public void sort_Should_Call_Repository_When_UserIsEmployee() {

        service.sort(Optional.empty(), Optional.empty(), Optional.empty(), createMockEmployee());

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
    public void getStatusOfParcel_Should_Throw_When_UserNotOwner() {
        var mockParcel = createMockParcel();
        var mockUser = createMockCustomer();
        mockUser.setId(3);

        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenReturn(mockParcel);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.getStatusOfParcel(mockUser, mockParcel.getId()));
    }

    @Test
    public void getStatusOfParcel_Should_ReturnStatus_When_UserIsOwner() {
        var mockParcel = createMockParcel();
        var mockUser = createMockCustomer();
        mockParcel.setUser(mockUser);

        Mockito.when(mockRepository.getById(anyInt()))
                .thenReturn(mockParcel);

        Mockito.when(mockRepository.getStatusOfParcel(anyInt()))
                .thenReturn(mockParcel.getShipment().getStatus().toString());

        var result = service.getStatusOfParcel(mockUser, mockParcel.getId());

        Assertions.assertEquals(result, mockParcel.getShipment().getStatus().toString());
    }

    @Test
    public void updatePickUpOption_Should_Throw_When_UserNotOwner() {
        var mockParcel = createMockParcel();
        var mockUser = createMockCustomer();
        mockUser.setId(3);

        Mockito.when(mockRepository.getById(Mockito.anyInt()))
                .thenReturn(mockParcel);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.updatePickUpOption(mockUser, mockParcel.getId(), "DELIVER_TO_ADDRESS"));
    }

    @Test
    public void updatePickUpOption_Should_Throw_When_StatusCompleted() {
        var mockParcel = createMockParcel();
        var mockUser = createMockCustomer();
        mockParcel.getShipment().setStatus(Status.COMPLETED);
        mockParcel.setUser(mockUser);

        Mockito.when(mockRepository.getById(anyInt()))
                .thenReturn(mockParcel);

        Assertions.assertThrows(EnumAlreadySameException.class,
                () -> service.updatePickUpOption(mockUser, mockParcel.getId(), "DELIVER_TO_ADDRESS"));

    }

    @Test
    public void updatePickUpOption_Should_Throw_When_PickUpOptionSame() {
        var mockParcel = createMockParcel();
        var mockUser = createMockCustomer();
        mockParcel.setUser(mockUser);
        mockParcel.setPickUpOption(PickUpOption.DELIVER_TO_ADDRESS);

        Mockito.when(mockRepository.getById(anyInt()))
                .thenReturn(mockParcel);

        Assertions.assertThrows(EnumAlreadySameException.class,
                () -> service.updatePickUpOption(mockUser, mockParcel.getId(), "DELIVER_TO_ADDRESS"));

    }

    @Test
    public void updatePickUpOption_Call_Repository_When_Valid() {
        var mockParcel = createMockParcel();
        var mockUser = createMockCustomer();
        mockParcel.setUser(mockUser);
        mockParcel.setPickUpOption(PickUpOption.PICK_UP_FROM_WAREHOUSE);

        Mockito.when(mockRepository.getById(anyInt()))
                .thenReturn(mockParcel);


        service.updatePickUpOption(mockUser, mockParcel.getId(), "DELIVER_TO_ADDRESS");

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .updatePickUpOption(mockParcel, PickUpOption.DELIVER_TO_ADDRESS);
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

}
