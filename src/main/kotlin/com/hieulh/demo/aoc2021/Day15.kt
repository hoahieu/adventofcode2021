package com.hieulh.demo.aoc2021

import java.io.File

object Day15 {
    fun run2(topo: Array<Array<Node>>) {
        val width = topo[0].size
        val height = topo.size
        val newTopo = Array(height * 5) { y ->
            Array(width * 5) { x ->
                val org = topo[y.mod(height)][x.mod(width)].w
                val delta = y.div(height) + x.div(width)
                val newValue = (org + delta - 1).mod(9) + 1
                if(x == 0) print(newValue.toString())
                Node(x, y, newValue)
            }
        }
        run1(newTopo)
    }

    fun run1(topo: Array<Array<Node>>) {
        val width = topo[0].size
        val height = topo.size

        val startingNode = topo[0][0]
        startingNode.accW = 0
        topo[1][0].accW = topo[1][0].w
        topo[0][1].accW = topo[0][1].w
        val endNode = topo[height - 1][width - 1]

        val nodePairQueue = ArrayDeque<Pair<Node, Node>>()

        // 5 pass?

        for (i in 1..10) {
            println("Iteration $i")
            for (y in 0 until height) {
                for (x in 0 until width) {
                    if (y == height - 1 && x == width - 1) {
                        // end node
                    } else {
                        val node = topo[y][x]
                        node.getNeighbourNodes(topo).forEach {
                            nodePairQueue.add(node to it)
                        }
                    }
                }
            }
            while (nodePairQueue.isNotEmpty()) {
                val nodePair = nodePairQueue.removeFirst()
                val previousNode = nodePair.first
                val currentNode = nodePair.second
                when {
                    previousNode.accW == null -> {
                        nodePairQueue.addLast(Pair(previousNode, currentNode))
                    }
                    currentNode.accW == null -> {
                        currentNode.accW = previousNode.accW!! + currentNode.w
                        currentNode.previousNode = previousNode
                    }
                    currentNode.accW!! > previousNode.accW!! + currentNode.w -> {
                        currentNode.previousNode = previousNode
                        currentNode.accW = previousNode.accW!! + currentNode.w
                    }
                    else -> {
                        // do nothing
                    }
                }
            }
        }

        val nodePath = getNodePath(topo[height - 1][width - 1])
        println(
            "Path = ${
                nodePath.joinToString(",") {
                    it.w.toString()
                }
            }"
        )
        println("Sum = ${topo[height - 1][width - 1].accW}")
    }

    fun getNodeFromRawData(lines: List<String>): Array<Array<Node>> {
        val height = lines.size
        val width = lines[0].length
        return Array(height) { y ->
            Array(width) { x ->
                Node(x, y, lines[y][x].digitToInt())
            }
        }
    }

    fun getNodePath(node: Node): List<Node> {
        val nodes = ArrayDeque<Node>()
        var currentNode: Node? = node

        while (currentNode != null) {
            nodes.addFirst(currentNode)
            currentNode = currentNode.previousNode
        }
        return nodes
    }
}

fun printNodes(topo: Array<Array<Node>>) {
    topo.joinToString("") {
        it.joinToString("") {
            it.w.toString()
        }
    }
}

fun Node.getNeighbourNodes(topo: Array<Array<Node>>): List<Node> {
    return listOfNotNull(
        topo.getOrNull(y)?.getOrNull(x - 1),
        topo.getOrNull(y)?.getOrNull(x + 1),
        topo.getOrNull(y - 1)?.getOrNull(x),
        topo.getOrNull(y + 1)?.getOrNull(x),
    )
}

data class Node(
    val x: Int,
    val y: Int,
    val w: Int
) {
    var accW: Int? = null
    var previousNode: Node? = null
}

fun main() {
    val sampleTopo1 = Day15.getNodeFromRawData(sample15)
    Day15.run1(sampleTopo1)

    val challengeTopo1 = Day15.getNodeFromRawData(challenge15)
    Day15.run1(challengeTopo1)

    val sampleTopo2 = Day15.getNodeFromRawData(sample15)
    Day15.run2(sampleTopo2)

    val challengeTopo2 = Day15.getNodeFromRawData(challenge15)
    Day15.run2(challengeTopo2)

}

val sample15 = listOf<String>(
    "1163751742",
    "1381373672",
    "2136511328",
    "3694931569",
    "7463417111",
    "1319128137",
    "1359912421",
    "3125421639",
    "1293138521",
    "2311944581",
)

val challenge15 = File("input/day_15.txt").readLines().filterNot { it.isNullOrBlank() }