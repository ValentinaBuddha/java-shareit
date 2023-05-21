//package ru.practicum.shareit.booking;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import ru.practicum.shareit.exception.NotUniqueEmailException;
//import ru.practicum.shareit.item.ItemDto;
//import ru.practicum.shareit.item.ItemService;
//import ru.practicum.shareit.user.UserDto;
//import ru.practicum.shareit.user.UserService;
//
//@Service
//@RequiredArgsConstructor
//public class BookingServiceImpl implements BookingService {
//    private final BookingRepository bookingRepository;
//    private final UserService userService;
//    private final ItemService itemService;
//
//    @Override
//    public BookingDto saveNewBooking(BookingDto bookingDto, long userId) {
//        UserDto user = userService.getUserById(userId);
//        ItemDto item = itemService.getItemById(bookingDto.getItemId());
//        if (!item.getAvailable()) {
//            throw new IllegalStateException("Вещь недоступна для брони");
//        }
//        if (user.getId() == item.getOwnerId()) {
//            throw new IllegalArgumentException("Функция бронировать собственную вещь отсутствует");
//        }
//        if (!bookingDto.getEnd().isAfter(bookingDto.getStart())) {
//            throw new NotUniqueEmailException("Дата начала бронирования должна быть раньше");
//        }
//        Booking booking = bookingRepository.save(BookingMapper.toBooking(bookingDto));
//        return BookingMapper.toBookingDto(booking);
//    }
//
//
//}
