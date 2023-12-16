package com.gallopdevs.athanhelper

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.gallopdevs.athanhelper.compose.DayViewScreen
import com.gallopdevs.athanhelper.compose.DayViewScreenConstants.DAY_VIEW_SCREEN
import org.junit.Rule
import org.junit.Test
import java.util.Calendar

class DayViewScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun dayViewScreenTest_nextPrayerHighlighted() {
        val c = Calendar.getInstance()
        val weekDay = c.get(Calendar.DAY_OF_WEEK)
        val month = c.get(Calendar.MONTH) + 1
        val dayOfMonth = c.get(Calendar.DAY_OF_MONTH)

        composeTestRule.setContent {
            DayViewScreen(weekDay = weekDay, month = month, dayOfMonth = dayOfMonth, pageIndex = 0)
        }

        composeTestRule
            .onNodeWithTag(DAY_VIEW_SCREEN)
            .assertIsDisplayed()
    }
}
