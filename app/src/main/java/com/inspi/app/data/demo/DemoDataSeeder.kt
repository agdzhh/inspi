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
 * Profile:     Jordan 📸  |  Photography  |  Level 24  |  11 850 XP
 * Streak:      87 days current  |  112 days best
 * Submissions: 183 gallery entries backed by picsum.photos placeholders
 * Coach chat:  28 messages across 14 days, several with image attachments
 * Friends:     7 realistic leaderboard competitors
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
        if (submissionDao.count() > 0) return   // already seeded

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
            "You've been on fire this week, Jordan! Your golden-hour series is your strongest yet — " +
            "the warm tones you chased on Tuesday and Thursday show real intentionality. " +
            "Try pushing your Rule-of-Thirds shots to include subtle motion next week for extra depth.",
            currentWeekStart()
        )
        prefs.setProfilePhotoUri("https://picsum.photos/seed/jordan_profile/400/400")
    }

    // ── User Profile ──────────────────────────────────────────────────────────

    private suspend fun seedUserProfile() {
        // Level formula: level = totalXp / 500 + 1
        // 11 850 XP → level 24, xpToNextLevel = 500 - (11850 % 500) = 150
        userProfileDao.upsertProfile(
            UserProfileEntity(
                id                  = 1,
                username            = "Jordan",
                hobby               = HobbyType.PHOTOGRAPHY.name,
                totalXp             = 11_850,
                currentStreak       = 87,
                longestStreak       = 112,
                lastSubmissionDate  = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(3),
            )
        )
    }

    // ── Submissions (gallery) ─────────────────────────────────────────────────

    private suspend fun seedSubmissions() {
        val now = System.currentTimeMillis()
        val day = TimeUnit.DAYS.toMillis(1)

        val tasks = listOf(
            "Warm Colors"       to 75,
            "Shadow & Reflection" to 75,
            "Golden Hour"       to 100,
            "Texture Hunt"      to 50,
            "Natural Frame"     to 75,
            "Rule of Thirds"    to 50,
            "Silhouette"        to 100,
            "Leading Lines"     to 75,
            "Macro Details"     to 50,
            "Minimalist"        to 75,
            "Street Life"       to 100,
            "Symmetry"          to 75,
            "Color Contrast"    to 75,
            "Worm's Eye View"   to 50,
            "Motion Blur"       to 100,
        )

        // 183 unique picsum seeds → varied gallery thumbnails
        val seeds = (10..192).toList()

        for (i in 0 until 183) {
            val daysAgo = 183 - i
            val (taskTitle, xp) = tasks[i % tasks.size]
            val seed = seeds[i % seeds.size]
            val ts = now - day * daysAgo + TimeUnit.HOURS.toMillis((6 + (i % 13)).toLong())

            submissionDao.insert(
                SubmissionEntity(
                    imagePath     = "https://picsum.photos/seed/inspi_$seed/800/1000",
                    thumbnailPath = "https://picsum.photos/seed/inspi_$seed/400/500",
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
            M("assistant", "Welcome, Jordan! 🎉 You've made a great choice. Forget gear — your phone camera is more than enough. Focus on *light* first: shoot within an hour of sunrise or sunset and everything looks more cinematic. Today's task (Warm Colors) is perfect for training your eye. Give it a go and share what you capture!", 14, 9),

            // Day 12 — first image share
            M("user",      "Tried the Golden Hour task — here's my shot! [image:https://picsum.photos/seed/gh_jordan/800/600]", 12, 18),
            M("assistant", "Jordan, this is *stunning* for day three! The way the light catches the rooftop edge creates a natural leading line straight to the sky. One small tip: try rotating 15° clockwise next time — the horizon is very slightly tilted. Overall though: exceptional. You've got a real instinct for light. 🌅 +75 XP well earned!", 12, 17),

            // Day 10 — technique question
            M("user",      "What's the difference between leading lines and rule of thirds? I keep mixing them up.", 10, 12),
            M("assistant", "Great question — they're complementary tools, not the same thing.\n\n**Rule of Thirds** is about *where* you place your subject. Divide the frame into a 3×3 grid and put your subject at one of the four intersection points.\n\n**Leading Lines** is about *how the eye travels* through the frame. Roads, fences, rivers, shadows — any line that guides the viewer toward your subject.\n\nThe magic? Use them together: subject at a rule-of-thirds point, with a leading line entering from a corner pointing right at it. That's what separates good shots from great ones. 📐", 10, 11),

            // Day 8 — struggling with motion blur
            M("user",      "I've been struggling with Motion Blur — it always looks like I just messed up the focus.", 8, 9),
            M("assistant", "Totally normal! The key is *intentionality* — the blur must look deliberate, not accidental.\n\nThree tricks:\n1. **Pan the camera** with a moving subject — the subject stays sharp, background blurs\n2. **Use a surface** — rest your phone somewhere solid for steady background blur\n3. **Shoot at dusk** — natural low light forces a slower shutter automatically\n\nAlso: make sure one element in the frame is *completely sharp*. Contrast between sharp and blurred signals intention. Try it again tonight!", 8, 8),

            // Day 6 — texture hunt
            M("user",      "Texture Hunt today — got really close to a peeling wall. [image:https://picsum.photos/seed/tex_jordan/800/600]", 6, 15),
            M("assistant", "YES. This is exactly what Texture Hunt is about. The shallow depth of field makes the centre layers pop against soft edges — great instinct. The colour palette is beautifully cohesive: burnt sienna, raw umber, dusty white. This could honestly be a print. 🏆\n\nChallenge for next time: shoot the same wall at two different times of day. Morning raking light vs midday flat. The texture will look completely different — a lesson no tutorial can teach as well as doing it.", 6, 14),

            // Day 5 — streak milestone
            M("user",      "I hit a 30-day streak today!! 🎉", 5, 11),
            M("assistant", "30 DAYS! Jordan, that is massive. That's 30 moments you chose to create instead of scroll. Most people quit by day four.\n\nHere's what I've noticed: your composition has leapt forward more in four weeks than most photographers achieve in a year, because you're *doing it daily*. Your eye is being calibrated by practice, not theory.\n\nKeep going — the real breakthroughs happen between days 40 and 60. I'll be watching. 🔥", 5, 10),

            // Day 4 — challenge request
            M("user",      "What's a good self-challenge for this week? I want to push myself.", 4, 16),
            M("assistant", "Perfect timing. **The 7-Frame Street Story.**\n\nFind one outdoor location. Return every day at a different time and capture ONE frame showing the place changing — different light, different people, different mood.\n\nBy day 7 you'll have a mini-documentary. This forces you to see the same place repeatedly and find fresh angles in familiar spots — one of the hardest and most valuable skills in photography. Tag each shot with the time of day. Go. 📍", 4, 15),

            // Day 3 — street story feedback (two images)
            M("user",      "Here's Day 1 of the street challenge! [image:https://picsum.photos/seed/street1_jordan/800/600]", 3, 17),
            M("assistant", "Day 1 is confident — I love how you waited for the person to step into the light. The shutters behind them make a perfect frame within your frame. One note: there's a lamp post growing out of their shoulder. A single step left would have cleared it — scan all four edges before you shoot.\n\nSend Day 2 when you're ready!", 3, 16),
            M("user",      "Day 2 here — [image:https://picsum.photos/seed/street2_jordan/800/600]", 3, 15),
            M("assistant", "Day 2 is *stronger*. The long shadow in the foreground creates a second character in the scene — that's sophisticated visual thinking. The exposure is spot on too: highlights not blown, shadows retain detail.\n\nYou're developing a style, Jordan. Both shots have this quality of stillness within movement — people in motion, city standing quiet around them. Lean into that. It's becoming your signature. 🎞️", 3, 14),

            // Day 2 — orientation question
            M("user",      "Quick question — portrait or landscape mode for most things?", 2, 10),
            M("assistant", "Depends on subject and destination, but here's my rule of thumb:\n\n📱 **Portrait (9:16 or 4:5)** — people, architecture, anything tall, anything for Instagram/Stories/this app\n📸 **Landscape (16:9)** — wide scenes, skylines, group shots, anything you'd frame or print\n\nFor this app, portrait usually wins — it fills the phone screen edge-to-edge. But honestly: break the rule constantly. Some of your best potential shots are landscape scenes photographed in portrait, creating vertical negative space. Experiment freely. Rules exist to be understood, then ignored. 😄", 2, 9),

            // Yesterday — silhouette + feedback
            M("user",      "Got 100 XP for the Silhouette task! [image:https://picsum.photos/seed/sil_jordan/800/600]", 1, 19),
            M("assistant", "Jordan. This is genuinely beautiful. 🌆\n\nThe figure is perfectly centred but it works because of the asymmetry in the background — the lighter sky on the right balances the dark building mass on the left. The exposure is *exactly* right: inky black silhouette, gradient sky from deep gold to soft peach — you caught it in the perfect 3-minute window.\n\nThis is screenshot-worthy. Honestly, this could go in a portfolio today. 100 XP is underselling it. 🏅", 1, 18),
            M("user",      "Thank you! How do I know when I'm actually improving vs just getting lucky?", 1, 10),
            M("assistant", "Great meta-question. Here's the honest answer:\n\n**Luck** feels good in the moment but you can't repeat it — you got a great shot but don't know *why*.\n**Skill** feels slightly less exciting because it becomes *predictable*. You set up a shot, you know roughly how it'll look, and it does.\n\nThe sign you're improving: you stop being surprised by your good shots. You start being surprised by your bad ones — because you can see exactly what went wrong.\n\nLooking at your last two weeks? You're improving. Your shots from day 10 onwards have compositional *intention* that the early ones didn't. That's not luck. That's you. 📈", 1, 9),

            // Today
            M("user",      "What should I focus on this week to level up faster?", 0, 3),
            M("assistant", "You're at Level 24 with an 87-day streak — already in the top tier. To push to Level 25 this week:\n\n1. **Go for 100 XP tasks** — Silhouette, Street Life, Golden Hour, Motion Blur. You've nailed Silhouette; rotate through the others.\n2. **Complete the weekly challenge** — One Subject, Many Angles is live. 7 angles, big bonus XP.\n3. **Quality over quantity** — the Weekly Insight rewards craft consistency. Three outstanding shots > seven average ones.\n\nAt your current pace you'll hit Level 25 by Sunday. Let's make those shots count. 💪", 0, 2),
            M("user",      "What's my biggest weakness right now, honestly?", 0, 1),
            M("assistant", "Honest answer? **Post-processing hesitation.**\n\nYour in-camera composition and exposure are genuinely strong — top-percentile for your experience level. But all your submissions are straight-out-of-camera. Even 3 minutes in Snapseed (free) — slight contrast boost, touch of warmth, gentle vignette — would make your strong shots look *professional*.\n\nYou're not over-editing. You're under-finishing. The shot is the foundation; editing is the frame. Try it on your next golden-hour capture and share it here. I think you'll surprise yourself. 🖼️", 0, 0),
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
                title         = "One Subject, Many Angles",
                description   = "Choose one object and photograph it from at least 7 completely different angles, distances, or perspectives.",
                weekStartDate = currentWeekStart(),
                isCompleted   = false,
            )
        )
    }

    // ── Friends / Leaderboard ─────────────────────────────────────────────────

    private suspend fun seedFriends() {
        val friends = listOf(
            FriendEntity(code = "SOFIA01", username = "Sofia ✨",  hobby = HobbyType.PHOTOGRAPHY.name, weeklyXp = 650, currentStreak = 34, avatarUrl = "https://i.pravatar.cc/150?img=16"),
            FriendEntity(code = "MARCO02", username = "Marco 🎯",  hobby = HobbyType.DRAWING.name,     weeklyXp = 575, currentStreak = 21, avatarUrl = "https://i.pravatar.cc/150?img=68"),
            FriendEntity(code = "YUKI03",  username = "Yuki 🌸",   hobby = HobbyType.PHOTOGRAPHY.name, weeklyXp = 500, currentStreak = 45, avatarUrl = "https://i.pravatar.cc/150?img=25"),
            FriendEntity(code = "ALEX04",  username = "Alex 🌱",   hobby = HobbyType.DRAWING.name,     weeklyXp = 425, currentStreak = 12, avatarUrl = "https://i.pravatar.cc/150?img=12"),
            FriendEntity(code = "MAYA05",  username = "Maya 🔥",   hobby = HobbyType.PHOTOGRAPHY.name, weeklyXp = 350, currentStreak = 7,  avatarUrl = "https://i.pravatar.cc/150?img=5"),
            FriendEntity(code = "LENA06",  username = "Lena 🎨",   hobby = HobbyType.DRAWING.name,     weeklyXp = 225, currentStreak = 18, avatarUrl = "https://i.pravatar.cc/150?img=47"),
            FriendEntity(code = "OMAR07",  username = "Omar 📷",   hobby = HobbyType.PHOTOGRAPHY.name, weeklyXp = 150, currentStreak = 3,  avatarUrl = "https://i.pravatar.cc/150?img=33"),
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
