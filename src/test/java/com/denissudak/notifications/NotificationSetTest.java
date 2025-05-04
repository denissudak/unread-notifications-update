package com.denissudak.notifications;

import com.denissudak.notifications.data.NewNotificationAddition;
import com.denissudak.notifications.data.NotificationData;
import com.denissudak.notifications.update.ExistingNotificationUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationSetTest {

    private NotificationSet notificationSet;

    @Mock
    private Notification notification1, notification2;

    @Mock
    private NotificationData notificationData;

    @Mock
    private ExistingNotificationUpdate notificationUpdate1, notificationUpdate2;

    @Mock
    private NewNotificationAddition notificationDataAddition;

    @Mock
    private NotificationFactory notificationFactory;

    @Mock
    private Notification newNotification;

    @Mock
    private ExistingNotificationUpdateFactory notificationUpdateFactory;

    @BeforeEach
    public void setUp() {
        notificationSet = new NotificationSet(newHashSet(notification1, notification2), notificationFactory, notificationUpdateFactory);
        when(notificationUpdateFactory.createNotificationUpdate(notification1)).thenReturn(notificationUpdate1);
        when(notificationUpdateFactory.createNotificationUpdate(notification2)).thenReturn(notificationUpdate2);
        when(notificationData.createNewNotificationAddition()).thenReturn(notificationDataAddition);
    }

    /**
     * Here notification set initially has two notifications: notification1 and notification2. New notification is expected to delete the second one and then be added to the set.
     */
    @Test
    public void shouldAddNotification() {
        // given
        when(notificationDataAddition.isRelevant()).thenReturn(true);
        when(notificationUpdate2.isDeleted()).thenReturn(true);
        when(notificationFactory.newNotification(notificationData)).thenReturn(newNotification);

        // when
        notificationSet.add(notificationData);

        // then
        verify(notificationDataAddition).call(notificationUpdate1);
        verify(notificationDataAddition).call(notificationUpdate2);

        // and
        assertThat(notificationSet.getNotifications()).hasSize(2).contains(notification1, newNotification);
    }

    /**
     * Here notification set initially has two notifications: notification1 and notification2. New notification is expected to only update existing notifications and not be added to the set.
     */
    @Test
    public void shouldNotAddNotification() {
        // given
        when(notificationDataAddition.isRelevant()).thenReturn(false);
        lenient().when(notificationFactory.newNotification(notificationData)).thenReturn(newNotification);

        // when
        notificationSet.add(notificationData);

        // then
        verify(notificationDataAddition).call(notificationUpdate1);
        verify(notificationDataAddition).call(notificationUpdate2);

        // and
        assertThat(notificationSet.getNotifications()).hasSize(2).contains(notification1, notification2);
    }


}
