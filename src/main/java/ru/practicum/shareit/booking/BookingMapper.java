//package ru.practicum.shareit.booking;
//
//import lombok.experimental.UtilityClass;
//import ru.practicum.shareit.item.Item;
//import ru.practicum.shareit.item.ItemDto;
//
//@UtilityClass
//public class BookingMapper {
//    public BookingDto toBookingDto(Booking booking) {
//        return BookingDto.builder()
//                .id(booking.getId())
//                .start(booking.getStart())
//                .end(booking.getEnd())
//                .itemId(booking.getItemId())
//                .bookerId(booking.getBookerId())
//                .status(booking.getStatus())
//                .build();
//    }
//
//    public Booking toBooking(BookingDto bookingDto) {
//        return Booking.builder()
//                .id(bookingDto.getId())
//                .start(bookingDto.getStart())
//                .end(bookingDto.getEnd())
//                .itemId(bookingDto.getItemId())
//                .bookerId(bookingDto.getBookerId())
//                .status(bookingDto.getStatus())
//                .build();
//    }
//}
