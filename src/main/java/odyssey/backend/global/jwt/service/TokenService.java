package odyssey.backend.global.jwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import odyssey.backend.global.jwt.domain.RefreshToken;
import odyssey.backend.global.jwt.domain.RefreshTokenRepository;
import odyssey.backend.global.jwt.domain.TokenType;
import odyssey.backend.global.jwt.exception.InvalidTokenException;
import odyssey.backend.global.jwt.exception.InvalidTokenTypeException;
import odyssey.backend.global.jwt.exception.TokenNotFoundException;
import odyssey.backend.user.domain.Role;
import odyssey.backend.user.domain.User;
import odyssey.backend.user.domain.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secret;

    public String generateAccessToken(User user) {
        Long accessTokenExpiration = 60 * 60 * 1000L;

        return generateToken(user.getUuid(), user.getRole(), TokenType.ACCESS_TOKEN, accessTokenExpiration);
    }

    public String generateRefreshToken(User user) {
        Long refreshTokenExpiration = 7 * 24 * 60 * 60 * 1000L;

        String token = generateToken(user.getUuid(), user.getRole(), TokenType.REFRESH_TOKEN, refreshTokenExpiration);

        refreshTokenRepository.save(RefreshToken.create(user.getUuid(), token));

        return token;
    }

    public String generateToken(Long uuid, Role role, TokenType type, Long time) {
        Claims claims = Jwts.claims();
        claims.put("uuid", uuid);
        claims.put("role", role.toString());
        claims.put("type", type.name());
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + time))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    public String refreshAccessToken(String refreshToken) {
        Claims claims = parseToken(refreshToken);

        String type = claims.get("type", String.class);
        if (!TokenType.REFRESH_TOKEN.name().equals(type)) {
            throw new InvalidTokenTypeException();
        }

        Long uuid = claims.get("uuid", Long.class);
        RefreshToken token = refreshTokenRepository.findByUuid(uuid)
                .orElseThrow(TokenNotFoundException::new);

        if (!token.getRefreshToken().equals(refreshToken)) {
            throw new InvalidTokenException();
        }

        User user = userRepository.findUserByUuid(uuid);

        return generateAccessToken(user);
    }


    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getType(String token) {
        return parseToken(token).get("type", String.class);
    }

    public User getUserByUuid(Long uuid) {
        return userRepository.findUserByUuid(uuid);
    }
}
