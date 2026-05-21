package com.inspi.app.ui.gallery

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.inspi.app.domain.models.Submission
import com.inspi.app.ui.navigation.InspiBottomBar
import com.inspi.app.ui.theme.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import com.inspi.app.ui.common.MascotImage
import com.inspi.app.ui.common.MascotMood

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    navController: NavController,
    viewModel: GalleryViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = InspyBackground,
        bottomBar = { InspiBottomBar(navController) },
        topBar = {
            TopAppBar(
                title = { Text("Gallery", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = InspyBackground),
            )
        }
    ) { padding ->

        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = InspyPrimary)
            }
            return@Scaffold
        }

        if (state.submissions.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                    MascotImage(
                        mood = MascotMood.SAD,
                        modifier = Modifier.size(120.dp),
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "No submissions yet\nComplete today's quest to start your gallery!",
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        color = InspyOnBackground.copy(alpha = 0.55f),
                    )
                }
            }
            return@Scaffold
        }

        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            state.flashback?.let { (past, latest) ->
                FlashbackCard(past = past, latest = latest)
            }

            // ── Счётчик над гридом ─────────────────────────────────────────
            Text(
                "${state.submissions.size} ${if (state.submissions.size == 1) "work" else "works"}",
                fontSize = 12.sp,
                color = InspyOnBackground.copy(alpha = 0.45f),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(state.submissions, key = { it.id }) { submission ->
                    SubmissionThumbnail(submission)
                }
            }
        }
    }
}

@Composable
private fun SubmissionThumbnail(submission: Submission) {
    val dateStr = remember(submission.createdAt) {
        SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(submission.createdAt))
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = InspySurface),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(File(submission.thumbnailPath).toUri())
                    .crossfade(true)
                    .size(300)
                    .build(),
                contentDescription = submission.taskTitle,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)),
            )
            Column(modifier = Modifier.padding(10.dp, 8.dp, 10.dp, 10.dp)) {
                Text(
                    submission.taskTitle,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = InspyOnBackground,
                    maxLines = 1,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    dateStr,
                    fontSize = 11.sp,
                    color = InspyOnBackground.copy(alpha = 0.45f),
                )
            }
        }
    }
}

@Composable
private fun FlashbackCard(past: Submission, latest: Submission) {
    val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())

    Card(
        modifier = Modifier.fillMaxWidth().padding(12.dp, 8.dp, 12.dp, 0.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = InspyHighlight),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("✨ Flashback", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = InspyPrimary)
            Spacer(Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImage(
                        model = File(past.thumbnailPath).toUri(),
                        contentDescription = "Past",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(14.dp)),
                    )
                    Spacer(Modifier.height(5.dp))
                    Text(dateFormat.format(Date(past.createdAt)), fontSize = 11.sp, color = InspyOnBackground.copy(alpha = 0.6f))
                }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImage(
                        model = File(latest.thumbnailPath).toUri(),
                        contentDescription = "Latest",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(14.dp)),
                    )
                    Spacer(Modifier.height(5.dp))
                    Text("Latest", fontSize = 11.sp, color = InspyPrimary, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}