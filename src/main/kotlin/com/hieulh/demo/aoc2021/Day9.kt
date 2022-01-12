package com.hieulh.demo.aoc2021

import java.io.File
import java.util.concurrent.SynchronousQueue

object Day9_1 {
    fun run(lines: List<String>): Int {
        val height = lines.size
        val width = lines[0].length

        val topo = getTopo(lines)
        var lowPointCount = 0
        var lowPointSum = 0
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (isLowPoint(topo, x, y, topo[y][x].value)) {
                    lowPointCount += 1
                    lowPointSum += (topo[y][x].value + 1)
                }
            }
        }
        return lowPointSum
    }
}

object Day9_2 {
    fun run(lines: List<String>): Long {
        val height = lines.size
        val width = lines[0].length
        val topo = getTopo(lines)

        val lowPoints = mutableListOf<Point>()
        var lowPointSum = 0
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (isLowPoint(topo, x, y, topo[y][x].value)) {
                    lowPoints.add(topo[y][x])
                }
            }
        }

        val basins = hashSetOf<HashSet<Point>>()
        lowPoints.forEach {
            val queue = ArrayDeque<Point>()
            val area = HashSet<Point>()
            queue.add(it)
            while(queue.isNotEmpty()) {
                val point = queue.removeFirst()
                if(point.value < 9) {
                    queue.addAll(
                        getAdjacentPoints(topo, point).filterNot {
                            area.contains(point)
                        }
                    )
                    area.add(point)
                }
            }
            basins.add(area)
        }

        return basins.sortedByDescending {
            it.size
        }
            .take(3)
            .fold(1L) { acc: Long, hashSet: HashSet<Point> ->
                acc * hashSet.size
            }
    }
}


fun getTopo(lines: List<String>): Array<Array<Point>> {
    val height = lines.size
    val width = lines[0].length
    return Array(height) { y ->
        Array(width) { x ->
            Point(x, y, lines[y][x].digitToInt())
        }
    }
}

fun isLowPoint(topo: Array<Array<Point>>, x: Int, y: Int, currentValue: Int): Boolean =
    getAdjacentValues(topo, x, y).all { it > currentValue }

fun getAdjacentValues(topo: Array<Array<Point>>, x: Int, y: Int): List<Int> {
    return listOfNotNull(
        topo.getOrNull(y - 1)?.getOrNull(x)?.value,
        topo.getOrNull(y + 1)?.getOrNull(x)?.value,
        topo.getOrNull(y)?.getOrNull(x - 1)?.value,
        topo.getOrNull(y)?.getOrNull(x + 1)?.value,
    )
}

fun getAdjacentPoints(topo: Array<Array<Point>>, point: Point): List<Point> {
    return listOfNotNull(
        topo.getOrNull(point.y - 1)?.getOrNull(point.x),
        topo.getOrNull(point.y + 1)?.getOrNull(point.x),
        topo.getOrNull(point.y)?.getOrNull(point.x - 1),
        topo.getOrNull(point.y)?.getOrNull(point.x + 1),
    )
}

data class Point(val x: Int, val y: Int, val value: Int)

fun main() {
    val sample = listOf(
        "2199943210",
        "3987894921",
        "9856789892",
        "8767896789",
        "9899965678"
    )
    println("Day 9 part 1 sample = ${Day9_1.run(sample)}")
    println("Day 9 part 1 result = ${Day9_1.run(File("input/day_9.txt").readLines())}")

    println("Day 9 part 2 sample = ${Day9_2.run(sample)}")
    println("Day 9 part 2 result = ${Day9_2.run(File("input/day_9.txt").readLines())}")
}