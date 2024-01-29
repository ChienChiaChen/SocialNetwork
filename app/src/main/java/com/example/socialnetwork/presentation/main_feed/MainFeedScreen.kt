package com.example.socialnetwork.presentation.main_feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.socialnetwork.R
import com.example.socialnetwork.common.components.PostUi
import com.example.socialnetwork.common.components.StandardToolbar
import com.example.socialnetwork.domain.Post

@Composable
fun MainFeedScreen(
    navController: NavController,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        StandardToolbar(
            navController = navController,
            title = {
                Text(
                    text = stringResource(id = R.string.main_feed_your_feed),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onBackground
                )
            },
            modifier = Modifier.fillMaxWidth(),
            showBackArrow = true,
        )
        PostUi(
            post = Post(
                username = "Jason Chien",
                imageUrl = "",
                profilePictureUrl = "",
                description = "ber...",
            )
        )
    }
}