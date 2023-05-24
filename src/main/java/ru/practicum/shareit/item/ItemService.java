package ru.practicum.shareit.item;

import ru.practicum.shareit.item.comment.CommentDtoIn;
import ru.practicum.shareit.item.comment.CommentDtoOut;

import java.util.List;

public interface ItemService {
    ItemDto getItemById(long itemId, long userId);

    List<ItemDto> getItemsByOwner(long userId);

    List<ItemDto> getItemBySearch(String text);

    ItemDto saveNewItem(ItemDto itemDto, long userId);

    ItemDto updateItem(long itemId, ItemDto itemDto, long userId);

    CommentDtoOut saveNewComment(long itemId, CommentDtoIn commentDtoIn, long userId);
}
