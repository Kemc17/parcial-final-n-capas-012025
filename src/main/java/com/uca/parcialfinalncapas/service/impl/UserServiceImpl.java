package com.uca.parcialfinalncapas.service.impl;

import com.uca.parcialfinalncapas.dto.request.UserCreateRequest;
import com.uca.parcialfinalncapas.dto.request.UserUpdateRequest;
import com.uca.parcialfinalncapas.dto.response.UserResponse;
import com.uca.parcialfinalncapas.entities.User;
import com.uca.parcialfinalncapas.exceptions.UserNotFoundException;
import com.uca.parcialfinalncapas.repository.UserRepository;
import com.uca.parcialfinalncapas.service.UserService;
import com.uca.parcialfinalncapas.utils.mappers.UserMapper;
import org.springframework.context.annotation.Lazy; // Importación correcta para @Lazy
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse findByCorreo(String correo) {
        User user = userRepository.findByCorreo(correo)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con correo: " + correo));
        return UserMapper.toDTO(user);
    }

    @Override
    public UserResponse save(UserCreateRequest userRequest) {
        if (userRepository.findByCorreo(userRequest.getCorreo()).isPresent()) {
            throw new UserNotFoundException("Ya existe un usuario con el correo: " + userRequest.getCorreo());
        }
        User user = UserMapper.toEntityCreate(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        return UserMapper.toDTO(userRepository.save(user));
    }

    @Override
    public UserResponse update(UserUpdateRequest userUpdate) {
        User existingUser = userRepository.findById(userUpdate.getId())
                .orElseThrow(() -> new UserNotFoundException("No se encontró un usuario con el ID: " + userUpdate.getId()));

        existingUser.setNombre(userUpdate.getNombre());
        existingUser.setNombreRol(userUpdate.getNombreRol());
        
        if (userUpdate.getPassword() != null && !userUpdate.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userUpdate.getPassword()));
        }
        return UserMapper.toDTO(userRepository.save(existingUser));
    }

    @Override
    public void delete(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new UserNotFoundException("No se encontró un usuario con el ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public List<UserResponse> findAll() {
        return UserMapper.toDTOList(userRepository.findAll());
    }
}
