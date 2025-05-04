package com.denissudak.notifications.data;

import com.denissudak.notifications.Notification;
import com.denissudak.notifications.update.ExistingNotificationUpdate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.requireNonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobDetailsChangeData implements NotificationData {
    private Long jobId;
    private JobDetails before;
    private JobDetails after;

    @JsonIgnore
    public boolean isDateChange() {
        return !before.getJobDate().isEqual(after.getJobDate());
    }

    @JsonIgnore
    public boolean isTimeChange() {
        return !before.getStartTime().equals(after.getStartTime()) || !before.getEndTime().equals(after.getEndTime());
    }

    @JsonIgnore
    public boolean isAddressChange() {
        return !before.getAddress().equals(after.getAddress());
    }

    @Override
    public void update(Notification notification) {
        checkNotNull(notification);

        notification.setJobDetailsChangeData(this);
    }

    @Override
    public NewNotificationAddition createNewNotificationAddition() {
        return new NotificationDataAdditionImpl(this);
    }

    private static class NotificationDataAdditionImpl implements NewNotificationAddition {
        private final JobDetailsChangeData data;
        private boolean relevant = true;

        private NotificationDataAdditionImpl(JobDetailsChangeData data) {
            this.data = requireNonNull(data);
        }

        @Override
        public void call(ExistingNotificationUpdate update) {
            if (update.process(data)) {
                relevant = false;
            }
        }

        @Override
        public boolean isRelevant() {
            return relevant;
        }
    }
}
