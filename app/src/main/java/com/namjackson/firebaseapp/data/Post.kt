package com.namjackson.firebaseapp.data

data class Post(
    val title: String,
    val content: String,
    var selectedImage: String = ""
)