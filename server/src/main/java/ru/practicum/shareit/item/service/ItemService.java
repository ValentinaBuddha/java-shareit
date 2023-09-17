package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.dto.CommentDtoIn;
import ru.practicum.shareit.item.comment.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;

import java.util.List;

public interface ItemService {
    ItemDtoOut saveNewItem(ItemDtoIn itemDtoIn, long userId);

    ItemDtoOut updateItem(long itemId, ItemDtoIn itemDtoIn, long userId);

    ItemDtoOut getItemById(long itemId, long userId);

    List<ItemDtoOut> getItemsByOwner(Integer from, Integer size, long userId);

    List<ItemDtoOut> getItemBySearch(Integer from, Integer size, String text);

    CommentDtoOut saveNewComment(long itemId, CommentDtoIn commentDtoIn, long userId);
}
