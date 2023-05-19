package ru.practicum.shareit.item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Optional<Item> getItemById(long itemId);

    List<Item> getItemsByOwner(long userId);

    List<Item> getItemBySearch(String text);

    Item saveNewItem(Item item, long userId);
}

