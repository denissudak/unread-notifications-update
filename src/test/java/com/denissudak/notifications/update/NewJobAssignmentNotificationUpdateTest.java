package com.denissudak.notifications.update;

import com.denissudak.notifications.Notification;
import com.denissudak.notifications.data.JobCancellationData;
import com.denissudak.notifications.data.JobDetails;
import com.denissudak.notifications.data.JobDetailsChangeData;
import com.denissudak.notifications.data.NewJobAssignmentData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NewJobAssignmentNotificationUpdateTest {

    @Mock
    private Notification notification;

    private NewJobAssignmentNotificationUpdate update;

    @Mock
    private NewJobAssignmentData thisNotificationAssignedRoleData;

    private static final long jobId = 1L;
    private static final long job2Id = 2L;

    @BeforeEach
    public void setUp() {
        when(notification.isNewJobAssignment()).thenReturn(true);
        when(thisNotificationAssignedRoleData.getJobId()).thenReturn(jobId);
        when(notification.getNewJobAssignmentData()).thenReturn(thisNotificationAssignedRoleData);
        update = new NewJobAssignmentNotificationUpdate(notification);
    }

    @Test
    public void shouldDeleteNotificationOnJobCancellation() {
        // when and then
        assertThat(update.process(new JobCancellationData(jobId))).isTrue();

        // and
        assertThat(update.isDeleted()).isTrue();
    }

    @Test
    public void shouldIgnoreOtherJobCancellation() {
        // when and then
        assertThat(update.process(new JobCancellationData(job2Id))).isFalse();

        // and
        assertThat(update.isDeleted()).isFalse();
    }

    @Test
    public void shouldChangeOnJobDetailsChange() {
        // given
        JobDetails before = mock(JobDetails.class);
        JobDetails after = mock(JobDetails.class);

        // when and then
        assertThat(update.process(new JobDetailsChangeData(jobId, before, after))).isTrue();

        // and
        InOrder inOrder = Mockito.inOrder(thisNotificationAssignedRoleData);
        inOrder.verify(thisNotificationAssignedRoleData).setJobDetails(after);
        inOrder.verify(thisNotificationAssignedRoleData).update(notification);

        // and
        assertThat(update.isDeleted()).isFalse();
    }

    @Test
    public void shouldIgnoreOtherJobDetailsChange() {
        // given
        JobDetails before = mock(JobDetails.class);
        JobDetails after = mock(JobDetails.class);

        // when and then
        assertThat(update.process(new JobDetailsChangeData(job2Id, before, after))).isFalse();

        // and
        assertThat(update.isDeleted()).isFalse();

    }

}
