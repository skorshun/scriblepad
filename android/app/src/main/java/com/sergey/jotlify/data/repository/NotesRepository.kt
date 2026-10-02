package com.sergey.jotlify.data.repository

import com.sergey.jotlify.data.model.Note

interface NotesRepository {
    /**
     * Возвращает все заметки, отсортированные по [Note.updatedAt], т.е. новые сверху.
     *
     * @throws java.io.IOException если данные не удалось получить
     */
    suspend fun getNotes(): List<Note>
}