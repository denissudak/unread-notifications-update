package com.denissudak.notifications.update;

import com.denissudak.notifications.Notification;
import com.denissudak.notifications.data.JobCancellationData;
import com.denissudak.notifications.data.JobDetailsChangeData;
import com.denissudak.notifications.data.NewJobEnquiryData;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.requireNonNull;

public class NewJobEnquiryNotificationUpdate implements ExistingNotificationUpdate {

    private final Notification notification;
    private boolean deleted = false;

    public NewJobEnquiryNotificationUpdate(Notification notification) {
        this.notification = requireNonNull(notification);
        checkState(notification.isNewJobEnquiry());
    }

    @Override
    public boolean process(JobDetailsChangeData newRoleData) {
        checkNotNull(newRoleData);
        checkState(!deleted);

        NewJobEnquiryData thisNotificationData = getThisNotificationData();
        if (newRoleData.getJobId().equals(thisNotificationData.getJobId())) {
            thisNotificationData.setJobDetails(newRoleData.getAfter());
            thisNotificationData.update(notification);

            return true;
        }

        return false;
    }

    @Override
    public boolean process(JobCancellationData cancellationData) {
        checkNotNull(cancellationData);
        checkState(!deleted);

        deleted = cancellationData.getJobId().equals(getThisNotificationData().getJobId());

        return deleted;
    }

    @Override
    public boolean process(NewJobEnquiryData newJobEnquiryData) {
        checkNotNull(newJobEnquiryData);
        checkState(!deleted);

        return newJobEnquiryData.getJobId().equals(getThisNotificationData().getJobId());
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public Notification getNotification() {
        return notification;
    }

    private NewJobEnquiryData getThisNotificationData() {
        return notification.getNewJobEnquiryData();
    }

}
