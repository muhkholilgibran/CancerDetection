package com.dicoding.asclepius.view.save

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.local.entity.History
import com.dicoding.asclepius.data.repository.HistoryRepository

class SaveViewModel(application: Application):ViewModel() {

    private val historyRepo: HistoryRepository = HistoryRepository(application)

    fun getAllHistory() : LiveData<List<History>> = historyRepo.getAllHistory()

    fun delete(history: History) {
        historyRepo.delete(history)
    }
}