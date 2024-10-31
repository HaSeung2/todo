package com.sparta.todo.domain.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import com.sparta.todo.config.PasswordEncoder;
import com.sparta.todo.domain.user.dto.JoinRequestDto;
import com.sparta.todo.domain.user.dto.LoginRequestDto;
import com.sparta.todo.domain.user.dto.UserResponseDto;
import com.sparta.todo.domain.user.entity.User;
import com.sparta.todo.domain.user.entity.UserRole;
import com.sparta.todo.domain.user.repository.UserRepository;
import com.sparta.todo.exception.CustomException;
import com.sparta.todo.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {


    @Mock
    private UserRepository userRepository;
    @Mock
    private  HttpServletResponse httpServletResponse;
    @Mock
    private  PasswordEncoder passwordEncoder;
    @Mock
    private  JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("유저 전체 조회 할 수 있다.")
    void test1(){
        long userId = 1L;
        User user = User.createUser("dldl@naver.com","1234","승승", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);

        List<User> userList = List.of(user);
        given(userRepository.findAll()).willReturn(userList);

        List<UserResponseDto> userResponseDtos = userService.findAll();
        assertEquals(1, userResponseDtos.size());
        assertEquals(userList.get(0).getId(), userResponseDtos.get(0).getId());
    }

    @Test
    @DisplayName("회원 가입 시 중복 이메일 있으면 예외처리")
    void test2(){
        User user = User.createUser("dldl@naver.com","1234","승승", UserRole.USER);

        JoinRequestDto join = new JoinRequestDto(user.getEmail(),user.getPassword(),user.getUserName(),"");

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));


        CustomException exception = assertThrows(CustomException.class, () -> {
           userService.join(join);
        });

        assertEquals("중복된 이메일입니다.", exception.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("회원가입 정상 처리")
    void test3(){
        User user = User.createUser("dldl@naver.com","1234","승승", UserRole.USER);

        JoinRequestDto join = new JoinRequestDto(user.getEmail(),user.getPassword(),user.getUserName(),"");

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.empty());
        given(userRepository.save(any())).willReturn(user);
        given(jwtUtil.createAccessToken(user.getId(),user.getRole().getAuthority())).willReturn("token");

        String token = userService.join(join);

        assertNotNull(token);
        assertEquals("token",token);
    }

    @Test
    @DisplayName("로그인 시 이메일 틀리면 예외 처리")
    void test4(){
        LoginRequestDto loginRequestDto = new LoginRequestDto("dldl@naver.com","1234");

        given(userRepository.findByEmail(loginRequestDto.getEmail())).willReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.login(loginRequestDto);
        });

        assertEquals("이메일과 비밀번호 틀림", exception.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("로그인 시 비밀번호 틀리면 예외 처리")
    void test5(){
        LoginRequestDto loginRequestDto = new LoginRequestDto("dldl@naver.com","1234");
        User user = User.createUser("dldl@naver.com","1234","승승", UserRole.USER);

        given(userRepository.findByEmail(loginRequestDto.getEmail())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(loginRequestDto.getPassword(),user.getPassword())).willReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.login(loginRequestDto);
        });

        assertEquals("이메일과 비밀번호 틀림", exception.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("로그인 정상 처리")
    void test6(){
        LoginRequestDto loginRequestDto = new LoginRequestDto("dldl@naver.com","1234");
        User user = User.createUser("dldl@naver.com","1234","승승", UserRole.USER);

        given(userRepository.findByEmail(loginRequestDto.getEmail())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(loginRequestDto.getPassword(),user.getPassword())).willReturn(true);
        given(jwtUtil.createAccessToken(user.getId(),user.getRole().getAuthority())).willReturn("token");

        UserResponseDto userResponseDto = userService.login(loginRequestDto);

        assertNotNull(userResponseDto);
        assertEquals(userResponseDto.getUserName(),user.getUserName());
    }

    @Test
    @DisplayName("삭제 요청 시 본인 계정 아닐 때 예외 처리")
    void test7(){
        Long userId = 1L;
        User user = User.createUser("dldl@naver.com","1234","승승", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", 2L);

        CustomException exception = assertThrows(CustomException.class, () -> {
           userService.delete(userId, user);
        });

        assertEquals("본인 계정만 삭제 및 수정할 수 있습니다.", exception.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("수정 요청 시 본인 계정 아닐 때 예외 처리")
    void test8(){
        Long userId = 1L;
        User user = User.createUser("dldl@naver.com","1234","승승", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", 2L);

        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.modify(userId,"승승승" ,user);
        });

        assertEquals("본인 계정만 삭제 및 수정할 수 있습니다.", exception.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("삭제 요청 성공")
    void test9(){
        Long userId = 1L;
        User user = User.createUser("dldl@naver.com","1234","승승", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);

        userService.delete(userId, user);
        Mockito.verify(userRepository, times(1)).delete(any());
    }

    @Test
    @DisplayName("수정 요청 성공")
    void test10(){
        Long userId = 1L;
        String userName = "승승승";
        User user = mock(User.class);
        ReflectionTestUtils.setField(user, "id", userId);

        given(user.getId()).willReturn(userId);
        given(userRepository.findByUserId(userId)).willReturn(user);

        userService.modify(userId,userName,user);
        Mockito.verify(user, times(1)).modify(any());
    }
}






















