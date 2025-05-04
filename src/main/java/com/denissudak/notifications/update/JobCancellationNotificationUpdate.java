package com.denissudak.notifications.update;

import com.denissudak.notifications.Notification;
import com.denissudak.notifications.data.JobCancellationData;
import com.denissudak.notifications.data.NewJobAssignmentData;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.requireNonNull;

public class JobCancellationNotificationUpdate implements ExistingNotificationUpdate {
    private final Notification notification;
    private boolean deleted = false;

    public JobCancellationNotificationUpdate(Notification notification) {
        this.notification = requireNonNull(notification);
        checkState(notification.isJobCancellation());
    }

    /**
     * If job got cancelled and then restored (assigned again), then it's as if it was never cancelled in the first place.
     */
    @Override
    public boolean process(NewJobAssignmentData data) {
        checkNotNull(data);
        checkState(!deleted);

        JobCancellationData jobCancellationData = notification.getJobCancellationData();
        deleted = jobCancellationData.getJobId().equals(data.getJobId());

        return deleted;
    }

    @Override
    public Notification getNotification() {
        return notification;
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }
}
