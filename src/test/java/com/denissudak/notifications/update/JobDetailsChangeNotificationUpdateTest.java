package com.denissudak.notifications.update;

import com.denissudak.notifications.Notification;
import com.denissudak.notifications.data.JobCancellationData;
import com.denissudak.notifications.data.JobDetails;
import com.denissudak.notifications.data.JobDetailsChangeData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JobDetailsChangeNotificationUpdateTest {
    @Mock
    private Notification notification;

    private JobDetailsChangeNotificationUpdate update;

    @Mock
    private JobDetailsChangeData thisNotificationJobDetailsChangeData;

    private static final long jobId = 1L;
    private static final long job2Id = 2L;

    @BeforeEach
    public void setUp() {
        when(notification.isJobDetailsChange()).thenReturn(true);
        when(notification.getJobDetailsChangeData()).thenReturn(thisNotificationJobDetailsChangeData);
        update = new JobDetailsChangeNotificationUpdate(notification);
        when(thisNotificationJobDetailsChangeData.getJobId()).thenReturn(jobId);
    }

    @Test
    public void shouldDeleteWhenChangesAreReverted() {
        // given
        JobDetails before = mock(JobDetails.class);
        JobDetails after = mock(JobDetails.class);
        when(thisNotificationJobDetailsChangeData.isAddressChange()).thenReturn(false);
        when(thisNotificationJobDetailsChangeData.isDateChange()).thenReturn(false);
        when(thisNotificationJobDetailsChangeData.isTimeChange()).thenReturn(false);

        // when and the
        assertThat(update.process(new JobDetailsChangeData(jobId, before, after))).isTrue();

        // then
        assertThat(update.isDeleted()).isTrue();
    }

    @Test
    public void shouldProcessMatchingJobChanges() {
        // given
        JobDetails before = mock(JobDetails.class);
        JobDetails after = mock(JobDetails.class);
        JobDetailsChangeData newData = new JobDetailsChangeData(jobId, before, after);
        when(thisNotificationJobDetailsChangeData.isAddressChange()).thenReturn(true);

        // when and then
        assertThat(update.process(newData)).isTrue();

        // and
        InOrder inOrder = Mockito.inOrder(thisNotificationJobDetailsChangeData);
        inOrder.verify(thisNotificationJobDetailsChangeData).setAfter(after);
        inOrder.verify(thisNotificationJobDetailsChangeData).update(notification);

        // and
        assertThat(update.isDeleted()).isFalse();
    }

    @Test
    public void shouldIgnoreOtherJobChanges() {
        // given
        JobDetails before = mock(JobDetails.class);
        JobDetails after = mock(JobDetails.class);
        JobDetailsChangeData newData = new JobDetailsChangeData(job2Id, before, after);

        // when and then
        assertThat(update.process(newData)).isFalse();

        // and
        assertThat(update.isDeleted()).isFalse();
    }

    @Test
    public void shouldProcessJobCancellation() {
        // given
        JobCancellationData newData = new JobCancellationData(jobId);

        // when and then
        assertThat(update.process(newData)).isTrue();

        // and
        assertThat(update.isDeleted()).isTrue();
    }

    @Test
    public void shouldIgnoreOtherJobCancellation() {
        // given
        JobCancellationData newData = new JobCancellationData(job2Id);

        // when and then
        assertThat(update.process(newData)).isFalse();

        // and
        assertThat(update.isDeleted()).isFalse();
    }
}
