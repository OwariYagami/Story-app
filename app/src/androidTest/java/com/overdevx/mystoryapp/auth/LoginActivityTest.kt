package com.overdevx.mystoryapp.auth


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.overdevx.mystoryapp.R
import com.overdevx.mystoryapp.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {
    @get:Rule
    val activity = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun loginAndNavigateToHomeFragment() {
        // Input email and password
        onView(withId(R.id.et_email)).perform(typeText("yagami@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.et_password)).perform(typeText("123456789"), closeSoftKeyboard())

        // Click login button
        onView(withId(R.id.btn_login)).perform(click())

        // Check if HomeFragment is displayed
        //onView(withId(R.id.recycler_item)).check(matches(isDisplayed()))

//        onView(withId(R.id.recycler_item)).perform(
//            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
//                10
//            )
//        )
    }
}