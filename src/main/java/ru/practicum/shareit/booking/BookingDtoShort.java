package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDtoShort {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
    private long bookerId;

}
