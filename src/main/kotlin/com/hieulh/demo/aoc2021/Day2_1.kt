package com.hieulh.demo.aoc2021

import java.io.File

object Day2_1 {
    fun run(lines: List<String>) {
        var x = 0L
        var y = 0L
        var aim = 0L
        val data = lines
            .map { Pair(
                it.substringBefore(' '),
                it.substringAfter(' ').toInt())
            }
            .forEach {
                when (it.first) {
                    "down" -> aim += it.second
                    "up" -> aim -= it.second
                    "forward" -> {
                        x += it.second
                        y += (aim * it.second)
                    }
                }
            }
        println("day 2 result = ${x*y}")
    }
}

fun main() {
    Day2_1.run(File("input/day_2.txt").readLines())
}