package edu.ucne.registrodejugadores.worker

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import edu.ucne.registrodejugadores.worker.SyncWorker

fun triggerSyncWorker(context: Context) {
    val request = OneTimeWorkRequestBuilder<SyncWorker>().build()
    WorkManager.getInstance(context).enqueue(request)
}