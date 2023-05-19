package ru.practicum.shareit.item;

import java.util.List;

public interface ItemService {
    ItemDto getItemById(long itemId);

    List<ItemDto> getItemsByOwner(long userId);

    List<ItemDto> getItemBySearch(String text);

    ItemDto saveNewItem(ItemDto itemDto, long userId);

    ItemDto updateItem(long itemId, ItemDto itemDto, long userId);
}
