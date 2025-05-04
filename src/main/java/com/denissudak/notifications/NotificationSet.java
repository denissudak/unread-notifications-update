package com.denissudak.notifications;

import com.denissudak.notifications.data.NewNotificationAddition;
import com.denissudak.notifications.data.NotificationData;
import com.denissudak.notifications.update.ExistingNotificationUpdate;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Objects.requireNonNull;

public class NotificationSet {
    private final Set<Notification> notifications;
    private final NotificationFactory notificationFactory;
    private final ExistingNotificationUpdateFactory notificationUpdateFactory;

    public NotificationSet(Set<Notification> notifications, NotificationFactory notificationFactory, ExistingNotificationUpdateFactory notificationUpdateFactory) {
        this.notifications = newHashSet(requireNonNull(notifications));
        this.notificationFactory = requireNonNull(notificationFactory);
        this.notificationUpdateFactory = requireNonNull(notificationUpdateFactory);
    }

    public void add(NotificationData newNotificationData) {
        checkNotNull(newNotificationData);

        NewNotificationAddition newNotificationAddition = newNotificationData.createNewNotificationAddition();
        for (Notification n : newHashSet(notifications)) {
            ExistingNotificationUpdate existingNotificationUpdate = notificationUpdateFactory.createNotificationUpdate(n);
            newNotificationAddition.call(existingNotificationUpdate);
            if (existingNotificationUpdate.isDeleted()) {
                notifications.remove(n);
            }
        }
        if (newNotificationAddition.isRelevant()) {
            notifications.add(notificationFactory.newNotification(newNotificationData));
        }
    }

    public Set<Notification> getNotifications() {
        return ImmutableSet.copyOf(notifications);
    }
}
