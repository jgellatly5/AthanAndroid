package com.gallopdevs.athanhelper

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.gallopdevs.athanhelper.compose.NotificationsOption
import com.gallopdevs.athanhelper.compose.SettingsScreen
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {

    private val context: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun notificationsOptionIsDisplayedCorrectly() {
        val notifications = context.getString(R.string.notifications)
        composeTestRule.apply {
            setContent {
                NotificationsOption(checked = false, onCheckedChange = {})
            }
            this.onNodeWithText(notifications)
                .assertIsDisplayed()

            this.onNodeWithContentDescription(notifications)
                .assertIsDisplayed()

            this.onNode(isToggleable())
                .assertIsDisplayed()
                .assertIsOff()
        }
    }

    @Test
    fun notificationsOptionSwitchTogglesOnAndOff() {
        composeTestRule.apply {
            setContent {
                SettingsScreen()
            }

            this.onNode(isToggleable())
                .performClick()
                .assertIsOn()
                .performClick()
                .assertIsOff()
        }
    }
}
