package com.example.socialnetwork.data.repository

import androidx.core.net.toUri
import com.example.socialnetwork.common.exception.NetworkException
import com.example.socialnetwork.common.wrapper.DataResult
import com.example.socialnetwork.common.wrapper.getResult
import com.example.socialnetwork.data.connectivity.ConnectivityChecker
import com.example.socialnetwork.domain.User
import com.example.socialnetwork.domain.repository.AccountRepository
import com.example.socialnetwork.domain.repository.PostRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val store: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val connectivityChecker: ConnectivityChecker,
    private val postRepository: PostRepository
) : AccountRepository {

    private val users = store.collection(USERS)

    override val currentUserId: String = auth.currentUser?.uid.orEmpty()
    override val hasUser: Boolean
        get() = auth.currentUser != null

    private val image = storage.reference.child("${PostRepositoryImpl.IMAGES}/")
    private val user = store.collection(USERS)


    override suspend fun register(
        username: String,
        email: String,
        password: String
    ): DataResult<Unit> {
        if (!connectivityChecker.hasInternetAccess()) {
            return DataResult.Error(NetworkException.NetworkUnavailable)
        }
        return getResult {
            auth.createUserWithEmailAndPassword(email, password).await()
            auth.currentUser?.uid?.let { uid ->
                users.document(uid).set(User(username, email)).await()
            }
        }

    }

    override suspend fun login(email: String, password: String): DataResult<Unit> {
        if (!connectivityChecker.hasInternetAccess()) {
            return DataResult.Error(NetworkException.NetworkUnavailable)
        }

        return getResult {
            auth.signInWithEmailAndPassword(email, password).await()
        }
    }

    override suspend fun logout(): DataResult<Unit> {
        if (!connectivityChecker.hasInternetAccess()) {
            return DataResult.Error(NetworkException.NetworkUnavailable)
        }

        return getResult {
            auth.signOut()
        }
    }

    override suspend fun fetchCurrentUser(): DataResult<User> {
        if (!connectivityChecker.hasInternetAccess()) {
            return DataResult.Error(NetworkException.NetworkUnavailable)
        }

        return getResult {
            users.document(currentUserId)
                .get().await().toObject<User>()
                ?: return DataResult.Error(NetworkException.NotAuthorized)
        }
    }

    override suspend fun updateCurrentUser(profilePictureUrl: String): DataResult<User> {
        if (!connectivityChecker.hasInternetAccess()) {
            return DataResult.Error(NetworkException.NetworkUnavailable)
        }
        val userData = user.document(currentUserId)
            .get().await().toObject<User>() ?: return DataResult.Error(NetworkException.NoData)

        val downloadUrl = if (profilePictureUrl.isNotBlank()) {
            val uri = profilePictureUrl.toUri().pathSegments.last() //To cut out file name
            val imageRef = image.child(uri)// To get Reference
            imageRef.putFile(profilePictureUrl.toUri()).await()// To upload file
            imageRef.downloadUrl.await().toString()// To get url for downloading in the future
        } else return DataResult.Error(NetworkException.BadRequest)

        // Update profile url in post.
        if (updatePostToProfile(downloadUrl)) return DataResult.Error(NetworkException.BadRequest)

        return getResult {
            val updatedUser = userData.copy(profilePictureUrl = downloadUrl)
            users.document(currentUserId).set(
                updatedUser
            ).await()
            updatedUser
        }
    }

    private suspend fun updatePostToProfile(downloadUrl: String): Boolean {
        val pastPost = (postRepository.fetchCurrentUserPost() as? DataResult.Success)
            ?: return true
        pastPost.data.forEach {
            postRepository.post.document(it.id)
                .update(PostRepositoryImpl.PROFILE_PICTURE_URL_FIELD, downloadUrl)
        }
        return false
    }

    companion object {
        const val USERS = "users"
    }
}