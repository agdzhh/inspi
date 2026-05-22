package com.inspi.app.ui.gallery

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.BrokenImage
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Size
import com.inspi.app.domain.models.Submission
import com.inspi.app.ui.common.MascotImage
import com.inspi.app.ui.common.MascotMood
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

    // Back handler: close viewer first, then navigate back
    val selectedIndex = state.selectedIndex
    BackHandler(enabled = selectedIndex != null) {
        viewModel.closeViewer()
    }

    Scaffold(
        containerColor = InspyBackground,
        bottomBar = {
            if (selectedIndex == null) InspiBottomBar(navController)
        },
        topBar = {
            if (selectedIndex == null) {
                TopAppBar(
                    title = { Text("Gallery", fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = InspyBackground),
                )
            }
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

        // ── Grid view ──────────────────────────────────────────────────────
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            state.flashback?.let { (past, latest) ->
                FlashbackCard(past = past, latest = latest)
            }

            Text(
                "${state.submissions.size} ${if (state.submissions.size == 1) "work" else "works"}",
                fontSize = 12.sp,
                color = InspyOnBackground.copy(alpha = 0.45f),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 2.dp, end = 2.dp, bottom = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                itemsIndexed(state.submissions, key = { _, it -> it.id }) { index, submission ->
                    GalleryThumbnail(
                        submission = submission,
                        onClick = { viewModel.openViewer(index) },
                    )
                }
            }
        }

        // ── Fullscreen photo viewer overlay ───────────────────────────────
        AnimatedVisibility(
            visible = selectedIndex != null,
            enter = fadeIn(tween(200)),
            exit = fadeOut(tween(200)),
        ) {
            if (selectedIndex != null) {
                FullscreenPhotoViewer(
                    submissions = state.submissions,
                    initialIndex = selectedIndex,
                    onClose = { viewModel.closeViewer() },
                )
            }
        }
    }
}

// ── High-quality thumbnail ────────────────────────────────────────────────────
@Composable
private fun GalleryThumbnail(submission: Submission, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick),
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(File(submission.imagePath).toUri())
                .memoryCacheKey(submission.imagePath)
                .diskCacheKey(submission.imagePath)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .crossfade(true)
                .build(),
            contentDescription = submission.taskTitle,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        ) {
            when (painter.state) {
                is AsyncImagePainter.State.Loading -> {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(InspySurface),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = InspyPrimary.copy(alpha = 0.5f),
                        )
                    }
                }
                is AsyncImagePainter.State.Error -> {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(InspySurface),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Outlined.BrokenImage,
                            contentDescription = null,
                            tint = InspyOnBackground.copy(alpha = 0.3f),
                            modifier = Modifier.size(28.dp),
                        )
                    }
                }
                else -> SubcomposeAsyncImageContent()
            }
        }
    }
}

