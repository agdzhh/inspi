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

private fun drawingUrl(seed: String) = "https://picsum.photos/seed/$seed/400/400?grayscale"
private fun photoUrl(seed: String)   = "https://picsum.photos/seed/$seed/400/400"

object FriendDatabase {

    private val profiles = listOf(

        // ── Sofia – Photography ─────────────────────────────────────────────
        FriendProfileSnapshot(
            code = "SOFIA01",
            username = "Sofia ✨",
            hobby = HobbyType.PHOTOGRAPHY,
            totalXp = 2_150,
            currentStreak = 34,
            longestStreak = 34,
            avatarUrl = "https://i.pravatar.cc/150?img=16",
            gallery = listOf(
                FriendGalleryItem(photoUrl("sofia_01"), "Golden Hour", 100),
                FriendGalleryItem(photoUrl("sofia_02"), "Leading Lines", 75),
                FriendGalleryItem(photoUrl("sofia_03"), "Silhouette", 100),
                FriendGalleryItem(photoUrl("sofia_04"), "Warm Colors", 75),
                FriendGalleryItem(photoUrl("sofia_05"), "Texture Hunt", 50),
                FriendGalleryItem(photoUrl("sofia_06"), "Natural Frame", 75),
                FriendGalleryItem(photoUrl("sofia_07"), "Street Life", 100),
                FriendGalleryItem(photoUrl("sofia_08"), "Symmetry", 75),
                FriendGalleryItem(photoUrl("sofia_09"), "Macro Details", 50),
                FriendGalleryItem(photoUrl("sofia_10"), "Minimalist", 75),
                FriendGalleryItem(photoUrl("sofia_11"), "Color Contrast", 75),
                FriendGalleryItem(photoUrl("sofia_12"), "Rule of Thirds", 50),
                FriendGalleryItem(photoUrl("sofia_13"), "Shadow & Reflection", 75),
                FriendGalleryItem(photoUrl("sofia_14"), "Worm's Eye View", 50),
                FriendGalleryItem(photoUrl("sofia_15"), "Motion Blur", 100),
                FriendGalleryItem(photoUrl("sofia_16"), "Golden Hour", 100),
                FriendGalleryItem(photoUrl("sofia_17"), "Leading Lines", 75),
                FriendGalleryItem(photoUrl("sofia_18"), "Natural Frame", 75),
                FriendGalleryItem(photoUrl("sofia_19"), "Silhouette", 100),
                FriendGalleryItem(photoUrl("sofia_20"), "Warm Colors", 75),
            )
        ),

        // ── Marco – Drawing ─────────────────────────────────────────────────
        FriendProfileSnapshot(
            code = "MARCO02",
            username = "Marco 🎯",
            hobby = HobbyType.DRAWING,
            totalXp = 1_780,
            currentStreak = 21,
            longestStreak = 28,
            avatarUrl = "https://i.pravatar.cc/150?img=68",
            gallery = listOf(
                FriendGalleryItem(drawingUrl("marco_01"), "Hand Study", 75),
                FriendGalleryItem(drawingUrl("marco_02"), "Gesture Figures", 75),
                FriendGalleryItem(drawingUrl("marco_03"), "3-Value Study", 75),
                FriendGalleryItem(drawingUrl("marco_04"), "Self-Portrait", 75),
                FriendGalleryItem(drawingUrl("marco_05"), "Blind Contour", 50),
                FriendGalleryItem(drawingUrl("marco_06"), "Memory Drawing", 75),
                FriendGalleryItem(drawingUrl("marco_07"), "One-Point Perspective", 75),
                FriendGalleryItem(drawingUrl("marco_08"), "Doodle Pattern", 50),
                FriendGalleryItem(drawingUrl("marco_09"), "Plant Study", 50),
                FriendGalleryItem(drawingUrl("marco_10"), "Food Illustration", 75),
                FriendGalleryItem(drawingUrl("marco_11"), "Quick Animal", 50),
                FriendGalleryItem(drawingUrl("marco_12"), "30-Second Faces", 75),
                FriendGalleryItem(drawingUrl("marco_13"), "Negative Space", 50),
                FriendGalleryItem(drawingUrl("marco_14"), "Mood Illustration", 75),
                FriendGalleryItem(drawingUrl("marco_15"), "Everyday Object", 50),
            )
        ),

        // ── Yuki – Photography ──────────────────────────────────────────────
        FriendProfileSnapshot(
            code = "YUKI03",
            username = "Yuki 🌸",
            hobby = HobbyType.PHOTOGRAPHY,
            totalXp = 2_450,
            currentStreak = 45,
            longestStreak = 45,
            avatarUrl = "https://i.pravatar.cc/150?img=25",
            gallery = listOf(
                FriendGalleryItem(photoUrl("yuki_01"), "Macro Details", 50),
                FriendGalleryItem(photoUrl("yuki_02"), "Symmetry", 75),
                FriendGalleryItem(photoUrl("yuki_03"), "Golden Hour", 100),
                FriendGalleryItem(photoUrl("yuki_04"), "Minimalist", 75),
                FriendGalleryItem(photoUrl("yuki_05"), "Color Contrast", 75),
                FriendGalleryItem(photoUrl("yuki_06"), "Texture Hunt", 50),
                FriendGalleryItem(photoUrl("yuki_07"), "Leading Lines", 75),
                FriendGalleryItem(photoUrl("yuki_08"), "Silhouette", 100),
                FriendGalleryItem(photoUrl("yuki_09"), "Street Life", 100),
                FriendGalleryItem(photoUrl("yuki_10"), "Natural Frame", 75),
                FriendGalleryItem(photoUrl("yuki_11"), "Rule of Thirds", 50),
                FriendGalleryItem(photoUrl("yuki_12"), "Shadow & Reflection", 75),
                FriendGalleryItem(photoUrl("yuki_13"), "Warm Colors", 75),
                FriendGalleryItem(photoUrl("yuki_14"), "Motion Blur", 100),
                FriendGalleryItem(photoUrl("yuki_15"), "Worm's Eye View", 50),
                FriendGalleryItem(photoUrl("yuki_16"), "Macro Details", 50),
                FriendGalleryItem(photoUrl("yuki_17"), "Symmetry", 75),
                FriendGalleryItem(photoUrl("yuki_18"), "Golden Hour", 100),
                FriendGalleryItem(photoUrl("yuki_19"), "Silhouette", 100),
                FriendGalleryItem(photoUrl("yuki_20"), "Street Life", 100),
                FriendGalleryItem(photoUrl("yuki_21"), "Minimalist", 75),
                FriendGalleryItem(photoUrl("yuki_22"), "Color Contrast", 75),
            )
        ),

        // ── Alex – Drawing ──────────────────────────────────────────────────
        FriendProfileSnapshot(
            code = "ALEX04",
            username = "Alex 🌱",
            hobby = HobbyType.DRAWING,
            totalXp = 1_120,
            currentStreak = 12,
            longestStreak = 19,
            avatarUrl = "https://i.pravatar.cc/150?img=12",
            gallery = listOf(
                FriendGalleryItem(drawingUrl("alex_01"), "Quick Animal", 50),
                FriendGalleryItem(drawingUrl("alex_02"), "Hand Study", 75),
                FriendGalleryItem(drawingUrl("alex_03"), "Blind Contour", 50),
                FriendGalleryItem(drawingUrl("alex_04"), "Everyday Object", 50),
                FriendGalleryItem(drawingUrl("alex_05"), "Gesture Figures", 75),
                FriendGalleryItem(drawingUrl("alex_06"), "3-Value Study", 75),
                FriendGalleryItem(drawingUrl("alex_07"), "Doodle Pattern", 50),
                FriendGalleryItem(drawingUrl("alex_08"), "Plant Study", 50),
                FriendGalleryItem(drawingUrl("alex_09"), "Memory Drawing", 75),
            )
        ),

        // ── Maya – Photography ──────────────────────────────────────────────
        FriendProfileSnapshot(
            code = "MAYA05",
            username = "Maya 🔥",
            hobby = HobbyType.PHOTOGRAPHY,
            totalXp = 870,
            currentStreak = 7,
            longestStreak = 14,
            avatarUrl = "https://i.pravatar.cc/150?img=5",
            gallery = listOf(
                FriendGalleryItem(photoUrl("maya_01"), "Golden Hour", 100),
                FriendGalleryItem(photoUrl("maya_02"), "Street Life", 100),
                FriendGalleryItem(photoUrl("maya_03"), "Macro Details", 50),
                FriendGalleryItem(photoUrl("maya_04"), "Symmetry", 75),
                FriendGalleryItem(photoUrl("maya_05"), "Shadow & Reflection", 75),
                FriendGalleryItem(photoUrl("maya_06"), "Warm Colors", 75),
                FriendGalleryItem(photoUrl("maya_07"), "Minimalist", 75),
            )
        ),

        // ── Lena – Drawing ──────────────────────────────────────────────────
        FriendProfileSnapshot(
            code = "LENA06",
            username = "Lena 🎨",
            hobby = HobbyType.DRAWING,
            totalXp = 1_340,
            currentStreak = 18,
            longestStreak = 22,
            avatarUrl = "https://i.pravatar.cc/150?img=47",
            gallery = listOf(
                FriendGalleryItem(drawingUrl("lena_01"), "Mood Illustration", 75),
                FriendGalleryItem(drawingUrl("lena_02"), "30-Second Faces", 75),
                FriendGalleryItem(drawingUrl("lena_03"), "Self-Portrait", 75),
                FriendGalleryItem(drawingUrl("lena_04"), "Food Illustration", 75),
                FriendGalleryItem(drawingUrl("lena_05"), "Plant Study", 50),
                FriendGalleryItem(drawingUrl("lena_06"), "Negative Space", 50),
                FriendGalleryItem(drawingUrl("lena_07"), "One-Point Perspective", 75),
                FriendGalleryItem(drawingUrl("lena_08"), "Doodle Pattern", 50),
                FriendGalleryItem(drawingUrl("lena_09"), "Quick Animal", 50),
                FriendGalleryItem(drawingUrl("lena_10"), "Gesture Figures", 75),
                FriendGalleryItem(drawingUrl("lena_11"), "Memory Drawing", 75),
            )
        ),

        // ── Omar – Photography ──────────────────────────────────────────────
        FriendProfileSnapshot(
            code = "OMAR07",
            username = "Omar 📷",
            hobby = HobbyType.PHOTOGRAPHY,
            totalXp = 420,
            currentStreak = 3,
            longestStreak = 7,
            avatarUrl = "https://i.pravatar.cc/150?img=33",
            gallery = listOf(
                FriendGalleryItem(photoUrl("omar_01"), "Rule of Thirds", 50),
                FriendGalleryItem(photoUrl("omar_02"), "Warm Colors", 75),
                FriendGalleryItem(photoUrl("omar_03"), "Natural Frame", 75),
            )
        ),

        // ── Sam – Drawing (magic demo code) ────────────────────────────────
        FriendProfileSnapshot(
            code = "INSPI1",
            username = "Sam 🌟",
            hobby = HobbyType.DRAWING,
            totalXp = 2_040,
            currentStreak = 31,
            longestStreak = 31,
            avatarUrl = "https://i.pravatar.cc/150?img=59",
            gallery = listOf(
                FriendGalleryItem(drawingUrl("sam_01"), "Gesture Figures", 75),
                FriendGalleryItem(drawingUrl("sam_02"), "3-Value Study", 75),
                FriendGalleryItem(drawingUrl("sam_03"), "Hand Study", 75),
                FriendGalleryItem(drawingUrl("sam_04"), "One-Point Perspective", 75),
                FriendGalleryItem(drawingUrl("sam_05"), "Mood Illustration", 75),
                FriendGalleryItem(drawingUrl("sam_06"), "Self-Portrait", 75),
                FriendGalleryItem(drawingUrl("sam_07"), "Blind Contour", 50),
                FriendGalleryItem(drawingUrl("sam_08"), "Plant Study", 50),
                FriendGalleryItem(drawingUrl("sam_09"), "30-Second Faces", 75),
                FriendGalleryItem(drawingUrl("sam_10"), "Food Illustration", 75),
                FriendGalleryItem(drawingUrl("sam_11"), "Doodle Pattern", 50),
                FriendGalleryItem(drawingUrl("sam_12"), "Quick Animal", 50),
            )
        ),
    )

    fun getProfile(code: String): FriendProfileSnapshot? =
        profiles.firstOrNull { it.code == code }
}