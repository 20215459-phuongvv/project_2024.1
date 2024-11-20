package com.hust.project3.services;

import com.hust.project3.entities.Book;
import com.hust.project3.entities.Notification;
import com.hust.project3.exceptions.NotFoundException;
import jakarta.mail.MessagingException;

import java.util.List;

public interface NotificationService {
    void createNotification(Long userId, String message);
    List<Notification> getUnreadNotifications(String jwt) throws NotFoundException;
    void markAsRead(Long notificationId);
    void notifySubscribers(Book book) throws MessagingException;
}
