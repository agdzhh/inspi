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
            // Empty state
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                    Text("👾", fontSize = 72.sp)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "No submissions yet — complete today's task!",
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        color = InspyOnBackground.copy(alpha = 0.65f),
                    )
                }
            }
            return@Scaffold
        }

        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            // Flashback section
            state.flashback?.let { (past, latest) ->
                FlashbackCard(past = past, latest = latest)
                Spacer(Modifier.height(8.dp))
            }

            // Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = InspySurface),
        elevation = CardDefaults.cardElevation(1.dp),
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
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(submission.taskTitle, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = InspyOnBackground, maxLines = 1)
                Text(dateStr, fontSize = 11.sp, color = InspyOnBackground.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
private fun FlashbackCard(past: Submission, latest: Submission) {
    val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = InspyHighlight),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("✨ Flashback", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = InspyPrimary)
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImage(
                        model = File(past.thumbnailPath).toUri(),
                        contentDescription = "Past submission",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(12.dp)),
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(dateFormat.format(Date(past.createdAt)), fontSize = 11.sp, color = InspyOnBackground.copy(alpha = 0.6f))
                }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImage(
                        model = File(latest.thumbnailPath).toUri(),
                        contentDescription = "Latest submission",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(12.dp)),
                    )
                    Spacer(Modifier.height(4.dp))
                    Text("Latest", fontSize = 11.sp, color = InspyPrimary, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}
