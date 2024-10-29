package com.sparta.todo.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.todo.exception.CustomException;
import com.sparta.todo.exception.ErrorCode;
import com.sparta.todo.exception.ErrorMessageResponseDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Slf4j
@Component
@Order(0)
public class AuthenticationExceptionHandleFilter extends OncePerRequestFilter{

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            log.info("로그 필터 시작");
            filterChain.doFilter(request, response);
        }
        catch (SecurityException | MalformedJwtException | SignatureException e) {
            setErrorResponse(response, ErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException  e) {
            setErrorResponse(response, ErrorCode.NOT_VALID_TOKEN);
        } catch (UnsupportedJwtException  e) {
            setErrorResponse(response, ErrorCode.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException | NullPointerException e) {
            setErrorResponse(response,ErrorCode.NULL_TOKEN);
        }
        catch (CustomException e){
            setErrorResponse(response,e.getErrorCode());
        }
        log.info("로그 필터 종료");
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) {
        response.setStatus(errorCode.getHttpStatus());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String json = new ObjectMapper().writeValueAsString(new ErrorMessageResponseDto(errorCode));
            response.getWriter().write(json);
        } catch(Exception e) {
            response.getStatus();
        }
    }
}
