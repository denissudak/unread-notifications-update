package com.denissudak.notifications.update;


import com.denissudak.notifications.Notification;
import com.denissudak.notifications.data.JobCancellationData;
import com.denissudak.notifications.data.JobDetailsChangeData;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.requireNonNull;

public class JobDetailsChangeNotificationUpdate implements ExistingNotificationUpdate {
    private final Notification notification;
    private boolean deleted = false;

    public JobDetailsChangeNotificationUpdate(Notification notification) {
        this.notification = requireNonNull(notification);
        checkState(notification.isJobDetailsChange());
    }

    @Override
    public boolean process(JobDetailsChangeData changeData) {
        checkNotNull(changeData);
        checkState(!deleted);

        JobDetailsChangeData jobDetailsChangeData = getThisNotificationData();
        if (changeData.getJobId().equals(jobDetailsChangeData.getJobId())) {
            jobDetailsChangeData.setAfter(changeData.getAfter());
            if (jobDetailsChangeData.isAddressChange() || jobDetailsChangeData.isDateChange() || jobDetailsChangeData.isTimeChange()) {
                jobDetailsChangeData.update(notification);
            } else {
                deleted = true;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean process(JobCancellationData cancellationData) {
        checkNotNull(cancellationData);
        checkState(!deleted);

        deleted = getThisNotificationData().getJobId().equals(cancellationData.getJobId());
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

    private JobDetailsChangeData getThisNotificationData() {
        return notification.getJobDetailsChangeData();
    }

}
