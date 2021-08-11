package com.team9.deliverit.services;

import com.team9.deliverit.exceptions.DuplicateEntityException;
import com.team9.deliverit.exceptions.UnauthorizedOperationException;
import com.team9.deliverit.models.User;
import com.team9.deliverit.models.Warehouse;
import com.team9.deliverit.models.dtos.WarehouseDisplayDto;
import com.team9.deliverit.repositories.contracts.WarehouseRepository;
import com.team9.deliverit.services.contracts.WarehouseService;
import com.team9.deliverit.services.mappers.WarehouseModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.team9.deliverit.services.utils.MessageConstants.UNAUTHORIZED_ACTION;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository repository;

    @Autowired
    public WarehouseServiceImpl(WarehouseRepository warehouseRepository) {
        this.repository = warehouseRepository;
    }

    @Override
    public List<Warehouse> getAll() {
        return repository.getAll();
    }

    @Override
    public Warehouse getById(User user, int id) {
        return repository.getById(id);
    }

    @Override
    public void create(Warehouse warehouse, User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "create", "warehouses"));
        }
        if (repository.isDuplicate(warehouse.getAddress().getId())) {
            throw new DuplicateEntityException("Warehouse", "address ID", String.valueOf(warehouse.getAddress().getId()));
        }
        repository.create(warehouse);
    }

    @Override
    public void update(Warehouse warehouse, User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "modify", "warehouses"));
        }
        if (repository.isDuplicate(warehouse.getAddress().getId())) {
            throw new DuplicateEntityException("Warehouse", "address ID", String.valueOf(warehouse.getAddress().getId()));
        }
        repository.update(warehouse);
    }

    @Override
    public void delete(int id, User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException(String.format(UNAUTHORIZED_ACTION, "employees", "delete", "warehouses"));
        }

        repository.delete(id);
    }
}
