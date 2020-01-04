package com.namjackson.firebaseapp.data

import android.net.Uri

data class Document(
    val uri: Uri,
    val documentId: String,
    val isSelected: Boolean
) {
    companion object {
        fun newDocument(uri: Uri, isSelected: Boolean): Document {
            val documentId = System.currentTimeMillis()
            return Document(uri, uri.lastPathSegment?:documentId.toString(), isSelected)
        }
    }
}