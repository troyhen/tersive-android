package com.troy.tersive.ui.nav

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class BackStack<T>(initialElement: T, val onElementRemoved: ((Int, T) -> Unit)) {
    var current by mutableStateOf(initialElement)
        private set
    private val elements = mutableListOf<T>()

    fun newRoot(element: T) {
        while (elements.isNotEmpty()) {
            onElementRemoved.invoke(elements.size, elements.removeLast())
        }
        onElementRemoved.invoke(elements.size, current)
        current = element
    }

    fun pop(): Boolean {
        return if (elements.isEmpty()) false else {
            onElementRemoved.invoke(elements.size, current)
            current = elements.removeLast()
            true
        }
    }

    fun popTo(screen: T, inclusive: Boolean = false): Boolean {
        var index = elements.lastIndexOf(screen)
        if (index < 0) return false
        if (!inclusive || index == 0) index++
        val range = elements.lastIndex.downTo(index)
        range.forEach {
            onElementRemoved.invoke(elements.size, elements.removeLast())
        }
        current = elements.removeLast()
        return !range.isEmpty()
    }

    fun push(element: T) {
        elements.add(current)
        current = element
    }

//    fun pushAndDropNested(element: T) {
//        onElementRemoved.invoke(lastIndex)
//        push(element)
//    }

    fun replace(element: T) {
        onElementRemoved.invoke(elements.size, current)
        current = element
    }
}