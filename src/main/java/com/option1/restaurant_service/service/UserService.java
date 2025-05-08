package com.option1.restaurant_service.service;

import com.option1.restaurant_service.dto.request.UserCreationRequest;
import com.option1.restaurant_service.dto.request.UserUpdateRequest;
import com.option1.restaurant_service.dto.response.UserResponse;
import com.option1.restaurant_service.entity.User;
import com.option1.restaurant_service.exception.AppException;
import com.option1.restaurant_service.exception.ErrorCode;
import com.option1.restaurant_service.mapper.UserMapper;
import com.option1.restaurant_service.repository.RoleRepository;
import com.option1.restaurant_service.repository.UserRepository;
import java.util.HashSet;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class UserService {

  UserRepository userRepository;
  UserMapper userMapper;
  PasswordEncoder passwordEncoder;
  private final RoleRepository roleRepository;

  public UserResponse createUser(UserCreationRequest request) {
    if (userRepository.existsByUsername(request.getUsername())) {
      throw new AppException(ErrorCode.USER_EXISTED);
    }

    User user = userMapper.toUser(request);
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    var roles = roleRepository.findAllById(request.getRoles());
    user.setRoles(new HashSet<>(roles));

    return userMapper.toUserResponse(userRepository.save(user));
  }

  public List<UserResponse> getUsers() {
    return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
  }

  public UserResponse updateUser(String userID, UserUpdateRequest request) {
    User user = userRepository.findById(userID)
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

    userMapper.updateUser(user, request);
//    user.setPassword(passwordEncoder.encode(request.getPassword()));

//    var roles = roleRepository.findAllById(request.getRoles());
//    user.setRoles(new HashSet<>(roles));

    return userMapper.toUserResponse(userRepository.save(user));
  }

  public void deleteUserID(String userID) {
    userRepository.deleteById(userID);
  }

}
