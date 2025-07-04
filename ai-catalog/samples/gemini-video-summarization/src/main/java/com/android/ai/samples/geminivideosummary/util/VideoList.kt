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
package com.android.ai.samples.geminivideosummary.util

import android.net.Uri
import androidx.core.net.toUri
import com.google.com.android.ai.samples.geminivideosummary.R

/**
 * Data class to represent a video item with a title and URI.
 */
data class VideoItem(
    val titleResId: Int,
    val uri: Uri,
)

val sampleVideoList = listOf(
    VideoItem(
        R.string.video_title_big_buck_bunny,
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4".toUri(),
    ),
    VideoItem(
        R.string.video_title_android_spotlight_shorts,
        "https://storage.googleapis.com/exoplayer-test-media-0/shorts_android_developers/shorts_10.mp4".toUri(),
    ),
    VideoItem(
        R.string.video_title_rio_de_janeiro,
        "gs://cloud-samples-data/generative-ai/video/rio_de_janeiro_beyond_the_map_rio.mp4".toUri(),
    ),
    VideoItem(
        R.string.video_title_youtube_google_tv,
        "https://www.youtube.com/watch?v=QFMIP5GOo70".toUri(),
    ),
    VideoItem(
        R.string.video_title_tears_of_steel,
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4".toUri(),
    ),
    VideoItem(
        R.string.video_title_for_bigger_blazes,
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4".toUri(),
    ),
    VideoItem(
        R.string.video_title_for_bigger_escape,
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4".toUri(),
    ),
)
