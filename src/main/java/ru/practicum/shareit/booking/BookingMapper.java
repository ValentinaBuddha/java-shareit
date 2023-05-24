package ru.practicum.shareit.booking;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BookingMapper {
    public BookingDtoOut toBookingDtoOut(Booking booking) {
        return new BookingDtoOut(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus()
        );
    }

//    public Booking toBooking(BookingDtoIn bookingDtoIn) {
//        return new Booking(
//                bookingDtoIn.getId(),
//                bookingDtoIn.getStart(),
//                bookingDtoIn.getEnd(),
//                bookingDtoIn.getItemId(),
//                bookingDtoIn.getBooker(),
//                bookingDtoIn.getStatus()
//        );
//    }

    public static Booking toBooking(BookingDtoIn bookingDtoIn, Booking booking) {
        booking.setStart(bookingDtoIn.getStart());
        booking.setEnd(bookingDtoIn.getEnd());
        booking.setStatus(BookingStatus.WAITING);

        return booking;
    }
}
