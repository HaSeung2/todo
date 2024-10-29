package com.sparta.todo.domain.user.service;

import com.sparta.todo.config.PasswordEncoder;
import com.sparta.todo.domain.user.dto.JoinRequestDto;
import com.sparta.todo.domain.user.dto.LoginRequestDto;
import com.sparta.todo.domain.user.dto.UserResponseDto;
import com.sparta.todo.domain.user.entity.User;
import com.sparta.todo.domain.user.entity.UserRole;
import com.sparta.todo.domain.user.repository.UserRepository;
import com.sparta.todo.exception.CustomException;
import com.sparta.todo.exception.ErrorCode;
import com.sparta.todo.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final HttpServletResponse httpServletResponse;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final String adminToken = "sksmsrhksflwkdla";

    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream().map(UserResponseDto::new).toList();
    }

    public UserResponseDto findById(User user) {
        return new UserResponseDto(userRepository.findByUserId(user.getId()));
    }

    public String join(JoinRequestDto joinRequestDto) {
        String password = passwordEncoder.encode(joinRequestDto.getPassword());
        String email = joinRequestDto.getEmail();

        Optional<User> checkUser = userRepository.findByEmail(email);
        if(checkUser.isPresent()) throw new CustomException(ErrorCode.EMAIL_DUPLICATION);

        UserRole role = UserRole.USER;

        if(joinRequestDto.getAdminToken().equals(adminToken)) role = UserRole.ADMIN;

        User user = User.createUser(email,password,joinRequestDto.getUserName(),role);
        userRepository.save(user);
        return jwtUtil.createAccessToken(user.getId(), user.getRole().getAuthority());
    }

    public UserResponseDto login(LoginRequestDto loginRequestDto){
        User findUser = userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(() ->  new CustomException(ErrorCode.NOT_MATCH_LOGIN));
        if(!passwordEncoder.matches(loginRequestDto.getPassword(), findUser.getPassword())) throw new CustomException(ErrorCode.NOT_MATCH_LOGIN);

        String token = jwtUtil.createAccessToken(findUser.getId(), findUser.getRole().getAuthority());
        jwtUtil.addJwtToHeader(httpServletResponse,token);
        return new UserResponseDto(findUser);
    }

    public void delete(Long id, User user) {
        getUser(id,user);
        userRepository.delete(user);
    }

    @Transactional
    public void modify(Long id, String userName, User user) {
        User findUser = getUser(id,user);
        findUser.modify(userName);
    }

    public User getUser(Long id, User user) {
        user.isValidUser(id);
        return userRepository.findByUserId(id);
    }
}
