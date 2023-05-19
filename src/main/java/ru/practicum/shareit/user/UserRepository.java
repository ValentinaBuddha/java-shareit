package ru.practicum.shareit.user;

import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> getAllUsers();

    User saveNewUser(User user);

    Optional<User> getUserById(long userId);

    void deleteUser(long userId);
}
