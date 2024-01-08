package com.gallopdevs.athanhelper.viewmodel

import com.gallopdevs.athanhelper.data.PreferencesManagerImpl.Companion.ENABLE_NOTIFICATIONS
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
}
