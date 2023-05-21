//package ru.practicum.shareit.booking;
//
//import lombok.Builder;
//import lombok.Data;
//import ru.practicum.shareit.utils.Create;
//
//import javax.validation.constraints.Future;
//import javax.validation.constraints.NotNull;
//import java.time.LocalDateTime;
//
//@Data
//@Builder
//public class BookingDto {
//
//    private long id;
//
//    @Future
//    @NotNull(groups = {Create.class})
//    private LocalDateTime start;
//
//    @Future
//    @NotNull(groups = {Create.class})
//    private LocalDateTime end;
//
//    @NotNull(groups = {Create.class})
//    private long itemId;
//
//    private long bookerId;
//
//    private BookingStatus status;
//}
