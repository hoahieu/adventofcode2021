package com.hieulh.demo.aoc2021

import java.io.File

object Day13_1 {
    fun run(rawInput: List<String>): Int {
        val inputPairs = mutableListOf<Pair<Int, Int>>()
        val foldInstructions = mutableListOf<Pair<Char, Int>>()
        for (line in rawInput) {
            when {
                line.contains(',') -> inputPairs.add(
                    Pair(
                        line.substringBefore(',').toInt(),
                        line.substringAfter(',').toInt()
                    )
                )
                line.contains("fold") -> foldInstructions.add(
                    Pair(
                        line.substringBefore('=').last(),
                        line.substringAfter('=').toInt()
                    )
                )
            }
        }
        val width = inputPairs.map { it.first }.maxOrNull()!! + 1
        val height = inputPairs.map { it.second }.maxOrNull()!! + 1

        var matrix: Array<Array<Boolean>> = Array(height) { Array(width) { false } }
        inputPairs.forEach { matrix[it.second][it.first] = true }
        var foldTime = 1
        foldInstructions.forEach { foldInstruction ->
            val width = matrix[0].size
            val height = matrix.size

            println("Fold time = $foldTime")
            if(foldInstruction.first == 'x') {
                val newWidth = foldInstruction.second
                val newMatrix = Array(height) { y ->
                    Array(newWidth) { x ->
                        matrix[y][x]
                    }
                }
                for(y in 0 until height) {
                    for(x in foldInstruction.second+1 until width) {
                        if(matrix[y][x]) newMatrix[y][newWidth - (x - newWidth)] = true
                    }
                }
                matrix = newMatrix
            } else if(foldInstruction.first == 'y') {
                val newHeight = foldInstruction.second
                val newMatrix = Array(newHeight) { y ->
                    Array(width) { x ->
                        matrix[y][x]
                    }
                }
                for(y in foldInstruction.second+1 until height) {
                    for(x in 0 until width) {
                        if(matrix[y][x]) newMatrix[newHeight - (y - newHeight)][x] = true
                    }
                }
                matrix = newMatrix
            }
            matrix.printMap()
            val dot = matrix.fold(mutableListOf<Boolean>()) { acc, booleans ->
                acc.apply { addAll(booleans) }
            }.count { it }
            println("Dot count = $dot")
            foldTime++
        }


        return 0
    }
}

fun Array<Array<Boolean>>.printMap() {
    forEach {
        it.forEach { if(it) print("#") else print(".") }
        println()
    }
}

fun main() {
    val sample1Result = Day13_1.run(day13sample1)
    println("Sample 1 result = $sample1Result")

    val challenge1Result = Day13_1.run(day13input)
    println("Challenge 13_1 result = $challenge1Result")
}

val day13input = File("input/day_13.txt").readLines()

val day13sample1 = listOf(
    "6,10",
    "0,14",
    "9,10",
    "0,3",
    "10,4",
    "4,11",
    "6,0",
    "6,12",
    "4,1",
    "0,13",
    "10,12",
    "3,4",
    "3,0",
    "8,4",
    "1,10",
    "2,14",
    "8,10",
    "9,0",
    "",
    "fold along y=7",
    "fold along x=5",
)

