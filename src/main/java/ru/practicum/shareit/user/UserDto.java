package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.utils.Create;
import ru.practicum.shareit.utils.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class UserDto {

    private long id;

    @NotBlank(groups = {Create.class})
    private String name;

    @NotEmpty(groups = {Create.class})
    @Email(groups = {Create.class, Update.class})
    private String email;
}
