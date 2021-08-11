package com.team9.deliverit.services;

import com.team9.deliverit.repositories.contracts.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.team9.deliverit.Helpers.*;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTests {

    @Mock
    RoleRepository mockRepository;

    @InjectMocks
    RoleServiceImpl service;

    @Test
    public void getById_Should_ReturnRole_When_MatchExist() {
        // Arrange
        var mockRole = createMockCustomerRole();
        Mockito.when(mockRepository.getById(anyInt()))
                .thenReturn(mockRole);
        // Act
        var result = service.getById(1);

        // Assert
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals(mockRole.getName(), result.getName());
    }
}
