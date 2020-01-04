package com.namjackson.firebaseapp.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.namjackson.firebaseapp.R
import com.namjackson.firebaseapp.data.Document
import com.namjackson.firebaseapp.data.Post
import kotlinx.android.synthetic.main.activity_add_post.*
import java.util.*

class AddPostActivity : AppCompatActivity() {

    private val db by lazy { FirebaseFirestore.getInstance().collection("posts") }

    private val images by lazy {
        intent.getParcelableArrayListExtra<Uri>(EXTRA_IMAGES).let {
            it?.mapIndexed { index, uri ->
                Document.newDocument(uri, index == 0)
            }
        }
    }

    private val storageRef by lazy {
        FirebaseStorage.getInstance().reference.child("images/posts/")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            val post = Post(
                title = et_title.text.toString(),
                content = et_content.text.toString()
            )
            upload(post)
        }


    }

    private fun init() {

    }


    private fun upload(post: Post) {
        val postDate = Date().toString()
        imageUpload(post,postDate)
    }


    private fun imageUpload(post: Post,postDate: String) {

        val postRef = storageRef.child(postDate)
        val selectedImage = images?.filter { it.isSelected }?.first()

        images?.forEach {
            Log.d("TEST","image Upload : "+ it.toString())
            postRef.child(it.documentId)
                .putFile(it.uri)
                .addOnCompleteListener {
                    if (postRef.activeUploadTasks.isEmpty()) {
                        uploadPost(postDate,post,selectedImage)
                    }
                }
        }

    }

    private fun uploadPost(postDate:String, post: Post, selectedImage:Document?){
        selectedImage?.let {
            post.selectedImage = selectedImage.documentId
        }
        db.document(postDate)
            .set(post)
            .addOnSuccessListener { documentReference ->
                finish()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    companion object {
        private const val EXTRA_IMAGES = "EXTRA_IMAGES"

        fun startActivity(context: Context?, images: List<Uri>) {
            context?.startActivity(Intent(context, AddPostActivity::class.java).apply {
                putExtras(bundleOf(EXTRA_IMAGES to images))
            })
        }
    }
}
