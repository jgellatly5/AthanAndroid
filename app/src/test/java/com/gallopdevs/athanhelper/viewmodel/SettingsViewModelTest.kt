package com.gallopdevs.athanhelper.viewmodel

import com.gallopdevs.athanhelper.data.PrayerCalculator.Companion.HANAFI
import com.gallopdevs.athanhelper.data.PrayerCalculator.Companion.ISNA
import com.gallopdevs.athanhelper.data.PrayerCalculator.Companion.JAFARI
import com.gallopdevs.athanhelper.data.PrayerCalculator.Companion.MIDNIGHT
import com.gallopdevs.athanhelper.data.PrayerCalculator.Companion.ONE_SEVENTH
import com.gallopdevs.athanhelper.data.PrayerCalculator.Companion.SHAFII
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.ASR_METHOD
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.CALCULATION_METHOD
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.ENABLE_NOTIFICATIONS
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.LATITUDES_METHOD
import com.gallopdevs.athanhelper.repository.SettingsRepo
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class SettingsViewModelTest {

    private lateinit var testObject: SettingsViewModel

    private val mockSettingsRepo: SettingsRepo = mock()

    @Test
    fun get_boolean_enable_notifications_true_successful() {
        val expectedBoolean = true
        whenever(mockSettingsRepo.getBoolean(ENABLE_NOTIFICATIONS, false))
            .thenReturn(expectedBoolean)

        testObject = SettingsViewModel(mockSettingsRepo)
        assertEquals(expectedBoolean, testObject.getBoolean(ENABLE_NOTIFICATIONS, false))
    }

    @Test
    fun get_int_calculation_method_isna_successful() {
        val expectedInt = ISNA
        whenever(mockSettingsRepo.getInt(CALCULATION_METHOD, JAFARI))
            .thenReturn(expectedInt)

        testObject = SettingsViewModel(mockSettingsRepo)
        assertEquals(expectedInt, testObject.getInt(CALCULATION_METHOD, JAFARI))
    }

    @Test
    fun get_int_asr_method_hanafi_successful() {
        val expectedInt = HANAFI
        whenever(mockSettingsRepo.getInt(ASR_METHOD, SHAFII))
            .thenReturn(expectedInt)

        testObject = SettingsViewModel(mockSettingsRepo)
        assertEquals(expectedInt, testObject.getInt(ASR_METHOD, SHAFII))
    }

    @Test
    fun get_int_latitudes_method_one_seventh_successful() {
        val expectedInt = ONE_SEVENTH
        whenever(mockSettingsRepo.getInt(LATITUDES_METHOD, MIDNIGHT))
            .thenReturn(expectedInt)

        testObject = SettingsViewModel(mockSettingsRepo)
        assertEquals(expectedInt, testObject.getInt(LATITUDES_METHOD, MIDNIGHT))
    }
}
