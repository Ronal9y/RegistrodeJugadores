package edu.ucne.registrodejugadores.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import edu.ucne.registrodejugadores.data.remote.Resource
import edu.ucne.registrodejugadores.domain.usecases.TriggerSyncUseCase

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val triggerSyncUseCase: TriggerSyncUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return when (val result = triggerSyncUseCase()) {
            is Resource.Success -> Result.success()
            is Resource.Error -> Result.retry()
            is Resource.Loading -> Result.retry()
        }
    }
}