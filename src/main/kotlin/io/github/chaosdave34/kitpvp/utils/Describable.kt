package io.github.chaosdave34.kitpvp.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import java.util.ArrayList

interface Describable {
    fun createSimpleDescription(description: String): List<Component> {
        val splits = description.split(" ")

        val lines: MutableList<String> = mutableListOf()
        var workingLine = ""
        for (split in splits) {
            if (workingLine.length + split.length < 30)
                workingLine += if (workingLine.isEmpty()) split else " $split"
            else {
                lines.add(workingLine)
                workingLine = split
            }
        }
        if (workingLine.isNotEmpty()) lines.add(workingLine)

        val componentList: MutableList<Component> = ArrayList()
        for (line in lines) componentList.add(Component.text(line, NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))
        return componentList
    }
}