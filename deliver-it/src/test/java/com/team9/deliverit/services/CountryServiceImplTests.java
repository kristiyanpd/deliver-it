package com.team9.deliverit.services;

import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.Country;
import com.team9.deliverit.repositories.contracts.CountryRepository;
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
public class CountryServiceImplTests {

    @Mock
    CountryRepository mockRepository;

    @InjectMocks
    CountryServiceImpl service;

    @Test
    public void getById_Should_ReturnCountry_When_MatchExist() {
        // Arrange
        Country mockCountry = createMockCountry();
        Mockito.when(mockRepository.getById(anyInt()))
                .thenReturn(mockCountry);
        // Act
        Country result = service.getById(1);

        // Assert
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals(mockCountry.getName(), result.getName());
    }

    @Test
    public void getAll_Should_ReturnEmptyList_When_RepositoryEmpty() {
        // Arrange
        List<Country> list = new ArrayList<>();

        Mockito.when(mockRepository.getAll())
                .thenReturn(list);
        // Act
        List<Country> result = service.getAll();

        // Assert
        Assertions.assertEquals(0, result.size());
    }

    @Test
    public void getByName_Should_ReturnCountry_When_MatchExist() {
        // Arrange

        Country mockCountry = createMockCountry();

        Mockito.when(mockRepository.getByField(anyString(),anyString()))
                .thenReturn(mockCountry);
        // Act
        Country result = service.getByName("Bulgaria");

        // Assert
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals(mockCountry.getName(), result.getName());
    }

    @Test
    public void SearchByName_Should_ReturnListOfCountry_When_MatchExist() {

        List<Country> mockList = new ArrayList<>();
        var mockCountry = createMockCountry();

        mockList.add(mockCountry);

        Mockito.when(mockRepository.searchByFieldList(anyString(),anyString()))
                .thenReturn(mockList);

        List<Country> result = service.searchByName("Bulgaria");

        // Assert
        Assertions.assertEquals(1, result.get(0).getId());
        Assertions.assertEquals(mockCountry.getName(), result.get(0).getName());


    }

    @Test
    public void create_Should_Throw_When_UserNotEmployee() {

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.create(createMockCustomer(), createMockCountry()));
    }

    @Test
    public void create_Should_Throw_When_DuplicateExits() {


        Mockito.when(mockRepository.isDuplicate(anyString()))
                .thenReturn(true);

        Assertions.assertThrows(DuplicateEntityException.class,
                () -> service.create(createMockEmployee(), createMockCountry()));
    }

    @Test
    public void create_Should_Call_Repository_When_CountryIsValid() {

        Country mockCountry = createMockCountry();

        Mockito.when(mockRepository.isDuplicate(anyString()))
                .thenReturn(false);

        // Act
        service.create(createMockEmployee(), mockCountry);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(Mockito.any(Country.class));
    }

    @Test
    public void update_Should_Throw_When_UserNotEmployee() {

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> service.update(createMockCustomer(), createMockCountry()));
    }

    @Test
    public void update_Should_Throw_When_DuplicateExits() {

        Mockito.when(mockRepository.isDuplicate(anyString()))
                .thenReturn(true);


        Assertions.assertThrows(DuplicateEntityException.class,
                () -> service.update(createMockEmployee(), createMockCountry()));
    }

    @Test
    public void update_Should_Call_Repository_When_CountryIsValid() {

        Country mockCountry = createMockCountry();

        Mockito.when(mockRepository.isDuplicate(anyString()))
                .thenReturn(false);

        // Act
        service.update(createMockEmployee(), mockCountry);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(Mockito.any(Country.class));
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
