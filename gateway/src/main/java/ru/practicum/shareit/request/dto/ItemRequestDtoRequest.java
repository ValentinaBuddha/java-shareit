package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.utils.Create;
import ru.practicum.shareit.utils.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDtoRequest {

    private long id;

    @NotBlank(groups = {Create.class, Update.class})
    @Size(max = 1000, groups = {Create.class, Update.class})
    private String description;
}
