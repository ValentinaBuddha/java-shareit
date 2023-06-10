package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    private final User user = new User(1L, "user", "user@mail.ru");
    private final Item item = new Item(1L, "item", "cool", true, user, null);

    @BeforeEach
    void setUp() {
        userRepository.save(user);
        itemRepository.save(item);
    }

    @Test
    void findAllByOwnerId() {
        List<Item> items = itemRepository.findAllByOwnerId(1L, Pageable.ofSize(10));

        assertThat(items.get(0).getId(), notNullValue());
        assertThat(items.get(0).getName(), equalTo(item.getName()));
        assertThat(items.size(), equalTo(1));
    }
}