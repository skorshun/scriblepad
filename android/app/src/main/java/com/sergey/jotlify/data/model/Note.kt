package com.sergey.jotlify.data.model

import java.time.Instant

data class Note(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: Instant,
    val updatedAt: Instant,
)
