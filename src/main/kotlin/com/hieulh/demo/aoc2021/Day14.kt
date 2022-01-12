package com.hieulh.demo.aoc2021

import java.io.File
import java.util.*

object Day14 {
    fun run1(rawInput: List<String>, steps: Int): Int {
        val polymer = LinkedList<Char>(rawInput[0].toList())
        val polymerMap = HashMap<Pair<Char, Char>, Char>()
        rawInput.takeLast(rawInput.size)
            .filter { it.length > 5 }
            .forEach {
//                println("${it[0]}${it[1]} -> ${it[6]}")
                polymerMap.put(it[0] to it[1], it[6])
            }
        for (i in 1..steps) {
            println("step $i")
            val orgSize = polymer.size
            for (j in orgSize - 1 downTo 1) {
                polymerMap[Pair(polymer[j-1],polymer[j])]?.let {
                    polymer.add(j, it)
                }
            }
        }
        val charMap = HashMap<Char, Int>()
        for(i in polymer) {
            charMap[i] = (charMap[i] ?:0 ) + 1
        }
        val charCount = charMap.map { it.value }
        return charCount.maxOrNull()!! - charCount.minOrNull()!!
    }
}

data class PolymerRule(val firstChar: Char, val secondChar: Char, val newChar: Char)

fun main() {
//    val sample1Result = Day14.run1(day14Sample,10)
//    println("sample1Result = $sample1Result")
//
//    val challenge1Result = Day14.run1(File("input/day_14.txt").readLines(), 10)
//    println("challenge1Result = $challenge1Result")

    val challenge2Result = Day14.run1(File("input/day_14.txt").readLines(), 14)
    println("challenge1Result = $challenge2Result")
}

val day14Sample = listOf(
    "NNCB",
    "",
    "CH -> B",
    "HH -> N",
    "CB -> H",
    "NH -> C",
    "HB -> C",
    "HC -> B",
    "HN -> C",
    "NN -> C",
    "BH -> H",
    "NC -> B",
    "NB -> B",
    "BN -> B",
    "BB -> N",
    "BC -> B",
    "CC -> N",
    "CN -> C",
)