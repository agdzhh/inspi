package com.inspi.app.data.demo

import com.inspi.app.data.local.dao.*
import com.inspi.app.data.local.entities.*
import com.inspi.app.data.preferences.InspiPreferences
import com.inspi.app.domain.models.HobbyType
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Seeds a "premium power-user" state for marketing screenshots.
 * Safe to call on every launch — skips seeding if submission data already exists.
 *
 * Profile:     Jordan 📸  |  Photography  |  Level 5  |  ~2 300 XP
 * Streak:      31 days current  |  31 days best  (~1 month of daily use)
 * Submissions: 31 gallery entries backed by picsum.photos placeholders
 * Coach chat:  22 messages across 14 days, several with image attachments
 * Friends:     7 leaderboard competitors (codes match FriendDatabase)
 */
@Singleton
class DemoDataSeeder @Inject constructor(
    private val prefs: InspiPreferences,
    private val userProfileDao: UserProfileDao,
    private val submissionDao: SubmissionDao,
    private val coachMessageDao: CoachMessageDao,
    private val challengeDao: ChallengeDao,
    private val friendDao: FriendDao,
) {
    suspend fun seedIfEmpty() {
        if (submissionDao.count() > 0) return

        seedPreferences()
        seedUserProfile()
        seedSubmissions()
        seedCoachMessages()
        seedChallenges()
        seedFriends()
    }

    // ── Preferences ───────────────────────────────────────────────────────────

    private suspend fun seedPreferences() {
        prefs.setUsername("Jordan")
        prefs.setHobby(HobbyType.PHOTOGRAPHY.name)
        prefs.setOnboardingComplete()
        prefs.setNotificationsOn(true)
        prefs.setReminderTime(8, 30)
        prefs.setWeeklyInsight(
            "Solid week, Jordan! Your golden-hour shots are getting sharper — " +
                    "Tuesday's silhouette especially shows great exposure control. " +
                    "Try experimenting with leading lines this week to add more depth.",
            currentWeekStart()
        )
        prefs.setProfilePhotoUri("https://picsum.photos/seed/jordan_profile/400/400")
    }

    // ── User Profile ──────────────────────────────────────────────────────────

    private suspend fun seedUserProfile() {
        // 31 submissions × ~75 XP avg = ~2 325 XP → level 5 (2300), 200 XP to next
        userProfileDao.upsertProfile(
            UserProfileEntity(
                id                 = 1,
                username           = "Jordan",
                hobby              = HobbyType.PHOTOGRAPHY.name,
                totalXp            = 2_300,
                currentStreak      = 31,
                longestStreak      = 31,
                lastSubmissionDate = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(3),
            )
        )
    }

    // ── Submissions (gallery — 31 entries, one per day) ───────────────────────

    private suspend fun seedSubmissions() {
        val now = System.currentTimeMillis()
        val day = TimeUnit.DAYS.toMillis(1)

        // One task per day, cycling through the full task pool
        val tasks = listOf(
            "Golden Hour"         to 100,
            "Leading Lines"       to 75,
            "Silhouette"          to 100,
            "Warm Colors"         to 75,
            "Texture Hunt"        to 50,
            "Natural Frame"       to 75,
            "Street Life"         to 100,
            "Symmetry"            to 75,
            "Macro Details"       to 50,
            "Minimalist"          to 75,
            "Color Contrast"      to 75,
            "Rule of Thirds"      to 50,
            "Shadow & Reflection" to 75,
            "Worm's Eye View"     to 50,
            "Motion Blur"         to 100,
            "Golden Hour"         to 100,
            "Leading Lines"       to 75,
            "Natural Frame"       to 75,
            "Silhouette"          to 100,
            "Warm Colors"         to 75,
            "Texture Hunt"        to 50,
            "Street Life"         to 100,
            "Macro Details"       to 50,
            "Symmetry"            to 75,
            "Color Contrast"      to 75,
            "Minimalist"          to 75,
            "Rule of Thirds"      to 50,
            "Motion Blur"         to 100,
            "Shadow & Reflection" to 75,
            "Golden Hour"         to 100,
            "Leading Lines"       to 75,   // today
        )

        for (i in tasks.indices) {
            val daysAgo = (tasks.size - 1 - i).toLong()
            val (taskTitle, xp) = tasks[i]
            val seedId = 200 + i   // picsum seeds 200–230
            val ts = now - day * daysAgo + TimeUnit.HOURS.toMillis((7 + (i % 9)).toLong())

            submissionDao.insert(
                SubmissionEntity(
                    imagePath     = "https://picsum.photos/seed/jd_$seedId/800/1000",
                    thumbnailPath = "https://picsum.photos/seed/jd_$seedId/400/500",
                    taskTitle     = taskTitle,
                    createdAt     = ts,
                    hobbyType     = HobbyType.PHOTOGRAPHY.name,
                    xpEarned      = xp,
                )
            )
        }
    }

    // ── Coach chat ────────────────────────────────────────────────────────────

    private suspend fun seedCoachMessages() {
        val now = System.currentTimeMillis()
        val hour = TimeUnit.HOURS.toMillis(1)
        val day  = TimeUnit.DAYS.toMillis(1)

        data class M(val role: String, val text: String, val daysAgo: Long, val hoursAgo: Long = 0L)

        val msgs = listOf(
            // Day 14 — first session
            M("user",      "Hi! I just started using the app. I picked Photography — any tips for a complete beginner?", 14, 10),
            M("assistant", "Welcome, Jordan! 🎉 You've made a great choice. Forget gear — your phone camera is more than enough. Focus on light first: shoot within an hour of sunrise or sunset and everything looks more cinematic. Today's task (Warm Colors) is perfect for training your eye. Give it a go and share what you capture!", 14, 9),

            // Day 12 — first image share (submission index 18 = seed jd_218)
            M("user",      "[img]https://picsum.photos/seed/jd_218/800/1000", 12, 18),
            M("assistant", "Jordan, this is stunning for day three! The way the light catches the rooftop edge creates a natural leading line straight to the sky. One small tip: try rotating 15° clockwise next time — the horizon is very slightly tilted. Overall though: exceptional. You've got a real instinct for light. 🌅 +100 XP well earned!", 12, 17),

            // Day 10 — technique question
            M("user",      "What's the difference between leading lines and rule of thirds? I keep mixing them up.", 10, 12),
            M("assistant", "Great question — they're complementary tools, not the same thing.\n\nRule of Thirds is about *where* you place your subject. Divide the frame into a 3×3 grid and put your subject at one of the four intersection points.\n\nLeading Lines is about *how the eye travels* through the frame. Roads, fences, rivers, shadows — any line that guides the viewer toward your subject.\n\nThe magic? Use them together: subject at a rule-of-thirds point, with a leading line entering from a corner pointing right at it. That's what separates good shots from great ones. 📐", 10, 11),

            // Day 8 — struggling with motion blur
            M("user",      "I've been struggling with Motion Blur — it always looks like I just messed up the focus.", 8, 9),
            M("assistant", "Totally normal! The key is *intentionality* — the blur must look deliberate, not accidental.\n\nThree tricks:\n1. **Pan the camera** with a moving subject — the subject stays sharp, background blurs\n2. **Use a surface** — rest your phone somewhere solid for steady background blur\n3. **Shoot at dusk** — natural low light forces a slower shutter automatically\n\nMake sure one element stays *completely sharp*. Contrast between sharp and blurred signals intention. Try it tonight!", 8, 8),

            // Day 6 — texture hunt image (submission index 24 = seed jd_224)
            M("user",      "[img]https://picsum.photos/seed/jd_224/800/1000", 6, 15),
            M("assistant", "YES. This is exactly what Texture Hunt is about. The shallow depth of field makes the centre layers pop against soft edges — great instinct. The colour palette is beautifully cohesive: burnt sienna, raw umber, dusty white. This could honestly be a print. 🏆\n\nChallenge for next time: shoot the same wall at two different times of day. Morning raking light vs midday flat. The texture will look completely different.", 6, 14),

            // Day 5 — streak milestone
            M("user",      "I hit a 2-week streak today! 🎉", 5, 11),
            M("assistant", "Two weeks straight! Jordan, that's already more consistent than most people ever get. You're building a real habit now.\n\nHere's what I've noticed: your composition instincts are sharpening fast. The framing in your last three shots shows you're starting to *see* before you shoot — not just react. Keep it going. 🔥", 5, 10),

            // Day 4 — challenge request
            M("user",      "What's a good self-challenge for this week? I want to push myself more.", 4, 16),
            M("assistant", "Perfect timing. The 7-Frame Street Story.\n\nFind one outdoor location. Return every day at a different time and capture ONE frame showing the place changing — different light, different people, different mood.\n\nBy day 7 you'll have a mini-documentary. This trains you to find fresh angles in familiar spots — one of the hardest and most valuable skills in photography. Tag each shot with the time of day. Go. 📍", 4, 15),

            // Day 3 — two street shots (submission index 27 = jd_227, index 28 = jd_228)
            M("user",      "[img]https://picsum.photos/seed/jd_227/800/1000", 3, 17),
            M("assistant", "Confident start — I love how you waited for the person to step into the light. The shutters behind them make a perfect frame within your frame. One note: there's a lamp post growing out of their shoulder. A single step left would have cleared it. Scan all four edges before you shoot.\n\nSend Day 2 when you're ready!", 3, 16),
            M("user",      "[img]https://picsum.photos/seed/jd_228/800/1000", 3, 15),
            M("assistant", "Day 2 is stronger. The long shadow in the foreground creates a second character — that's sophisticated visual thinking. Exposure is spot on too: highlights not blown, shadows retain detail.\n\nYou're developing a style, Jordan. Both shots have this quality of stillness within movement. Lean into that. 🎞️", 3, 14),

            // Yesterday — silhouette (submission index 29 = seed jd_229)
            M("user",      "[img]https://picsum.photos/seed/jd_229/800/1000", 1, 19),
            M("assistant", "Jordan. This is genuinely beautiful. 🌆\n\nThe figure is perfectly centred but it works because of the asymmetry in the background — the lighter sky on the right balances the dark building mass on the left. The exposure is *exactly* right: inky black silhouette, gradient sky from deep gold to soft peach — you caught it in the perfect 3-minute window.\n\nThis is screenshot-worthy. 100 XP is underselling it. 🏅", 1, 18),

            // Today
            M("user",      "What should I focus on to keep improving this week?", 0, 3),
            M("assistant", "You're at Level 5 with a 31-day streak — you're in a great place for someone one month in. To keep the momentum:\n\n1. **Chase the 100 XP tasks** — Golden Hour, Silhouette, Street Life, Motion Blur. You've proven you can nail these.\n2. **Complete the weekly challenge** — Golden Hour Series is live. One shot a day during sunset = big bonus XP.\n3. **Try Snapseed** (free) — even a small contrast boost and slight vignette will make your strong shots look polished.\n\nYou're on track to hit Level 6 by the end of the week. Let's go. 💪", 0, 2),
        )

        for (m in msgs) {
            val ts = now - day * m.daysAgo - hour * m.hoursAgo
            coachMessageDao.insert(CoachMessageEntity(role = m.role, content = m.text, createdAt = ts))
        }
    }

    // ── Challenge ─────────────────────────────────────────────────────────────

    private suspend fun seedChallenges() {
        challengeDao.upsert(
            ChallengeEntity(
                hobbyType     = HobbyType.PHOTOGRAPHY.name,
                title         = "Golden Hour Series",
                description   = "This week, take a photo every day during the last 30 minutes of sunlight. At the end, pick your favourite.",
                weekStartDate = currentWeekStart(),
                isCompleted   = false,
            )
        )
    }

    // ── Friends / Leaderboard — codes must match FriendDatabase ──────────────

    private suspend fun seedFriends() {
        val friends = listOf(
            FriendEntity(code = "SOFIA01", username = "Sofia ✨",  hobby = HobbyType.PHOTOGRAPHY.name, weeklyXp = 450, currentStreak = 34, avatarUrl = "https://i.pravatar.cc/150?img=16"),
            FriendEntity(code = "MARCO02", username = "Marco 🎯",  hobby = HobbyType.DRAWING.name,     weeklyXp = 375, currentStreak = 21, avatarUrl = "https://i.pravatar.cc/150?img=68"),
            FriendEntity(code = "YUKI03",  username = "Yuki 🌸",   hobby = HobbyType.PHOTOGRAPHY.name, weeklyXp = 500, currentStreak = 45, avatarUrl = "https://i.pravatar.cc/150?img=25"),
            FriendEntity(code = "ALEX04",  username = "Alex 🌱",   hobby = HobbyType.DRAWING.name,     weeklyXp = 275, currentStreak = 12, avatarUrl = "https://i.pravatar.cc/150?img=12"),
            FriendEntity(code = "MAYA05",  username = "Maya 🔥",   hobby = HobbyType.PHOTOGRAPHY.name, weeklyXp = 200, currentStreak = 7,  avatarUrl = "https://i.pravatar.cc/150?img=5"),
            FriendEntity(code = "LENA06",  username = "Lena 🎨",   hobby = HobbyType.DRAWING.name,     weeklyXp = 325, currentStreak = 18, avatarUrl = "https://i.pravatar.cc/150?img=47"),
            FriendEntity(code = "OMAR07",  username = "Omar 📷",   hobby = HobbyType.PHOTOGRAPHY.name, weeklyXp = 100, currentStreak = 3,  avatarUrl = "https://i.pravatar.cc/150?img=33"),
        )
        for (f in friends) {
            if (friendDao.findByCode(f.code) == null) friendDao.insert(f)
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun currentWeekStart(): Long {
        val cal = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
        cal.set(java.util.Calendar.MINUTE, 0)
        cal.set(java.util.Calendar.SECOND, 0)
        cal.set(java.util.Calendar.MILLISECOND, 0)
        val diff = (cal.get(java.util.Calendar.DAY_OF_WEEK) - java.util.Calendar.MONDAY + 7) % 7
        cal.add(java.util.Calendar.DAY_OF_MONTH, -diff)
        return cal.timeInMillis
    }
}