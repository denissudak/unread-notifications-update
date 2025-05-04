package com.denissudak.notifications.update;


import com.denissudak.notifications.Notification;
import com.denissudak.notifications.data.JobCancellationData;
import com.denissudak.notifications.data.JobDetailsChangeData;
import com.denissudak.notifications.data.NewJobAssignmentData;
import com.denissudak.notifications.data.NewJobEnquiryData;

/**
 * <p>Represents an update of the existing notification in response to the new data.</p>
 * <code>process()</code> methods return true if this notification was updated by the data.
 * If that's true that means that the existing notification has "absorbed" this new data and as a result there is no need to display this new data in a separate notification.
 * <p>
 * For example a new notification about changes in the job details becomes irrelevant if it has updated the existing job changes data.
 * Alternatively the assigned job cancellation is still relevant even after it removed job details changes notification,
 * but becomes irrelevant after removing new job assignment notification.
 * </p>
 */
public interface ExistingNotificationUpdate {
    Notification getNotification();

    default boolean process(NewJobAssignmentData data) {
        return false;
    }

    default boolean process(JobCancellationData data) {
        return false;
    }

    default boolean process(JobDetailsChangeData data) {
        return false;
    }

    default boolean process(NewJobEnquiryData data) {
        return false;
    }


    boolean isDeleted();
}
