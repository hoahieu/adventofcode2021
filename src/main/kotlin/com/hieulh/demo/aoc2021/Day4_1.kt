package com.hieulh.demo.aoc2021

import java.io.File

object Day4_1 {
    fun run(filePath: String) {
        val file = File(filePath)
        var input = listOf<Int>()
        val boards = mutableListOf<Board5x5>()
        var tempBoard: Board5x5? = null
        val winningBoards = mutableListOf<Pair<Board5x5, Int>>()
        var line = 0
        val boardIndexes = mutableListOf<Int>()

        file.readLines().forEachIndexed { index, s ->
            when {
                index == 0 -> input = s.split(' ', ',').filter { it.isNotBlank() }.map { it.toInt() }
                s.trim().isBlank() -> {
                    tempBoard?.run(boards::add)
                    tempBoard = Board5x5(Array(5) {
                        Array(5) {
                            IntPair(-1)
                        }
                    })
                    line = 0
                }
                else -> {
                    val row = s.split(' ').filter { it.isNotBlank() }.map { IntPair(it.toInt()) }.toTypedArray()
                    tempBoard!!.data[line] = row
                    line++
                }
            }
        }
        println(
            input.joinToString(separator = " ")
        )

//        input.forEach { number ->
//            boards.forEach {
//                it.markNumber(number)
//                if (it.won()) {
//                    winningBoard = it
//                }
//            }
//        }

        boards.forEach {
            it.setInput(input)
        }

        for(i in input.indices) {
            boards.forEach {
                if(it.round == i) {
                    winningBoards.add(it to input[i])
                }
            }
        }

        winningBoards.first().run {
            val sum = first.data.join().unmarkedSum()
            println("Day 4_1")
            println("first winning board")
            first.print()
            println("round = ${first.round} sum = $sum, number = $second, result = ${sum * second}")
        }

        winningBoards.last().run {
            val sum = first.data.join().unmarkedSum()
            println("Day 4_2")
            println("last winning board")
            first.print()
            println("round = ${first.round} sum = $sum, number = $second, result = ${sum * second}")
        }

    }
}

data class Board5x5(var data: Array<Array<IntPair>>) {
    var wonNumber = Int.MIN_VALUE
    var round = -1
    fun setInput(input: List<Int>) {
        round = -1
        for(i in input.indices) {
            markNumber(input[i])
            round++
            if(won()) {
                wonNumber = i
                break
            }
        }
    }

    fun print() {
        data.forEach {
            println(it.joinToString(separator = " ") { intPair ->
                if (intPair.isSelected) {
                    "[${intPair.number.toString()}]"
                } else {
                    intPair.number.toString()
                }
            })
        }
    }

    fun won(): Boolean {
        for (i in 0..4) {
            // row check
            if (data[i].all { it.isSelected }) return true
            // column check
            if (data.map { it[i] }.all { it.isSelected }) return true
        }
        return false
    }

    private fun markNumber(number: Int) {
        data.forEach { row -> row.forEach { if (it.number == number) it.isSelected = true } }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Board5x5

        if (!data.contentDeepEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        return data.contentDeepHashCode()
    }
}

data class IntPair(val number: Int, var isSelected: Boolean = false)

fun Iterable<IntPair>.unmarkedSum() = filter { !it.isSelected }.fold(0) { acc: Int, intPair: IntPair ->
    acc + intPair.number
}

fun <T> Array<Array<T>>.join(): List<T> = fold(mutableListOf()) { acc: MutableList<T>, array: Array<T> ->
    acc.apply {
        addAll(array)
    }
}

fun main() {
    Day4_1.run("input/day_4.txt")

    val testBoard1 = Board5x5(
        data = arrayOf(
            arrayOf(IntPair(0), IntPair(0), IntPair(0), IntPair(0), IntPair(0, true)),
            arrayOf(IntPair(0), IntPair(0), IntPair(0), IntPair(0), IntPair(0, true)),
            arrayOf(IntPair(0), IntPair(0), IntPair(0), IntPair(0), IntPair(0, true)),
            arrayOf(IntPair(0), IntPair(0), IntPair(0), IntPair(0), IntPair(0, true)),
            arrayOf(IntPair(0), IntPair(0), IntPair(0), IntPair(0), IntPair(0, true)),
        )
    )
    println("testBoard1 won = ${testBoard1.won()}")

    val testBoard2 = Board5x5(
        data = arrayOf(
            arrayOf(IntPair(0), IntPair(0), IntPair(0), IntPair(0), IntPair(0)),
            arrayOf(IntPair(0), IntPair(0), IntPair(0), IntPair(0), IntPair(0)),
            arrayOf(IntPair(0), IntPair(0), IntPair(0), IntPair(0), IntPair(0)),
            arrayOf(IntPair(0), IntPair(0, true), IntPair(0, true), IntPair(0, true), IntPair(0, true)),
            arrayOf(IntPair(0), IntPair(0), IntPair(0), IntPair(0), IntPair(0)),
        )
    )
    println("testBoard1 won = ${testBoard2.won()}")
}