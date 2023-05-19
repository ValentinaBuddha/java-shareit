package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.utils.Create;
import ru.practicum.shareit.utils.Update;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("GET / users");
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable long userId) {
        log.info("GET / users / {}", userId);
        return userService.getUserById(userId);
    }

    @PostMapping
    public UserDto saveNewUser(@Validated(Create.class) @RequestBody UserDto userDto) {
        log.info("POST / users / {}", userDto.getName());
        return userService.saveNewUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable long userId, @Validated(Update.class) @RequestBody UserDto userDto) {
        log.info("PATCH / users / {}", userId);
        return userService.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("DELETE / users / {}", userId);
        userService.deleteUser(userId);
    }
}
