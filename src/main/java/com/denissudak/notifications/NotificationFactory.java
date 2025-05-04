package com.denissudak.notifications;

import com.denissudak.notifications.data.NotificationData;

import static com.google.common.base.Preconditions.checkNotNull;

public class NotificationFactory {

    public Notification newNotification(NotificationData notificationData){
        checkNotNull(notificationData);

        Notification notification = new Notification();
        notification.setNotificationData(notificationData);

        return notification;
    }
}
