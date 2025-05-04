package com.denissudak.notifications.data;

import com.denissudak.notifications.Notification;
import com.denissudak.notifications.update.ExistingNotificationUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Objects.requireNonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobCancellationData implements NotificationData {

    private Long jobId;

    @Override
    public void update(Notification notification) {
        checkNotNull(notification);

        notification.setJobCancellationData(this);
    }

    @Override
    public NewNotificationAddition createNewNotificationAddition() {
        return new NotificationDataAdditionImpl(this);
    }

    private static class NotificationDataAdditionImpl implements NewNotificationAddition {
        private final JobCancellationData data;
        private boolean relevant = true;

        private NotificationDataAdditionImpl(JobCancellationData data) {
            this.data = requireNonNull(data);
        }

        @Override
        public void call(ExistingNotificationUpdate update) {
            Notification existingNotification = update.getNotification();
            if (update.process(data) && (existingNotification.isNewJobAssignment() || existingNotification.isNewJobEnquiry())) {
                relevant = false;
            }
        }

        @Override
        public boolean isRelevant() {
            return relevant;
        }
    }
}
