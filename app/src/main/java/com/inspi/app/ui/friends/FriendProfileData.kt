package com.inspi.app.data.fake

import com.inspi.app.domain.models.HobbyType

data class FriendGalleryItem(
    val imageUrl: String,
    val taskTitle: String,
    val xpEarned: Int,
)

data class FriendProfileSnapshot(
    val code: String,
    val username: String,
    val hobby: HobbyType,
    val totalXp: Int,
    val currentStreak: Int,
    val longestStreak: Int,
    val avatarUrl: String = "",
    val level: Int = totalXp / 500 + 1,
    val xpToNextLevel: Int = 500 - (totalXp % 500),
    val xpProgressFraction: Float = (totalXp % 500) / 500f,
    val gallery: List<FriendGalleryItem>,
) {
    val totalWorks: Int get() = gallery.size
}

// Drawing  → grayscale (sketch feel)
// Photography → normal colour
private fun drawingUrl(seed: String) =
    "https://picsum.photos/seed/$seed/400/400?grayscale"

private fun photoUrl(seed: String) =
    "https://picsum.photos/seed/$seed/400/400"

object FriendDatabase {

    private val profiles = listOf(

        // ── Alex – Drawing ──────────────────────────────────────────────────
        FriendProfileSnapshot(
            code = "ALEX01",
            username = "Alex 🌱",
            hobby = HobbyType.DRAWING,
            totalXp = 1840,
            currentStreak = 12,
            longestStreak = 19,
            avatarUrl = "https://i.pravatar.cc/150?img=12",
            gallery = listOf(
                FriendGalleryItem(drawingUrl("alex_1"), "Still Life Sketch", 50),
                FriendGalleryItem(drawingUrl("alex_2"), "Portrait Study", 75),
                FriendGalleryItem(drawingUrl("alex_3"), "Urban Landscape", 50),
                FriendGalleryItem(drawingUrl("alex_4"), "Abstract Forms", 60),
                FriendGalleryItem(drawingUrl("alex_5"), "Character Design", 75),
                FriendGalleryItem(drawingUrl("alex_6"), "Nature Doodle", 50),
                FriendGalleryItem(drawingUrl("alex_7"), "Gesture Drawing", 50),
                FriendGalleryItem(drawingUrl("alex_8"), "Ink Study", 75),
                FriendGalleryItem(drawingUrl("alex_9"), "Perspective Practice", 60),
            )
        ),

        // ── Maya – Photography ──────────────────────────────────────────────
        FriendProfileSnapshot(
            code = "MAYA02",
            username = "Maya ✨",
            hobby = HobbyType.PHOTOGRAPHY,
            totalXp = 1250,
            currentStreak = 7,
            longestStreak = 14,
            avatarUrl = "https://i.pravatar.cc/150?img=5",
            gallery = listOf(
                FriendGalleryItem(photoUrl("maya_1"), "Golden Hour Shot", 50),
                FriendGalleryItem(photoUrl("maya_2"), "Street Candid", 75),
                FriendGalleryItem(photoUrl("maya_3"), "Macro Flowers", 50),
                FriendGalleryItem(photoUrl("maya_4"), "Symmetry Challenge", 60),
                FriendGalleryItem(photoUrl("maya_5"), "Shadow Play", 75),
                FriendGalleryItem(photoUrl("maya_6"), "Reflections", 50),
                FriendGalleryItem(photoUrl("maya_7"), "Minimalist Composition", 50),
            )
        ),

        // ── Jake – Drawing ──────────────────────────────────────────────────
        FriendProfileSnapshot(
            code = "JAKE03",
            username = "Jake 🏃",
            hobby = HobbyType.DRAWING,
            totalXp = 870,
            currentStreak = 5,
            longestStreak = 8,
            avatarUrl = "https://i.pravatar.cc/150?img=33",
            gallery = listOf(
                FriendGalleryItem(drawingUrl("jake_1"), "Quick Sketch", 50),
                FriendGalleryItem(drawingUrl("jake_2"), "Animal Study", 50),
                FriendGalleryItem(drawingUrl("jake_3"), "Cartoon Character", 75),
                FriendGalleryItem(drawingUrl("jake_4"), "Environment Concept", 60),
            )
        ),

        // ── Nina – Photography ──────────────────────────────────────────────
        FriendProfileSnapshot(
            code = "NINA04",
            username = "Nina 🎨",
            hobby = HobbyType.PHOTOGRAPHY,
            totalXp = 490,
            currentStreak = 3,
            longestStreak = 5,
            avatarUrl = "https://i.pravatar.cc/150?img=47",
            gallery = listOf(
                FriendGalleryItem(photoUrl("nina_1"), "Morning Light", 50),
                FriendGalleryItem(photoUrl("nina_2"), "Food Photography", 75),
                FriendGalleryItem(photoUrl("nina_3"), "Texture Close-up", 50),
            )
        ),

        // ── Sam – Drawing ───────────────────────────────────────────────────
        FriendProfileSnapshot(
            code = "INSPI1",
            username = "Sam 🌟",
            hobby = HobbyType.DRAWING,
            totalXp = 920,
            currentStreak = 9,
            longestStreak = 15,
            avatarUrl = "https://i.pravatar.cc/150?img=59",
            gallery = listOf(
                FriendGalleryItem(drawingUrl("sam_1"), "Gesture Drawing", 50),
                FriendGalleryItem(drawingUrl("sam_2"), "Ink Study", 75),
                FriendGalleryItem(drawingUrl("sam_3"), "Digital Sketch", 50),
                FriendGalleryItem(drawingUrl("sam_4"), "Perspective Practice", 60),
                FriendGalleryItem(drawingUrl("sam_5"), "Composition Challenge", 75),
            )
        ),
    )

    fun getProfile(code: String): FriendProfileSnapshot? =
        profiles.firstOrNull { it.code == code }
}
