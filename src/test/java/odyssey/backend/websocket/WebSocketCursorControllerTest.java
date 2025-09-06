package odyssey.backend.websocket;

import odyssey.backend.domain.auth.Role;
import odyssey.backend.domain.auth.User;
import odyssey.backend.presentation.websocket.WebSocketCursorController;
import odyssey.backend.presentation.websocket.dto.CursorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WebSocketCursorControllerTest {

    @Mock
    private SimpMessagingTemplate messaging;

    @InjectMocks
    private WebSocketCursorController controller;

    private User testUser;
    private CursorMessage cursorMessage;

    @BeforeEach
    void setUp() {
        testUser = createTestUser(1L, "testUser");
        cursorMessage = new CursorMessage(100L, 150, 200);
    }

    private User createTestUser(Long uuid, String username) {
        User user = new User("test@example.com", username, "password", Role.USER);
        try {
            var uuidField = user.getClass().getDeclaredField("uuid");
            uuidField.setAccessible(true);
            uuidField.set(user, uuid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Test
    void 커서_메시지를_처리하고_브로드캐스트한다() {
        controller.handleCursor(cursorMessage, testUser);

        verify(messaging).convertAndSend(
            eq("/topic/cursor/" + cursorMessage.getRoadmapId()),
            any(WebSocketCursorController.CursorWithUserMessage.class)
        );
    }
}