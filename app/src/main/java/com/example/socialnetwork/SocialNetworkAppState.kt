package com.example.socialnetwork

import android.content.res.Resources
import androidx.compose.material.ScaffoldState
import androidx.navigation.NavHostController
import com.example.socialnetwork.common.snackbar.SnackbarManager
import com.example.socialnetwork.common.snackbar.SnackbarMessage.Companion.toMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class SocialNetworkAppState(
    val scaffoldState: ScaffoldState,
    val navController: NavHostController,
    private val snackbarManager: SnackbarManager,
    private val resources: Resources,
    coroutineScope: CoroutineScope
) {
    init {
        coroutineScope.launch {
            snackbarManager.snackbarMessages.filterNotNull().collect { snackbarMessage ->
                val text = snackbarMessage.toMessage(resources)
                scaffoldState.snackbarHostState.showSnackbar(text)
                snackbarManager.clearSnackbarState()
            }
        }
    }
}
