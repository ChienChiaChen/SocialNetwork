package com.example.socialnetwork.data.repository

import androidx.core.net.toUri
import com.example.socialnetwork.common.exception.NetworkException
import com.example.socialnetwork.common.wrapper.DataResult
import com.example.socialnetwork.common.wrapper.getResult
import com.example.socialnetwork.data.connectivity.ConnectivityChecker
import com.example.socialnetwork.domain.Post
import com.example.socialnetwork.domain.User
import com.example.socialnetwork.domain.repository.PostRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val store: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val connectivityChecker: ConnectivityChecker
) : PostRepository {


    private val post = store.collection(POST)
    private val user = store.collection(AccountRepositoryImpl.USERS)
    private val image = storage.reference.child("$IMAGES/")

    override suspend fun createPost(description: String, imageUri: String): DataResult<Unit> {
        if (!connectivityChecker.hasInternetAccess()) {
            return DataResult.Error(NetworkException.NetworkUnavailable)
        }
        val currentUser =
            auth.currentUser ?: return DataResult.Error(NetworkException.NotAuthorized)

        val userData = user.document(currentUser.uid)
            .get().await().toObject<User>() ?: return DataResult.Error(NetworkException.NoData)

        if (imageUri.isNotBlank()) {
            val uri = imageUri.toUri().pathSegments.last()
            image.child(uri).putFile(imageUri.toUri()).await()
        }

        return getResult {
            post.add(
                Post(
                    currentUser.uid,
                    userData.username,
                    imageUri,
                    userData.profilePictureUrl,
                    description
                )
            )
        }
    }

    companion object {
        private const val POST = "Post"
        private const val IMAGES = "images"
    }
}