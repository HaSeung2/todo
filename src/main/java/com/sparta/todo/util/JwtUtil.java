package com.sparta.todo.util;

import com.sparta.todo.exception.CustomException;
import com.sparta.todo.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer ";
    private final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L; // 60분

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct
    public void init(){
        byte[] keyBytes = secretKey.getBytes();
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Long id, String role){
        Date date = new Date();

        Claims claims = Jwts.claims();

        claims.put("id", id);
        claims.put(AUTHORIZATION_KEY, role);

        return BEARER_PREFIX+
                Jwts.builder()
                        .setClaims(claims)
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key,signatureAlgorithm)
                        .compact();
    }

    public void addJwtToHeader(HttpServletResponse response, String token){
            logger.info("토큰 추가");
        try {
            token = URLEncoder.encode(token, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader(AUTHORIZATION_HEADER,token);
        } catch (UnsupportedEncodingException e) {
            throw new CustomException(ErrorCode.INVALID_ENCODE);
        }
    }

    public Claims getUserInfo(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String getTokenFromRequest(HttpServletRequest request) throws UnsupportedEncodingException {
        String token = request.getHeader(AUTHORIZATION_HEADER);

        token = URLDecoder.decode(token,"UTF-8");

        if (!(StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX))) throw  new CustomException(ErrorCode.NULL_TOKEN);
        return token.substring(BEARER_PREFIX.length());
    }
}
