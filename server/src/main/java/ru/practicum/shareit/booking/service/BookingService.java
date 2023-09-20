package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import java.util.List;

public interface BookingService {
    BookingDtoOut saveNewBooking(BookingDtoIn bookingDtoIn, long userId);

    BookingDtoOut approve(long bookingId, Boolean isApproved, long userId);

    BookingDtoOut getBookingById(long bookingId, long userId);

    List<BookingDtoOut> getAllByBooker(Integer from, Integer size, String state, long bookerId);

    List<BookingDtoOut> getAllByOwner(Integer from, Integer size, String state, long ownerId);
}
