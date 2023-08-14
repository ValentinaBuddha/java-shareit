package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingRepository bookingRepository;

    private final User user = new User(null, "user", "user@mail.ru");
    private final User booker = new User(null, "user2", "user2@mail.ru");
    private final Item item = new Item(null, "item", "cool", true, user, null);
    private final Booking booking = new Booking(1L,
            LocalDateTime.of(2023, 7, 1, 12, 12, 12),
            LocalDateTime.of(2023, 7, 30, 12, 12, 12),
            item, booker, BookingStatus.WAITING);

    @BeforeEach
    void setUp() {
        userRepository.save(user);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);
    }

    @Test
    @DirtiesContext
    void findAllByBookerId() {
        List<Booking> bookings = bookingRepository.findAllByBookerId(2L, Pageable.ofSize(10));

        assertThat(bookings.get(0).getId(), equalTo(booking.getId()));
        assertThat(bookings.size(), equalTo(1));
    }
}