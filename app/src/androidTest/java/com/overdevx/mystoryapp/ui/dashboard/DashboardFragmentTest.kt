package com.overdevx.mystoryapp.ui.dashboard

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
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
import com.overdevx.mystoryapp.data.retrofit.ApiConfig
import com.overdevx.mystoryapp.utils.EspressoIdlingResource
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class DashboardFragmentTest{
    private val mockWebServer = MockWebServer()
    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        mockWebServer.start(8080)
        ApiConfig.BASE_URL = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun uploadDataTest() {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("upload_response.json"))
        mockWebServer.enqueue(mockResponse)
        Thread.sleep(2000)

        // Navigate to the dashboard fragment
        onView(withId(R.id.nav_view)).perform(selectBottomNavItem(R.id.navigation_dashboard))
        onView(withId(R.id.fragment_dashboard)).check(matches(isDisplayed()))
        onView(withId(R.id.card_choose)).perform(click())
        onView(withId(R.id.materialCardView)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.captureImage)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.et_desc)).perform(typeText("Sample description"), closeSoftKeyboard())
        onView(withId(R.id.btn_upload)).perform(click())
        onView(withText(R.string.your_story_has_been_successfully_uploaded)).check(matches(isDisplayed()))
        Thread.sleep(2000)
        onView(withId(R.id.btn_oke)).perform(click())

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