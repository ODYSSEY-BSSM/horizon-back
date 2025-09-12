package odyssey.backend.infrastructure.websocket;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import odyssey.backend.domain.auth.User;
import odyssey.backend.infrastructure.jwt.service.TokenService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

    private final TokenService tokenService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");
            log.info("WebSocket CONNECT 시도 - Authorization 헤더: '{}'", token);

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                log.info("Bearer 제거 후 토큰 길이: {}, 시작 부분: {}", token.length(),
                        token.length() > 20 ? token.substring(0, 20) + "..." : token);
                try {
                    Claims claims = tokenService.parseToken(token);
                    log.info("토큰 파싱 성공 - UUID: {}", claims.get("uuid", Long.class));

                    Long uuid = claims.get("uuid", Long.class);
                    User user = tokenService.getUserByUuid(uuid);
                    if (user == null) {
                        log.error("WebSocket 인증 실패: DB에서 uuid '{}'에 해당하는 사용자를 찾을 수 없습니다.", uuid);
                        throw new IllegalArgumentException("User not found in DB");
                    }

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
                            );

                    accessor.setUser(authentication);
                    log.info("WebSocket 인증 성공 - 사용자: {}", user.getUsername());

                } catch (Exception e) {
                    log.error("WebSocket 토큰 파싱 실패: {} - {}", e.getClass().getSimpleName(), e.getMessage());
                    throw new IllegalArgumentException("Invalid token");
                }
            } else {
                log.error("WebSocket 연결 시 토큰이 없거나 Bearer로 시작하지 않음: '{}'", token);
                throw new IllegalArgumentException("Authorization token required");
            }
        }

        return message;
    }
}
