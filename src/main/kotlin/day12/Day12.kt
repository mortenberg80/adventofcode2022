package day12

import java.lang.IllegalArgumentException


class Day12(val input: List<String>) {
    
    private val inputArray = input.filterNot { it.isEmpty() }.map { it.toCharArray() }.toTypedArray()
    private lateinit var trackingArray: Array<IntArray>
    
    fun part1(): Int {
        trackingArray = createTrackingArray(inputArray.size, inputArray[0].size)

        val start = findChar(inputArray, 'S')
        return when (val result = findEnd(start.first, start.second, 0)) {
            is FindResult.FoundResult -> result.value - 1
            FindResult.NoResult -> -1
        }
    }

    private fun createTrackingArray(rows: Int, columns: Int): Array<IntArray> = Array(rows) { IntArray(columns) { -1 } }
    

    private fun findEnd(row: Int, column: Int, currentLength: Int): FindResult {
        if (inputArray[row][column] == 'E') return FindResult.FoundResult(currentLength + 1)
        if (trackingArray[row][column] != -1 && trackingArray[row][column] <= currentLength) return FindResult.NoResult
        
        trackingArray[row][column] = currentLength
        val resultUp = if (row == 0) {
            FindResult.NoResult
        } else {
            if (canNavigate(inputArray[row][column], inputArray[row-1][column])) {
                findEnd(row - 1, column, currentLength + 1)
            } else {
                FindResult.NoResult
            }
        }

        val resultDown = if (row == inputArray.size - 1) {
            FindResult.NoResult
        } else {
            if (canNavigate(inputArray[row][column], inputArray[row+1][column])) {
                findEnd(row + 1, column, currentLength + 1)
            } else {
                FindResult.NoResult
            }
        }

        val resultLeft = if (column == 0) {
            FindResult.NoResult
        } else {
            if (canNavigate(inputArray[row][column], inputArray[row][column - 1])) {
                findEnd(row , column - 1, currentLength + 1)
            } else {
                FindResult.NoResult
            }
        }

        val resultRight = if (column == inputArray[0].size - 1) {
            FindResult.NoResult
        } else {
            if (canNavigate(inputArray[row][column], inputArray[row][column + 1])) {
                findEnd(row, column + 1, currentLength + 1)
            } else {
                FindResult.NoResult
            }
        }
        
        return resultUp.combine(resultDown).combine(resultLeft).combine(resultRight)
    }

    private fun canNavigate(current: Char, neighbour: Char): Boolean {
        if (neighbour == 'E' && current != 'z') return false
        if (current == 'S') return true
        if (neighbour == 'S') return false
        if (neighbour.toInt() - current.toInt() > 1) return false   
        return true
    }

    fun part2(): Int {
        trackingArray = createTrackingArray(inputArray.size, inputArray[0].size)
        val start = findChar(inputArray, 'S')
        return when (val result = findEnd2(start.first, start.second, 0)) {
            is FindResult.FoundResult -> result.value - 1
            FindResult.NoResult -> -1
        }
    }

    private fun findEnd2(row: Int, column: Int, currentLength: Int): FindResult {
        if (inputArray[row][column] == 'E') return FindResult.FoundResult(currentLength + 1)
        val thisLength = if (inputArray[row][column] == 'a') {
            0
        } else {
            currentLength
        }
        if (trackingArray[row][column] != -1 && trackingArray[row][column] <= thisLength) return FindResult.NoResult
        
        trackingArray[row][column] = thisLength

        val resultUp = if (row == 0) {
            FindResult.NoResult
        } else {
            if (canNavigate(inputArray[row][column], inputArray[row-1][column])) {
                findEnd2(row - 1, column, thisLength + 1)
            } else {
                FindResult.NoResult
            }
        }

        val resultDown = if (row == inputArray.size - 1) {
            FindResult.NoResult
        } else {
            if (canNavigate(inputArray[row][column], inputArray[row+1][column])) {
                findEnd2(row + 1, column, thisLength + 1)
            } else {
                FindResult.NoResult
            }
        }

        val resultLeft = if (column == 0) {
            FindResult.NoResult
        } else {
            if (canNavigate(inputArray[row][column], inputArray[row][column - 1])) {
                findEnd2(row , column - 1, thisLength + 1)
            } else {
                FindResult.NoResult
            }
        }

        val resultRight = if (column == inputArray[0].size - 1) {
            FindResult.NoResult
        } else {
            if (canNavigate(inputArray[row][column], inputArray[row][column + 1])) {
                findEnd2(row, column + 1, thisLength + 1)
            } else {
                FindResult.NoResult
            }
        }

        return resultUp.combine(resultDown).combine(resultLeft).combine(resultRight)
    }
    
    fun findChar(input: Array<CharArray>, searchChar: Char): Pair<Int, Int> {
        input.forEachIndexed {
            row, columns -> columns.forEachIndexed { column, char -> if (char == searchChar) return Pair(row, column) }
        }
        throw IllegalArgumentException("Could not find start")
    }
    
    fun printArray(input: Array<CharArray>) {
        input.forEach { 
            it.forEach { char -> print(char) }
            println()
        }
    }
}

sealed class FindResult {
    object NoResult: FindResult() {
        override fun combine(other: FindResult): FindResult {
            return other
        }
    }

    data class FoundResult(val value: Int): FindResult() {
        override fun combine(other: FindResult): FindResult {
            return when (other) {
                is FoundResult -> if (this.value <= other.value) return this else return other
                NoResult -> this
            }
        }
    }

    abstract fun combine(other: FindResult): FindResult
}

fun main() {
    val testInput = Day12::class.java.getResourceAsStream("/day12_test.txt").bufferedReader().readLines()
    val input = Day12::class.java.getResourceAsStream("/day12_input.txt").bufferedReader().readLines()

    val day12test = Day12(testInput)
    val day12input = Day12(input)

    println("Day12 part 1 test result: ${day12test.part1()}")
    println("Day12 part 1 result: ${day12input.part1()}")
    println("Day12 part 2 test result: ${day12test.part2()}")
    println("Day12 part 2 result: ${day12input.part2()}")
}
