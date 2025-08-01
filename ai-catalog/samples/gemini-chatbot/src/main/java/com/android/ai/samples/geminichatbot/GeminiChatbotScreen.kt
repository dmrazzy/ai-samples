/*
 * Copyright 2025 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.ai.samples.geminichatbot

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeminiChatbotScreen(viewModel: GeminiChatbotViewModel = hiltViewModel()) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var message by rememberSaveable { mutableStateOf("") }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .imePadding(),
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                title = {
                    Text(text = stringResource(id = R.string.geminichatbot_title_bar))
                },
                actions = {
                    SeeCodeButton()
                },
            )
        },
    ) { innerPadding ->
        Column {
            MessageList(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .weight(1f),
                messages = uiState.messages,
                contentPadding = innerPadding,
            )

            when (val state = uiState.geminiMessageState) {
                is GeminiMessageState.Generating -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .align(Alignment.CenterHorizontally),
                    )
                }

                is GeminiMessageState.Error -> {
                    AlertDialog(
                        onDismissRequest = { viewModel.dismissError() },
                        title = { Text(text = stringResource(R.string.error)) },
                        text = { Text(text = state.errorMessage) },
                        confirmButton = {
                            Button(onClick = { viewModel.dismissError() }) {
                                Text(text = stringResource(R.string.dismiss_button))
                            }
                        },
                    )
                }
                else -> { /* No additional UI for waiting state */ }
            }

            InputBar(
                value = message,
                placeholder = stringResource(R.string.geminichatbot_input_placeholder),
                onInputChanged = {
                    message = it
                },
                onSendClick = {
                    viewModel.sendMessage(message)
                    message = ""
                },
                sendEnabled = uiState.geminiMessageState !is GeminiMessageState.Generating,
            )
        }
    }
}

@Composable
fun MessageList(messages: List<ChatMessage>, contentPadding: PaddingValues, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        reverseLayout = true,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom),
    ) {
        items(items = messages) { message ->
            MessageBubble(
                message = message,
            )
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = if (message.isIncoming) Alignment.CenterStart else Alignment.CenterEnd,
    ) {
        Surface(
            modifier = Modifier.widthIn(max = 300.dp),
            color = if (message.isIncoming) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.primary
            },
            shape = MaterialTheme.shapes.large,
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = message.text,
            )
        }
    }
}

@Composable
fun SeeCodeButton() {
    val context = LocalContext.current
    val githubLink = "https://github.com/android/ai-samples/tree/main/ai-catalog/samples/gemini-chatbot"

    Button(
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, githubLink.toUri())
            context.startActivity(intent)
        },
        modifier = Modifier.padding(end = 8.dp),
    ) {
        Icon(Icons.Filled.Code, contentDescription = "See code")
        Text(
            modifier = Modifier.padding(start = 8.dp),
            fontSize = 12.sp,
            text = stringResource(R.string.see_code),
        )
    }
}
