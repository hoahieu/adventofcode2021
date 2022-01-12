package com.hieulh.demo.aoc2021

import java.io.File
import kotlin.math.abs

object Day7_1 {
    fun run(input: List<Int>): Long {
        val inputMap = HashMap<Int, Int>()
        var minPos = Int.MAX_VALUE
        var maxPos = Int.MIN_VALUE
        input.forEach {
            if(it > maxPos) maxPos = it
            if(it < minPos) minPos = it
            inputMap[it] = (inputMap[it] ?: 0) + 1
        }

        var minTotal: Long = Long.MAX_VALUE
        var selectedPosition = -1
        for(pos in minPos until maxPos) {
            val total = inputMap.map { abs(it.key-pos)*it.value.toLong() }.fold(0L) { acc: Long, l: Long ->
                acc+l
            }
            if(total < minTotal) {
                minTotal = total
                selectedPosition = pos
            }
        }
        return minTotal
    }
}

object Day7_2 {
    fun run(input: List<Int>): Long {
        val inputMap = HashMap<Int, Int>()
        var minPos = Int.MAX_VALUE
        var maxPos = Int.MIN_VALUE
        input.forEach {
            if(it > maxPos) maxPos = it
            if(it < minPos) minPos = it
            inputMap[it] = (inputMap[it] ?: 0) + 1
        }

        var minTotal: Long = Long.MAX_VALUE
        var selectedPosition = -1
        for(pos in minPos until maxPos) {
            val total = inputMap.map { getDistance(it.key, pos)*it.value.toLong() }.fold(0L) { acc: Long, l: Long ->
                acc+l
            }
            if(total < minTotal) {
                minTotal = total
                selectedPosition = pos
            }
        }
        return minTotal
    }

    private fun getDistance(currentPosition: Int, selectedPosition: Int): Long {
        val n = abs(currentPosition-selectedPosition)
        return ((n+1).toLong() * n) / 2
    }
}

fun main() {
    // Test
    println("Sample 1 result = ${Day7_1.run("16,1,2,0,4,2,7,1,2,14".split(',').map { it.toInt() })}")
    println("Challenge 7_1 result = ${Day7_1.run(File("input/day_7.txt").readLines()[0].split(',').map { it.toInt() })}")
    println("Sample 2 result = ${Day7_2.run("16,1,2,0,4,2,7,1,2,14".split(',').map { it.toInt() })}")
    println("Challenge 7_2 result = ${Day7_2.run(File("input/day_7.txt").readLines()[0].split(',').map { it.toInt() })}")
}