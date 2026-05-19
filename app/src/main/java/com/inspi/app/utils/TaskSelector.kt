package com.inspi.app.utils

import com.inspi.app.domain.models.DailyTask
import com.inspi.app.domain.models.HobbyType
import java.util.Calendar

object TaskSelector {

    private val photographyTasks = listOf(
        DailyTask("Warm Colors",        "Find something red, orange, or yellow and make it the clear hero of your frame — fill at least half the shot with it.", HobbyType.PHOTOGRAPHY),
        DailyTask("Shadow & Reflection","Hunt for a shadow or reflection and compose the shot so the shadow IS the subject, not just a background detail.",      HobbyType.PHOTOGRAPHY),
        DailyTask("Motion Blur",        "Set your phone to the slowest shutter speed it allows and capture something moving — aim for visible blur, not freeze.", HobbyType.PHOTOGRAPHY),
        DailyTask("Texture Hunt",       "Get close enough that your subject fills the entire frame with texture — no context, just surface detail.",             HobbyType.PHOTOGRAPHY),
        DailyTask("Natural Frame",      "Use a doorway, arch, window, or overhanging branches to frame your subject. The frame should be in sharp focus too.",   HobbyType.PHOTOGRAPHY),
        DailyTask("Rule of Thirds",     "Place your subject at one of the four intersection points — not centered. Check the grid overlay if your camera has one.", HobbyType.PHOTOGRAPHY),
        DailyTask("Silhouette",         "Expose for the bright background, not your subject. Shoot during golden hour or against a window for best results.",    HobbyType.PHOTOGRAPHY),
        DailyTask("Leading Lines",      "Find a road, fence, railing, or shadow that points toward your subject. The line should enter from a corner.",          HobbyType.PHOTOGRAPHY),
        DailyTask("Macro Details",      "Get as close as your camera will focus — aim for something smaller than a coin. Steady your hand or use a surface.",    HobbyType.PHOTOGRAPHY),
        DailyTask("Golden Hour",        "Shoot in the 30 minutes after sunrise or before sunset. Use the light sideways — let it graze across your subject.",    HobbyType.PHOTOGRAPHY),
        DailyTask("Minimalist",         "One subject, one clean background — remove everything else from the frame. Negative space is part of the composition.", HobbyType.PHOTOGRAPHY),
        DailyTask("Street Life",        "Wait for a person to enter your frame, then shoot. Don't ask — capture a genuine moment of someone just living.",       HobbyType.PHOTOGRAPHY),
        DailyTask("Symmetry",           "Find a reflection, corridor, or building with strong symmetry and center it exactly. Even small misalignment matters.", HobbyType.PHOTOGRAPHY),
        DailyTask("Color Contrast",     "Find two opposing colors in the same frame (red/green, blue/orange, yellow/purple). Let them compete for attention.",   HobbyType.PHOTOGRAPHY),
        DailyTask("Worm's Eye View",    "Place your camera at ground level or below knee height and shoot upward. Ordinary objects become unexpectedly dramatic.", HobbyType.PHOTOGRAPHY),
    )

    private val drawingTasks = listOf(
        DailyTask("Quick Animal",       "Sketch any animal from imagination in under 5 minutes — no erasing allowed. Commit to every line.",                     HobbyType.DRAWING),
        DailyTask("Hand Study",         "Draw your non-dominant hand in a relaxed pose. Focus on proportions and the spaces between fingers, not outlines.",     HobbyType.DRAWING),
        DailyTask("Mood Illustration",  "Translate your current mood into a simple image using only shapes and lines — no text, no face, no literal symbols.",   HobbyType.DRAWING),
        DailyTask("Memory Drawing",     "Think of a place you know well, then draw it entirely from memory — no reference, no photos.",                          HobbyType.DRAWING),
        DailyTask("30-Second Faces",    "Draw 6 faces showing different emotions — spend exactly 30 seconds each. Use a timer.",                                 HobbyType.DRAWING),
        DailyTask("Blind Contour",      "Draw any object without lifting your pen and without looking at the paper. Only look at the object.",                   HobbyType.DRAWING),
        DailyTask("Negative Space",     "Draw only the shapes of empty space around a chair or table leg — ignore the object itself.",                           HobbyType.DRAWING),
        DailyTask("3-Value Study",      "Pick any object and draw it using exactly 3 tones: white, mid-grey, and black. No outlines — only filled shapes.",     HobbyType.DRAWING),
        DailyTask("Gesture Figures",    "Do 5 gesture drawings of a human figure, 1 minute each. Focus on movement and weight, not detail.",                    HobbyType.DRAWING),
        DailyTask("Everyday Object",    "Pick something within arm's reach and sketch it carefully — measure proportions using your pencil as a ruler.",         HobbyType.DRAWING),
        DailyTask("Doodle Pattern",     "Design a repeating tile pattern and fill an A5-sized area with it. Every tile must connect to its neighbors.",          HobbyType.DRAWING),
        DailyTask("Plant Study",        "Draw any plant or leaf — focus on where light hits the surface and where it falls into shadow.",                        HobbyType.DRAWING),
        DailyTask("One-Point Perspective","Draw a hallway, road, or room using a single vanishing point on the horizon. Use a ruler for the perspective lines.", HobbyType.DRAWING),
        DailyTask("Self-Portrait",      "Draw yourself without a mirror or photo — work purely from imagination and how you feel today.",                        HobbyType.DRAWING),
        DailyTask("Food Illustration",  "Draw whatever you ate or drank today. Focus on the light source: where is the brightest highlight on each surface?",   HobbyType.DRAWING),
    )

    fun getTodayTask(hobby: HobbyType): DailyTask {
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val pool = when (hobby) {
            HobbyType.PHOTOGRAPHY -> photographyTasks
            HobbyType.DRAWING     -> drawingTasks
        }
        return pool[dayOfYear % pool.size]
    }

    // Weekly challenges
    private val photographyChallenges = listOf(
        "Golden Hour Series" to "This week, take a photo every day during the last 30 minutes of sunlight. At the end, pick your favourite.",
        "One Subject, Many Angles" to "Choose one object and photograph it from at least 7 completely different angles, distances, or perspectives.",
        "Colour Story" to "Tell a story using only one dominant colour. All your shots this week should feature that colour prominently.",
        "People & Places" to "Capture 3 candid street scenes where the environment is as important as the person in it.",
    )

    private val drawingChallenges = listOf(
        "Character Sheet" to "Design an original character. Draw them front, side, and in at least two different poses or expressions.",
        "Environment Sketch" to "Draw a full environment — interior or exterior — with foreground, mid-ground, and background depth.",
        "Still Life Series" to "Arrange 3–5 objects on a table and draw the same arrangement from 3 different viewpoints.",
        "Narrative Panel" to "Create a 4-panel comic strip that tells a tiny, wordless story from your day.",
    )

    fun getCurrentChallenge(hobby: HobbyType): Pair<String, String> {
        val weekOfYear = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)
        val pool = when (hobby) {
            HobbyType.PHOTOGRAPHY -> photographyChallenges
            HobbyType.DRAWING     -> drawingChallenges
        }
        return pool[weekOfYear % pool.size]
    }
}
