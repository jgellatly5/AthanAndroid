package com.gallopdevs.athanhelper.viewmodel

import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.ASR_METHOD
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.CALCULATION_METHOD
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.ENABLE_NOTIFICATIONS
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.LATITUDES_METHOD
import com.gallopdevs.athanhelper.domain.GetSharedPrefsUseCase
import com.gallopdevs.athanhelper.domain.SaveSharedPrefsUseCase
import com.gallopdevs.athanhelper.domain.SharedPrefType
import com.gallopdevs.athanhelper.utilities.HANAFI
import com.gallopdevs.athanhelper.utilities.ISNA
import com.gallopdevs.athanhelper.utilities.JAFARI
import com.gallopdevs.athanhelper.utilities.MIDNIGHT
import com.gallopdevs.athanhelper.utilities.ONE_SEVENTH
import com.gallopdevs.athanhelper.utilities.SHAFII
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class SettingsViewModelTest {

    private lateinit var testObject: SettingsViewModel

    private val getSharedPrefsUseCase: GetSharedPrefsUseCase = mock()
    private val saveSharedPrefsUseCase: SaveSharedPrefsUseCase = mock()

    @Test
    fun get_boolean_enable_notifications_true_successful() {
        val expectedBoolean = true
        whenever(getSharedPrefsUseCase(SharedPrefType.BOOLEAN, ENABLE_NOTIFICATIONS, false))
            .thenReturn(expectedBoolean)

        testObject = SettingsViewModel(getSharedPrefsUseCase, saveSharedPrefsUseCase)
        assertEquals(expectedBoolean, testObject.getBoolean(ENABLE_NOTIFICATIONS, false))
    }

    @Test
    fun get_int_calculation_method_isna_successful() {
        val expectedInt = ISNA
        whenever(getSharedPrefsUseCase(SharedPrefType.INT, CALCULATION_METHOD, JAFARI))
            .thenReturn(expectedInt)

        testObject = SettingsViewModel(getSharedPrefsUseCase, saveSharedPrefsUseCase)
        assertEquals(expectedInt, testObject.getInt(CALCULATION_METHOD, JAFARI))
    }

    @Test
    fun get_int_asr_method_hanafi_successful() {
        val expectedInt = HANAFI
        whenever(getSharedPrefsUseCase(SharedPrefType.INT, ASR_METHOD, SHAFII))
            .thenReturn(expectedInt)

        testObject = SettingsViewModel(getSharedPrefsUseCase, saveSharedPrefsUseCase)
        assertEquals(expectedInt, testObject.getInt(ASR_METHOD, SHAFII))
    }

    @Test
    fun get_int_latitudes_method_one_seventh_successful() {
        val expectedInt = ONE_SEVENTH
        whenever(getSharedPrefsUseCase(SharedPrefType.INT, LATITUDES_METHOD, MIDNIGHT))
            .thenReturn(expectedInt)

        testObject = SettingsViewModel(getSharedPrefsUseCase, saveSharedPrefsUseCase)
        assertEquals(expectedInt, testObject.getInt(LATITUDES_METHOD, MIDNIGHT))
    }
}
