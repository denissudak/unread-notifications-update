package com.denissudak.notifications.data;

import com.denissudak.notifications.Notification;
import com.denissudak.notifications.update.ExistingNotificationUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import static com.google.common.base.Preconditions.checkNotNull;

@NoArgsConstructor
public class NewJobEnquiryData extends BaseNotificationData implements NotificationData {

    public NewJobEnquiryData(Long jobId, JobDetails jobDetails) {
        super(jobId, jobDetails);
    }

    @Override
    public void update(Notification notification) {
        checkNotNull(notification);

        notification.setNewJobEnquiryData(this);
    }

    @Override
    public NewNotificationAddition createNewNotificationAddition() {
        return new NotificationDataAdditionImpl(this);
    }

    @RequiredArgsConstructor
    private static class NotificationDataAdditionImpl implements NewNotificationAddition {
        private final NewJobEnquiryData data;
        private boolean relevant = true;

        @Override
        public void call(ExistingNotificationUpdate update) {
            checkNotNull(update);

            if (update.process(data) && update.getNotification().isNewJobEnquiry()) {
                relevant = false;
            }
        }

        @Override
        public boolean isRelevant() {
            return relevant;
        }
    }
}
