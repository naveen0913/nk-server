package com.sample.sample.Service;

import com.sample.sample.DTO.NotificationDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyAdmin(NotificationDTO notification) {
        messagingTemplate.convertAndSend("/topic/admin", notification);
    }

    public void notifyUser(String userId, NotificationDTO notification) {
        messagingTemplate.convertAndSend("/topic/user/" + userId, notification);
    }
}
