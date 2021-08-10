package com.team9.deliverit;

import com.team9.deliverit.models.*;
import com.team9.deliverit.models.enums.Category;
import com.team9.deliverit.models.enums.PickUpOption;
import com.team9.deliverit.models.enums.Status;

import java.sql.Date;
import java.time.LocalDate;


public class Helpers {

    public static User createMockCustomer() {
        var mockUser = new User();
        mockUser.setId(1);
        mockUser.setEmail("mock@user.com");
        mockUser.setLastName("MockLastName");
        mockUser.setFirstName("MockFirstName");
        mockUser.setAddress(createMockAddress());
        mockUser.setRole(createMockCustomerRole());
        return mockUser;
    }

    public static Role createMockCustomerRole() {
        var mockRole = new Role();
        mockRole.setId(1);
        mockRole.setName("Customer");
        return mockRole;
    }

    public static User createMockEmployee() {
        var mockUser = new User();
        mockUser.setId(1);
        mockUser.setEmail("mock12@user.com");
        mockUser.setLastName("MockLastName");
        mockUser.setFirstName("MockFirstName");
        mockUser.setAddress(createMockAddress());
        mockUser.setRole(createMockEmployeeRole());
        return mockUser;
    }

    public static Role createMockEmployeeRole() {
        var mockRole = new Role();
        mockRole.setId(2);
        mockRole.setName("Employee");
        return mockRole;
    }

    public static Address createMockAddress() {
        var mockAddress = new Address();
        mockAddress.setId(1);
        mockAddress.setStreetName("Dondukov");
        mockAddress.setCity(createMockCity());

        return mockAddress;

    }

    public static Country createMockCountry() {
        var mockCountry = new Country();
        mockCountry.setId(1);
        mockCountry.setName("Bulgaria");

        return mockCountry;
    }

    public static City createMockCity() {
        var mockCity = new City();
        mockCity.setId(1);
        mockCity.setName("Sofia");
        mockCity.setCountry(createMockCountry());

        return mockCity;
    }

    public static Warehouse createMockWarehouse() {
        var mockWarehouse = new Warehouse();
        mockWarehouse.setId(1);
        mockWarehouse.setAddress(createMockAddress());

        return mockWarehouse;
    }

    public static Shipment createMockShipment() {
        var mockShipment = new Shipment();
        mockShipment.setId(1);
        mockShipment.setArrivalDate(Date.valueOf(LocalDate.MAX));
        mockShipment.setDepartureDate(Date.valueOf(LocalDate.MIN));
        mockShipment.setFull(false);
        mockShipment.setStatus(Status.ON_THE_WAY);
        mockShipment.setOriginWarehouse(createMockWarehouse());
        Address address = createMockAddress();
        address.setStreetName("Pirotska");
        address.setId(2);
        Warehouse warehouse = createMockWarehouse();
        warehouse.setAddress(address);
        warehouse.setId(2);
        mockShipment.setDestinationWarehouse(warehouse);

        return mockShipment;
    }

    public static Parcel createMockParcel() {
        var mockParcel = new Parcel();
        mockParcel.setId(1);
        mockParcel.setPickUpOption(PickUpOption.PICK_UP_FROM_WAREHOUSE);
        mockParcel.setUser(createMockCustomer());
        mockParcel.setShipment(createMockShipment());
        mockParcel.setCategory(Category.CLOTHING);
        mockParcel.setWeight(1.2);

        return mockParcel;
    }
}
