package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private BookingService bookingService;

    private final User user = new User(1L, "User", "user@mail.ru");
    private final User booker = new User(2L, "user2", "user2@mail.ru");
    private final Item item = new Item(1L, "item", "cool", true, user, null);
    private final Booking booking = new Booking(1L,
            LocalDateTime.of(2023, 7, 1, 12, 12, 12),
            LocalDateTime.of(2023, 7, 30, 12, 12, 12),
            item, booker, BookingStatus.WAITING);
    private final BookingDtoIn bookingDtoIn = new BookingDtoIn(
            LocalDateTime.of(2023, 7, 1, 12, 12, 12),
            LocalDateTime.of(2023, 7, 30, 12, 12, 12), 1L);
    private final BookingDtoIn bookingDtoWrongItem = new BookingDtoIn(
            LocalDateTime.of(2023, 7, 1, 12, 12, 12),
            LocalDateTime.of(2023, 7, 30, 12, 12, 12), 2L);

    @Test
    void saveNewBooking_whenItemAvailable_thenSavedBooking() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(bookingRepository.save(any())).thenReturn(booking);

        BookingDtoOut actualBooking = bookingService.saveNewBooking(bookingDtoIn, 2L);

        Assertions.assertEquals(booking.getStart(), actualBooking.getStart());
        Assertions.assertEquals(booking.getEnd(), actualBooking.getEnd());
        Assertions.assertEquals(ItemMapper.toItemDtoShort(booking.getItem()), actualBooking.getItem());
        Assertions.assertEquals(UserMapper.toUserDtoShort(booking.getBooker()), actualBooking.getBooker());
    }

    @Test
    void saveNewBooking_whenUserNotFound_thenThrownException() {
        when((userRepository).findById(3L)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () ->
                bookingService.saveNewBooking(bookingDtoIn, 3L));
    }

    @Test
    void saveNewBooking_whenItemNotFound_thenThrownException() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when((itemRepository).findById(2L)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () ->
                bookingService.saveNewBooking(bookingDtoWrongItem, 2L));
    }

    @Test
    void saveNewBooking_whenItemNotAvailable_thenThrownException() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        item.setAvailable(false);

        Assertions.assertThrows(ItemIsNotAvailableException.class, () ->
                bookingService.saveNewBooking(bookingDtoIn, 2L));
    }

    @Test
    void saveNewBooking_whenBookerIsOwner_thenThrownException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Assertions.assertThrows(NotAvailableToBookOwnItemsException.class, () ->
                bookingService.saveNewBooking(bookingDtoIn, 1L));
    }

    @Test
    void saveNewBooking_whenOwnerIsBooker_thenThrownException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Assertions.assertThrows(NotAvailableToBookOwnItemsException.class, () ->
                bookingService.saveNewBooking(bookingDtoIn, 1L));
    }

    @Test
    void approve() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        BookingDtoOut actualBooking = bookingService.approve(1L, true, 1L);

        Assertions.assertEquals(BookingStatus.APPROVED, actualBooking.getStatus());
    }

    @Test
    void approve_whenBookingNotFound_thenThrownException() {
        when((bookingRepository).findById(2L)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () ->
                bookingService.approve(2L, true, 1L));
    }

    @Test
    void approve_whenItemAlreadyBooked_thenThrownException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        booking.setStatus(BookingStatus.APPROVED);

        Assertions.assertThrows(ItemIsNotAvailableException.class, () ->
                bookingService.approve(1L, true, 1L));
    }

    @Test
    void getBookingById_whenUserIsOwner_thenReturnBooking() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        BookingDtoOut actualBooking = bookingService.getBookingById(1L, 1L);

        Assertions.assertEquals(BookingMapper.toBookingDtoOut(booking), actualBooking);
    }

    @Test
    void getBookingById_whenUserIsNotAuthorOrOwner_thenThrownException() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Assertions.assertThrows(IllegalVewAndUpdateException.class, () ->
                bookingService.getBookingById(1L, 3L));
    }

    @Test
    void getAllByBooker_whenStateAll_thenReturnAllBookings() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBookerId(anyLong(), any())).thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookings = bookingService.getAllByBooker(0, 10, "ALL", 2L);

        Assertions.assertEquals(List.of(BookingMapper.toBookingDtoOut(booking)), actualBookings);
    }

    @Test
    void getAllByBooker_whenStateCurrent_thenReturnListOfBookings() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBookerIdAndStateCurrent(anyLong(), any())).thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookings = bookingService.getAllByBooker(0, 10, "CURRENT", 2L);

        Assertions.assertEquals(List.of(BookingMapper.toBookingDtoOut(booking)), actualBookings);
    }

    @Test
    void getAllByBooker_whenStatePast_thenReturnListOfBookings() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBookerIdAndStatePast(anyLong(), any())).thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookings = bookingService.getAllByBooker(0, 10, "PAST", 2L);

        Assertions.assertEquals(List.of(BookingMapper.toBookingDtoOut(booking)), actualBookings);
    }

    @Test
    void getAllByBooker_whenStateFuture_thenReturnListOfBookings() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBookerIdAndStateFuture(anyLong(), any())).thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookings = bookingService.getAllByBooker(0, 10, "FUTURE", 2L);

        Assertions.assertEquals(List.of(BookingMapper.toBookingDtoOut(booking)), actualBookings);
    }

    @Test
    void getAllByBooker_whenStateWaiting_thenReturnListOfBookings() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(bookingRepository.findAllByBookerIdAndStatus(anyLong(), any(), any())).thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookings = bookingService.getAllByBooker(0, 10, "WAITING", 2L);

        Assertions.assertEquals(List.of(BookingMapper.toBookingDtoOut(booking)), actualBookings);
    }

    @Test
    void getAllByBooker_whenStateUnsupported_thenExceptionThrown() {
        Assertions.assertThrows(UnsupportedStatusException.class, () ->
                bookingService.getAllByBooker(0, 10, "a", 2L));
    }

    @Test
    void getAllByOwner_whenStateAll_thenReturnAllBookings() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByOwnerId(anyLong(), any())).thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookings = bookingService.getAllByOwner(0, 10, "ALL", 1L);

        Assertions.assertEquals(List.of(BookingMapper.toBookingDtoOut(booking)), actualBookings);

    }

    @Test
    void getAllByOwner_whenStateCurrent_thenReturnListOfBookings() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByOwnerIdAndStateCurrent(anyLong(), any())).thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookings = bookingService.getAllByOwner(0, 10, "CURRENT", 1L);

        Assertions.assertEquals(List.of(BookingMapper.toBookingDtoOut(booking)), actualBookings);
    }

    @Test
    void getAllByOwner_whenStatePast_thenReturnListOfBookings() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByOwnerIdAndStatePast(anyLong(), any())).thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookings = bookingService.getAllByOwner(0, 10, "PAST", 1L);

        Assertions.assertEquals(List.of(BookingMapper.toBookingDtoOut(booking)), actualBookings);
    }

    @Test
    void getAllByOwner_whenStateFuture_thenReturnListOfBookings() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByOwnerIdAndStateFuture(anyLong(), any())).thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookings = bookingService.getAllByOwner(0, 10, "FUTURE", 1L);

        Assertions.assertEquals(List.of(BookingMapper.toBookingDtoOut(booking)), actualBookings);
    }

    @Test
    void getAllByOwner_whenStateWaiting_thenReturnListOfBookings() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByOwnerIdAndStatus(anyLong(), any(), any())).thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookings = bookingService.getAllByOwner(0, 10, "WAITING", 1L);

        Assertions.assertEquals(List.of(BookingMapper.toBookingDtoOut(booking)), actualBookings);
    }
}