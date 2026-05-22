package com.inspi.app.ui.friends

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.inspi.app.data.fake.FriendDatabase
import com.inspi.app.data.fake.FriendGalleryItem
import com.inspi.app.data.fake.FriendProfileSnapshot
import com.inspi.app.domain.models.HobbyType
import com.inspi.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendProfileScreen(
    navController: NavController,
    friendCode: String,
) {
    val profile = remember(friendCode) { FriendDatabase.getProfile(friendCode) }
    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    BackHandler(enabled = selectedIndex != null) { selectedIndex = null }

    Scaffold(
        containerColor = InspyBackground,
        topBar = {
            if (selectedIndex == null) {
                TopAppBar(
                    title = { Text(profile?.username ?: "Profile", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.Outlined.ArrowBack,
                                contentDescription = "Back",
                                tint = InspyOnBackground,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = InspyBackground),
                )
            }
        }
    ) { padding ->

        if (profile == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Profile not found", color = InspyOnBackground.copy(alpha = 0.5f))
            }
            return@Scaffold
        }

        // Gallery rows (3 per row, flat-indexed)
        val galleryRows = remember(profile.gallery) { profile.gallery.chunked(3) }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {

            // ── Avatar + name ─────────────────────────────────────────────
            item(key = "header") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Box(
                            modifier = Modifier
                                .size(88.dp)
                                .clip(CircleShape)
                                .background(InspyHighlight),
                            contentAlignment = Alignment.Center,
                        ) {
                            if (profile.avatarUrl.isNotEmpty()) {
                                AsyncImage(
                                    model = profile.avatarUrl,
                                    contentDescription = profile.username,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize(),
                                )
                            } else {
                                Text(
                                    if (profile.hobby == HobbyType.PHOTOGRAPHY) "📸" else "🎨",
                                    fontSize = 40.sp,
                                )
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(50.dp),
                            color = InspyPrimary,
                            modifier = Modifier.padding(2.dp),
                        ) {
                            Text(
                                "Lv ${profile.level}",
                                fontSize = 10.sp,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        profile.username,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = InspyOnBackground,
                    )
                    Spacer(Modifier.height(4.dp))
                    Surface(shape = RoundedCornerShape(50.dp), color = InspyHighlight) {
                        Text(
                            profile.hobby.displayName,
                            fontSize = 12.sp,
                            color = InspyPrimary,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp),
                        )
                    }
                }
            }

            // ── XP progress ───────────────────────────────────────────────
            item(key = "xp") {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = InspySurface),
                    elevation = CardDefaults.cardElevation(0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column {
                                Text(
                                    "Level ${profile.level}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = InspyOnBackground,
                                )
                                Text(
                                    "${profile.xpToNextLevel} XP to next level",
                                    fontSize = 12.sp,
                                    color = InspyOnBackground.copy(alpha = 0.5f),
                                )
                            }
                            Icon(
                                Icons.Outlined.Star,
                                contentDescription = null,
                                tint = InspyPrimary,
                                modifier = Modifier.size(28.dp),
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(7.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .background(InspyHighlight),
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(profile.xpProgressFraction.coerceIn(0f, 1f))
                                    .fillMaxHeight()
                                    .background(InspyPrimary),
                            )
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))
            }

            // ── Stat cards row 1 ──────────────────────────────────────────
            item(key = "stats1") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    FriendStatCard(
                        icon = Icons.Outlined.Whatshot,
                        iconTint = Color(0xFFE8804A),
                        value = "${profile.currentStreak}",
                        label = "Streak",
                        sub = "Best: ${profile.longestStreak}d",
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                    )
                    FriendStatCard(
                        icon = Icons.Outlined.PhotoLibrary,
                        iconTint = InspyPrimary,
                        value = "${profile.totalWorks}",
                        label = "Works",
                        sub = null,
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                    )
                }
                Spacer(Modifier.height(10.dp))
            }

            // ── Stat cards row 2 ──────────────────────────────────────────
            item(key = "stats2") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    FriendStatCard(
                        icon = Icons.Outlined.EmojiEvents,
                        iconTint = Color(0xFFC4922A),
                        value = "${profile.totalXp} XP",
                        label = "Total earned",
                        sub = null,
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                    )
                    FriendStatCard(
                        icon = Icons.Outlined.CalendarToday,
                        iconTint = Color(0xFF3A9E75),
                        value = "${profile.totalWorks}",
                        label = "Quests done",
                        sub = null,
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                    )
                }
                Spacer(Modifier.height(24.dp))
            }

            // ── Gallery header ────────────────────────────────────────────
            item(key = "gallery_header") {
                Text(
                    "${profile.totalWorks} ${if (profile.totalWorks == 1) "work" else "works"}",
                    fontSize = 12.sp,
                    color = InspyOnBackground.copy(alpha = 0.45f),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }

            // ── Gallery rows ──────────────────────────────────────────────
            if (profile.gallery.isEmpty()) {
                item(key = "gallery_empty") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "No works yet",
                            color = InspyOnBackground.copy(alpha = 0.4f),
                            fontSize = 14.sp,
                        )
                    }
                }
            } else {
                itemsIndexed(galleryRows, key = { i, _ -> "row_$i" }) { rowIndex, row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(
                                if (rowIndex > 0) Modifier.padding(top = 2.dp) else Modifier
                            ),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        row.forEachIndexed { colIndex, item ->
                            val flatIndex = rowIndex * 3 + colIndex
                            FriendGalleryThumbnail(
                                item = item,
                                modifier = Modifier.weight(1f),
                                onClick = { selectedIndex = flatIndex },
                            )
                        }
                        // Fill empty slots in the last row
                        repeat(3 - row.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            item(key = "bottom_space") { Spacer(Modifier.height(24.dp)) }
        }

        // ── Fullscreen viewer overlay ─────────────────────────────────────
        AnimatedVisibility(
            visible = selectedIndex != null,
            enter = fadeIn(tween(200)),
            exit = fadeOut(tween(200)),
        ) {
            val idx = selectedIndex
            if (idx != null) {
                FriendFullscreenViewer(
                    items = profile.gallery,
                    initialIndex = idx,
                    onClose = { selectedIndex = null },
                )
            }
        }
    }
}

