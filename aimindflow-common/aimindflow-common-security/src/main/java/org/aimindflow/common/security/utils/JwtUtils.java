package org.aimindflow.common.security.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.security.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * JWT工具类
 *
 * @author HezaoHezao
 */
@Slf4j
@Component
public class JwtUtils {

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 生成JWT令牌
     *
     * @param subject 主题（通常是用户ID）
     * @param claims  自定义声明
     * @return JWT令牌
     */
    public String generateToken(String subject, Map<String, Object> claims) {
        try {
            Date now = new Date();
            Date expiration = new Date(now.getTime() + securityProperties.getJwt().getExpiration() * 1000);

            JwtBuilder builder = Jwts.builder()
                    .setSubject(subject)
                    .setIssuedAt(now)
                    .setExpiration(expiration)
                    .signWith(getSignKey(), SignatureAlgorithm.HS256);

            if (claims != null && !claims.isEmpty()) {
                builder.addClaims(claims);
            }

            return builder.compact();
        } catch (Exception e) {
            log.error("生成JWT令牌失败", e);
            throw new RuntimeException("生成JWT令牌失败", e);
        }
    }

    /**
     * 生成JWT令牌（无自定义声明）
     *
     * @param subject 主题（通常是用户ID）
     * @return JWT令牌
     */
    public String generateToken(String subject) {
        return generateToken(subject, null);
    }

    /**
     * 生成刷新令牌
     *
     * @param subject 主题（通常是用户ID）
     * @return 刷新令牌
     */
    public String generateRefreshToken(String subject) {
        try {
            Date now = new Date();
            Date expiration = new Date(now.getTime() + securityProperties.getJwt().getRefreshExpiration() * 1000);

            return Jwts.builder()
                    .setSubject(subject)
                    .setIssuedAt(now)
                    .setExpiration(expiration)
                    .signWith(getSignKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            log.error("生成刷新令牌失败", e);
            throw new RuntimeException("生成刷新令牌失败", e);
        }
    }

    /**
     * 解析JWT令牌
     *
     * @param token JWT令牌
     * @return Claims对象
     */
    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("JWT令牌已过期: {}", token);
            throw new RuntimeException("JWT令牌已过期", e);
        } catch (UnsupportedJwtException e) {
            log.warn("不支持的JWT令牌: {}", token);
            throw new RuntimeException("不支持的JWT令牌", e);
        } catch (MalformedJwtException e) {
            log.warn("JWT令牌格式错误: {}", token);
            throw new RuntimeException("JWT令牌格式错误", e);
        } catch (SecurityException e) {
            log.warn("JWT令牌签名验证失败: {}", token);
            throw new RuntimeException("JWT令牌签名验证失败", e);
        } catch (IllegalArgumentException e) {
            log.warn("JWT令牌为空: {}", token);
            throw new RuntimeException("JWT令牌为空", e);
        }
    }

    /**
     * 从令牌中获取用户ID
     *
     * @param token JWT令牌
     * @return 用户ID
     */
    public String getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * 从令牌中获取自定义声明
     *
     * @param token JWT令牌
     * @param key   声明键
     * @return 声明值
     */
    public Object getClaimFromToken(String token, String key) {
        Claims claims = parseToken(token);
        return claims.get(key);
    }

    /**
     * 验证JWT令牌是否有效
     *
     * @param token JWT令牌
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查JWT令牌是否过期
     *
     * @param token JWT令牌
     * @return 是否过期
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 获取JWT令牌的过期时间
     *
     * @param token JWT令牌
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration();
    }

    /**
     * 获取签名密钥
     *
     * @return 签名密钥
     */
    private SecretKey getSignKey() {
        byte[] keyBytes = securityProperties.getJwt().getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 从请求头中提取JWT令牌
     *
     * @param authHeader 授权头
     * @return JWT令牌
     */
    public String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith(securityProperties.getJwt().getTokenPrefix())) {
            return authHeader.substring(securityProperties.getJwt().getTokenPrefix().length());
        }
        return null;
    }
}