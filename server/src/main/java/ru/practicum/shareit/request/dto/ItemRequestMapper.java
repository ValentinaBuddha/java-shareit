package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.ItemRequest;

@UtilityClass
public class ItemRequestMapper {
    public ItemRequest toItemRequest(ItemRequestDtoIn requestDtoIn) {
        return new ItemRequest(
                requestDtoIn.getDescription()
        );
    }

    public ItemRequestDtoOut toItemRequestDtoOut(ItemRequest request) {
        return new ItemRequestDtoOut(
                request.getId(),
                request.getDescription(),
                request.getRequestor().getId(),
                request.getCreated()
        );
    }
}