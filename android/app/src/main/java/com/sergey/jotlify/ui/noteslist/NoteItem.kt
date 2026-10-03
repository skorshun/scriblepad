package com.sergey.jotlify.ui.noteslist

import android.content.res.Configuration
import android.text.format.DateUtils
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sergey.jotlify.R
import com.sergey.jotlify.data.model.Note
import com.sergey.jotlify.ui.theme.JotlifyTheme
import java.time.Instant
import java.time.temporal.ChronoUnit

@Composable
fun NoteItem(
    note: Note,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            NoteTitle(title = note.title)

            if (note.content.isEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = note.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = formatRelativeTime(note.updatedAt),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun NoteTitle(title: String) {
    val isUntitled = title.isBlank()

    Text(
        text = if (isUntitled) stringResource(R.string.note_untitled) else title,
        style = MaterialTheme.typography.titleMedium,
        color = if (isUntitled) MaterialTheme.colorScheme.onSurfaceVariant else Color.Unspecified,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

private fun formatRelativeTime(instant: Instant): String =
    DateUtils.getRelativeTimeSpanString(
        instant.toEpochMilli(),
        System.currentTimeMillis(),
        DateUtils.MINUTE_IN_MILLIS
    ).toString()

private val previewNote = Note(
    id = "1",
    title = "Shopping List",
    content = "Milk, eggs, bread, coffee",
    createdAt = Instant.now().minus(1, ChronoUnit.DAYS),
    updatedAt = Instant.now().minus(10, ChronoUnit.MINUTES)
)

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NoteItemPreview() {
    JotlifyTheme {
        NoteItem(
            note = previewNote,
            onClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(name = "Untitled + long text", showBackground = true)
@Composable
private fun NoteItemEdgeCasePreview() {
    JotlifyTheme {
        NoteItem(
            note = previewNote.copy(
                title = "",
                content = "The kitchen was too quiet, save for the rhythmic, metallic ticking of the old Sears clock on the wall. Martha sat at the Formica table, a fresh sheet of lined paper smoothing out under her palm. Her ballpoint pen hovered. Writing the weekly shopping list was usually a chore of utility, a mechanical exercise in inventory. But today, with her daughter Sarah returning from her first semester at college tomorrow evening, the blank page felt like a script for a reunion.",
            ),
            onClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}