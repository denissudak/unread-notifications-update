package com.denissudak.notifications;

import com.denissudak.notifications.data.*;
import lombok.Getter;
import lombok.Setter;

import static com.denissudak.notifications.JsonSerializer.toJson;
import static com.denissudak.notifications.JsonSerializer.toObject;
import static com.google.common.base.Preconditions.*;

@Getter
@Setter
public class Notification {
    private NotificationType type;
    private String bodyJson;

    public void setJobDetailsChangeData(JobDetailsChangeData jobContactDetailsChangeData) {
        checkNotNull(jobContactDetailsChangeData);

        this.type = NotificationType.JOB_DETAILS_CHANGE;
        this.bodyJson = toJson(jobContactDetailsChangeData);
    }

    public void setNewJobEnquiryData(NewJobEnquiryData newJobEnquiryData) {
        checkNotNull(newJobEnquiryData);

        this.type = NotificationType.NEW_JOB_ENQUIRY;
        this.bodyJson = toJson(newJobEnquiryData);
    }

    public void setNewJobAssignmentData(NewJobAssignmentData newJobAssignmentData) {
        checkNotNull(newJobAssignmentData);

        this.type = NotificationType.NEW_JOB_ASSIGNMENT;
        this.bodyJson = toJson(newJobAssignmentData);
    }

    public NewJobEnquiryData getNewJobEnquiryData() {
        checkState(isNewJobEnquiry());

        return toObject(bodyJson, NewJobEnquiryData.class);
    }

    public JobCancellationData getJobCancellationData() {
        checkState(isJobCancellation());

        return toObject(bodyJson, JobCancellationData.class);
    }

    public JobDetailsChangeData getJobDetailsChangeData() {
        checkState(isJobDetailsChange());

        return toObject(bodyJson, JobDetailsChangeData.class);
    }

    public NewJobAssignmentData getNewJobAssignmentData() {
        checkState(isNewJobAssignment());

        return toObject(bodyJson, NewJobAssignmentData.class);
    }

    public void setNotificationData(NotificationData notificationData) {
        checkNotNull(notificationData);

        notificationData.update(this);
    }

    public boolean isNewJobEnquiry() {
        return type == NotificationType.NEW_JOB_ENQUIRY;
    }

    public boolean isNewJobAssignment() {
        return type == NotificationType.NEW_JOB_ASSIGNMENT;
    }

    public boolean isJobDetailsChange() {
        return type == NotificationType.JOB_DETAILS_CHANGE;
    }

    public boolean isJobCancellation() {
        return type == NotificationType.JOB_CANCELLATION;
    }

    public void setJobCancellationData(JobCancellationData jobCancellationData) {
        checkNotNull(jobCancellationData);

        this.type = NotificationType.JOB_CANCELLATION;
        this.bodyJson = toJson(jobCancellationData);
    }

    public enum NotificationType {
        NEW_JOB_ENQUIRY,
        NEW_JOB_ASSIGNMENT,
        JOB_DETAILS_CHANGE,
        JOB_CANCELLATION,
    }

    @Override
    public String toString() {
        if (isNewJobEnquiry()) {
            NewJobEnquiryData data = getNewJobEnquiryData();
            return "New job (%d) enquiry: ".formatted(data.getJobId()) + data.getJobDetails();
        } else if (isNewJobAssignment()) {
            NewJobAssignmentData data = getNewJobAssignmentData();
            return "You are assigned to the job %d: ".formatted(data.getJobId()) + data.getJobDetails();
        } else if (isJobDetailsChange()) {
            JobDetailsChangeData data = getJobDetailsChangeData();
            return "Job %d has changed to ".formatted(data.getJobId()) + data.getAfter();
        } else if (isJobCancellation()) {
            return "Job %d is cancelled".formatted(getJobCancellationData().getJobId());
        } else {
            throw new IllegalStateException(type + " can not be converted to a string");
        }
    }
}
