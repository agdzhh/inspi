package com.inspi.app.utils

import com.inspi.app.domain.models.DailyTask
import com.inspi.app.domain.models.HobbyType
import java.util.Calendar

object TaskSelector {

    private val photographyTasks = listOf(
        DailyTask("Warm Colors",        "Take a photo of something with a warm color (red, orange, yellow).", HobbyType.PHOTOGRAPHY),
        DailyTask("Shadow & Reflection","Photograph a shadow or a reflection in any surface.",               HobbyType.PHOTOGRAPHY),
        DailyTask("Motion",             "Capture something in motion — freeze it or let it blur.",            HobbyType.PHOTOGRAPHY),
        DailyTask("Texture Hunt",       "Find an interesting texture and photograph it up close.",            HobbyType.PHOTOGRAPHY),
        DailyTask("Natural Frame",      "Use a doorway, window, or branches to frame your subject.",          HobbyType.PHOTOGRAPHY),
        DailyTask("Rule of Thirds",     "Compose a shot where your subject sits at a power point.",           HobbyType.PHOTOGRAPHY),
        DailyTask("Silhouette",         "Photograph a silhouette against a bright background.",               HobbyType.PHOTOGRAPHY),
        DailyTask("Leading Lines",      "Find lines that draw the viewer's eye into the photo.",              HobbyType.PHOTOGRAPHY),
        DailyTask("Macro Details",      "Get extremely close to something small and photograph it.",          HobbyType.PHOTOGRAPHY),
        DailyTask("Golden Hour",        "Shoot something during the first or last hour of sunlight.",         HobbyType.PHOTOGRAPHY),
        DailyTask("Minimalist",         "Photograph a subject with a very simple, clean background.",         HobbyType.PHOTOGRAPHY),
        DailyTask("Street Life",        "Capture a candid moment of everyday life outside.",                  HobbyType.PHOTOGRAPHY),
        DailyTask("Symmetry",           "Find perfect or near-perfect symmetry and photograph it.",           HobbyType.PHOTOGRAPHY),
        DailyTask("Color Contrast",     "Compose a shot that uses two contrasting colors powerfully.",        HobbyType.PHOTOGRAPHY),
        DailyTask("Point of View",      "Take a photo from an unusual angle — low, high, or tilted.",        HobbyType.PHOTOGRAPHY),
    )

    private val drawingTasks = listOf(
        DailyTask("Quick Animal",       "Sketch any animal in under 5 minutes from imagination.",            HobbyType.DRAWING),
        DailyTask("Hand Study",         "Draw your own hand from careful observation.",                       HobbyType.DRAWING),
        DailyTask("Mood Illustration",  "Illustrate your current mood as an abstract image or character.",   HobbyType.DRAWING),
        DailyTask("Memory Drawing",     "Draw something from memory without using any reference.",            HobbyType.DRAWING),
        DailyTask("30-Second Faces",    "Draw 6 quick faces showing different emotions in 3 minutes.",        HobbyType.DRAWING),
        DailyTask("Object Contour",     "Pick any object nearby and draw only its outline without lifting the pen.", HobbyType.DRAWING),
        DailyTask("Negative Space",     "Draw the space around an object rather than the object itself.",    HobbyType.DRAWING),
        DailyTask("Texture Practice",   "Fill a page with 4 different drawn textures (fur, stone, fabric, water).", HobbyType.DRAWING),
        DailyTask("Gesture Figures",    "Do 5 one-minute gesture drawings of a human figure.",               HobbyType.DRAWING),
        DailyTask("Everyday Object",    "Pick something within arm's reach and sketch it carefully.",        HobbyType.DRAWING),
        DailyTask("Doodle Pattern",     "Fill a page with a repeating pattern of your own invention.",       HobbyType.DRAWING),
        DailyTask("Plant Study",        "Draw any plant or leaf you can find, focusing on detail.",          HobbyType.DRAWING),
        DailyTask("Architecture Sketch","Draw any building or interior space, paying attention to perspective.", HobbyType.DRAWING),
        DailyTask("Self-Portrait",      "Draw a quick self-portrait — no reference, just your imagination.", HobbyType.DRAWING),
        DailyTask("Food Illustration",  "Draw whatever you ate or drank today.",                             HobbyType.DRAWING),
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
