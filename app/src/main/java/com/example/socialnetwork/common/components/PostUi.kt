package com.example.socialnetwork.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.socialnetwork.R
import com.example.socialnetwork.common.DateUtils
import com.example.socialnetwork.domain.Post
import com.example.socialnetwork.presentation.create_post.CreatePostContract
import com.example.socialnetwork.ui.theme.MediumGray
import com.example.socialnetwork.ui.theme.ProfilePictureSizeMedium
import com.example.socialnetwork.ui.theme.SpaceMedium
import com.example.socialnetwork.ui.theme.SpaceSmall

@Composable
fun PostUi(
    post: Post,
    modifier: Modifier = Modifier,
    showProfileImage: Boolean = true,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(SpaceMedium)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(
                    y = if (showProfileImage) {
                        ProfilePictureSizeMedium / 2f
                    } else 0.dp
                )
                .clip(MaterialTheme.shapes.medium)
                .shadow(5.dp)
                .background(MediumGray)
        ) {
            if (post.imageUrl.isNotBlank()) {
                AsyncImage(
                    model = post.imageUrl,
                    contentDescription = "Post image",
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SpaceMedium)
            ) {
                ActionRow(
                    username = post.username,
                    modifier = Modifier.fillMaxWidth(),
                    onUsernameClick = { username ->

                    }
                )
                Spacer(modifier = Modifier.height(SpaceMedium))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = post.description,
                        style = MaterialTheme.typography.body1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Text(
                        text = DateUtils.format(post.createdAt),
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier
                            .align(Alignment.CenterEnd),
                    )
                }


            }
        }
        if (showProfileImage) {
            Image(
                painterResource(id = R.drawable.eva_avatar),
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(ProfilePictureSizeMedium)
                    .clip(CircleShape)
                    .align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
fun ActionRow(
    modifier: Modifier = Modifier,
    username: String,
    onUsernameClick: (String) -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Text(
            text = username,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary
            ),
            modifier = Modifier
                .clickable {
                    onUsernameClick(username)
                }
        )
    }
}