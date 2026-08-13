package com.events.api.service;

import com.events.api.domain.address.Address;
import com.events.api.domain.address.AddressRequestDTO;
import com.events.api.domain.event.Event;
import com.events.api.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    @Autowired
    private AddressRepository repository;

    public Address createAddress(Event event, AddressRequestDTO data) {
        Address address = new Address();
        address.setCity(data.city());
        address.setEvent(event);
        address.setState(data.state());

        return repository.save(address);
    }
}
