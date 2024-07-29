package com.overdevx.mystoryapp.ui.dashboard

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.overdevx.mystoryapp.MainActivity
import com.overdevx.mystoryapp.R
import com.overdevx.mystoryapp.utils.EspressoIdlingResource
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class DashboardFragmentTest{
    //private lateinit var simpleIdlingResource: SimpleIdlingResource
    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        //simpleIdlingResource = SimpleIdlingResource(5000)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun uploadDataTest() {
        Thread.sleep(5000)
        // Navigate to the upload fragment using BottomNavigationView
        onView(withId(R.id.nav_view)).perform(selectBottomNavItem(R.id.navigation_dashboard))

        // Check if the upload fragment is displayed
        onView(withId(R.id.fragment_dashboard)).check(matches(isDisplayed()))

//        // Click the button to upload image
//        onView(withId(R.id.card_choose)).perform(click())
//
//        // Alternatively, if choosing from camera
//        onView(withId(R.id.materialCardView)).perform(click())
//
//        //tak a picture
//        onView(withId(R.id.captureImage)).perform(click())

        // Fill in the data
        onView(withId(R.id.et_desc)).perform(click(), replaceText("Sample description"), closeSoftKeyboard())
        onView(withId(R.id.btn_upload)).perform(click())

        // Check if success dialog is displayed
        onView(withText(R.string.your_story_has_been_successfully_uploaded)).check(matches(isDisplayed()))

        // Close the dialog
        onView(withId(R.id.btn_oke)).perform(click())

        // Verify if the description field is cleared
        onView(withId(R.id.et_desc)).check(matches(withText("")))

    }
    private fun selectBottomNavItem(itemId: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "Click on bottom navigation item with id $itemId"
            }

            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(BottomNavigationView::class.java)
            }

            override fun perform(uiController: UiController?, view: View?) {
                (view as BottomNavigationView).selectedItemId = itemId
            }
        }
    }
}