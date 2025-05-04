package com.denissudak.notifications.data;

import com.denissudak.notifications.update.ExistingNotificationUpdate;

/**
 * Linked to a new notification and represents "addition" of this new notification to the set of existing notification.
 */
public interface NewNotificationAddition {
    /**
     * Updates existing notification with this new data.
     */
    void call(ExistingNotificationUpdate update);

    /**
     * <p>Return true if this notification is still relevant to be presented to the user.</p>
     * <p>A new notification data might become irrelevant after calling {@link ExistingNotificationUpdate}.</p>
     * <p>For example a new notification about changes in the job details is irrelevant if it has updated the existing notification about job details changes.
     * Another example: the job cancellation notification is still relevant even after it removed job details changes notification</p>
     */
    boolean isRelevant();
}
