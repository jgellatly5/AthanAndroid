package com.gallopdevs.athanhelper

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import com.gallopdevs.athanhelper.ui.dayview.DayViewScreenConstants.NEXT_PRAYER
import com.gallopdevs.athanhelper.ui.dayview.HighlightedPrayer
import org.junit.Rule
import org.junit.Test

class DayViewScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenPrayerNameIsHighlightedShowOval() {
        composeTestRule.setContent {
            HighlightedPrayer(prayerTitle = stringResource(id = R.string.dawn), showHighlighted = true)
        }

        composeTestRule
            .onNodeWithContentDescription(NEXT_PRAYER)
            .assertIsDisplayed()
    }

    @Test
    fun whenPrayerNameIsNotHighlightedDoNotShowOval() {
        composeTestRule.setContent {
            HighlightedPrayer(prayerTitle = stringResource(id = R.string.dawn), showHighlighted = false)
        }

        composeTestRule
            .onNodeWithContentDescription("Next Prayer")
            .assertDoesNotExist()
    }
}
