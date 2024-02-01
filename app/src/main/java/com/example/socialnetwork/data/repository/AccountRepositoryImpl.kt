package com.example.socialnetwork.data.repository

import com.example.socialnetwork.common.wrapper.DataResult
import com.example.socialnetwork.common.exception.NetworkException
import com.example.socialnetwork.data.connectivity.ConnectivityChecker
import com.example.socialnetwork.domain.repository.AccountRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val connectivityChecker: ConnectivityChecker,
) : AccountRepository {

    override val currentUserId: String = auth.currentUser?.uid.orEmpty()
    override val hasUser: Boolean
        get() = auth.currentUser != null

    override suspend fun register(email: String, password: String): DataResult<Unit> {
        if (!connectivityChecker.hasInternetAccess()) {
            return DataResult.Error(NetworkException.NetworkUnavailable)
        }
        auth.createUserWithEmailAndPassword(email, password).await()
        return DataResult.Success(Unit)
    }

    override suspend fun login(email: String, password: String): DataResult<Unit> {
        if (!connectivityChecker.hasInternetAccess()) {
            return DataResult.Error(NetworkException.NetworkUnavailable)
        }
        auth.signInWithEmailAndPassword(email, password).await()
        return DataResult.Success(Unit)
    }

    override suspend fun logout(): DataResult<Unit> {
        if (!connectivityChecker.hasInternetAccess()) {
            return DataResult.Error(NetworkException.NetworkUnavailable)
        }
        auth.signOut()
        return DataResult.Success(Unit)
    }
}