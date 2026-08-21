package com.events.api.address.service;

import com.events.api.address.model.Address;
import com.events.api.address.dto.AddressRequestDTO;
import com.events.api.user.model.User;
import com.events.api.address.repository.AddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressService {
    private final AddressRepository repository;

    public AddressService(AddressRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Address createAddress(User owner, AddressRequestDTO data) {
        Address address = new Address();
        address.setOwner(owner);
        address.setCity(data.city());
        address.setState(data.state());

        return repository.save(address);
    }
}
