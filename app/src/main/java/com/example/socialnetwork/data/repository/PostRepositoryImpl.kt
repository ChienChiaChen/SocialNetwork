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
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
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

        val downloadUrl = if (imageUri.isNotBlank()) {
            val uri = imageUri.toUri().pathSegments.last()
            val imageRef = image.child(uri)
            imageRef.putFile(imageUri.toUri()).await()
            imageRef.downloadUrl.await().toString()
        } else ""

        return getResult {
            post.add(
                Post(
                    currentUser.uid,
                    userData.username,
                    downloadUrl,
                    userData.profilePictureUrl,
                    description
                )
            )
        }
    }

    override suspend fun fetchCurrentUserPost(): DataResult<List<Post>> {
        val uid = auth.currentUser?.uid ?: return DataResult.Error(NetworkException.NotAuthorized)
        return getResult {
            post.whereEqualTo(UID_FIELD, uid)
                .orderBy(CREATED_AT_FIELD, Query.Direction.DESCENDING)
                .get().await().toObjects<Post>()
        }
    }

    override suspend fun fetchAllUserPost(): DataResult<List<Post>> {
        return getResult {
            post.orderBy(CREATED_AT_FIELD, Query.Direction.DESCENDING)
                .get().await().toObjects<Post>()
        }
    }

    companion object {
        public const val IMAGES = "images"
        private const val POST = "Post"
        private const val CREATED_AT_FIELD = "createdAt"
        private const val UID_FIELD = "uid"

    }
}