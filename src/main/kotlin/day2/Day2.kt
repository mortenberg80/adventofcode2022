package day2

import java.lang.IllegalArgumentException

class Day2 {
    fun getGameStore(filename: String): GameStore {
        val readLines = this::class.java.getResourceAsStream(filename).bufferedReader().readLines()

        return GameStore(
            readLines.map { Round(Shape.fromString(it[0]), Shape.fromString(it[2])) },
            readLines.map { Round(Shape.fromString(it[0]), Shape.fromStringv2(Shape.fromString(it[0]), it[2])) }
        )
    }
}

sealed class Shape {
    abstract fun score(): Int
    abstract fun winShape(): Shape
    abstract fun drawShape(): Shape
    abstract fun loseShape(): Shape

    object Rock: Shape() {
        override fun score(): Int = 1
        override fun winShape(): Shape = Paper
        override fun drawShape(): Shape = Rock
        override fun loseShape(): Shape = Scissors
    }
    object Paper: Shape() {
        override fun score(): Int = 2
        override fun winShape(): Shape = Scissors
        override fun drawShape(): Shape = Paper
        override fun loseShape(): Shape = Rock
    }
    object Scissors: Shape() {
        override fun score(): Int = 3
        override fun winShape(): Shape = Rock
        override fun drawShape(): Shape = Scissors
        override fun loseShape(): Shape = Paper
    }

    companion object {
        fun fromString(input: Char): Shape {
            return when(input) {
                'A' -> Rock
                'B' -> Paper
                'C' -> Scissors
                'Y' -> Paper
                'X' -> Rock
                'Z' -> Scissors
                else -> throw IllegalArgumentException("Unknown shape $input")
            }
        }

        fun fromStringv2(opponent: Shape, input: Char): Shape {
            return when(input) {
                'Y' -> opponent.drawShape()
                'X' -> opponent.loseShape()
                'Z' -> opponent.winShape()
                else -> throw IllegalArgumentException("Unknown shape $input")
            }
        }
    }
}

class Round(private val opponent: Shape, private val you: Shape) {
    override fun toString(): String {
        return "$opponent vs $you"
    }

    private fun shapeScore(): Int = you.score()

    private fun roundScore(): Int {
        return when(opponent) {
            Shape.Paper -> when(you) {
                Shape.Paper -> 3
                Shape.Rock -> 0
                Shape.Scissors -> 6
            }
            Shape.Rock -> when(you) {
                Shape.Paper -> 6
                Shape.Rock -> 3
                Shape.Scissors -> 0
            }
            Shape.Scissors -> when(you) {
                Shape.Paper -> 0
                Shape.Rock -> 6
                Shape.Scissors -> 3
            }
        }
    }

    fun score(): Int = roundScore() + shapeScore()
}

class GameStore(val roundList: List<Round>, val roundListPart2: List<Round>) {

    fun score(): Int = roundList.sumOf { it.score() }
    fun scorePart2(): Int = roundListPart2.sumOf { it.score() }
}

fun main() {
    println(Day2().getGameStore("/day2_test.txt").score())
    println(Day2().getGameStore("/day2_input.txt").score())

    println(Day2().getGameStore("/day2_test.txt").scorePart2())
    println(Day2().getGameStore("/day2_input.txt").scorePart2())
}
