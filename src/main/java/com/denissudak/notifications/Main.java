package com.denissudak.notifications;

import com.denissudak.notifications.data.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

public class Main {

    private final NotificationFactory notificationFactory = new NotificationFactory();
    private final ExistingNotificationUpdateFactory notificationUpdateFactory = new ExistingNotificationUpdateFactory();

    private NotificationSet existingNotifications(Set<Notification> notifications) {
        return new NotificationSet(notifications, notificationFactory, notificationUpdateFactory);
    }

    /**
     * <p>The set of existing notification contains:</p>
     * <ul>
     *     <li>New job (1) enquiry: 2025-04-21, 12:00pm - 3:00pm, 48 Pirrama Rd, Pyrmont NSW 2009</li>
     *     <li>New job (2) enquiry: 2025-04-25, 7:00am - 9:00am, 44 Driver Avenue, Moore Park NSW 2021</li>
     * </ul>
     *
     * <p>The new notification is: "The job (1) has changed to 18th of April, 12pm - 5pm,  48 Pirrama Rd, Pyrmont NSW 2009"</p>
     * <p>We expect the set of existing notifications to be updated so that it contains:</p>
     * <ul>
     *     <li>New job (1) enquiry: 2025-04-21, 12:00pm - <strong>5:00pm</strong>, 48 Pirrama Rd, Pyrmont NSW 2009</li>
     *     <li>New job (2) enquiry: 2025-04-25, 7:00am - 9:00am, 44 Driver Avenue, Moore Park NSW 2021</li>
     * </ul>
     * <p>Then we receive notification about job 1 being cancelled. The user should be presented only with:</p>
     * <ul>
     *     <li>New job (2) enquiry: 2025-04-25, 7:00am - 9:00am, 44 Driver Avenue, Moore Park NSW 2021</li>
     * </ul>
     *
     */
    private void runUseCase1() {
        JobDetails job1Details = new JobDetails(
                LocalDate.of(2025, 4, 21),
                LocalTime.of(12, 0),
                LocalTime.of(15, 0),
                "48 Pirrama Rd, Pyrmont NSW 2009"
        );
        JobDetails job2Details = new JobDetails(
                LocalDate.of(2025, 4, 25),
                LocalTime.of(7, 0),
                LocalTime.of(9, 0),
                "44 Driver Avenue, Moore Park NSW 2021"
        );
        NotificationSet existingNotifications = existingNotifications(newHashSet(
                newJobEnquiry(1L, job1Details),
                newJobEnquiry(2L, job2Details)
        ));

        // new notification
        JobDetails newJobDetails = new JobDetails(
                LocalDate.of(2025, 4, 21),
                LocalTime.of(12, 0),
                LocalTime.of(17, 0),
                "48 Pirrama Rd, Pyrmont NSW 2009"
        );
        existingNotifications.add(new JobDetailsChangeData(1L, job1Details, newJobDetails));

        // expecting to see single updated new job enquiry
        System.out.println(existingNotifications.getNotifications());

        // another notification
        existingNotifications.add(new JobCancellationData(2L));

        // expecting notification about job 2 to be removed: asking a user if they can do a job and that then got cancelled is pointless.
        System.out.println(existingNotifications.getNotifications());
    }

    /**
     * <p>
     * Here, at the start, we only have one notification:
     * <ul>
     *     <li>You are assigned to the job (1): 2025-04-21, 12:00pm - 3:00pm, 48 Pirrama Rd, Pyrmont NSW 2009</li>
     * </ul>
     * </p>
     * <p>The new notification is: "The job (1) has changed to 2025-04-21, 12pm - 5pm, 48 Pirrama Rd, Pyrmont NSW 2009"</p>
     * <p>We expect the set of existing notifications to be updated so that it contains:</p>
     * <ul>
     *     <li>You are assigned to the job (1): 2025-04-21, 12:00pm - <strong>5:00pm</strong>, 48 Pirrama Rd, Pyrmont NSW 2009</li>
     * </ul>
     * <p>Then we receive notification about job 1 being cancelled. User should see no notifications: whatever job assignment they had is cancelled</p>
     */
    private void runUseCase2() {
        //assignment
        JobDetails jobDetails = new JobDetails(
                LocalDate.of(2025, 4, 21),
                LocalTime.of(12, 0),
                LocalTime.of(15, 0),
                "48 Pirrama Rd, Pyrmont NSW 2009"
        );
        Notification newJobAssignment = newJobAssignment(1L, jobDetails);
        NotificationSet existingNotifications = existingNotifications(newHashSet(newJobAssignment));

        // new notification
        JobDetails changedJobDetails = new JobDetails(
                LocalDate.of(2025, 4, 21),
                LocalTime.of(12, 0),
                LocalTime.of(17, 0),
                "48 Pirrama Rd, Pyrmont NSW 2009"
        );
        existingNotifications.add(new JobDetailsChangeData(1L, jobDetails, changedJobDetails));

        // expecting to see single updated new job assignment
        System.out.println(existingNotifications.getNotifications());

        // another notification
        existingNotifications.add(new JobCancellationData(1L));

        // expecting to see an empty set as it's the same as letting the user know that they've got the job that then got cancelled
        System.out.println(existingNotifications.getNotifications());
    }

    public static void main(String[] args) {
        Main testCases = new Main();
        System.out.println("===Use case 1===");
        testCases.runUseCase1();
        System.out.println("===Use case 2===");
        testCases.runUseCase2();
    }

    public static Notification newJobEnquiry(Long jobId, JobDetails jobDetails) {
        Notification notification = new Notification();
        NewJobEnquiryData newJobData = new NewJobEnquiryData(jobId, jobDetails);
        notification.setNewJobEnquiryData(newJobData);

        return notification;
    }

    public static Notification newJobAssignment(Long jobId, JobDetails jobDetails) {
        Notification notification = new Notification();
        notification.setNewJobAssignmentData(new NewJobAssignmentData(jobId, jobDetails));

        return notification;
    }
}
