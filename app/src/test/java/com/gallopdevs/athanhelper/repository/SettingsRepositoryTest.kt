package com.gallopdevs.athanhelper.repository

import com.gallopdevs.athanhelper.data.SettingsLocalDataSource
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.ASR_METHOD
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.CALCULATION_METHOD
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.ENABLE_NOTIFICATIONS
import com.gallopdevs.athanhelper.data.SharedPreferencesLocalDataSource.Companion.LATITUDES_METHOD
import com.gallopdevs.athanhelper.utilities.HANAFI
import com.gallopdevs.athanhelper.utilities.ISNA
import com.gallopdevs.athanhelper.utilities.JAFARI
import com.gallopdevs.athanhelper.utilities.MIDNIGHT
import com.gallopdevs.athanhelper.utilities.ONE_SEVENTH
import com.gallopdevs.athanhelper.utilities.SHAFII
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub

class SettingsRepositoryTest {

    private lateinit var testObject: SettingsRepository

    private val mockSettingsLocalDataSource: SettingsLocalDataSource = mock()

    @Before
    fun setup() {
        testObject = SettingsRepository(mockSettingsLocalDataSource)
    }

    @Test
    fun get_boolean_enable_notifications_true_successful() {
        val expectedBoolean = true
        mockSettingsLocalDataSource.stub {
            on {
                getBoolean(ENABLE_NOTIFICATIONS, false)
            } doReturn expectedBoolean
        }

        assertEquals(expectedBoolean, testObject.getBoolean(ENABLE_NOTIFICATIONS, false))
    }

    @Test
    fun get_int_calculation_method_isna_successful() {
        val expectedInt = ISNA
        mockSettingsLocalDataSource.stub {
            on {
                getInt(CALCULATION_METHOD, JAFARI)
            } doReturn expectedInt
        }

        assertEquals(expectedInt, testObject.getInt(CALCULATION_METHOD, JAFARI))
    }

    @Test
    fun get_int_asr_method_hanafi_successful() {
        val expectedInt = HANAFI
        mockSettingsLocalDataSource.stub {
            on {
                getInt(ASR_METHOD, SHAFII)
            } doReturn expectedInt
        }

        assertEquals(expectedInt, testObject.getInt(ASR_METHOD, SHAFII))
    }

    @Test
    fun get_int_latitudes_method_one_seventh_successful() {
        val expectedInt = ONE_SEVENTH
        mockSettingsLocalDataSource.stub {
            on {
                getInt(LATITUDES_METHOD, MIDNIGHT)
            } doReturn expectedInt
        }

        assertEquals(expectedInt, testObject.getInt(LATITUDES_METHOD, MIDNIGHT))
    }
}
