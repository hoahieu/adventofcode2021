package com.hieulh.demo.aoc2021

import java.io.File

object Day3_2 {
    fun run(lines: List<String>) {
        var oxygenIndexSet = hashSetOf<Int>()
        var co2IndexSet = hashSetOf<Int>()
        var oxygen: Int = 0
        var co2: Int = 0
        val data = lines.mapIndexed { index, bit ->
            oxygenIndexSet.add(index)
            co2IndexSet.add(index)
            lineToBitIntArray(bit)
        }
        val bitLength = data.first().size

        for (i in 0 until bitLength) {
            val zeroes = hashSetOf<Int>()
            val ones = hashSetOf<Int>()
            oxygenIndexSet.forEach { numberIndex ->
                if (data[numberIndex][i] == 1) ones.add(numberIndex)
                else zeroes.add(numberIndex)
            }
            if (ones.size >= zeroes.size) {
                oxygenIndexSet = ones
            } else {
                oxygenIndexSet = zeroes
            }
            println("Day 3_2 oxygen step $i size = ${oxygenIndexSet.size}")
        }

        oxygenIndexSet.forEach {
            oxygen = rawRateToInt(data[it])
            println("Day 3_2 result oxygen = $oxygen")
        }

        for (i in 0 until bitLength) {
            val zeroes = hashSetOf<Int>()
            val ones = hashSetOf<Int>()
            if(co2IndexSet.size >1) {
                co2IndexSet.forEach { numberIndex ->
                    if (data[numberIndex][i] == 1) ones.add(numberIndex)
                    else zeroes.add(numberIndex)
                }
                if(zeroes.size <= ones.size) {
                    co2IndexSet = zeroes
                } else {
                    co2IndexSet = ones
                }
            }
            println("Day 3_2 co2 step $i size = ${co2IndexSet.size}")
        }

        co2IndexSet.forEach {
            co2 = rawRateToInt(data[it])
            println("Day 3_2 result co2 = $co2")
        }

        println("Day 3_2 final result ${co2 * oxygen}")
    }
}

fun List<Boolean>.booleanArrayToBitString() = map { if (it) "1" else "0" }.fold("") { acc: String, s: String ->
    acc + s
}

fun main() {
    Day3_2.run(File("input/day_3.txt").readLines())
//    Day3_2.run(
//        listOf(
//            "00100",
//            "11110",
//            "10110",
//            "10111",
//            "10101",
//            "01111",
//            "00111",
//            "11100",
//            "10000",
//            "11001",
//            "00010",
//            "01010"
//        )
//    )
}

