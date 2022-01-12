package com.hieulh.demo.aoc2021

import java.io.File
import java.lang.Exception

object Day8_1 {
    fun run(rawData: List<String>): Int {
        return rawData.map { it.substringAfter('|') }
            .map { it.split(' ') }
            .map {
                it.filter { segments ->
                    segments.length == 2 || segments.length == 3 || segments.length == 4 || segments.length == 7
                }.count()
            }
            .fold(0) { acc: Int, i: Int ->
                acc + i
            }
    }
}

object Day8_2 {
    fun run(rawData: List<String>): Long {
        return rawData.map(::getOutput)
            .fold(0L) { acc: Long, l: Long ->
                acc+l
            }
    }
}

fun getOutput(rawLine: String): Long {
    val allD = HashSet<HashSet<Char>>()
    val input = rawLine.substringBefore('|')
        .split(' ')
        .filter { it.isNotBlank() }
        .map { it.toHashSet() }
        .onEach(allD::add)
    val output = rawLine.substringAfter('|')
        .split(' ')
        .filter { it.isNotBlank() }
        .map { it.toHashSet() }
        .onEach(allD::add)

    if(allD.size != 10) throw UnsupportedOperationException("$rawLine not supported")
    val d1 = allD.first { it.size == 2 }
    allD.remove(d1)
    val d7 = allD.first { it.size == 3 }
    allD.remove(d7)
    val d4 = allD.first { it.size == 4 }
    allD.remove(d4)
    val d8 = allD.first { it.size == 7 }
    allD.remove(d8)
    val d9 = allD.first { it.size == 6 && it.containsAll(d4) }
    allD.remove(d9)
    val d0 = allD.first { it.size == 6 && it.containsAll(d7) }
    allD.remove(d0)
    val d6 = allD.first { it.size == 6 }
    allD.remove(d6)
    val d5 = allD.first { it.size == 5 && d6.containsAll(it) }
    allD.remove(d5)
    val d3 = allD.first { it.size == 5 && d9.containsAll(it) }
    allD.remove(d3)
    val d2 = allD.first()

    return output.map {
        when(it) {
            d0 -> "0"
            d1 -> "1"
            d2 -> "2"
            d3 -> "3"
            d4 -> "4"
            d5 -> "5"
            d6 -> "6"
            d7 -> "7"
            d8 -> "8"
            d9 -> "9"
            else -> ""
        }
    }
        .joinToString(separator = "")
        .toLong()
        .also {
            println(it)
        }
}

fun main() {
    val sampleData = listOf(
        "be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe",
        "edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc",
        "fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg",
        "fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb",
        "aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea",
        "fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb",
        "dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe",
        "bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef",
        "egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb",
        "gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce"
    )

//    println("Day 8 part 1 sample = ${Day8_1.run(sampleData)}")

    val inputFile = File("input/day_8.txt")
    val rawData = inputFile.readLines()
//    println("Day 8 part 1 result = ${Day8_1.run(rawData)}")
    println("Day 8 part 2 result = ${Day8_2.run(rawData)}")
//    println(getOutput("fge abefgd acdef eg fgdab adgbfc egfad dcbegf bgae bcdgfae | bfdgcea fge fegbda cgdfba"))
}