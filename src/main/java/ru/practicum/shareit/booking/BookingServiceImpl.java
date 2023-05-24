package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    public BookingDtoOut saveNewBooking(BookingDtoIn bookingDtoIn, long userId) {
        User booker = UserMapper.toUser(userService.getUserById(userId));
        Item item = ItemMapper.toItem(itemService.getItemById(bookingDtoIn.getItemId(), userId));
        if (!item.getAvailable()) {
            throw new ItemIsNotAvailableException("Вещь недоступна для брони");
        }
        if (booker.getId() == item.getOwner().getId()) {
            throw new NotAvailableToBookOwnItemsException("Функция бронировать собственную вещь отсутствует");
        }
        if (!bookingDtoIn.getEnd().isAfter(bookingDtoIn.getStart()) ||
                bookingDtoIn.getStart().isBefore(LocalDateTime.now())) {
            throw new WrongDatesException("Дата начала бронирования должна быть раньше даты возврата");
        }
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        bookingRepository.save(BookingMapper.toBooking(bookingDtoIn, booking));
        log.info("Бронирование с идентификатором {} создано", booking.getId());
        return BookingMapper.toBookingDtoOut(booking);
    }

    @Override
    public BookingDtoOut approve(long bookingId, Boolean isApproved, long userId) {
        User owner = UserMapper.toUser(userService.getUserById(userId));
        Booking booking = getById(bookingId);
        Item item = ItemMapper.toItem(itemService.getItemById(booking.getItem().getId(), userId));
        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ItemIsNotAvailableException("Вещь уже забронирована");
        }
        if (owner.getId() != item.getOwner().getId()) {
            throw new IllegalVewAndUpdateException("Подтвердить бронирование может только собственник вещи");
        }
        BookingStatus newBookingStatus = isApproved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        booking.setStatus(newBookingStatus);
        bookingRepository.save(booking);
        log.info("Бронирование с идентификатором {} обновлено", booking.getId());
        return BookingMapper.toBookingDtoOut(booking);
    }

    @Transactional(readOnly = true)
    @Override
    public BookingDtoOut getBookingById(long bookingId, long userId) {
        log.info("Получение бронирования по идентификатору {}", bookingId);
        Booking booking = getById(bookingId);
        User booker = booking.getBooker();
        User owner = UserMapper.toUser(userService.getUserById(booking.getItem().getOwner().getId()));
        if (booker.getId() != userId && owner.getId() != userId) {
            throw new IllegalVewAndUpdateException("Только автор или владелец может просматривать данное броинрование");
        }
        return BookingMapper.toBookingDtoOut(booking);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDtoOut> getAllByBooker(String state, long bookerId) {
        User booker = UserMapper.toUser(userService.getUserById(bookerId));
        List<Booking> bookings;
        switch (state.toUpperCase()) {
            case "ALL":
                bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(booker.getId());
                break;
            case "CURRENT":
                bookings = bookingRepository.findAllByBookerIdAndStateCurrentOrderByStartDesc(booker.getId());
                break;
            case "PAST":
                bookings = bookingRepository.findAllByBookerIdAndStatePastOrderByStartDesc(booker.getId());
                break;
            case "FUTURE":
                bookings = bookingRepository.findAllByBookerIdAndStateFutureOrderByStartDesc(booker.getId());
                break;
            case "WAITING":
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(booker.getId(),
                        BookingStatus.WAITING);
                break;
            case "REJECTED":
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(booker.getId(),
                        BookingStatus.REJECTED);
                break;
            default:
                throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream().map(BookingMapper::toBookingDtoOut).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDtoOut> getAllByOwner(long ownerId, String state) {
        User owner = UserMapper.toUser(userService.getUserById(ownerId));
        List<Booking> bookings;
        switch (state.toUpperCase()) {
            case "ALL":
                bookings = bookingRepository.findAllByOwnerIdOrderByStartDesc(owner.getId());
                break;
            case "CURRENT":
                bookings = bookingRepository.findAllByOwnerIdAndStateCurrentOrderByStartDesc(owner.getId());
                break;
            case "PAST":
                bookings = bookingRepository.findAllByOwnerIdAndStatePastOrderByStartDesc(owner.getId());
                break;
            case "FUTURE":
                bookings = bookingRepository.findAllByOwnerIdAndStateFutureOrderByStartDesc(owner.getId());
                break;
            case "WAITING":
                bookings = bookingRepository.findAllByOwnerIdAndStatusOrderByStartDesc(owner.getId(),
                        BookingStatus.WAITING);
                break;
            case "REJECTED":
                bookings = bookingRepository.findAllByOwnerIdAndStatusOrderByStartDesc(owner.getId(),
                        BookingStatus.REJECTED);
                break;
            default:
                throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream().map(BookingMapper::toBookingDtoOut).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Booking getById(long bookingId) {
        log.info("Получение бронирования по идентификатору {}", bookingId);
        return bookingRepository.findById(bookingId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Объект класса %s не найден", Booking.class)));
    }
}