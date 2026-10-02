package com.sergey.jotlify.ui.noteslist

import com.sergey.jotlify.data.model.Note

/**
 * Все возможные состояния экрана списка заметок.
 * В каждый момент времени экран находится в одном из них.
 */
sealed interface NotesListUiState {
    data object Loading: NotesListUiState
    data object Empty: NotesListUiState
    data class Content(val notes: List<Note>): NotesListUiState
    data object Error: NotesListUiState
}