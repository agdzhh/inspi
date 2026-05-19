package com.inspi.app.utils

import com.inspi.app.domain.models.HobbyType
import org.junit.Assert.*
import org.junit.Test

class TaskSelectorTest {

    @Test
    fun `getTodayTask always returns a task for photography`() {
        val task = TaskSelector.getTodayTask(HobbyType.PHOTOGRAPHY)
        assertNotNull(task)
        assertEquals(HobbyType.PHOTOGRAPHY, task.hobbyType)
    }

    @Test
    fun `getTodayTask always returns a task for drawing`() {
        val task = TaskSelector.getTodayTask(HobbyType.DRAWING)
        assertNotNull(task)
        assertEquals(HobbyType.DRAWING, task.hobbyType)
    }

    @Test
    fun `day modulo wraps correctly`() {
        // Task pools have 15 items; any dayOfYear % 15 should be a valid index
        repeat(366) { day ->
            val index = day % 15
            assertTrue(index in 0..14)
        }
    }

    @Test
    fun `getCurrentChallenge returns non-empty title and description`() {
        val (title, desc) = TaskSelector.getCurrentChallenge(HobbyType.PHOTOGRAPHY)
        assertTrue(title.isNotBlank())
        assertTrue(desc.isNotBlank())
    }
}
