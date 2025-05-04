package com.denissudak.notifications.update;

import com.denissudak.notifications.Notification;
import com.denissudak.notifications.data.JobCancellationData;
import com.denissudak.notifications.data.JobDetailsChangeData;
import com.denissudak.notifications.data.NewJobAssignmentData;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.requireNonNull;

public class NewJobAssignmentNotificationUpdate implements ExistingNotificationUpdate {
    private final Notification notification;
    private boolean deleted;

    public NewJobAssignmentNotificationUpdate(Notification notification) {
        this.notification = requireNonNull(notification);
        checkState(notification.isNewJobAssignment());
    }

    @Override
    public boolean process(JobCancellationData assignedJobCancellationData) {
        checkNotNull(assignedJobCancellationData);
        checkState(!deleted);

        deleted = assignedJobCancellationData.getJobId().equals(getThisNotificationData().getJobId());

        return deleted;
    }

    @Override
    public boolean process(JobDetailsChangeData jobDetailsChangeData) {
        checkNotNull(jobDetailsChangeData);

        NewJobAssignmentData thisNotificationData = getThisNotificationData();
        if (thisNotificationData.getJobId().equals(jobDetailsChangeData.getJobId())) {
            thisNotificationData.setJobDetails(jobDetailsChangeData.getAfter());
            thisNotificationData.update(notification);

            return true;
        }

        return false;
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public Notification getNotification() {
        return notification;
    }

    private NewJobAssignmentData getThisNotificationData() {
        return notification.getNewJobAssignmentData();
    }

}
