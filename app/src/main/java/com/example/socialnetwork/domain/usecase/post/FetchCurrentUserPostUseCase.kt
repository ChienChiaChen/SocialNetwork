package com.example.socialnetwork.domain.usecase.post

import com.example.socialnetwork.common.wrapper.DataResult
import com.example.socialnetwork.domain.Post
import com.example.socialnetwork.domain.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchCurrentUserPostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(): DataResult<List<Post>> {
        return when (val result =
            withContext(Dispatchers.IO) {
                postRepository.fetchCurrentUserPost()
            }) {
            is DataResult.Success -> DataResult.Success(result.data)
            is DataResult.Error -> DataResult.Error(result.exception)
        }
    }

}
