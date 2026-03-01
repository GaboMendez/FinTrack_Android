package com.usj.fintrack

import android.app.Application
import com.usj.fintrack.domain.usecase.seed.SeedDatabaseUseCase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class FinTrackApplication : Application() {

    @Inject
    lateinit var seedDatabaseUseCase: SeedDatabaseUseCase

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        applicationScope.launch {
            seedDatabaseUseCase()
        }
    }
}
