package com.denissudak.notifications;


import com.denissudak.notifications.data.*;
import com.denissudak.notifications.test.JsonAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.denissudak.notifications.Notification.NotificationType.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NotificationTest {

    private Notification notification;

    @BeforeEach
    public void setUp() {
        notification = new Notification();
    }

    @Test
    public void shouldSetJobEnquiryData() {
        // given
        JobDetails jobDetailsData = new JobDetails(LocalDate.of(2022, 1, 10), LocalTime.of(7, 40), LocalTime.of(11, 30), "address");
        NewJobEnquiryData newRoleData = new NewJobEnquiryData(1L, jobDetailsData);

        // when
        notification.setNewJobEnquiryData(newRoleData);

        // then
        JsonAssert.assertThat(notification.getBodyJson())
                .jsonPath("$.jobId", is(1))
                .jsonPath("$.jobDetails.jobDate", is("2022-01-10"))
                .jsonPath("$.jobDetails.startTime", is("07:40:00"))
                .jsonPath("$.jobDetails.endTime", is("11:30:00"))
                .jsonPath("$.jobDetails.address", is("address"));
        assertThat(notification.getType()).isEqualTo(NEW_JOB_ENQUIRY);
        assertThat(notification.getNewJobEnquiryData()).usingRecursiveComparison().ignoringFields("jobId").isEqualTo(newRoleData);
    }

    @Test
    public void shouldSetJobDetailsChangeData() {
        // given
        JobDetails before = new JobDetails(LocalDate.of(2022, 1, 10), LocalTime.of(7, 40), LocalTime.of(11, 30), "address");
        JobDetails after = new JobDetails(LocalDate.of(2022, 1, 12), LocalTime.of(7, 45), LocalTime.of(12, 50), "address2");
        JobDetailsChangeData jobContactDetailsChangeData = new JobDetailsChangeData(1L, before, after);

        // when
        notification.setJobDetailsChangeData(jobContactDetailsChangeData);

        // then
        JsonAssert.assertThat(notification.getBodyJson())
                .jsonPath("$.jobId", is(1))
                .jsonPath("$.before.jobDate", is("2022-01-10"))
                .jsonPath("$.before.startTime", is("07:40:00"))
                .jsonPath("$.before.endTime", is("11:30:00"))
                .jsonPath("$.before.address", is("address"))
                .jsonPath("$.after.jobDate", is("2022-01-12"))
                .jsonPath("$.after.startTime", is("07:45:00"))
                .jsonPath("$.after.endTime", is("12:50:00"))
                .jsonPath("$.after.address", is("address2"));
        assertThat(notification.getType()).isEqualTo(JOB_DETAILS_CHANGE);
        assertThat(notification.getJobDetailsChangeData()).usingRecursiveComparison().isEqualTo(jobContactDetailsChangeData);
    }

    @Test
    public void shouldSetAssignedRoleData() {
        // given
        JobDetails jobDetailsData = new JobDetails(LocalDate.of(2022, 1, 10), LocalTime.of(7, 40), LocalTime.of(11, 30), "address");
        NewJobAssignmentData assignedRoleData = new NewJobAssignmentData(3L, jobDetailsData);

        // when
        notification.setNewJobAssignmentData(assignedRoleData);

        // then
        JsonAssert.assertThat(notification.getBodyJson())
                .jsonPath("$.jobId", is(3))
                .jsonPath("$.jobDetails.jobDate", is("2022-01-10"))
                .jsonPath("$.jobDetails.startTime", is("07:40:00"))
                .jsonPath("$.jobDetails.endTime", is("11:30:00"))
                .jsonPath("$.jobDetails.address", is("address"));
        assertThat(notification.getType()).isEqualTo(NEW_JOB_ASSIGNMENT);
        assertThat(notification.getNewJobAssignmentData()).usingRecursiveComparison().ignoringFields("jobId").isEqualTo(assignedRoleData);
    }


    @Test
    public void shouldSetNotificationData() {
        // given
        NotificationData notificationData = mock(NotificationData.class);

        // when
        notification.setNotificationData(notificationData);

        // then
        verify(notificationData).update(notification);
    }

    @Test
    public void shouldDetectNotificationType() {
        // given
        notification.setType(NEW_JOB_ENQUIRY);

        // when and then
        assertThat(notification.isNewJobEnquiry()).isTrue();
        assertThat(notification.isJobCancellation()).isFalse();

        /////////// and given
        notification.setType(JOB_CANCELLATION);

        // when and then
        assertThat(notification.isJobCancellation()).isTrue();
        assertThat(notification.isJobDetailsChange()).isFalse();

        /////////// and given
        notification.setType(JOB_DETAILS_CHANGE);

        // when and then
        assertThat(notification.isJobDetailsChange()).isTrue();
        assertThat(notification.isNewJobAssignment()).isFalse();

        /////////// and given
        notification.setType(NEW_JOB_ASSIGNMENT);

        // when and then
        assertThat(notification.isNewJobAssignment()).isTrue();
        assertThat(notification.isJobDetailsChange()).isFalse();
    }
}
