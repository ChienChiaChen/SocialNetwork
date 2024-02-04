package com.example.socialnetwork.domain.usecase.post

import com.example.socialnetwork.common.exception.NetworkException
import com.example.socialnetwork.common.result.CreatePostResult
import com.example.socialnetwork.common.wrapper.DataResult
import com.example.socialnetwork.domain.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreatePostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(description: String, imageUri: String): CreatePostResult {
        if (description.isBlank() && imageUri.isBlank()) {
            return CreatePostResult(result = DataResult.Error(NetworkException.BadRequest))
        }

        return when (val result =
            withContext(Dispatchers.IO) {
                postRepository.createPost(description, imageUri)
            }) {
            is DataResult.Success -> CreatePostResult(result = DataResult.Success(Unit))
            is DataResult.Error -> CreatePostResult(result = result)
        }
    }

}
