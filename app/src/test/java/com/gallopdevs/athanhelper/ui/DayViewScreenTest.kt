package com.gallopdevs.athanhelper.ui

import android.app.Application
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.gallopdevs.athanhelper.data.models.TimingsResponse
import com.gallopdevs.athanhelper.domain.NextPrayer
import com.gallopdevs.athanhelper.domain.NextPrayerTime
import com.gallopdevs.athanhelper.domain.PrayerInfo
import com.gallopdevs.athanhelper.domain.PrayerTimes
import com.gallopdevs.athanhelper.test
import com.gallopdevs.athanhelper.ui.dayview.DayViewScreen
import com.gallopdevs.athanhelper.ui.dayview.DayViewScreenConstants.DAY_OF_WEEK_PLUS_DATE_HEADER
import com.gallopdevs.athanhelper.ui.dayview.DayViewScreenConstants.LOADING_STATE
import com.gallopdevs.athanhelper.ui.dayview.DayViewScreenConstants.PRAYER_ROW
import com.gallopdevs.athanhelper.viewmodel.PrayerInfoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
@Config(application = Application::class)
class DayViewScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        ShadowLog.stream = System.out
    }

    @Test
    fun `Loading state shows progress indicator`() {
        createDayViewScreen(prayerInfoUiState = PrayerInfoUiState.Loading)

        composeTestRule.onNodeWithTag(LOADING_STATE).assertIsDisplayed()
    }

    @Test
    fun `Success state shows prayer info`() {
        val pageIndex = 0
        val prayerInfo = PrayerInfo.test(
            nextPrayerTime = NextPrayerTime.test(
                nextPrayerTimeMillis = 10000,
                nextPrayer = NextPrayer.test(
                    name = "Fajr",
                    index = 0
                )
            ),
            prayerTimesList = listOf(
                PrayerTimes.test(
                    date = "24 Apr 2024",
                    timingsResponse = TimingsResponse.test()
                )
            )
        )

        createDayViewScreen(
            pageIndex = pageIndex,
            prayerInfo = prayerInfo,
            prayerInfoUiState = PrayerInfoUiState.Success(prayerInfo)
        )

        with(composeTestRule) {
            onRoot().printToLog("Success")
            onNodeWithTag(LOADING_STATE).assertDoesNotExist()
            onNodeWithTag(DAY_OF_WEEK_PLUS_DATE_HEADER).assertIsDisplayed()
            onNodeWithTag(DAY_OF_WEEK_PLUS_DATE_HEADER).assertTextContains(prayerInfo.prayerTimesList[pageIndex].date)
            onAllNodesWithTag(PRAYER_ROW).apply {
                fetchSemanticsNodes().forEachIndexed { i, _ ->
                    get(i).assertIsDisplayed()
                    prayerInfo.prayerTimesList[pageIndex].timingsResponse.timings?.fajr?.let {
                        get(i).onChildren()[2].assertTextContains(it)
                    }
                }
            }
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
