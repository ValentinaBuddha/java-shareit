package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.BookingDtoShort;
import ru.practicum.shareit.item.comment.CommentDtoOut;
import ru.practicum.shareit.user.UserDtoShort;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemDtoOut {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDtoShort lastBooking;
    private BookingDtoShort nextBooking;
    private List<CommentDtoOut> comments;
    private UserDtoShort owner;

    public ItemDtoOut(long id, String name, String description, Boolean available, UserDtoShort owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
    }
}
