package com.gallopdevs.athanhelper.ui

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.gallopdevs.athanhelper.domain.NextPrayerTime
import com.gallopdevs.athanhelper.domain.PrayerInfo
import com.gallopdevs.athanhelper.domain.PrayerTimes
import com.gallopdevs.athanhelper.test
import com.gallopdevs.athanhelper.ui.dayview.DayViewScreen
import com.gallopdevs.athanhelper.ui.dayview.DayViewScreenConstants.DAY_OF_WEEK_PLUS_DATE_HEADER
import com.gallopdevs.athanhelper.ui.dayview.DayViewScreenConstants.LOADING_STATE
import com.gallopdevs.athanhelper.viewmodel.PrayerInfoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = Application::class)
class DayViewScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `Loading state shows progress indicator`() {
        createDayViewScreen(prayerInfoUiState = PrayerInfoUiState.Loading)

        composeTestRule.onNodeWithTag(LOADING_STATE).assertIsDisplayed()
    }

    @Test
    fun `Success state shows prayer info`() {
        val prayerInfo = PrayerInfo.test(
            nextPrayerTime = NextPrayerTime.test(),
            prayerTimesList = listOf(
                PrayerTimes.test()
            )
        )

        createDayViewScreen(
            prayerInfo = prayerInfo,
            prayerInfoUiState = PrayerInfoUiState.Success(prayerInfo)
        )

        with(composeTestRule) {
            onNodeWithTag(LOADING_STATE).assertDoesNotExist()
            onNodeWithTag(DAY_OF_WEEK_PLUS_DATE_HEADER).assertIsDisplayed()
        }
    }

    private fun createDayViewScreen(
        pageIndex: Int = 0,
        prayerInfo: PrayerInfo = PrayerInfo.test(),
        prayerInfoUiState: PrayerInfoUiState
    ) {
        val uiState = when (prayerInfoUiState) {
            PrayerInfoUiState.Loading -> MutableStateFlow(PrayerInfoUiState.Loading)
            is PrayerInfoUiState.Success -> MutableStateFlow(PrayerInfoUiState.Success(prayerInfo = prayerInfo))
            is PrayerInfoUiState.Error -> MutableStateFlow(PrayerInfoUiState.Error(message = "Error"))
        }
        composeTestRule.setContent {
            DayViewScreen(
                pageIndex = pageIndex,
                prayerViewModel = mock {
                    on { this.prayerInfoUiState } doReturn uiState
                }
            )
        }
    }
}
