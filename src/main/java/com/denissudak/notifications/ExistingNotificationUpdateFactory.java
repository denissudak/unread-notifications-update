package com.denissudak.notifications;

import com.denissudak.notifications.update.*;

import static com.google.common.base.Preconditions.checkNotNull;

public class ExistingNotificationUpdateFactory {

    public ExistingNotificationUpdate createNotificationUpdate(Notification notification) {
        checkNotNull(notification);

        return switch (notification.getType()) {
            case NEW_JOB_ENQUIRY -> new NewJobEnquiryNotificationUpdate(notification);
            case NEW_JOB_ASSIGNMENT -> new NewJobAssignmentNotificationUpdate(notification);
            case JOB_DETAILS_CHANGE -> new JobDetailsChangeNotificationUpdate(notification);
            case JOB_CANCELLATION -> new JobCancellationNotificationUpdate(notification);
        };
    }
}
