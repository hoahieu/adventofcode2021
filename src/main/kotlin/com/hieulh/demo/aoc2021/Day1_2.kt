package com.hieulh.demo.aoc2021

import java.io.File

object Day1_2 {
    fun run(filePath: String) {
        val depthMeasurement = File(filePath).readLines().map { it.toInt() }
        val maxLength = depthMeasurement.size
        val processedDepthMeasurement = mutableListOf<Int>()
        depthMeasurement.forEachIndexed { index, number ->
            if (index < maxLength - 2) {
                processedDepthMeasurement.add(depthMeasurement[index] + depthMeasurement[index + 1] + depthMeasurement[index + 2])
            }
        }
        println("Day 1_2 = ${processedDepthMeasurement.countIncreased()}")
    }
}

fun main() {
    Day1_2.run("input/day_1_1.txt")
}