// ── Thumbnail ─────────────────────────────────────────────────────────────────
@Composable
private fun FriendGalleryThumbnail(
    item: FriendGalleryItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick),
    ) {
        SubcomposeAsyncImage(
            model = item.imageUrl,
            contentDescription = item.taskTitle,
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
private fun FriendFullscreenViewer(
    items: List<FriendGalleryItem>,
    initialIndex: Int,
    onClose: () -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = initialIndex,
        pageCount = { items.size },
    )
    val current = items.getOrNull(pagerState.currentPage)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        // ── Swipeable pages ───────────────────────────────────────────────
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            beyondViewportPageCount = 1,
        ) { page ->
            SubcomposeAsyncImage(
                model = items[page].imageUrl,
                contentDescription = items[page].taskTitle,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) { detectTapGestures() },
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

        // ── Top chrome: close + counter ───────────────────────────────────
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
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(40.dp)
                    .background(Color.Black.copy(alpha = 0.35f), CircleShape),
            ) {
                Icon(Icons.Filled.Close, contentDescription = "Close", tint = Color.White)
            }
            Text(
                "${pagerState.currentPage + 1} / ${items.size}",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.Center),
            )
        }

        // ── Bottom chrome: title + xp + dots ─────────────────────────────
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
                current?.let {
                    Text(
                        it.taskTitle,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "+${it.xpEarned} XP",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 13.sp,
                    )
                }
                if (items.size > 1) {
                    Spacer(Modifier.height(12.dp))
                    FriendPageDots(count = items.size, current = pagerState.currentPage)
                }
            }
        }
    }
}

@Composable
private fun FriendPageDots(count: Int, current: Int) {
    if (count <= 7) {
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
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
        }
    } else {
        Text(
            "${current + 1} of $count",
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 11.sp,
        )
    }
}

// ── Stat card ─────────────────────────────────────────────────────────────────
@Composable
private fun FriendStatCard(
    icon: ImageVector,
    iconTint: Color,
    value: String,
    label: String,
    sub: String?,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = InspySurface),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(22.dp))
            Column {
                Spacer(Modifier.height(8.dp))
                Text(value, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = InspyOnBackground)
                Text(label, fontSize = 11.sp, color = InspyOnBackground.copy(alpha = 0.5f))
                Text(
                    sub ?: "",
                    fontSize = 11.sp,
                    color = InspyPrimary,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}