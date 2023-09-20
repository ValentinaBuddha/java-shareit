package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exception.WrongDatesException;
import ru.practicum.shareit.utils.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingGController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> saveNewBooking(@Validated(Create.class) @RequestBody BookingDtoRequest bookingDto,
                                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("POST / bookings");
        if (!bookingDto.getEnd().isAfter(bookingDto.getStart())) {
            throw new WrongDatesException("Дата начала бронирования должна быть раньше даты возврата");
        }
        return bookingClient.saveNewBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@PathVariable long bookingId,
                                          @RequestParam(name = "approved") Boolean isApproved,
                                          @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("PATCH / bookings / {}", bookingId);
        return bookingClient.approve(bookingId, isApproved, userId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@PathVariable long bookingId,
                                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBookingById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByBooker(@RequestParam(defaultValue = "1") @PositiveOrZero Integer from,
                                                 @RequestParam(defaultValue = "10") @Positive Integer size,
                                                 @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                                 @RequestHeader("X-Sharer-User-Id") long bookerId) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("GET / ByBooker {}", bookerId);
        return bookingClient.getAllByBooker(from, size, state, bookerId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByOwner(@RequestParam(defaultValue = "1") @PositiveOrZero Integer from,
                                                @RequestParam(defaultValue = "10") @Positive Integer size,
                                                @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                                @RequestHeader("X-Sharer-User-Id") long ownerId) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("GET / ByOwner / {}", ownerId);
        return bookingClient.getAllByOwner(from, size, state, ownerId);
    }
}
