package com.denissudak.notifications;
 
import com.denissudak.notifications.update.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith; 
import org.mockito.Mock; 
import org.mockito.junit.jupiter.MockitoExtension;

import static com.denissudak.notifications.Notification.NotificationType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExistingNotificationUpdateFactoryTest {

    private final ExistingNotificationUpdateFactory factory = new ExistingNotificationUpdateFactory();

    @Mock
    private Notification notification;

    @Test
    public void shouldCreateNewJobEnquiryNotificationUpdate() {
        // given
        when(notification.isNewJobEnquiry()).thenReturn(true);
        when(notification.getType()).thenReturn(NEW_JOB_ENQUIRY);

        // when
        ExistingNotificationUpdate notificationUpdate = factory.createNotificationUpdate(notification);

        // then
        assertThat(notificationUpdate).isNotNull().isInstanceOf(NewJobEnquiryNotificationUpdate.class);
    }

    @Test
    public void shouldCreateNewJobAssignmentNotificationUpdate() {
        // given
        when(notification.isNewJobAssignment()).thenReturn(true);
        when(notification.getType()).thenReturn(NEW_JOB_ASSIGNMENT);

        // when
        ExistingNotificationUpdate notificationUpdate = factory.createNotificationUpdate(notification);

        // then
        assertThat(notificationUpdate).isNotNull().isInstanceOf(NewJobAssignmentNotificationUpdate.class);
    }

    @Test
    public void shouldCreateJobDetailsChangeNotificationUpdate() {
        // given
        when(notification.isJobDetailsChange()).thenReturn(true);
        when(notification.getType()).thenReturn(JOB_DETAILS_CHANGE);

        // when
        ExistingNotificationUpdate notificationUpdate = factory.createNotificationUpdate(notification);

        // then
        assertThat(notificationUpdate).isNotNull().isInstanceOf(JobDetailsChangeNotificationUpdate.class);
    }

    @Test
    public void shouldCreateJobCancellationNotificationUpdate() {
        // given
        when(notification.isJobCancellation()).thenReturn(true);
        when(notification.getType()).thenReturn(JOB_CANCELLATION);

        // when
        ExistingNotificationUpdate notificationUpdate = factory.createNotificationUpdate(notification);

        // then
        assertThat(notificationUpdate).isNotNull().isInstanceOf(JobCancellationNotificationUpdate.class);
    }

}
