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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


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

    /**
     * Test that calling suspend without providing a suspension end date permanently suspends the user
     */
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

    /**
     * Test that calling suspend with a suspension end date properly sets end date
     */
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

    /**
     * Test that expired suspensions are revoked by isSuspended();
     */
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
        Assert.assertFalse(user.getIsSuspended());
        Assert.assertNull(user.getSuspendedUntil());
    }

    /**
     * Test that valid suspensions are not revoked by isSuspended();
     */
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

    /**
     * Test average ratings are being calculated properly
     */
    @Test
    public void averageRatings() {
        MealerUser user = new MealerUser("id", MealerRole.CHEF, "firstName", "lastName", "email@mealer.com", "100 adr st", "1111111111111111", "a desc", "void");

        HashMap<String, List<String>> ratings = new HashMap<>();

        // Two 1 star reviews
        List<String> rate1Star = new ArrayList<>();
        rate1Star.add("userId1");
        rate1Star.add("userId2");
        ratings.put("0", rate1Star);

        // One 5 star review
        List<String> rate5Star = new ArrayList<>();
        rate5Star.add("userId3");
        ratings.put("4", rate5Star);

        user.setRatings(ratings);

        Assert.assertEquals(7f / 3, user.averageRating(), 0.1f);
    }

    @Test
    public void rateAddRating() {
        MealerUser user = new MealerUser("id", MealerRole.CHEF, "firstName", "lastName", "email@mealer.com", "100 adr st", "1111111111111111", "a desc", "void");

        HashMap<String, List<String>> ratings = new HashMap<>();

        // Two 1 star reviews
        List<String> rate1Star = new ArrayList<>();
        rate1Star.add("userId1");
        rate1Star.add("userId2");
        ratings.put("0", rate1Star);

        // One 5 star review
        List<String> rate5Star = new ArrayList<>();
        rate5Star.add("userId3");
        ratings.put("4", rate5Star);

        user.setRatings(ratings);

        Assert.assertEquals(7f / 3, user.averageRating(), 0.1f);

        user.rate(4, "userId4");

        Assert.assertEquals(12f / 4, user.averageRating(), 0.1f);
    }

    @Test
    public void rateChangeRating() {
        MealerUser user = new MealerUser("id", MealerRole.CHEF, "firstName", "lastName", "email@mealer.com", "100 adr st", "1111111111111111", "a desc", "void");

        HashMap<String, List<String>> ratings = new HashMap<>();

        // Two 1 star reviews
        List<String> rate1Star = new ArrayList<>();
        rate1Star.add("userId1");
        rate1Star.add("userId2");
        ratings.put("0", rate1Star);

        // One 5 star review
        List<String> rate5Star = new ArrayList<>();
        rate5Star.add("userId3");
        ratings.put("4", rate5Star);

        user.setRatings(ratings);

        Assert.assertEquals(7f / 3, user.averageRating(), 0.1f);

        user.rate(0, "userId3");

        Assert.assertEquals(3f / 3, user.averageRating(), 0.1f);
    }

    @Test
    public void rateChangeRatingRepeated() {
        MealerUser user = new MealerUser("id", MealerRole.CHEF, "firstName", "lastName", "email@mealer.com", "100 adr st", "1111111111111111", "a desc", "void");

        HashMap<String, List<String>> ratings = new HashMap<>();

        // Two 1 star reviews
        List<String> rate1Star = new ArrayList<>();
        rate1Star.add("userId1");
        rate1Star.add("userId2");
        ratings.put("0", rate1Star);

        // One 5 star review
        List<String> rate5Star = new ArrayList<>();
        rate5Star.add("userId3");
        ratings.put("4", rate5Star);

        user.setRatings(ratings);

        Assert.assertEquals(7f / 3, user.averageRating(), 0.1f);

        user.rate(0, "userId3");

        Assert.assertEquals(3f / 3, user.averageRating(), 0.1f);

        user.rate(2, "userId3");

        Assert.assertEquals(5f / 3, user.averageRating(), 0.1f);

        user.rate(3, "userId3");

        Assert.assertEquals(6f / 3, user.averageRating(), 0.1f);

    }
}
