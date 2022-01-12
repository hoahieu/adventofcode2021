package com.hieulh.demo.aoc2021


object Day16 {
    fun run1(data: List<Int>) {
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
        packetList
    }

    fun readData(data: ArrayDeque<Int>): Packet {
        val version = data.takeFirstAndRemove(3).getBinaryValue()
        val type = data.takeFirstAndRemove(3).getBinaryValue()
        println("Version = $version")
        println("Type = $type")
        printBinary(data)
        return if (type == 4) {
            LiteralPacket.readData(version, type, data)
        } else {
            val lengthType = data.takeFirstAndRemove(1).getBinaryValue()
            OperatorPacket.readData(version, type, data)
        }
    }
}

open class Packet(
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
}

class LiteralPacket(
    version: Int,
    type: Int,
    val literals: List<Int>
) : Packet(version, type, ArrayDeque()) {
    companion object {
        fun readData(version: Int, type: Int, binaryData: ArrayDeque<Int>): LiteralPacket {
            val literals = mutableListOf<Int>()
            var shouldStop = false
            while (!shouldStop) {
                val isLastLiteral = binaryData.takeFirstAndRemove() == 1
                literals.add(binaryData.takeFirstAndRemove(4).getBinaryValue())
                shouldStop = isLastLiteral
            }
            return LiteralPacket(version, type, literals)
        }
    }
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
                    val packet = Packet.readData(binaryData)
                    packets.add(packet)
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
    val data = convertHexToInt(sample16_3)
    printBinary(data)

    Day16.run1(data)
}

val sample16_1 = "8A004A801A8002F478"
val sample16_2 = "620080001611562C8802118E34"
val sample16_3 = "C0015000016115A2E0802F182340"
val sample16_4 = "A0016C880162017C3686B18A3D4780"
