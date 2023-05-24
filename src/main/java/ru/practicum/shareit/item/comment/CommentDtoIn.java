package ru.practicum.shareit.item.comment;

import lombok.Data;
import ru.practicum.shareit.utils.Create;

import javax.validation.constraints.NotBlank;

@Data
public class CommentDtoIn {
    @NotBlank(groups = {Create.class})
    private String text;
}
