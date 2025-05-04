package com.denissudak.notifications.data;

import com.denissudak.notifications.Notification;
import com.denissudak.notifications.update.ExistingNotificationUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


@Getter
@NoArgsConstructor
public class NewJobAssignmentData extends BaseNotificationData implements NotificationData {

    public NewJobAssignmentData(Long jobId, JobDetails jobDetails) {
        super(jobId, jobDetails);
    }

    @Override
    public void update(Notification notification) {
        checkNotNull(notification);

        notification.setNewJobAssignmentData(this);
    }

    @Override
    public NewNotificationAddition createNewNotificationAddition() {
        return new NotificationDataAdditionImpl(this);
    }

    @RequiredArgsConstructor
    private static class NotificationDataAdditionImpl implements NewNotificationAddition {
        private final NewJobAssignmentData data;

        @Override
        public void call(ExistingNotificationUpdate update) {
            update.process(data);
        }

        @Override
        public boolean isRelevant() {
            return true;
        }
    }
}
