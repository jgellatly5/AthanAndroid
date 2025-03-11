package com.gallopdevs.athanhelper.domain

import com.gallopdevs.athanhelper.repository.LocationRepository
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocationUpdatesUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    operator fun invoke(): Flow<LocationResult> {
        return locationRepository.getLocationUpdates()
    }
}
