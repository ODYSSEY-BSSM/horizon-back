package odyssey.backend.websocket.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class WebSocketSessionManager {

    private final Map<String, String> sessionToUser = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> userToSessions = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> roadmapSubscriptions = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> teamSubscriptions = new ConcurrentHashMap<>();

    public void addSession(String sessionId, String username) {
        sessionToUser.put(sessionId, username);
        userToSessions.computeIfAbsent(username, k -> new HashSet<>()).add(sessionId);
        log.info("WebSocket 세션 추가 - 세션ID: {}, 사용자: {}", sessionId, username);
    }

    public void removeSession(String sessionId) {
        String username = sessionToUser.remove(sessionId);
        if (username != null) {
            Set<String> sessions = userToSessions.get(username);
            if (sessions != null) {
                sessions.remove(sessionId);
                if (sessions.isEmpty()) {
                    userToSessions.remove(username);
                }
            }
        }


        log.info("WebSocket 세션 제거 - 세션ID: {}, 사용자: {}", sessionId, username);
    }

    public void subscribeToRoadmap(String userId, Long roadmapId) {
        roadmapSubscriptions.computeIfAbsent(roadmapId.toString(), k -> new HashSet<>()).add(userId);
        log.info("로드맵 구독 - 사용자: {}, 로드맵ID: {}", userId, roadmapId);
    }

    public void unsubscribeFromRoadmap(String userId, Long roadmapId) {
        Set<String> subscribers = roadmapSubscriptions.get(roadmapId.toString());
        if (subscribers != null) {
            subscribers.remove(userId);
            if (subscribers.isEmpty()) {
                roadmapSubscriptions.remove(roadmapId.toString());
            }
        }
        log.info("로드맵 구독 해제 - 사용자: {}, 로드맵ID: {}", userId, roadmapId);
    }

    public Set<String> getUsersSubscribedToRoadmap(Long roadmapId) {
        return roadmapSubscriptions.getOrDefault(roadmapId.toString(), new HashSet<>());
    }

    public String getUserBySession(String sessionId) {
        return sessionToUser.get(sessionId);
    }

    public Set<String> getSessionsByUser(String username) {
        return userToSessions.getOrDefault(username, new HashSet<>());
    }

    public int getTotalSessions() {
        return sessionToUser.size();
    }

    public int getTotalRoadmapSubscriptions() {
        return roadmapSubscriptions.size();
    }

    public void subscribeToTeam(String userId, Long teamId) {
        teamSubscriptions.computeIfAbsent(teamId.toString(), k -> new HashSet<>()).add(userId);
        log.info("팀 구독 - 사용자: {}, 팀ID: {}", userId, teamId);
    }

    public void unsubscribeFromTeam(String userId, Long teamId) {
        Set<String> subscribers = teamSubscriptions.get(teamId.toString());
        if (subscribers != null) {
            subscribers.remove(userId);
            if (subscribers.isEmpty()) {
                teamSubscriptions.remove(teamId.toString());
            }
        }
        log.info("팀 구독 해제 - 사용자: {}, 팀ID: {}", userId, teamId);
    }

    public Set<String> getUsersSubscribedToTeam(Long teamId) {
        return teamSubscriptions.getOrDefault(teamId.toString(), new HashSet<>());
    }

    public int getTotalTeamSubscriptions() {
        return teamSubscriptions.size();
    }
}