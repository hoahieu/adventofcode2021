package com.hieulh.demo.aoc2021

import java.io.File
import java.math.BigDecimal


object Day16 {
    fun run1(data: List<Int>) {
        val packetList = readData(data)
        var v = 0
        val packetQueue = ArrayDeque<Packet>()
        packetQueue.addAll(packetList)
        while (packetQueue.isNotEmpty()) {
            val p = packetQueue.removeFirst()
            v += p.version
            if (p is OperatorPacket) packetQueue.addAll(p.subPackets)
        }
        println("version sum = $v")
    }

    fun run2(data: List<Int>) {
        val packetList = readData(data)
        println("Calculated value = ${packetList.first().calculated}")
    }

    private fun readData(data: List<Int>): List<Packet> {
        val binaryData = ArrayDeque(data)
        val packetList = mutableListOf<Packet>()
        while (binaryData.size > 10) {
            try {
                val packet = Packet.readData(binaryData)
                packetList.add(packet)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return packetList
    }
}

abstract class Packet(
    val version: Int,
    val type: Int, // 4 is literal, the rest is operator
    val data: ArrayDeque<Int>,
) {
    companion object {
        const val TYPE_LITERAL = 4

        fun readData(data: ArrayDeque<Int>): Packet {
            val version = data.takeFirstAndRemove(3).getBinaryValue()
            val type = data.takeFirstAndRemove(3).getBinaryValue()
            return when (type) {
                4 -> LiteralPacket.readData(version, type, data)
                else -> OperatorPacket.readData(version, type, data)
            }
        }
    }

    val parentPacket: Packet? = null

    val typeString: String
        get() = when (type) {
            0 -> "sum"
            1 -> "product"
            2 -> "minimum"
            3 -> "maximum"
            4 -> "literal"
            5 -> "greater than"
            6 -> "less than"
            7 -> "equal to"
            else -> "unknown"
        }

    abstract val calculated: BigDecimal
}

class LiteralPacket(
    version: Int,
    type: Int,
    private val literals: List<Int>
) : Packet(version, type, ArrayDeque()) {
    companion object {
        fun readData(version: Int, type: Int, binaryData: ArrayDeque<Int>): LiteralPacket {
            val literals = mutableListOf<Int>()
            var shouldStop = false
            while (!shouldStop) {
                if (binaryData.size >= 4) {
                    val isLastLiteral = binaryData.takeFirstAndRemove() == 0 //
                    literals.add(binaryData.takeFirstAndRemove(4).getBinaryValue())
                    shouldStop = isLastLiteral
                } else {
                    shouldStop = true
                }

            }
            return LiteralPacket(version, type, literals)
        }
    }

//    fun getTotalValue(): BigDecimal {
//        var total = BigDecimal.ZERO
//        val size = literals.size
//        literals.forEachIndexed { index, num ->
//            total += BigDecimal(num)*(BigDecimal(16).pow(size - index - 1))
//        }
//        return total
//    }

    private fun getTotalValue(): BigDecimal =
        literals.foldRightIndexed(BigDecimal.ZERO) { index: Int, num: Int, acc: BigDecimal ->
            acc + BigDecimal(num) * (BigDecimal(16).pow(literals.size - index - 1))
        }

    override val calculated: BigDecimal
        get() = getTotalValue()
}

class TotalLengthPacket(
    version: Int,
    type: Int,
    val totalLength: Int,
    data: ArrayDeque<Int>
) : OperatorPacket(
    version, type, data, lengthType = LENGTH_TYPE_TOTAL_LENGTH
) {
    companion object {
        fun readData(version: Int, type: Int, binaryData: ArrayDeque<Int>): TotalLengthPacket {
            val totalLength = binaryData.takeFirstAndRemove(15).getBinaryValue()
            val remainingBinary = ArrayDeque(binaryData.takeFirstAndRemove(totalLength))
            val subPackets = mutableListOf<Packet>()
            while (remainingBinary.size > 6) {
                try {
                    val packet = Packet.readData(remainingBinary)
                    subPackets.add(packet)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return TotalLengthPacket(
                version,
                type,
                totalLength,
                ArrayDeque()
            ).apply {
                this.subPackets.addAll(subPackets)
            }
        }
    }
}

class CounterPacket(
    version: Int,
    type: Int,
    val count: Int,
    data: ArrayDeque<Int>
) : OperatorPacket(
    version, type, data, lengthType = LENGTH_TYPE_COUNT
) {
    companion object {
        fun readData(version: Int, type: Int, binaryData: ArrayDeque<Int>): CounterPacket {
            val count = binaryData.takeFirstAndRemove(11).getBinaryValue()
            val packets = mutableListOf<Packet>()
            for (i in 0 until count) {
                try {
                    if (binaryData.size >= 6) {
                        val packet = Packet.readData(binaryData)
                        packets.add(packet)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            return CounterPacket(
                version,
                type,
                count,
                ArrayDeque()
            ).apply {
                this.subPackets.addAll(packets)
            }
        }
    }
}

open class OperatorPacket(
    version: Int,
    type: Int,
    data: ArrayDeque<Int>,
    val lengthType: Int
) : Packet(version, type, data) {
    companion object {
        const val LENGTH_TYPE_TOTAL_LENGTH = 0
        const val LENGTH_TYPE_COUNT = 1

        fun readData(version: Int, type: Int, binaryData: ArrayDeque<Int>): OperatorPacket {
            return when (val lengthType = binaryData.takeFirstAndRemove(1).getBinaryValue()) {
                LENGTH_TYPE_TOTAL_LENGTH -> TotalLengthPacket.readData(version, type, binaryData)
                LENGTH_TYPE_COUNT -> CounterPacket.readData(version, type, binaryData)
                else -> throw UnsupportedOperationException("Unsupported lengthType $lengthType")
            }
        }
    }

    val subPackets = mutableListOf<Packet>()

    /*
    type ID 0 are sum packets - their value is the sum of the values of their sub-packets. If they only have a single sub-packet, their value is the value of the sub-packet.
    type ID 1 are product packets - their value is the result of multiplying together the values of their sub-packets. If they only have a single sub-packet, their value is the value of the sub-packet.
    type ID 2 are minimum packets - their value is the minimum of the values of their sub-packets.
    type ID 3 are maximum packets - their value is the maximum of the values of their sub-packets.
    type ID 5 are greater than packets - their value is 1 if the value of the first sub-packet is greater than the value of the second sub-packet; otherwise, their value is 0. These packets always have exactly two sub-packets.
    type ID 6 are less than packets - their value is 1 if the value of the first sub-packet is less than the value of the second sub-packet; otherwise, their value is 0. These packets always have exactly two sub-packets.
    type ID 7 are equal to packets - their value is 1 if the value of the first sub-packet is equal to the value of the second sub-packet; otherwise, their value is 0. These packets always have exactly two sub-packets.
    */
    override val calculated: BigDecimal
        get() = when (type) {
            0 -> {
                if (subPackets.size == 1) subPackets.first().calculated
                else subPackets.fold(BigDecimal.ZERO) { acc: BigDecimal, packet: Packet -> acc + packet.calculated }
            }
            1 -> {
                if (subPackets.size == 1) subPackets.first().calculated
                else subPackets.fold(BigDecimal.ONE) { acc: BigDecimal, packet: Packet -> acc * packet.calculated }
            }
            2 -> subPackets.map { it.calculated }.minOrNull() ?: throw Exception("no subpacket")
            3 -> subPackets.map { it.calculated }.maxOrNull() ?: throw Exception("no subpacket")
            5 -> when {
                subPackets.size != 2 -> throw Exception("type ID 5 expects 2 subpackets")
                subPackets[0].calculated > subPackets[1].calculated -> BigDecimal(1)
                else -> BigDecimal.ZERO
            }
            6 -> when {
                subPackets.size != 2 -> throw Exception("type ID 6 expects 2 subpackets")
                subPackets[0].calculated < subPackets[1].calculated -> BigDecimal.ONE
                else -> BigDecimal.ZERO
            }
            7 -> when {
                subPackets.size != 2 -> throw Exception("type ID 5 expects 2 subpackets")
                subPackets[0].calculated == subPackets[1].calculated -> BigDecimal.ONE
                else -> BigDecimal.ZERO
            }
            else -> throw UnsupportedOperationException("No type ID $type")
        }
}

fun convertHexToInt(hexString: String): List<Int> {
    val data = mutableListOf<Int>()
    hexString.uppercase().forEach {
        when (it) {
            '0' -> data.addAll(listOf(0, 0, 0, 0))
            '1' -> data.addAll(listOf(0, 0, 0, 1))
            '2' -> data.addAll(listOf(0, 0, 1, 0))
            '3' -> data.addAll(listOf(0, 0, 1, 1))
            '4' -> data.addAll(listOf(0, 1, 0, 0))
            '5' -> data.addAll(listOf(0, 1, 0, 1))
            '6' -> data.addAll(listOf(0, 1, 1, 0))
            '7' -> data.addAll(listOf(0, 1, 1, 1))
            '8' -> data.addAll(listOf(1, 0, 0, 0))
            '9' -> data.addAll(listOf(1, 0, 0, 1))
            'A' -> data.addAll(listOf(1, 0, 1, 0))
            'B' -> data.addAll(listOf(1, 0, 1, 1))
            'C' -> data.addAll(listOf(1, 1, 0, 0))
            'D' -> data.addAll(listOf(1, 1, 0, 1))
            'E' -> data.addAll(listOf(1, 1, 1, 0))
            'F' -> data.addAll(listOf(1, 1, 1, 1))
            else -> throw NullPointerException("")
        }
    }
    return data
}

fun printBinary(list: List<Int>) {
    println(list.joinToString("") { it.toString() })
}

fun List<Int>.getBinaryValue(): Int {
    return this.foldRightIndexed(0) { index: Int, i: Int, acc: Int ->
        if (i != 0 && i != 1) throw IllegalArgumentException("Only 0 and 1 accepted")
        acc + i.shl(size - index - 1)
    }
}

fun ArrayDeque<Int>.takeFirstAndRemove(): Int {
    val head = this.first()
    val tail = this.takeLast(this.size - 1)
    this.clear()
    this.addAll(tail)
    return head
}

fun ArrayDeque<Int>.takeFirstAndRemove(count: Int): List<Int> {
    val head = this.take(count)
    val tail = this.takeLast(this.size - count)
    this.clear()
    this.addAll(tail)
    return head
}


fun main() {
//    Day16.run1(convertHexToInt("8A004A801A8002F478"))
//    Day16.run1(convertHexToInt("620080001611562C8802118E34"))
//    Day16.run1(convertHexToInt("C0015000016115A2E0802F182340"))
//    Day16.run1(convertHexToInt("A0016C880162017C3686B18A3D4780"))

    println("Challenge 1 Result")
    Day16.run1(convertHexToInt(File("input/day_16.txt").readLines()[0]))

//    Day16.run2(convertHexToInt("C200B40A82"))
//    Day16.run2(convertHexToInt("04005AC33890"))
//    Day16.run2(convertHexToInt("880086C3E88112"))
//    Day16.run2(convertHexToInt("CE00C43D881120"))
//    Day16.run2(convertHexToInt("D8005AC2A8F0"))
//    Day16.run2(convertHexToInt("F600BC2D8F"))
//    Day16.run2(convertHexToInt("9C005AC2F8F0"))
//    Day16.run2(convertHexToInt("9C0141080250320F1802104A08"))

    println("Challenge 2 Result")
    Day16.run2(convertHexToInt(File("input/day_16.txt").readLines()[0]))

}