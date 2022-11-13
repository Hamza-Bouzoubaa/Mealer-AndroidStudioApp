package com.SEG2505_Group8.mealer;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.SEG2505_Group8.mealer.Database.DatabaseClient;
import com.SEG2505_Group8.mealer.Database.Models.MealerRole;
import com.SEG2505_Group8.mealer.Database.Models.MealerUser;
import com.google.common.util.concurrent.SettableFuture;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.MockitoSession;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Calendar;
import java.util.Date;


@RunWith(MockitoJUnitRunner.class)
public class UserTests {

    @Mock
    DatabaseClient databaseClient;

    @Before
    public void setupTests() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.openMocks(this);

        SettableFuture<Boolean> falseFuture = SettableFuture.create();
        falseFuture.set(false);

        when(databaseClient.updateUser(any())).thenReturn(falseFuture);
    }

    @Test
    public void suspendUserPermanently() {
        MealerUser user = new MealerUser("testId", MealerRole.USER, "fName", "lName", "email@example.com", "100 test st", "1111111111111111", "d", "void@example.com");

        // Ensure user is not suspended
        Assert.assertFalse(user.getIsSuspended());

        // Suspend the user
        user.suspend(null);

        // Ensure they were permanently suspended
        Assert.assertTrue(user.getIsSuspended());
        Assert.assertNull(user.getSuspendedUntil());
    }

    @Test
    public void suspendUserTemporarily() {
        MealerUser user = new MealerUser("testId", MealerRole.USER, "fName", "lName", "email@example.com", "100 test st", "1111111111111111", "d", "void@example.com");

        // Create a Date object
        Date suspensionEndDate = new Date();

        // Set the date to the next day
        Calendar c = Calendar.getInstance();
        c.setTime(suspensionEndDate);
        c.add(Calendar.DATE, 1);
        suspensionEndDate = c.getTime();

        // Ensure user is not suspended
        Assert.assertFalse(user.getIsSuspended());

        // Suspend the user
        user.suspend(suspensionEndDate);

        // Ensure they were permanently suspended
        Assert.assertTrue(user.getIsSuspended());
        Assert.assertNotNull(user.getSuspendedUntil());
    }

    @Test
    public void expirationOfExpiredSuspension() {
        MealerUser user = new MealerUser("testId", MealerRole.USER, "fName", "lName", "email@example.com", "100 test st", "1111111111111111", "d", "void@example.com");

        // Create a Date object
        Date suspensionEndDate = new Date();

        // Set the date to the previous day (Thus creating a suspension that's already passed)
        Calendar c = Calendar.getInstance();
        c.setTime(suspensionEndDate);
        c.add(Calendar.DATE, -1);
        suspensionEndDate = c.getTime();

        // Ensure user is not suspended
        Assert.assertFalse(user.getIsSuspended());

        // Suspend the user
        user.suspend(suspensionEndDate);

        // Ensure they were permanently suspended
        Assert.assertFalse(user.isSuspended(databaseClient));
        Assert.assertNull(user.getSuspendedUntil());
    }

    @Test
    public void expirationOfActiveSuspension() {
        MealerUser user = new MealerUser("testId", MealerRole.USER, "fName", "lName", "email@example.com", "100 test st", "1111111111111111", "d", "void@example.com");

        // Create a Date object
        Date suspensionEndDate = new Date();

        // Set the date to the next day (Thus creating a suspension that's still valid)
        Calendar c = Calendar.getInstance();
        c.setTime(suspensionEndDate);
        c.add(Calendar.DATE, 1);
        suspensionEndDate = c.getTime();

        // Ensure user is not suspended
        Assert.assertFalse(user.getIsSuspended());

        // Suspend the user
        user.suspend(suspensionEndDate);

        // Ensure they were permanently suspended
        Assert.assertTrue(user.isSuspended(databaseClient));
        Assert.assertNotNull(user.getSuspendedUntil());
    }
}
