package com.sparta.todo.filter;

import com.sparta.todo.domain.user.entity.User;
import com.sparta.todo.domain.user.entity.UserRole;
import com.sparta.todo.domain.user.repository.UserRepository;
import com.sparta.todo.exception.CustomException;
import com.sparta.todo.exception.ErrorCode;
import com.sparta.todo.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("토큰 필터 시작");
        String uri = request.getRequestURI();
        if(uri.startsWith("/api/user/join") || uri.startsWith("/api/user/login")) filterChain.doFilter(request, response);

        String token = jwtUtil.getTokenFromRequest(request);

        if(uri.startsWith("/api/todo/modify")|| uri.startsWith("/api/todo/delete")){
            if(!jwtUtil.getUserInfo(token).get("auth").equals(UserRole.ADMIN.getAuthority()))throw new CustomException(ErrorCode.NOT_ADMIN);
        }

        Long id = jwtUtil.getUserInfo(token).get("id", Long.class);
        User user = repository.findById(id).orElseThrow(()-> new CustomException(ErrorCode.NOT_USER_ID));

        request.setAttribute("user", user);
        filterChain.doFilter(request, response);
        log.info("토큰 필터 종료");
    }
}
