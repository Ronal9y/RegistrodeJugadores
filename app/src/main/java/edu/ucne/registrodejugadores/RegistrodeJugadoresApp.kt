package edu.ucne.registrodejugadores

import android.app.Application
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import edu.ucne.registrodejugadores.worker.AppWorkerFactory
import javax.inject.Inject

@HiltAndroidApp
class RegistrodeJugadoresApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: AppWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}