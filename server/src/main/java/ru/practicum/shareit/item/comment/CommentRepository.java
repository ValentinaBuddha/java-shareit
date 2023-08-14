package ru.practicum.shareit.item.comment;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.Item;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByItemId(long itemId);

    List<Comment> findByItemIn(List<Item> allByOwnerId, Sort created);
}