// ── Fullscreen swipeable viewer ───────────────────────────────────────────────
@Composable
private fun FullscreenPhotoViewer(
    submissions: List<Submission>,
    initialIndex: Int,
    onClose: () -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = initialIndex,
        pageCount = { submissions.size },
    )
    val currentSubmission = submissions.getOrNull(pagerState.currentPage)
    val dateStr = remember(currentSubmission?.createdAt) {
        currentSubmission?.let {
            SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(Date(it.createdAt))
        } ?: ""
    }

    // Зум-стейт: сбрасывается при смене страницы
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    LaunchedEffect(pagerState.currentPage) {
        scale = 1f
        offset = Offset.Zero
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        // ── Pager: листание отключается при зуме ──────────────────────────
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            beyondViewportPageCount = 1,
            userScrollEnabled = scale == 1f,
        ) { page ->
            val submission = submissions[page]

            val transformState = rememberTransformableState { zoomChange, panChange, _ ->
                scale = (scale * zoomChange).coerceIn(1f, 5f)
                if (scale > 1f) {
                    val maxOffset = 2000f * (scale - 1f) / scale
                    offset = Offset(
                        x = (offset.x + panChange.x).coerceIn(-maxOffset, maxOffset),
                        y = (offset.y + panChange.y).coerceIn(-maxOffset, maxOffset),
                    )
                } else {
                    offset = Offset.Zero
                }
            }

            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(File(submission.imagePath).toUri())
                    .memoryCacheKey(submission.imagePath)
                    .diskCacheKey(submission.imagePath)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .size(Size.ORIGINAL)
                    .crossfade(true)
                    .build(),
                contentDescription = submission.taskTitle,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .transformable(state = transformState)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y,
                    )
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                // двойной тап: зум 2× или сброс
                                if (scale > 1f) {
                                    scale = 1f
                                    offset = Offset.Zero
                                } else {
                                    scale = 2.5f
                                }
                            }
                        )
                    },
            ) {
                when (painter.state) {
                    is AsyncImagePainter.State.Loading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color.White.copy(alpha = 0.7f))
                        }
                    }
                    is AsyncImagePainter.State.Error -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Outlined.BrokenImage,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.4f),
                                modifier = Modifier.size(48.dp),
                            )
                        }
                    }
                    else -> SubcomposeAsyncImageContent()
                }
            }
        }

        // ── Top chrome: close button + counter ────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Black.copy(alpha = 0.55f), Color.Transparent)
                    )
                )
                .padding(WindowInsets.statusBars.asPaddingValues())
                .padding(horizontal = 8.dp, vertical = 8.dp),
        ) {
            // Close
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(40.dp)
                    .background(Color.Black.copy(alpha = 0.35f), CircleShape),
            ) {
                Icon(Icons.Filled.Close, contentDescription = "Close", tint = Color.White)
            }

            // Counter
            Text(
                "${pagerState.currentPage + 1} / ${submissions.size}",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.Center),
            )
        }

        // ── Bottom chrome: title + date ───────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                    )
                )
                .padding(WindowInsets.navigationBars.asPaddingValues())
                .padding(horizontal = 20.dp, vertical = 16.dp),
        ) {
            Column {
                currentSubmission?.let {
                    Text(
                        it.taskTitle,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        dateStr,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 13.sp,
                    )
                }

                // Page dots
                if (submissions.size > 1) {
                    Spacer(Modifier.height(12.dp))
                    PageDots(
                        count = submissions.size,
                        current = pagerState.currentPage,
                    )
                }
            }
        }
    }
}

@Composable
private fun PageDots(count: Int, current: Int) {
    // Show at most 7 dots, compress if more
    val maxDots = 7
    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        if (count <= maxDots) {
            repeat(count) { index ->
                Box(
                    modifier = Modifier
                        .size(if (index == current) 8.dp else 5.dp)
                        .clip(CircleShape)
                        .background(
                            if (index == current) Color.White
                            else Color.White.copy(alpha = 0.45f)
                        ),
                )
            }
        } else {
            // Compressed indicator: show "● ● ● … ●" style
            Text(
                "${current + 1} of $count",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 11.sp,
            )
        }
    }
}

// ── Flashback card ────────────────────────────────────────────────────────────
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
            Text("Flashback", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = InspyPrimary)
            Spacer(Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(File(past.imagePath).toUri())
                            .memoryCacheKey(past.imagePath)
                            .diskCacheKey(past.imagePath)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Past",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(14.dp)),
                    ) {
                        when (painter.state) {
                            is AsyncImagePainter.State.Loading -> {
                                Box(Modifier.fillMaxSize().background(InspySurface).clip(RoundedCornerShape(14.dp)))
                            }
                            else -> SubcomposeAsyncImageContent()
                        }
                    }
                    Spacer(Modifier.height(5.dp))
                    Text(dateFormat.format(Date(past.createdAt)), fontSize = 11.sp, color = InspyOnBackground.copy(alpha = 0.6f))
                }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(File(latest.imagePath).toUri())
                            .memoryCacheKey(latest.imagePath)
                            .diskCacheKey(latest.imagePath)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Latest",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(14.dp)),
                    ) {
                        when (painter.state) {
                            is AsyncImagePainter.State.Loading -> {
                                Box(Modifier.fillMaxSize().background(InspySurface).clip(RoundedCornerShape(14.dp)))
                            }
                            else -> SubcomposeAsyncImageContent()
                        }
                    }
                    Spacer(Modifier.height(5.dp))
                    Text("Latest", fontSize = 11.sp, color = InspyPrimary, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}