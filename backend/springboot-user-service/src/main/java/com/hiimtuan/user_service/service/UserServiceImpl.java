package com.hiimtuan.user_service.service;

import com.hiimtuan.common_service.constant.RoleEnum;
import com.hiimtuan.common_service.service.JwtService;
import com.hiimtuan.user_service.dto.request.RegisterRequestDto;
import com.hiimtuan.user_service.dto.response.*;
import com.hiimtuan.user_service.entity.Role;
import com.hiimtuan.user_service.entity.User;
import com.hiimtuan.user_service.exception.custom.AccountException;
import com.hiimtuan.user_service.mapper.UserMapper;
import com.hiimtuan.user_service.repository.RoleRepository;
import com.hiimtuan.user_service.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    @Override
    public RegisterResponseDto register(RegisterRequestDto registerRequestDto) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);

        return getRegisterResponseDto(registerRequestDto, optionalRole);
    }

    @Override
    public RegisterResponseDto registerAdmin(RegisterRequestDto registerRequestDto) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ADMIN);

        return getRegisterResponseDto(registerRequestDto, optionalRole);
    }

    @Override
    public List<UserResponseDto> allUsers(Pageable pageable) {

        List<User> users = new ArrayList<>(userRepository.findAll(pageable));

        return users.stream().map(userMapper::UserToUserResponse).toList();
    }

    @Override
    public UserResponseDto getProfile(String token) {
        String id = jwtService.extractUsername(token);
        
        Optional<User> user = userRepository.findById(Long.parseLong(id.trim()));

        if (user.isEmpty()){
           throw new AccountException("Email doesn't exist");
        }

        return userMapper.UserToUserResponse(user.get());
    }

    private RegisterResponseDto getRegisterResponseDto(RegisterRequestDto registerRequestDto, Optional<Role> optionalRole) {
        if (optionalRole.isEmpty()){
            return null;
        }

        Optional<User> userExist = userRepository.findByEmail(registerRequestDto.getEmail());

        if (userExist.isPresent()){
            throw new AccountException("Email already exist");
        }

        userRepository.save(userMapper.RegisterRequestToUser(registerRequestDto, optionalRole.get()));

        return userMapper.UserToRegisterResponse();
    }
}
