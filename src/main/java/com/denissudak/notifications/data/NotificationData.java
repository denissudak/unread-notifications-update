package com.denissudak.notifications.data;


import com.denissudak.notifications.Notification;

public interface NotificationData {

    /**
     * Updates notification with its data
     */
    void update(Notification notification);

    NewNotificationAddition createNewNotificationAddition();
}
