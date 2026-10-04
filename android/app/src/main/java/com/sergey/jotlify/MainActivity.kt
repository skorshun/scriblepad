package com.sergey.jotlify

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sergey.jotlify.ui.noteslist.NotesListRoute
import com.sergey.jotlify.ui.theme.JotlifyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JotlifyTheme {
                NotesListRoute(
                    // TODO: navigate to the editing screen
                    onNoteClick = { nodeId ->
                        Log.d(TAG, "Note clicked: $nodeId")
                    },
                    // TODO: navigate to the creation screen
                    onAddClick = {
                        Log.d(TAG, "Add note clicked")
                    }
                )
            }
        }
    }

    private companion object {
        const val TAG = "MainActivity"
    }
}