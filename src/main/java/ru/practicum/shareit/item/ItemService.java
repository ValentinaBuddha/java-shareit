package ru.practicum.shareit.item;

import ru.practicum.shareit.item.comment.dto.CommentDtoIn;
import ru.practicum.shareit.item.comment.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;

import java.util.List;

public interface ItemService {
    ItemDtoOut getItemById(long itemId, long userId);

    List<ItemDtoOut> getItemsByOwner(long userId);

    List<ItemDtoOut> getItemBySearch(String text);

    ItemDtoOut saveNewItem(ItemDtoIn itemDtoIn, long userId);

    ItemDtoOut updateItem(long itemId, ItemDtoIn itemDtoIn, long userId);

    CommentDtoOut saveNewComment(long itemId, CommentDtoIn commentDtoIn, long userId);
}
