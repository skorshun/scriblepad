package com.sergey.jotlify.ui.noteslist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sergey.jotlify.R
import com.sergey.jotlify.data.model.Note
import com.sergey.jotlify.ui.theme.JotlifyTheme
import java.time.Instant
import java.time.temporal.ChronoUnit

@Composable
fun NotesListRoute(
    onNoteClick: (noteId: String) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotesListViewModel = viewModel(factory = NotesListViewModel.Factory),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NotesListScreen(
        uiState = uiState,
        onNoteClick = onNoteClick,
        onAddClick = onAddClick,
        onRetryClick = viewModel::onRetryClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(
    uiState: NotesListUiState,
    onNoteClick: (noteId: String) -> Unit,
    onAddClick: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text(text= stringResource(R.string.app_name)) })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.action_add_note)
                )
            }
        },
    ) {
        innerPadding -> val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        when (uiState) {
            NotesListUiState.Loading -> LoadingContent(modifier = contentModifier)
            NotesListUiState.Empty -> EmptyContent(modifier = contentModifier)
            NotesListUiState.Error -> ErrorContent(
                onRetryClick = onRetryClick,
                modifier = contentModifier,
            )
            is NotesListUiState.Content -> NotesContent(
                notes = uiState.notes,
                onNoteClick = onNoteClick,
                modifier = contentModifier
            )
        }
    }
}

@Composable
private fun NotesContent(
    notes: List<Note>,
    onNoteClick: (noteId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 8.dp,
            end = 16.dp,
            bottom = 88.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items = notes, key = { note -> note.id }) {
            note -> NoteItem(
                note = note,
                onClick = { onNoteClick(note.id) },
            )
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.notes_empty),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(32.dp)
        )
    }
}

@Composable
private fun ErrorContent(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.notes_error),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetryClick) {
            Text(text = stringResource(R.string.action_retry))
        }
    }
}

// region preview

private val previewNotes = listOf(
    Note(
        id = "1",
        title = "Shopping list",
        content = "Milk, eggs, coffee",
        createdAt = Instant.now().minus(1, ChronoUnit.DAYS),
        updatedAt = Instant.now().minus(10, ChronoUnit.MINUTES)
    ),
    Note(
        id = "2",
        title = "Ideas for Jotlify",
        content = "Search by notes, tags, dark theme, synchronization with the Go backend",
        createdAt = Instant.now().minus(2, ChronoUnit.DAYS),
        updatedAt = Instant.now().minus(2, ChronoUnit.HOURS)
    ),
    Note(
        id = "3",
        title = "",
        content = "Untitled Note",
        createdAt = Instant.now().minus(5, ChronoUnit.DAYS),
        updatedAt = Instant.now().minus(5, ChronoUnit.DAYS)
    ),
)

@Composable
private fun NotesListScreenPreview(uiState: NotesListUiState) {
    JotlifyTheme {
        NotesListScreen(
            uiState = uiState,
            onNoteClick = {},
            onAddClick = {},
            onRetryClick = {},
        )
    }
}

@Preview(name = "Content", showBackground = true)
@Composable
private fun ContentPreview() = NotesListScreenPreview(
    NotesListUiState.Content(previewNotes)
)

@Preview(name = "Loading", showBackground = true)
@Composable
private fun LoadingPreview() = NotesListScreenPreview(NotesListUiState.Loading)

@Preview(name = "Empty", showBackground = true)
@Composable
private fun EmptyPreview() = NotesListScreenPreview(NotesListUiState.Empty)

@Preview(name = "Error", showBackground = true)
@Composable
private fun ErrorPreview() = NotesListScreenPreview(NotesListUiState.Error)

// endregion