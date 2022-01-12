package com.hieulh.demo.aoc2021

import java.io.File

object Day10_1 {
    fun run(lines: List<String>): Int {
        val validators = lines.map { Validator(it) }
        validators.forEach { it.validate() }
        val result = validators
            .mapNotNull { it.invalidChar }
            .map {
                when (it) {
                    ')' -> 3
                    ']' -> 57
                    '}' -> 1197
                    '>' -> 25137
                    else -> 0
                }
            }
            .fold(0) { acc: Int, i: Int ->
                acc+i
            }
        return result
    }
}

object Day10_2 {
    fun run(lines: List<String>): Long {
        val validators = lines.map { Validator(it) }
        validators.forEach { it.validate() }
        val completionPoints = validators.filter { it.isIncomplete == true }
            .mapNotNull {
                it.getCompletionPoint()
            }
            .sorted()

        val size = completionPoints.size
        return completionPoints[size/2]
    }
}


//If a chunk opens with (, it must close with ).
//If a chunk opens with [, it must close with ].
//If a chunk opens with {, it must close with }.
//If a chunk opens with <, it must close with >.

data class Validator(val input: String) {
    var invalidChar: Char? = null
    var isIncomplete: Boolean? = null

    val openingChars = listOf('(', '[', '{', '<')
    val closingChars = listOf(')', ']', '}', '>')
    val closingMap = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
    var charDeque : ArrayDeque<Char>? = null

    fun validate(): Boolean {
        val deque = ArrayDeque<Char>()
        for (char in input) {
            when {
                openingChars.contains(char) -> {
                    deque.addLast(char)
                }
                deque.isEmpty() -> {
                    invalidChar = char
                    break
                }
                closingMap[deque.last()] == char -> {
                    deque.removeLast()
                }
                else -> {
                    invalidChar = char
                    break
                }
            }
        }

        if (invalidChar == null && deque.size >= 0) {
            isIncomplete = true
            charDeque = deque
        }
        return invalidChar == null
    }

    fun getCompletionPoint(): Long? {
        if(isIncomplete != true || charDeque == null) return null
        val completion = charDeque!!
            .reversed()
            .map {
                closingMap[it] !!
            }
        val point = completion.fold(0L) { acc: Long, c: Char ->
            acc * 5 + when(c) {
                ')' -> 1
                ']' -> 2
                '}' -> 3
                '>' -> 4
                else -> 0
            }
        }

        return point
    }
}

fun main() {
    val sample1 = listOf(
        "[({(<(())[]>[[{[]{<()<>>",
        "[(()[<>])]({[<{<<[]>>(",
        "{([(<{}[<>[]}>{[]{[(<()>",
        "(((({<>}<{<{<>}{[]{[]{}",
        "[[<[([]))<([[{}[[()]]]",
        "[{[{({}]{}}([{[{{{}}([]",
        "{<[[]]>}<{[{[{[]{()[[[]",
        "[<(<(<(<{}))><([]([]()",
        "<{([([[(<>()){}]>(<<{{",
        "<{([{{}}[<[[[<>{}]]]>[]]",
    )
    println("Day 10 part 1 sample = ${Day10_1.run(sample1)}")
    println("Day 10 part 1 result = ${Day10_1.run(File("input/day_10.txt").readLines())}")

    println("Day 10 part 2 sample = ${Day10_2.run(sample1)}")
    println("Day 10 part 2 result = ${Day10_2.run(File("input/day_10.txt").readLines())}")
}