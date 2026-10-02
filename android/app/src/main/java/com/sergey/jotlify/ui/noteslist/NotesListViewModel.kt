package com.sergey.jotlify.ui.noteslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sergey.jotlify.data.repository.FakeNotesRepository
import com.sergey.jotlify.data.repository.NotesRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class NotesListViewModel(
    private val repository: NotesRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<NotesListUiState>(NotesListUiState.Loading)
    val uiState: StateFlow<NotesListUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadNotes()
    }

    fun onRetryClick() {
        loadNotes()
    }

    private fun loadNotes() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = NotesListUiState.Loading

            _uiState.value = try {
                val notes = repository.getNotes()
                if (notes.isEmpty()) {
                    NotesListUiState.Empty
                } else {
                    NotesListUiState.Content(notes)
                }
            } catch (e: IOException) {
                NotesListUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                NotesListViewModel(repository = FakeNotesRepository())
            }
        }
    }
}