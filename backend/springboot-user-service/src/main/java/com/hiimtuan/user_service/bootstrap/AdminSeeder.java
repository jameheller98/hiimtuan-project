package com.hiimtuan.user_service.bootstrap;

import com.hiimtuan.common_service.constant.RoleEnum;
import com.hiimtuan.user_service.dto.request.RegisterRequestDto;
import com.hiimtuan.user_service.entity.Role;
import com.hiimtuan.user_service.entity.User;
import com.hiimtuan.user_service.repository.RoleRepository;
import com.hiimtuan.user_service.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
@Order(2)
public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent contextRefreshedEvent) {
        this.createSuperAdministrator();
    }

    private void createSuperAdministrator() {
        RegisterRequestDto registerRequestDto =  RegisterRequestDto.builder()
                .fullName("Nguyen Hoang Anh Tuan").email("nguyentuanwd.ou@gmail.com").password("261098").build();

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.SUPER_ADMIN);
        Optional<User> optionalUser = userRepository.findByEmail(registerRequestDto.getEmail());

        if (optionalRole.isEmpty() || optionalUser.isPresent()) {
            return;
        }

        User user =  User.builder()
                .fullName(registerRequestDto.getFullName())
                .email(registerRequestDto.getEmail())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .role(optionalRole.get()).build();

        userRepository.save(user);
    }
}