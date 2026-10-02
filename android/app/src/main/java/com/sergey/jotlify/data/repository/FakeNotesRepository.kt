package com.sergey.jotlify.data.repository

import com.sergey.jotlify.data.model.Note
import kotlinx.coroutines.delay
import java.io.IOException
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds

/**
 * Фейковый репозиторий с данными в памяти.
 * Нужен, пока нет бекенда, а так же для превью и тестов.
 *
 * @param networkDelayMs имитация задержки сети, чтобы увидеть состояние загрузки.
 * @param shouldFail если true, getNotes() бросает ошибку, чтобы проверить состояние ошибки.
 */
class FakeNotesRepository(
    private val networkDelayMs: Long = 1_000L,
    private val shouldFail: Boolean = false
): NotesRepository {

    private val notes: List<Note> = createSampleNotes()

    override suspend fun getNotes(): List<Note> {
        delay(networkDelayMs.milliseconds)

        if (shouldFail) {
            throw IOException("Не удалось загрузить заметки")
        }

        return notes.sortedByDescending { it.updatedAt }
    }

    private fun createSampleNotes(): List<Note> {
        val now = Instant.now()

        return listOf(
            sampleNote(
                title = "Список покупок",
                content = "Молоко, хлеб, яйца, кофе, сыр",
                updatedAt = now.minus(10, ChronoUnit.MINUTES),
            ),
            sampleNote(
                title = "Идеи для Jotlify",
                content = "Поиск по заметкам, теги, тёмная тема, синхронизация с бэкендом на Go",
                updatedAt = now.minus(2, ChronoUnit.HOURS),
            ),
            sampleNote(
                title = "Очень длинная заметка",
                content = "Этот текст специально длинный, чтобы проверить, как карточка обрезает " +
                        "превью: должно показываться не больше двух строк, а дальше многоточие. " +
                        "Если видишь весь этот текст целиком, значит maxLines не сработал.",
                updatedAt = now.minus(1, ChronoUnit.DAYS),
            ),
            sampleNote(
                title = "",
                content = "Заметка без заголовка: проверяем пограничный случай",
                updatedAt = now.minus(3, ChronoUnit.DAYS),
            ),
            sampleNote(
                title = "Kotlin: что почитать",
                content = "Kotlin in Action, документация по корутинам, гайды по Compose",
                updatedAt = now.minus(7, ChronoUnit.DAYS),
            ),
        )
    }

    private fun sampleNote(
        title: String,
        content: String,
        updatedAt: Instant
    ): Note = Note(
        id = UUID.randomUUID().toString(),
        title = title,
        content = content,
        createdAt = updatedAt.minus(1, ChronoUnit.DAYS),
        updatedAt = updatedAt
    )
}