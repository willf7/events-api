package com.events.api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.events.api.config.JWTUserData;
import com.events.api.domain.address.AddressRequestDTO;
import com.events.api.domain.coupon.Coupon;
import com.events.api.domain.event.Event;
import com.events.api.domain.event.EventDetailsDto;
import com.events.api.domain.event.EventRequestDTO;
import com.events.api.domain.event.EventResponseDTO;
import com.events.api.domain.user.User;
import com.events.api.exceptions.EntityNotFoundException;
import com.events.api.repositories.EventRepository;
import com.events.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class EventService {
    @Value("${storage.bucket}")
    private String bucket;

    private final AmazonS3 s3Client;
    private final EventRepository repository;
    private final AddressService addressService;
    private final CouponService couponService;
    private final UserRepository userRepository;

    public EventService(AmazonS3 s3Client, EventRepository repository, AddressService addressService, CouponService couponService, UserRepository userRepository) {
        this.s3Client = s3Client;
        this.repository = repository;
        this.addressService = addressService;
        this.couponService = couponService;
        this.userRepository = userRepository;
    }

    public Event createEvent(EventRequestDTO data) {
        JWTUserData userData = (JWTUserData) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User owner = userRepository.findById(userData.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        String imgUrl = null;

        if (data.image() != null) {
            imgUrl = this.uploadImage(data.image());
        }

        Event newEvent = new Event();
        newEvent.setTitle(data.title());
        newEvent.setDescription(data.description());
        newEvent.setEventUrl(data.eventUrl());
        newEvent.setImgUrl(imgUrl);
        newEvent.setRemote(data.remote());
        newEvent.setDate(data.date());
        newEvent.setOwner(owner);

        repository.save(newEvent);

        if (Boolean.FALSE.equals(data.remote())) {
            addressService.createAddress(newEvent, new AddressRequestDTO(data.state(), data.city()));
        }

        return newEvent;
    }

    private String uploadImage(MultipartFile multipartFile) {
        String fileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        try {
            File file = this.convertMultipartToFile(multipartFile);
            s3Client.putObject(bucket, fileName, file);
            file.delete();
            return s3Client.getUrl(bucket, fileName).toString();
        } catch (Exception e) {
            System.out.println("Erro on upload image.");
            return null;
        }
    }

    private File convertMultipartToFile(MultipartFile multipartFile) throws IOException {
        File convertedFile = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(multipartFile.getBytes());
        fos.close();

        return convertedFile;
    }

    public List<EventResponseDTO> getUpcomingEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventsPage = repository.findUpcomingEvents(OffsetDateTime.now(), pageable);

        return eventsPage.map(event -> new EventResponseDTO(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getEventUrl(),
                        event.getRemote(),
                        event.getAddress() != null ? event.getAddress().getCity() : "",
                        event.getAddress() != null ? event.getAddress().getState() : "",
                        event.getDate(),
                        event.getImgUrl()))
                .stream().toList();
    }

    public List<EventResponseDTO> getFilteredEvents(int page, int size, String title, String city, String state, OffsetDateTime startDate, OffsetDateTime endDate) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventsPage = repository.findFilteredEvents(title, city, state, startDate, endDate, pageable);

        return eventsPage.map(event -> new EventResponseDTO(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getEventUrl(),
                        event.getRemote(),
                        event.getAddress() != null ? event.getAddress().getCity() : "",
                        event.getAddress() != null ? event.getAddress().getState() : "",
                        event.getDate(),
                        event.getImgUrl()))
                .stream().toList();
    }

    public EventDetailsDto getEventDetails(UUID eventId) {
        Event event = repository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        List<Coupon> coupons = couponService.getCouponsByEvent(event.getId(), OffsetDateTime.now());

        List<EventDetailsDto.CouponDTO> couponDTOS = coupons.stream()
                .map(coupon -> new EventDetailsDto.CouponDTO(
                        coupon.getCode(),
                        coupon.getDiscount(),
                        coupon.getValidUntil()
                )).toList();

        return new EventDetailsDto(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getEventUrl(),
                event.getRemote(),
                event.getAddress() != null ?  event.getAddress().getCity() : "",
                event.getAddress() != null ? event.getAddress().getState() : "",
                event.getDate(),
                event.getImgUrl(),
                couponDTOS
                );
    }

    public void deleteEvent(UUID eventId) {
        Event event = repository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        repository.delete(event);
    }
}
