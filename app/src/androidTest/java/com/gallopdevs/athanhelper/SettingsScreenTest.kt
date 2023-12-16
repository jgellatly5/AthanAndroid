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
import com.gallopdevs.athanhelper.settings.PreferencesManager
import com.gallopdevs.athanhelper.settings.PreferencesManagerImpl.Companion.ENABLE_NOTIFICATIONS
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class SettingsScreenTest {

    private val context: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext

    private val preferencesManager: PreferencesManager = mock()

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
        whenever(preferencesManager.getData(ENABLE_NOTIFICATIONS, false)).thenReturn(false)
        composeTestRule.apply {
            setContent {
                SettingsScreen(preferencesManager)
            }

            this.onNode(isToggleable())
                .performClick()
                .assertIsOn()
                .performClick()
                .assertIsOff()
        }
    }

    @Test
    fun notificationsOptionSwitchIsOnIfSharedPrefsTrue() {
        whenever(preferencesManager.getData(ENABLE_NOTIFICATIONS, false)).thenReturn(true)
        composeTestRule.apply {
            setContent {
                SettingsScreen(preferencesManager)
            }

            this.onNode(isToggleable())
                .assertIsOn()
        }
    }
}
