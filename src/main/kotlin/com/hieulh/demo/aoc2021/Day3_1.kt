package com.hieulh.demo.aoc2021

import java.io.File

object Day3_1 {
    fun run(lines: List<String>) {
        var signalLength = lines[0].length
        val gammaRateRaw = Array<Int>(size = signalLength, init = { 0 })
        lines.forEach {
            lineToBitArray(it).forEachIndexed { index, value ->
                if (value) {
                    gammaRateRaw[index] = gammaRateRaw[index] + 1
                } else {
                    gammaRateRaw[index] = gammaRateRaw[index] - 1
                }
            }
        }
        val gammaRate = rawRateToInt(gammaRateRaw.toList())
        val epsilonRate = rawRateToInt(flipBitArray(gammaRateRaw))
        println(gammaRateRaw.toList().reduceToBit())
        println(flipBitArray(gammaRateRaw))
        println(gammaRate)
        println(epsilonRate)
        println(gammaRate*epsilonRate)
    }


}

fun flipBitArray(array: Array<Int>): List<Int> = array.map { if (it > 0) 0 else 1 }

fun lineToBitIntArray(line: String) = line.map { if(it == '1') 1 else 0 }
fun lineToBitArray(line: String) = line.map { it == '1' }
fun Iterable<Int>.reduceToBit(): List<Int> = map { if (it > 0) 1 else 0 }
fun rawRateToInt(rawRate: Array<Int>) = rawRate.reversed()
    .reduceToBit()
    .foldIndexed(0) { index: Int, acc: Int, i: Int -> acc + i.shl(index) }

fun rawRateToInt(rawRate: Iterable<Int>) = rawRate.reversed()
    .reduceToBit()
    .foldIndexed(0) { index: Int, acc: Int, i: Int -> acc + i.shl(index) }



fun main() {
    Day3_1.run(File("input/day_3.txt").readLines())
//    print("rawRateToInt 1111 = ${rawRateToInt(arrayOf(10,0,0,0))}")
}