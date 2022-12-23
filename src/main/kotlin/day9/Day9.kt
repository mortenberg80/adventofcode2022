package day9

import java.lang.IllegalArgumentException

class Day9(val input: List<String>) {

    private val board = Board(Point(0, 0), Point(0, 0), Point(0, 0), mapOf())
    private val rope = (1..10).map { Point(0, 0) }.toMutableList()
    private val board2 = Board2(Point(0, 0), rope, mutableMapOf())

    private fun toCommandList(input: String): List<Command> {
        val direction = input.split(" ")[0]
        val repetitions = input.split(" ")[1].toInt()
        return when (direction) {
            "U" -> (1..repetitions).map { Command.Up(1) }
            "D" -> (1..repetitions).map { Command.Down(1) }
            "L" -> (1..repetitions).map { Command.Left(1) }
            "R" -> (1..repetitions).map { Command.Right(1) }
            else -> {
                throw IllegalArgumentException()
            }
        }
    }

    fun part1(): Int {
        val resultingBoard = input.flatMap { toCommandList(it) }
            .fold(board) { board, command -> board.handleCommand(command) }
        return resultingBoard.tailVisitationMap.size
    }

    fun part2(): Int {
        val resultingBoard = input.flatMap { toCommandList(it) }
            .fold(board2) { board2, command -> board2.handleCommand(command) }
        return resultingBoard.tailVisitationMap.size
    }
}

data class Board(val start: Point, val head: Point, val tail: Point, val tailVisitationMap: Map<Point, Int>) {
    fun handleCommand(command: Command): Board {
        when (command) {
            is Command.Down -> {
                val newHead = Point(head.x, head.y - 1)
                val newTail = calculateTail(newHead, tail)
                val count = tailVisitationMap[newTail] ?: 0
                return this.copy(
                    head = newHead,
                    tail = newTail,
                    tailVisitationMap = tailVisitationMap + (newTail to count + 1)
                )
            }

            is Command.Left -> {
                val newHead = Point(head.x - 1, head.y)
                val newTail = calculateTail(newHead, tail)
                val count = tailVisitationMap[newTail] ?: 0
                return this.copy(
                    head = newHead,
                    tail = newTail,
                    tailVisitationMap = tailVisitationMap + (newTail to count + 1)
                )
            }

            is Command.Right -> {
                val newHead = Point(head.x + 1, head.y)
                val newTail = calculateTail(newHead, tail)
                val count = tailVisitationMap[newTail] ?: 0
                return this.copy(
                    head = newHead,
                    tail = newTail,
                    tailVisitationMap = tailVisitationMap + (newTail to count + 1)
                )
            }

            is Command.Up -> {
                val newHead = Point(head.x, head.y + 1)
                val newTail = calculateTail(newHead, tail)
                val count = tailVisitationMap[newTail] ?: 0
                return this.copy(
                    head = newHead,
                    tail = newTail,
                    tailVisitationMap = tailVisitationMap + (newTail to count + 1)
                )
            }
        }
    }
}


data class Board2(val start: Point, val points: MutableList<Point>, val tailVisitationMap: MutableMap<Point, Int>) {

    fun handleCommand(command: Command): Board2 {
        when (command) {
            is Command.Down -> {
                points[0] = Point(points[0].x, points[0].y - 1)
                calculate()
                return this
            }

            is Command.Left -> {
                points[0] = Point(points[0].x - 1, points[0].y)
                calculate()
                return this
            }

            is Command.Right -> {
                points[0] = Point(points[0].x + 1, points[0].y)
                calculate()
                return this
            }

            is Command.Up -> {
                points[0] = Point(points[0].x, points[0].y + 1)
                calculate()
                return this
            }
        }
    }

    private fun calculate() {
        for (i in 1 until points.size) {
            points[i] = calculateTail(points[i - 1], points[i])
        }
        tailVisitationMap.compute(points.last()) { _, oldValue -> (oldValue ?: 0) + 1 }
    }
}

private fun calculateTail(newHead: Point, existingTail: Point): Point {
    if (newHead == existingTail) return existingTail
    if (newHead.x == existingTail.x + 1 && newHead.y == existingTail.y + 1) return existingTail
    if (newHead.x == existingTail.x + 1 && newHead.y == existingTail.y - 1) return existingTail
    if (newHead.x == existingTail.x - 1 && newHead.y == existingTail.y + 1) return existingTail
    if (newHead.x == existingTail.x - 1 && newHead.y == existingTail.y - 1) return existingTail
    if (newHead.x == existingTail.x && newHead.y == existingTail.y + 1) return existingTail
    if (newHead.x == existingTail.x && newHead.y == existingTail.y - 1) return existingTail
    if (newHead.x == existingTail.x + 1 && newHead.y == existingTail.y) return existingTail
    if (newHead.x == existingTail.x - 1 && newHead.y == existingTail.y) return existingTail


    // same column
    if (newHead.x == existingTail.x) {
        // Only one space apart
        if (newHead.y == existingTail.y + 1 || newHead.y == existingTail.y - 1) {
            return existingTail
        }
        return if (newHead.y > existingTail.y) {
            Point(existingTail.x, existingTail.y + 1)
        } else {
            Point(existingTail.x, existingTail.y - 1)
        }
    }
    if (newHead.y == existingTail.y) {
        // same row
        if (newHead.x == existingTail.x + 1 || newHead.x == existingTail.x - 1) {
            return existingTail
        }
        return if (newHead.x > existingTail.x) {
            Point(existingTail.x + 1, existingTail.y)
        } else {
            Point(existingTail.x - 1, existingTail.y)
        }
    }
    // neither on same row nor column
    /*
    .....    .....
    ...H.    ...H.
    ..... -> ...T.
    ..T..    .....
    .....    .....
     */
    if (newHead.x > existingTail.x && newHead.y > existingTail.y) return Point(existingTail.x + 1, existingTail.y + 1)
    if (newHead.x < existingTail.x && newHead.y > existingTail.y) return Point(existingTail.x - 1, existingTail.y + 1)
    return if (newHead.x > existingTail.x) {
        Point(existingTail.x + 1, existingTail.y - 1)
    } else {
        Point(existingTail.x - 1, existingTail.y - 1)
    }
}

sealed class Command {
    abstract val repetitions: Int

    data class Up(override val repetitions: Int) : Command()
    data class Down(override val repetitions: Int) : Command()
    data class Left(override val repetitions: Int) : Command()
    data class Right(override val repetitions: Int) : Command()
}

data class Point(val x: Int, val y: Int)

fun main() {
    val testInput = Day9::class.java.getResourceAsStream("/day9_test.txt").bufferedReader().readLines()
    val test2Input = Day9::class.java.getResourceAsStream("/day9_test2.txt").bufferedReader().readLines()
    val input = Day9::class.java.getResourceAsStream("/day9_input.txt").bufferedReader().readLines()

    val day9test = Day9(testInput)
    val day9test2 = Day9(test2Input)
    val day9input = Day9(input)

    println("Day9 part 1 test result: ${day9test.part1()}")
    println("Day9 part 1 result: ${day9input.part1()}")
    println("Day9 part 2 test result: ${day9test2.part2()}")
    println("Day9 part 2 result: ${day9input.part2()}")
}

