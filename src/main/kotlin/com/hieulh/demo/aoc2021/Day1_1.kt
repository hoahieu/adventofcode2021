package com.hieulh.demo.aoc2021

import java.io.File

fun List<Int>.countIncreased(): Int {
    return filterIndexed { index, number ->
        index != 0 && number > get(index - 1)
    }
        .count()

}

object Day1_1 {
    fun run(filePath: String) {
        val increaseCount = File(filePath).readLines()
            .map { it.toInt() }
            .countIncreased()
        println("Day 1_1 = $increaseCount")
    }
}



fun main() {
    Day1_1.run("input/day_1_1.txt")
}