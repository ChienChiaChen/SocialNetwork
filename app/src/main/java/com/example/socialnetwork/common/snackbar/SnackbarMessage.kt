/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.example.socialnetwork.common.snackbar

import android.content.res.Resources
import androidx.annotation.StringRes
import com.example.socialnetwork.R
import com.example.socialnetwork.common.exception.UNREAD_ERROR_CODE
import com.example.socialnetwork.common.exception.getStringResId

sealed class SnackbarMessage {
    class StringSnackbar(val message: String) : SnackbarMessage()
    class ResourceSnackbar(@StringRes val message: Int) : SnackbarMessage()

    companion object {
        fun SnackbarMessage.toMessage(resources: Resources): String {
            return when (this) {
                is StringSnackbar -> this.message
                is ResourceSnackbar -> resources.getString(this.message)
            }
        }

        fun Throwable.toSnackbarMessage(): SnackbarMessage {
            val message = this.message.orEmpty()

            return if (message.isNotBlank()) StringSnackbar(message)
            else if (getStringResId() != UNREAD_ERROR_CODE) ResourceSnackbar(getStringResId())
            else ResourceSnackbar(R.string.error_unknown_network_error_message)
        }
    }
}
