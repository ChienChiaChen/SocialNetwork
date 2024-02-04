package com.example.socialnetwork.presentation.create_post

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.socialnetwork.R
import com.example.socialnetwork.common.components.StandardTextField
import com.example.socialnetwork.common.components.StandardToolbar
import com.example.socialnetwork.ui.theme.SpaceLarge
import com.example.socialnetwork.ui.theme.SpaceMedium
import com.example.socialnetwork.ui.theme.SpaceSmall
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CreatePostScreen(
    navController: NavController,
    viewModel: CreatePostViewModel = hiltViewModel()
) {
    val postState by viewModel.descriptionState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            when (it) {
                is CreatePostContract.CreatePostEffect.NavigateTo -> {
                    navController.popBackStack()
                }
            }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                viewModel.onEvent(CreatePostContract.CreatePostEvent.EnteredImageUri(it.toString()))
            }
        }
    )
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        StandardToolbar(
            navController = navController,
            showBackArrow = true,
            title = {
                Text(
                    text = stringResource(id = R.string.create_post_create_a_new_post),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onBackground
                )
            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(SpaceLarge)
        ) {
            if (postState.isLoading) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colors.onBackground
                    )
                }
            } else {
                if (postState.imageUri.isNotBlank()) {
                    Image(
                        painter = rememberAsyncImagePainter(model = postState.imageUri),
                        contentDescription = null,
                        modifier = Modifier
                            .aspectRatio(16f / 9f)
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .clickable { galleryLauncher.launch("image/*") },
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .aspectRatio(16f / 9f)
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colors.onBackground,
                                shape = MaterialTheme.shapes.medium
                            )
                            .clickable {
                                galleryLauncher.launch("image/*")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(id = R.string.create_post_choose_image),
                            tint = MaterialTheme.colors.onBackground
                        )
                    }
                }
                Spacer(modifier = Modifier.height(SpaceMedium))
                StandardTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = postState.text,
                    hint = stringResource(id = R.string.create_post_description),
                    error = stringResource(postState.error),
                    singleLine = false,
                    maxLines = 5,
                    onValueChange = {
                        viewModel.onEvent(CreatePostContract.CreatePostEvent.EnteredDescription(it))
                    }
                )
                Spacer(modifier = Modifier.height(SpaceLarge))
                Button(
                    onClick = {
                        viewModel.onEvent(CreatePostContract.CreatePostEvent.Post)
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = stringResource(id = R.string.create_post),
                        color = MaterialTheme.colors.onPrimary
                    )
                    Spacer(modifier = Modifier.width(SpaceSmall))
                    Icon(imageVector = Icons.Default.Send, contentDescription = null)
                }
            }
        }
    }
}