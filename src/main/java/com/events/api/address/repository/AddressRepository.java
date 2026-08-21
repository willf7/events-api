package com.events.api.address.repository;

import com.events.api.address.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    Optional<Address> findByIdAndOwnerId(UUID id, UUID ownerId);
}
