package com.uca.parcialfinalncapas.service;

import com.uca.parcialfinalncapas.dto.request.UserCreateRequest;
import com.uca.parcialfinalncapas.dto.request.UserUpdateRequest;
import com.uca.parcialfinalncapas.dto.response.UserResponse;
import com.uca.parcialfinalncapas.entities.User;

import java.util.List;

public interface UserService {
    UserResponse findByCorreo(String correo);
    UserResponse save(UserCreateRequest user);
    UserResponse update(UserUpdateRequest user);
    void delete(Long id);
    List<UserResponse> findAll();
}
