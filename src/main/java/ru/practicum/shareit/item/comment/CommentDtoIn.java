package ru.practicum.shareit.item.comment;

import lombok.Data;
import ru.practicum.shareit.utils.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CommentDtoIn {
    @Size(max = 1000)
    @NotBlank(groups = {Create.class})
    private String text;
}
