package com.namjackson.firebaseapp.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.namjackson.firebaseapp.R
import gun0912.tedimagepicker.builder.TedImagePicker
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val firebaseUser by lazy { FirebaseAuth.getInstance().currentUser ?: error("잘못 된 접근") }

    private val storageReference =
        FirebaseStorage.getInstance().reference.child("images/profile/${firebaseUser.email}.jpg")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initProfile()
        initListener()
    }

    private fun initProfile() {
        storageReference.downloadUrl
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result ?: return@addOnCompleteListener
                    showProfileImage(result)
                } else {
                    firebaseUser.photoUrl?.let(this::showProfileImage)
                }
            }
        firebaseUser.displayName?.let(this::showProfileName)
    }

    private fun initListener() {
        profile_img.setOnClickListener {
            TedImagePicker.with(context!!)
                .start { uri -> showProfileImage(uri) }
        }
    }

    private fun uploadProfileImg() {

    }

    private fun showProfileName(name: String) {
        profile_name.text = name
    }

    private fun showProfileImage(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .circleCrop()
            .into(profile_img)
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}
