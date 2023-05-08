package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
public class UserRepositoryImpl implements UserRepository {
    private static int generatorId = 0;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getUserById(long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public User saveNewUser(User user) {
        user.setId(++generatorId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteUser(long userId) {
        if (!users.containsKey(userId)) {
            throw new EntityNotFoundException(String.format("Объект класса %s не найден", User.class));
        }
        users.remove(userId);
    }
}
