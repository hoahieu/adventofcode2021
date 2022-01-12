package com.hieulh.demo.aoc2021

import java.io.File

object Day6_2 {
    fun run(initState: List<Int>, days: Int): Long {
        val fishPool = Array<Long>(9) { 0 }
        initState.forEach { fishPool[it]++ }
        proceed(fishPool, days)
        return fishPool.fold(0L) { acc: Long, l: Long -> acc + l }
    }

    private fun proceed(fishPool: Array<Long>, days: Int) {
        for (i in 0 until days) {
            proceed(fishPool)
        }
    }

    private fun proceed(fishPool: Array<Long>) {
        val newFish = fishPool[0]
        fishPool[0] = fishPool[1]
        fishPool[1] = fishPool[2]
        fishPool[2] = fishPool[3]
        fishPool[3] = fishPool[4]
        fishPool[4] = fishPool[5]
        fishPool[5] = fishPool[6]
        fishPool[6] = fishPool[7] + newFish
        fishPool[7] = fishPool[8]
        fishPool[8] = newFish
    }
}

fun main() {
    val input = File("input/day_6.txt")
    val days = 256
    val initState = input.readLines()[0].split(',').map { it.toInt() }
    println("Result = ${Day6_2.run(initState, days)}")
}