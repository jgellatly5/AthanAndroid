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
import com.gallopdevs.athanhelper.ui.settings.ExpandableItem
import com.gallopdevs.athanhelper.ui.settings.ExpandableListItem
import com.gallopdevs.athanhelper.ui.settings.NotificationsOption
import com.gallopdevs.athanhelper.ui.settings.SettingsScreen
import com.gallopdevs.athanhelper.ui.settings.PreferencesManager
import com.gallopdevs.athanhelper.ui.settings.PreferencesManagerImpl.Companion.SETTINGS
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
        whenever(preferencesManager.getBoolean(SETTINGS, false)).thenReturn(false)
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
        whenever(preferencesManager.getBoolean(SETTINGS, false)).thenReturn(true)
        composeTestRule.apply {
            setContent {
                SettingsScreen(preferencesManager)
            }

            this.onNode(isToggleable())
                .assertIsOn()
        }
    }

    @Test
    fun calculationMethodsOptionIsDisplayedCorrectly() {
        val calculationMethod = context.resources.getString(R.string.calculation_method)
        val calculationMethodOptions = context.resources.getStringArray(R.array.calculation_methods).toList()
        val expandableItem = ExpandableItem(
            title = calculationMethod,
            drawableId = R.drawable.sum_icon,
            options = calculationMethodOptions
        )
        composeTestRule.apply {
            setContent {
                ExpandableListItem(expandableItem)
            }
            this.onNodeWithText(calculationMethod)
                .assertIsDisplayed()

            this.onNodeWithContentDescription(calculationMethod)
                .assertIsDisplayed()
        }
    }
}
