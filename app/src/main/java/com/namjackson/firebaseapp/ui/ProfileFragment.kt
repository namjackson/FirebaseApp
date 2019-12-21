package com.namjackson.firebaseapp.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
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
                .start { uri -> uploadProfileImg(uri) }
        }
    }

    private fun uploadProfileImg(uri: Uri) {
        showLoading()
        storageReference.putFile(uri)
            .continueWithTask {
                if (!it.isSuccessful) {
                    it.exception?.let {
                        throw it
                    }
                }
                storageReference.downloadUrl
            }
            .addOnCompleteListener { it ->
                hideLoading()
                if (it.isSuccessful) {
                    val result = it.result ?: return@addOnCompleteListener
                    updateFirebaseImgUpdate(result)
                }
            }


    }

    private fun updateFirebaseImgUpdate(uri: Uri) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setPhotoUri(uri)
            .build()

        firebaseUser.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showProfileImage(uri)
                }
            }
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

    private fun hideLoading() {
        loading.isVisible = false
    }

    private fun showLoading() {
        loading.isVisible = true
    }


    companion object {
        fun newInstance() = ProfileFragment()
    }
}
