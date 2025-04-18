package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final Map<Long, User> users = new HashMap<>();
    private final Map<String, Long> emailToId = new HashMap<>();

    public Collection<User> findAll() {
        return users.values();
    }

    public User create(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        if (emailToId.containsKey(user.getEmail())) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }
        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);
        emailToId.put(user.getEmail(), user.getId());
        return user;
    }

    public User update(User newUser) {
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (!users.containsKey(newUser.getId())) {
            throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
        }
        User oldUser = users.get(newUser.getId());
        if (newUser.getEmail() != null && !newUser.getEmail().equals(oldUser.getEmail())) {
            if (emailToId.containsKey(newUser.getEmail())) {
                throw new DuplicatedDataException("Этот имейл уже используется");
            }
            emailToId.remove(oldUser.getEmail());
            emailToId.put(newUser.getEmail(), newUser.getId());
            oldUser.setEmail(newUser.getEmail());
        }
        if (newUser.getUsername() != null) {
            oldUser.setUsername(newUser.getUsername());
        }
        if (newUser.getPassword() != null) {
            oldUser.setPassword(newUser.getPassword());
        }
        return oldUser;
    }

    public Optional<User> findUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    private long getNextId() {
        return users.keySet().stream().mapToLong(Long::longValue).max().orElse(0) + 1;
    }
}
