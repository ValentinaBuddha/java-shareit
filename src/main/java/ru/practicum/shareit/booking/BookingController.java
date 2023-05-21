//package ru.practicum.shareit.booking;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//import ru.practicum.shareit.item.ItemDto;
//import ru.practicum.shareit.utils.Create;
//
//@Slf4j
//@RequiredArgsConstructor
//@RestController
//@RequestMapping(path = "/bookings")
//public class BookingController {
//    private final BookingService bookingService;
//
//    @PostMapping
//    public BookingDto saveNewBooking(@Validated(Create.class) @RequestBody BookingDto bookingDto,
//                               @RequestHeader("X-Sharer-User-Id") long userId) {
//        log.info("POST / bookings");
//        return bookingService.saveNewBooking(bookingDto, userId);
//    }
//
////    @PatchMapping("/{bookingId}")
////    public BookingDto approve(@RequestHeader("X-Sharer-User-Id") int userId,
////                              @PathVariable int bookingId,
////                              @RequestParam(name = "approved") Boolean isApproved) {
////        return bookingService.approve(userId, bookingId, isApproved);
////    }
//}
