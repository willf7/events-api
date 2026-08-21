package com.events.api.service;

import com.events.api.domain.address.Address;
import com.events.api.domain.address.AddressRequestDTO;
import com.events.api.domain.common.PageResponseDTO;
import com.events.api.domain.coupon.Coupon;
import com.events.api.domain.event.Event;
import com.events.api.domain.event.EventDetailsDTO;
import com.events.api.domain.event.EventMapper;
import com.events.api.domain.event.EventRequestDTO;
import com.events.api.domain.event.EventResponseDTO;
import com.events.api.domain.event.EventSearchRequestDTO;
import com.events.api.domain.event.EventUpdateRequestDTO;
import com.events.api.domain.user.User;
import com.events.api.exceptions.EntityNotFoundException;
import com.events.api.exceptions.FileUploadException;
import com.events.api.repositories.AddressRepository;
import com.events.api.repositories.EventRepository;
import com.events.api.repositories.UserRepository;
import com.events.api.security.JWTUserData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class EventService {
    @Value("${storage.bucket}")
    private String bucket;

    private final S3Client s3Client;
    private final EventRepository eventRepository;
    private final AddressService addressService;
    private final CouponService couponService;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final EventMapper eventMapper;

    public EventService(S3Client s3Client, EventRepository eventRepository, AddressService addressService, CouponService couponService, UserRepository userRepository, AddressRepository addressRepository, EventMapper eventMapper) {
        this.s3Client = s3Client;
        this.eventRepository = eventRepository;
        this.addressService = addressService;
        this.couponService = couponService;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.eventMapper = eventMapper;
    }

    public EventResponseDTO create(EventRequestDTO request, JWTUserData userData) {
        User owner = userRepository.findById(userData.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        String imgUrl = this.uploadImage(request.image());
        Address address = resolveAddress(request.remote(), request.addressId(), request.city(), request.state(), owner);

        Event event = eventMapper.toEntity(request, owner, imgUrl, address);

        eventRepository.save(event);

        return eventMapper.toResponseDTO(event);
    }

    private String uploadImage(MultipartFile multipartFile) {
        String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        try (InputStream inputStream = multipartFile.getInputStream()) {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(multipartFile.getContentType())
                    .build();

            s3Client.putObject(
                    request,
                    RequestBody.fromInputStream(
                            inputStream,
                            multipartFile.getSize()
                    )
            );

            return s3Client.utilities()
                    .getUrl(builder -> builder
                            .bucket(bucket)
                            .key(fileName))
                    .toExternalForm();
        } catch (Exception e) {
            log.error(
                    "Error uploading image to S3. filename={}",
                    fileName,
                    e
            );

            throw new FileUploadException(e);
        }
    }

    public PageResponseDTO<EventResponseDTO> findAll(int page, int size, EventSearchRequestDTO request) {
        Pageable pageable = PageRequest.of(page, size);
        String title = blankToNull(request.title());
        String city = blankToNull(request.city());
        String state = blankToNull(request.state());
        OffsetDateTime startDate = request.startDate() != null
                ? request.startDate()
                : OffsetDateTime.now();
        OffsetDateTime endDate = request.endDate();

        Page<Event> events = eventRepository.findFilteredEvents(title, city, state, startDate, endDate, pageable);

        return PageResponseDTO.from(events.map(eventMapper::toResponseDTO));
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    public EventDetailsDTO findById(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        List<Coupon> coupons = couponService.getCouponsByEvent(event.getId(), OffsetDateTime.now());

        return eventMapper.toDetailsDTO(event, coupons);
    }

    public EventResponseDTO update(UUID eventId, EventUpdateRequestDTO request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        Address address = resolveAddress(request.remote(), request.addressId(), request.city(), request.state(), event.getOwner());
        String imageUrl = null;

        if (request.image() != null && !request.image().isEmpty()) {
            imageUrl = this.uploadImage(request.image());
        }

        eventMapper.updateEntity(event, request, address);

        if (imageUrl != null) {
            event.setImgUrl(imageUrl);
        }

        Event savedEvent = eventRepository.save(event);

        return eventMapper.toResponseDTO(savedEvent);
    }

    private Address resolveAddress(Boolean remote, UUID addressId, String city, String state, User owner) {
        if (Boolean.TRUE.equals(remote)) {
            return null;
        }

        if (addressId != null) {
            return addressRepository.findByIdAndOwnerId(addressId, owner.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Address not found"));
        }

        return addressService.createAddress(owner, new AddressRequestDTO(state, city));
    }

    public void delete(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        eventRepository.delete(event);
    }
}
