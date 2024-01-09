package com.gallopdevs.athanhelper.repository

import com.gallopdevs.athanhelper.data.PrayerCalculator.Companion.HANAFI
import com.gallopdevs.athanhelper.data.PrayerCalculator.Companion.ISNA
import com.gallopdevs.athanhelper.data.PrayerCalculator.Companion.JAFARI
import com.gallopdevs.athanhelper.data.PrayerCalculator.Companion.MIDNIGHT
import com.gallopdevs.athanhelper.data.PrayerCalculator.Companion.ONE_SEVENTH
import com.gallopdevs.athanhelper.data.PrayerCalculator.Companion.SHAFII
import com.gallopdevs.athanhelper.data.PreferencesMgr
import com.gallopdevs.athanhelper.data.PreferencesManager.Companion.ASR_METHOD
import com.gallopdevs.athanhelper.data.PreferencesManager.Companion.CALCULATION_METHOD
import com.gallopdevs.athanhelper.data.PreferencesManager.Companion.ENABLE_NOTIFICATIONS
import com.gallopdevs.athanhelper.data.PreferencesManager.Companion.LATITUDES_METHOD
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class SettingsRepositoryTest {

    private lateinit var testObject: SettingsRepository

    private val mockPreferencesMgr: PreferencesMgr = mock()

    @Test
    fun get_boolean_enable_notifications_true_successful() {
        val expectedBoolean = true
        whenever(mockPreferencesMgr.getBoolean(ENABLE_NOTIFICATIONS, false))
            .thenReturn(expectedBoolean)

        testObject = SettingsRepository(mockPreferencesMgr)
        assertEquals(expectedBoolean, testObject.getBoolean(ENABLE_NOTIFICATIONS, false))
    }

    @Test
    fun get_int_calculation_method_isna_successful() {
        val expectedInt = ISNA
        whenever(mockPreferencesMgr.getInt(CALCULATION_METHOD, JAFARI)).thenReturn(expectedInt)

        testObject = SettingsRepository(mockPreferencesMgr)
        assertEquals(expectedInt, testObject.getInt(CALCULATION_METHOD, JAFARI))
    }

    @Test
    fun get_int_asr_method_hanafi_successful() {
        val expectedInt = HANAFI
        whenever(mockPreferencesMgr.getInt(ASR_METHOD, SHAFII)).thenReturn(expectedInt)

        testObject = SettingsRepository(mockPreferencesMgr)
        assertEquals(expectedInt, testObject.getInt(ASR_METHOD, SHAFII))
    }

    @Test
    fun get_int_latitudes_method_one_seventh_successful() {
        val expectedInt = ONE_SEVENTH
        whenever(mockPreferencesMgr.getInt(LATITUDES_METHOD, MIDNIGHT)).thenReturn(expectedInt)

        testObject = SettingsRepository(mockPreferencesMgr)
        assertEquals(expectedInt, testObject.getInt(LATITUDES_METHOD, MIDNIGHT))
    }
}
