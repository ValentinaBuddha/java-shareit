package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;


    private final User user = new User(null, "user", "user@mail.ru");
    private final Item item = new Item(null, "item", "cool", true, user, null);
    private final Comment comment = new Comment(null, "abc", item, user,
            LocalDateTime.of(2023, 7, 1, 12, 12, 12));

    @BeforeEach
    void setUp() {
        userRepository.save(user);
        itemRepository.save(item);
        commentRepository.save(comment);
    }

    @Test
    @DirtiesContext
    void findAllByItemId() {
        List<Comment> comments = commentRepository.findAllByItemId(item.getId());

        assertThat(comments.get(0).getId(), notNullValue());
        assertThat(comments.get(0).getText(), equalTo(comment.getText()));
        assertThat(comments.size(), equalTo(1));
    }
}