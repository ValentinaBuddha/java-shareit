package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDtoShort {
    private long id;
    private String name;
}