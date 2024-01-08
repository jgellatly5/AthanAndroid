package com.gallopdevs.athanhelper.repository

import com.gallopdevs.athanhelper.data.PrayerCalculatorIpml.Companion.HANAFI
import com.gallopdevs.athanhelper.data.PrayerCalculatorIpml.Companion.ISNA
import com.gallopdevs.athanhelper.data.PrayerCalculatorIpml.Companion.JAFARI
import com.gallopdevs.athanhelper.data.PrayerCalculatorIpml.Companion.MIDNIGHT
import com.gallopdevs.athanhelper.data.PrayerCalculatorIpml.Companion.ONE_SEVENTH
import com.gallopdevs.athanhelper.data.PrayerCalculatorIpml.Companion.SHAFII
import com.gallopdevs.athanhelper.data.PreferencesManager
import com.gallopdevs.athanhelper.data.PreferencesManagerImpl.Companion.ASR_METHOD
import com.gallopdevs.athanhelper.data.PreferencesManagerImpl.Companion.CALCULATION_METHOD
import com.gallopdevs.athanhelper.data.PreferencesManagerImpl.Companion.ENABLE_NOTIFICATIONS
import com.gallopdevs.athanhelper.data.PreferencesManagerImpl.Companion.LATITUDES_METHOD
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class SettingsRepositoryTest {

    private lateinit var testObject: SettingsRepository

    private val mockPreferencesManager: PreferencesManager = mock()

    @Test
    fun get_boolean_enable_notifications_true_successful() {
        val expectedBoolean = true
        whenever(mockPreferencesManager.getBoolean(ENABLE_NOTIFICATIONS, false))
            .thenReturn(expectedBoolean)

        testObject = SettingsRepository(mockPreferencesManager)
        assertEquals(expectedBoolean, testObject.getBoolean(ENABLE_NOTIFICATIONS, false))
    }

    @Test
    fun get_int_calculation_method_isna_successful() {
        val expectedInt = ISNA
        whenever(mockPreferencesManager.getInt(CALCULATION_METHOD, JAFARI)).thenReturn(expectedInt)

        testObject = SettingsRepository(mockPreferencesManager)
        assertEquals(expectedInt, testObject.getInt(CALCULATION_METHOD, JAFARI))
    }

    @Test
    fun get_int_asr_method_hanafi_successful() {
        val expectedInt = HANAFI
        whenever(mockPreferencesManager.getInt(ASR_METHOD, SHAFII)).thenReturn(expectedInt)

        testObject = SettingsRepository(mockPreferencesManager)
        assertEquals(expectedInt, testObject.getInt(ASR_METHOD, SHAFII))
    }

    @Test
    fun get_int_latitudes_method_one_seventh_successful() {
        val expectedInt = ONE_SEVENTH
        whenever(mockPreferencesManager.getInt(LATITUDES_METHOD, MIDNIGHT)).thenReturn(expectedInt)

        testObject = SettingsRepository(mockPreferencesManager)
        assertEquals(expectedInt, testObject.getInt(LATITUDES_METHOD, MIDNIGHT))
    }
}
