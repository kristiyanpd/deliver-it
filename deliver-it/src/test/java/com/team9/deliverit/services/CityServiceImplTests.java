package com.team9.deliverit.services;

import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.City;
import com.team9.deliverit.repositories.contracts.CityRepository;
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
public class CityServiceImplTests {

    @Mock
    CityRepository mockRepository;

    @InjectMocks
    CityServiceImpl service;

    @Test
    public void getById_Should_ReturnCity_When_MatchExist() {
        // Arrange
        City mockCity = createMockCity();
        Mockito.when(mockRepository.getById(anyInt()))
                .thenReturn(mockCity);
        // Act
        City result = service.getById(1);

        // Assert
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals(mockCity.getName(), result.getName());
    }

    @Test
    public void getAll_Should_ReturnEmptyList_When_RepositoryEmpty() {
        // Arrange
        List<City> list = new ArrayList<>();

        Mockito.when(mockRepository.getAll())
                .thenReturn(list);
        // Act
        List<City> result = service.getAll();

        // Assert
        Assertions.assertEquals(0, result.size());
    }

    @Test
    public void getByName_Should_ReturnCity_When_MatchExist() {
        // Arrange

        List<City> list = new ArrayList<>();
        City mockCity = createMockCity();
        list.add(mockCity);

        Mockito.when(mockRepository.getByName(anyString()))
                .thenReturn(list);
        // Act
        List<City> result = service.getByName("Bulgaria");

        // Assert
        Assertions.assertEquals(1, result.get(0).getId());
        Assertions.assertEquals(mockCity.getName(), result.get(0).getName());
    }

    @Test
    public void create_Should_Throw_When_UserNotEmployee() {

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.create(createMockCity(), createMockCustomer()));
    }

    @Test
    public void create_Should_Throw_When_DuplicateExits() {

        Mockito.when(mockRepository.isDuplicate(anyString(), anyInt()))
                .thenReturn(true);

        Assertions.assertThrows(DuplicateEntityException.class,
                () -> service.create(createMockCity(), createMockEmployee()));
    }

    @Test
    public void create_Should_Call_Repository_When_CityIsValid() {

        City mockCity = createMockCity();

        Mockito.when(mockRepository.isDuplicate(anyString(), anyInt()))
                .thenReturn(false);

        // Act
        service.create(mockCity, createMockEmployee());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(Mockito.any(City.class));
    }

    @Test
    public void update_Should_Throw_When_UserNotEmployee() {

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.update(createMockCity(), createMockCustomer()));
    }

    @Test
    public void update_Should_Throw_When_DuplicateExits() {

        Mockito.when(mockRepository.isDuplicate(anyString(), anyInt()))
                .thenReturn(true);


        Assertions.assertThrows(DuplicateEntityException.class,
                () -> service.update(createMockCity(), createMockEmployee()));
    }

    @Test
    public void update_Should_Call_Repository_When_CityIsValid() {

        City mockCity = createMockCity();

        Mockito.when(mockRepository.isDuplicate(anyString(), anyInt()))
                .thenReturn(false);

        // Act
        service.update(mockCity, createMockEmployee());

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(Mockito.any(City.class));
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
