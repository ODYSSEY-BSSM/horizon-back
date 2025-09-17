package odyssey.backend.infrastructure.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class WebSocketSessionManager {

    private final Map<String, Long> sessionToUserId = new ConcurrentHashMap<>();
    private final Map<Long, Set<String>> userIdToSessions = new ConcurrentHashMap<>();
    private final Map<String, Set<Long>> roadmapSubscriptions = new ConcurrentHashMap<>();
    private final Map<String, Set<Long>> teamSubscriptions = new ConcurrentHashMap<>();

    public void addSession(String sessionId, Long userId) {
        sessionToUserId.put(sessionId, userId);
        userIdToSessions.computeIfAbsent(userId, k -> new HashSet<>()).add(sessionId);
    }

    public void removeSession(String sessionId) {
        Long userId = sessionToUserId.remove(sessionId);
        if (userId != null) {
            Set<String> sessions = userIdToSessions.get(userId);
            if (sessions != null) {
                sessions.remove(sessionId);
                if (sessions.isEmpty()) {
                    userIdToSessions.remove(userId);
                    cleanupUserSubscriptions(userId);
                }
            }
        }
    }
    
    private void cleanupUserSubscriptions(Long userId) {
        roadmapSubscriptions.values().forEach(subscribers -> subscribers.remove(userId));
        roadmapSubscriptions.entrySet().removeIf(entry -> entry.getValue().isEmpty());
        
        teamSubscriptions.values().forEach(subscribers -> subscribers.remove(userId));
        teamSubscriptions.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }

    public void subscribeToRoadmap(Long userId, Long roadmapId) {
        roadmapSubscriptions.computeIfAbsent(roadmapId.toString(), k -> new HashSet<>()).add(userId);
    }

    public void unsubscribeFromRoadmap(Long userId, Long roadmapId) {
        Set<Long> subscribers = roadmapSubscriptions.get(roadmapId.toString());
        if (subscribers != null) {
            subscribers.remove(userId);
            if (subscribers.isEmpty()) {
                roadmapSubscriptions.remove(roadmapId.toString());
            }
        }
    }

    public Set<Long> getUsersSubscribedToRoadmap(Long roadmapId) {
        return roadmapSubscriptions.getOrDefault(roadmapId.toString(), new HashSet<>());
    }

    public Long getUserIdBySession(String sessionId) {
        return sessionToUserId.get(sessionId);
    }

    public Set<String> getSessionsByUserId(Long userId) {
        return userIdToSessions.getOrDefault(userId, new HashSet<>());
    }

    public int getTotalSessions() {
        return sessionToUserId.size();
    }

    public int getTotalRoadmapSubscriptions() {
        return roadmapSubscriptions.size();
    }

    public void subscribeToTeam(Long userId, Long teamId) {
        teamSubscriptions.computeIfAbsent(teamId.toString(), k -> new HashSet<>()).add(userId);
    }

    public void unsubscribeFromTeam(Long userId, Long teamId) {
        Set<Long> subscribers = teamSubscriptions.get(teamId.toString());
        if (subscribers != null) {
            subscribers.remove(userId);
            if (subscribers.isEmpty()) {
                teamSubscriptions.remove(teamId.toString());
            }
        }
    }

    public Set<Long> getUsersSubscribedToTeam(Long teamId) {
        return teamSubscriptions.getOrDefault(teamId.toString(), new HashSet<>());
    }

    public int getTotalTeamSubscriptions() {
        return teamSubscriptions.size();
    }
}
