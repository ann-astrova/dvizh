package com.example.demo.service;

import com.example.demo.dto.UpdateProfileRequest;
import com.example.demo.dto.UpdateRoleRequest;
import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.CurrentAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Возвращает текущего пользователя по authId из токена.
    // Если пользователя ещё нет в БД (первый вход) — создаёт нового с ролью student.
    // Это и есть "создание профиля при первом входе" из требований.
    @Transactional
    public User getOrCreateCurrentUser() {
        String authId = CurrentAuth.authId();
        if (authId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Не удалось определить пользователя");
        }
        return userRepository.findByAuthId(authId).orElseGet(() -> {
            User u = new User();
            u.setAuthId(authId);
            String email = CurrentAuth.email();
            u.setEmail(email != null ? email : "");
            u.setName("Новый пользователь");
            u.setDescription("");
            u.setRole(Role.student);
            u.setBalance(0);
            return userRepository.save(u);
        });
    }

    // Эндпоинт 2: PATCH /me — меняем имя/описание, если они переданы.
    @Transactional
    public User updateProfile(UpdateProfileRequest req) {
        User user = getOrCreateCurrentUser();
        if (req.name() != null) {
            user.setName(req.name());
        }
        if (req.description() != null) {
            user.setDescription(req.description());
        }
        return userRepository.save(user);
    }

    // Эндпоинт 3: PATCH /admin/users/{id}/role — только админ.
    @Transactional
    public User updateRole(UUID targetUserId, UpdateRoleRequest req) {
        requireAdmin();
        if (req.role() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Поле 'role' обязательно");
        }
        User target = userRepository.findById(targetUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));
        target.setRole(req.role());
        return userRepository.save(target);
    }

    // Эндпоинт 19: GET /admin/users — только админ.
    public List<User> getAllUsers() {
        requireAdmin();
        return userRepository.findAll();
    }

    private void requireAdmin() {
        User current = getOrCreateCurrentUser();
        if (current.getRole() != Role.admin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Требуются права администратора");
        }
    }
}
