package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.NotUniqueEmailException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getAllUsers() {
        log.info("Получение всех пользователей");
        return userRepository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto getUserById(long userId) {
        log.info("Получение пользователя по идентификатору {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Объект класса %s не найден", User.class)));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto saveNewUser(UserDto userDto) {
        try {
            log.info("Создание нового пользователя {}", userDto.getName());
            User user = userRepository.save(UserMapper.toUser(userDto));
            log.info("Новый пользователь создан с идентификтором {}", user.getId());
            return UserMapper.toUserDto(user);

        } catch (DataIntegrityViolationException e) {
            log.info("Email {} уже используется", userDto.getEmail());
            throw new NotUniqueEmailException(String.format("Email %s уже используется.", userDto.getEmail()));
        }
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        log.info("Обновление существующего пользователя {}", userDto.getName());
        User oldUser = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Объект класса %s не найден", User.class)));
        String name = userDto.getName();
        String email = userDto.getEmail();
        if (name != null && !name.isBlank()) {
            oldUser.setName(name);
        }
        if (email != null && !email.isBlank()) {
//            if (!userDto.getEmail().equals(userDto.getEmail())) {
//                if (userRepository.findAll().stream().anyMatch(user -> user.getEmail().equals(email))) {
//                    throw new NotUniqueEmailException(String.format("Email %s уже используется.", email));
//                }
//            }
            oldUser.setEmail(email);
        }
        return UserMapper.toUserDto(userRepository.save(oldUser));
    }

    @Override
    public void deleteUser(long id) {
        log.info("Удаление пользователя по идентификатору {}", id);
        userRepository.deleteById(id);
    }
}
