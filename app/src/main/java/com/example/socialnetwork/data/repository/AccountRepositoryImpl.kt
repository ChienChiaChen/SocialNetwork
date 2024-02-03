package com.example.socialnetwork.data.repository

import com.example.socialnetwork.common.exception.NetworkException
import com.example.socialnetwork.common.wrapper.DataResult
import com.example.socialnetwork.common.wrapper.getResult
import com.example.socialnetwork.data.connectivity.ConnectivityChecker
import com.example.socialnetwork.domain.User
import com.example.socialnetwork.domain.repository.AccountRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val store: FirebaseFirestore,
    private val connectivityChecker: ConnectivityChecker,
) : AccountRepository {

    private val users = store.collection(USERS)

    override val currentUserId: String = auth.currentUser?.uid.orEmpty()
    override val hasUser: Boolean
        get() = auth.currentUser != null

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

    companion object {
        const val USERS = "users"
    }
}