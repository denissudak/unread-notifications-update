package com.denissudak.notifications.update;

import com.denissudak.notifications.Notification;
import com.denissudak.notifications.data.JobCancellationData;
import com.denissudak.notifications.data.JobDetails;
import com.denissudak.notifications.data.NewJobAssignmentData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JobCancellationNotificationUpdateTest {
    @Mock
    private Notification notification;

    private JobCancellationNotificationUpdate update;

    @Mock
    private JobCancellationData thisNotificationJobCancellationData;

    private static final long jobId = 1L;
    private static final long job2Id = 2L;

    @BeforeEach
    public void setUp() {
        when(notification.isJobCancellation()).thenReturn(true);
        when(notification.getJobCancellationData()).thenReturn(thisNotificationJobCancellationData);
        update = new JobCancellationNotificationUpdate(notification);
        when(thisNotificationJobCancellationData.getJobId()).thenReturn(jobId);
    }

    @Test
    public void shouldDeleteOnMatchingNewJobAssignment() {
        // when and then
        assertThat(update.process(new NewJobAssignmentData(jobId, mock(JobDetails.class)))).isTrue();

        // then
        assertThat(update.isDeleted()).isTrue();
    }

    @Test
    public void shouldIgnoreOtherNewJobAssignments() {
        // when and then
        assertThat(update.process(new NewJobAssignmentData(job2Id, mock(JobDetails.class)))).isFalse();

        // then
        assertThat(update.isDeleted()).isFalse();
    }


}
