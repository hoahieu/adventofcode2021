package com.hieulh.demo.aoc2021

import java.io.File

object Day11_1 {
    fun run(octoMap: OctoMap, step: Int): Int {
//        println("day 0")
//        octoMap.print()
        for (i in 1 .. step) {
            octoMap.proceed()
//            println("day $i")
//            octoMap.print()
        }

        return octoMap.flashCount
    }
}

object Day11_2 {
    fun run(octoMap: OctoMap): Int {
        var dayAfter = 0
        println("Octomap day $dayAfter")
        octoMap.print()
        while(!octoMap.isBrightAsFuck()) {
            dayAfter++
            octoMap.proceed()
//            println("Octomap day $dayAfter")
            if(octoMap.isBrightAsFuck()) {
                println("Octomap is bright as f")
//                octoMap.print()
                break
            }
        }
        return dayAfter
    }
}


data class Octopus(val x: Int, val y: Int, var energy: Int) {
    var onFlashedListener: ((Octopus) -> Unit)? = null
    fun increase() {
        when {
            energy == 9 -> {
                energy = 0
                onFlashedListener?.invoke(this)
            }
            energy > 0 -> {
                energy++
            }
            else -> {
                // do nothing
            }
        }
    }

    fun proceed() {
        if (energy == 0) energy++
        else increase()
    }
}

data class OctoMap(val width: Int, val height: Int, val topo: Array<Array<Octopus>>) {
    var flashCount: Int = 0
    val increaseQueue = ArrayDeque<Octopus>()
    private val octoFlashListener: ((Octopus) -> Unit) = {
        getAdjacentPoints(it).forEach { nearbyOctopus ->
            increaseQueue.add(nearbyOctopus)
        }
        flashCount++
    }

    init {
        topo.forEach { row ->
            row.forEach { octopus ->
                octopus.onFlashedListener = octoFlashListener
            }
        }
    }

    fun proceed() {
        topo.forEach {
            it.forEach {
                it.proceed()
            }
        }

        while(increaseQueue.isNotEmpty()) {
            increaseQueue.removeLast().increase()
        }
    }

    fun print() {
        topo.forEach {
            println(it.joinToString("") { o ->
                o.energy.toString()
            })
        }
    }

    fun getAdjacentPoints(octopus: Octopus): List<Octopus> {
        return listOfNotNull(
            topo.getOrNull(octopus.y - 1)?.getOrNull(octopus.x),
            topo.getOrNull(octopus.y + 1)?.getOrNull(octopus.x),
            topo.getOrNull(octopus.y)?.getOrNull(octopus.x - 1),
            topo.getOrNull(octopus.y)?.getOrNull(octopus.x + 1),
            topo.getOrNull(octopus.y - 1)?.getOrNull(octopus.x - 1),
            topo.getOrNull(octopus.y + 1)?.getOrNull(octopus.x - 1),
            topo.getOrNull(octopus.y - 1)?.getOrNull(octopus.x + 1),
            topo.getOrNull(octopus.y + 1)?.getOrNull(octopus.x + 1),
        )
    }

    fun isBrightAsFuck(): Boolean {
        return topo.fold(mutableListOf<Octopus>()) { acc: MutableList<Octopus>, octopuses: Array<Octopus> ->
            acc.addAll(octopuses)
            acc
        }
            .all { it.energy == 0 }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OctoMap

        if (width != other.width) return false
        if (height != other.height) return false
        if (!topo.contentDeepEquals(other.topo)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        result = 31 * result + topo.contentDeepHashCode()
        return result
    }
}

fun convertLinesToOcto(lines: List<String>): OctoMap {
    val height = lines.size
    val width = lines[0].length
    val octopus = Array(height) { y ->
        Array(width) { x ->
            Octopus(x, y, lines[y][x].digitToInt())
        }
    }

    return OctoMap(
        width,
        height,
        octopus
    )
}

fun main() {
    val sample1 = listOf(
        "5483143223",
        "2745854711",
        "5264556173",
        "6141336146",
        "6357385478",
        "4167524645",
        "2176841721",
        "6882881134",
        "4846848554",
        "5283751526"
    )
    val result = Day11_1.run(convertLinesToOcto(sample1), 100)
    println("Result sample 11_1 = $result")

    val input1 = convertLinesToOcto(File("input/day_11.txt").readLines())
    println("Result day 11_1 = ${Day11_1.run(input1, 100)}")

    println("Result sample 11_2 =${Day11_2.run(convertLinesToOcto(sample1))}")

    val result2 = Day11_2.run(convertLinesToOcto(File("input/day_11.txt").readLines()))
    println("Result day 11_1 = $result2")
}