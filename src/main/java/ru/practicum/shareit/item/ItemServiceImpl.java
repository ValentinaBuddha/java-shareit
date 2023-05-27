package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.NotBookerException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.item.comment.*;
import ru.practicum.shareit.item.comment.dto.CommentDtoIn;
import ru.practicum.shareit.item.comment.dto.CommentDtoOut;
import ru.practicum.shareit.item.comment.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDtoOut saveNewItem(ItemDtoIn itemDtoIn, long userId) {
        log.info("Создание новой вещи {}", itemDtoIn.getName());
        User owner = getUser(userId);
        Item item = ItemMapper.toItem(itemDtoIn);
        item.setOwner(owner);
        return ItemMapper.toItemDtoOut(itemRepository.save(item));
    }

    @Override
    public ItemDtoOut updateItem(long itemId, ItemDtoIn itemDtoIn, long userId) {
        log.info("Обновление вещи {} с идентификатором {}", itemDtoIn.getName(), itemId);
        getUser(userId);
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Объект класса %s не найден", Item.class)));
        String name = itemDtoIn.getName();
        String description = itemDtoIn.getDescription();
        Boolean available = itemDtoIn.getAvailable();
        if (item.getOwner().getId() == userId) {
            if (name != null && !name.isBlank()) {
                item.setName(name);
            }
            if (description != null && !description.isBlank()) {
                item.setDescription(description);
            }
            if (available != null) {
                item.setAvailable(available);
            }
        } else {
            throw new NotOwnerException(String.format("Пользователь с id %s не является собственником %s",
                    userId, name));
        }
        return ItemMapper.toItemDtoOut(item);
    }

    @Transactional(readOnly = true)
    @Override
    public ItemDtoOut getItemById(long itemId, long userId) {
        log.info("Получение вещи по идентификатору {}", itemId);
        return itemRepository.findById(itemId).map(item -> addBookingsAndComments(item, userId)).orElseThrow(() ->
                new EntityNotFoundException(String.format("Объект класса %s не найден", Item.class)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDtoOut> getItemsByOwner(long userId) {
        log.info("Получение вещи по владельцу {}", userId);
        getUser(userId);
        List<Item> items = itemRepository.findAllByOwnerId(userId);
        return addBookingsAndCommentsForInList(items);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDtoOut> getItemBySearch(String text) {
        log.info("Получение вещи по поиску {}", text);
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.search(text).stream().map(ItemMapper::toItemDtoOut).collect(toList());
    }

    @Override
    public CommentDtoOut saveNewComment(long itemId, CommentDtoIn commentDtoIn, long userId) {
        User user = getUser(userId);
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Объект класса %s не найден", Item.class)));
        if (!bookingRepository.existsByBookerIdAndItemIdAndEndBefore(user.getId(), item.getId(), LocalDateTime.now())) {
            throw new NotBookerException("Пользователь не пользовался вещью");
        }
        Comment comment = commentRepository.save(CommentMapper.toComment(commentDtoIn, item, user));
        return CommentMapper.toCommentDtoOut(comment);
    }

    private ItemDtoOut addBookingsAndComments(Item item, long userId) {
        ItemDtoOut itemDtoOut = ItemMapper.toItemDtoOut(item);

        LocalDateTime thisMoment = LocalDateTime.now();
        if (itemDtoOut.getOwner().getId() == userId) {
            itemDtoOut.setLastBooking(bookingRepository
                    .findFirstByItemIdAndStartLessThanEqualAndStatus(itemDtoOut.getId(), thisMoment,
                            BookingStatus.APPROVED, Sort.by(DESC, "end"))
                    .map(BookingMapper::toBookingDtoShort)
                    .orElse(null));

            itemDtoOut.setNextBooking(bookingRepository
                    .findFirstByItemIdAndStartAfterAndStatus(itemDtoOut.getId(), thisMoment,
                            BookingStatus.APPROVED, Sort.by(ASC, "end"))
                    .map(BookingMapper::toBookingDtoShort)
                    .orElse(null));
        }

        itemDtoOut.setComments(commentRepository.findAllByItemId(itemDtoOut.getId())
                .stream()
                .map(CommentMapper::toCommentDtoOut)
                .collect(toList()));

        return itemDtoOut;
    }

    private List<ItemDtoOut> addBookingsAndCommentsForInList(List<Item> items) {
        LocalDateTime thisMoment = LocalDateTime.now();

        Map<Item, Booking> itemsWithLastBookings = bookingRepository
                .findFirstByItemInAndStartLessThanEqualAndStatus(items, thisMoment,
                        BookingStatus.APPROVED, Sort.by(DESC, "end"))
                .stream()
                .collect(Collectors.toMap(Booking::getItem, Function.identity()));

        Map<Item, Booking> itemsWithNextBookings = bookingRepository
                .findFirstByItemInAndStartAfterAndStatus(items, thisMoment,
                        BookingStatus.APPROVED, Sort.by(ASC, "end"))
                .stream()
                .collect(Collectors.toMap(Booking::getItem, Function.identity()));

        Map<Item, List<Comment>> itemsWithComments = commentRepository
                .findByItemIn(items, Sort.by(DESC, "created"))
                .stream()
                .collect(groupingBy(Comment::getItem, toList()));

        List<ItemDtoOut> itemDtoOuts = new ArrayList<>();
        for (Item item : items) {
            ItemDtoOut itemDtoOut = ItemMapper.toItemDtoOut(item);
            Booking lastBooking = itemsWithLastBookings.get(item);
            if (itemsWithLastBookings.size() > 0 && lastBooking != null) {
                itemDtoOut.setLastBooking(BookingMapper.toBookingDtoShort(lastBooking));
            }
            Booking nextBooking = itemsWithNextBookings.get(item);
            if (itemsWithNextBookings.size() > 0 && nextBooking != null) {
                itemDtoOut.setNextBooking(BookingMapper.toBookingDtoShort(nextBooking));
            }
            List<CommentDtoOut> commentDtoOuts = itemsWithComments.getOrDefault(item, Collections.emptyList())
                    .stream()
                    .map(CommentMapper::toCommentDtoOut)
                    .collect(toList());
            itemDtoOut.setComments(commentDtoOuts);

            itemDtoOuts.add(itemDtoOut);
        }
        return itemDtoOuts;
    }

    private User getUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Объект класса %s не найден", User.class)));
    }
}
