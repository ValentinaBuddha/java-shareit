package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.exception.NotBookerException;
import ru.practicum.shareit.item.comment.*;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto saveNewItem(ItemDto itemDto, long userId) {
        log.info("Создание новой вещи {}", itemDto.getName());
        User owner = UserMapper.toUser(userService.getUserById(userId));
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(owner);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(long itemId, ItemDto itemDto, long userId) {
        log.info("Обновление вещи {} с идентификатором {}", itemDto.getName(), itemId);
        userService.getUserById(userId);
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Объект класса %s не найден", Item.class)));
        String name = itemDto.getName();
        String description = itemDto.getDescription();
        Boolean available = itemDto.getAvailable();
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
        return ItemMapper.toItemDto(item);
    }

    @Transactional(readOnly = true)
    @Override
    public ItemDto getItemById(long itemId, long userId) {
        log.info("Получение вещи по идентификатору {}", itemId);
        return itemRepository.findById(itemId).map(item -> addBookingsAndComments(item, userId)).orElseThrow(() ->
                new EntityNotFoundException(String.format("Объект класса %s не найден", Item.class)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> getItemsByOwner(long userId) {
        log.info("Получение вещи по владельцу {}", userId);
        userService.getUserById(userId);
        return itemRepository.findAllByOwnerId(userId).stream()
                .map(item -> addBookingsAndComments(item, userId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> getItemBySearch(String text) {
        log.info("Получение вещи по поиску {}", text);
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.search(text).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public CommentDtoOut saveNewComment(long itemId, CommentDtoIn commentDtoIn, long userId) {
        User user = UserMapper.toUser(userService.getUserById(userId));
        Item item = ItemMapper.toItem(getItemById(itemId, userId));
        if (!bookingRepository.existsByBookerIdAndItemIdAndEndBefore(user.getId(), item.getId(), LocalDateTime.now())) {
            throw new NotBookerException("Пользователь не пользовался вещью");
        }
        Comment comment = commentRepository.save(CommentMapper.toComment(commentDtoIn, item, user));
        return CommentMapper.toCommentDtoOut(comment);
    }

    private ItemDto addBookingsAndComments(Item item, long userId) {
        ItemDto itemDto = ItemMapper.toItemDto(item);

        if (itemDto.getOwner().getId() == userId) {
            itemDto.setLastBooking(bookingRepository
                    .findFirstByItemIdAndStartBeforeAndStatusOrderByEndDesc(itemDto.getId(), LocalDateTime.now(),
                            BookingStatus.APPROVED)
                    .map(BookingMapper::toBookingDtoShort)
                    .orElse(null));

            itemDto.setNextBooking(bookingRepository
                    .findFirstByItemIdAndStartAfterAndStatusOrderByEndAsc(itemDto.getId(), LocalDateTime.now(),
                            BookingStatus.APPROVED)
                    .map(BookingMapper::toBookingDtoShort)
                    .orElse(null));
        }

        itemDto.setComments(commentRepository.findAllByItemId(itemDto.getId())
                .stream()
                .map(CommentMapper::toCommentDtoOut)
                .collect(Collectors.toList()));

        return itemDto;
    }
}
