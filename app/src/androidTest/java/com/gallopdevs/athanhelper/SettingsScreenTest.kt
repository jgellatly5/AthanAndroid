package com.gallopdevs.athanhelper

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import com.gallopdevs.athanhelper.compose.NotificationsOption
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {

    private val context: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun settingsOptionIsDisplayedWithTextPassedIntoIt() {
        val notifications = context.getString(R.string.notifications)
        composeTestRule.setContent {
            NotificationsOption()
        }
        composeTestRule
            .onNodeWithText(notifications)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(notifications)
            .assertIsDisplayed()
    }
}
