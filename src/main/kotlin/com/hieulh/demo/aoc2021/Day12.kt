package com.hieulh.demo.aoc2021

import java.io.File
import kotlin.collections.ArrayDeque

const val START = "start"
const val END = "end"

object Day12_1 {
    fun run(map: List<Pair<String, String>>): Int {
        // return number of available route
        val completedRoutes = hashSetOf<List<String>>()
        val routeQueue = ArrayDeque<ArrayList<String>>()
        routeQueue.add(arrayListOf(START))

        while (routeQueue.isNotEmpty()) {
            val route = routeQueue.removeFirst()
            val lastNode = route.last()
            map
                .filter { it.first == lastNode || it.second == lastNode }
                .forEach {
                    val node = if (it.first == lastNode) it.second else it.first
//                    if ((node.isLarge() || !route.contains(node)) && !route.containLines(lastNode, node)) {
                    if ((node.isLarge() || !route.contains(node))) {
                        if (node == END) {
                            completedRoutes.add(route.toMutableList().apply { add(node) })
                        } else {
                            routeQueue.add(ArrayList(route).apply { add(node) })
                        }
                    }
                }
        }
        return completedRoutes.size
    }
}

object Day12_2 {
    fun run(map: List<Pair<String, String>>): Int {
        // return number of available route
        val completedRoutes = hashSetOf<List<String>>()
        val routeQueue = ArrayDeque<ArrayList<String>>()
        routeQueue.add(arrayListOf(START))

        while (routeQueue.isNotEmpty()) {
            val route = routeQueue.removeFirst()
            val lastNode = route.last()
            map
                .filter { it.first == lastNode || it.second == lastNode }
                .forEach {
                    val node = if (it.first == lastNode) it.second else it.first
                    if (route.canJumpTo(node)) {
                        if (node == END) {
                            completedRoutes.add(route.toMutableList().apply { add(node) })
                        } else {
                            routeQueue.add(ArrayList(route).apply { add(node) })
                        }
                    }
                }
        }

        return completedRoutes.size
    }
}

fun List<String>.containLines(lastNode: String, node: String): Boolean {
    if (size < 2) return false
    else {
        for (i in 0 until size - 1) {
            if (lastNode == get(i) && node == get(i + 1)) {
                return true
            }
        }
        return false
    }
}

fun ArrayList<String>.noSmallDoubleJumpYet(): Boolean {
    filter { it.isSmall() }.forEach { small ->
        if (this.count { it == small } >= 2) return false
    }

    return true
}

fun ArrayList<String>.canJumpTo(node: String): Boolean {
    return when {
        node.isLarge() -> true
        !contains(node) -> true
        node == START -> false
        node.isSmall() && noSmallDoubleJumpYet() -> true
        else -> false
    }
}

fun ArrayList<String>.isCompletedRoute(): Boolean {
    return (size >= 2 && first() == START && last() == END)
}

fun String.isSmall(): Boolean {
    return first().isLowerCase()
}

fun String.isLarge() = !isSmall()

fun main() {
    /*val sample11Result = Day12_2.run(
        listOf("start-A", "start-b", "A-c", "A-b", "b-d", "A-end", "b-end")
            .map {
                Pair(it.substringBefore('-'), it.substringAfter('-'))
            }
    )
    println("Sample 12 1 result = $sample11Result")
    val sample12Result = Day12_1.run(
        listOf("dc-end", "HN-start", "start-kj", "dc-start", "dc-HN", "LN-dc", "HN-end", "kj-sa", "kj-HN", "kj-dc")
            .map { Pair(it.substringBefore('-'), it.substringAfter('-')) }
    )
    println("Sample 12 2 result = $sample12Result")

    val sample13Result = Day12_1.run(
        listOf(
            "fs-end",
            "he-DX",
            "fs-he",
            "start-DX",
            "pj-DX",
            "end-zg",
            "zg-sl",
            "zg-pj",
            "pj-he",
            "RW-he",
            "fs-DX",
            "pj-RW",
            "zg-RW",
            "start-pj",
            "he-WI",
            "zg-he",
            "pj-fs",
            "start-RW"
        ).map { Pair(it.substringBefore('-'), it.substringAfter('-')) }
    )

    println("Sample 12 3 result = $sample13Result")

    val day12result1 = Day12_1.run(
        File("input/day_12.txt")
            .readLines()
            .map { Pair(it.substringBefore('-'), it.substringAfter('-')) }
    )

    println("Day 12 1 result = $day12result1")*/


    val sample12Result = Day12_2.run(
        listOf("start-A", "start-b", "A-c", "A-b", "b-d", "A-end", "b-end")
            .map {
                Pair(it.substringBefore('-'), it.substringAfter('-'))
            }
    )
    println("Sample 12 2 result = $sample12Result")
    val day12result2 = Day12_2.run(
        File("input/day_12.txt")
            .readLines()
            .map { Pair(it.substringBefore('-'), it.substringAfter('-')) }
    )

    println("Day 12 2 result = $day12result2")
}