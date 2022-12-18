package day8


class Day8(val input: List<String>) {
    
    private val transposed = transpose(input)
    val inputInts = input.map { it.toCharArray().map { it.digitToInt() } }
    val transposedInts = transposed.map { it.toCharArray().map { it.digitToInt() } }
    val visibleFromLeft = input.mapIndexed { rowNo, row -> visibleFromStart(rowNo, row) }.flatten().toSet()
    val visibleFromRight = input.mapIndexed { rowNo, row -> visibleFromEnd(rowNo, row) }.flatten().toSet()
    val visibleFromTop = transposed.mapIndexed { rowNo, row -> visibleFromTop(rowNo, row) }.flatten().toSet()
    val visibleFromBottom = transposed.mapIndexed { rowNo, row -> visibleFromBottom(rowNo, row) }.flatten().toSet()

    fun visibleFromStart(rowNo: Int, row: String): Set<Pair<Int, Int>> {
        if (row.isBlank()) return setOf()
        val rowInfo = row.foldIndexed(RowInfo(rowNo, -1, setOf())) { index, rowInfo, char -> rowInfo.add(char, index) }
        return rowInfo.positions.map { Pair(rowNo, it) }.toSet()
    }    
    
    fun visibleFromEnd(rowNo: Int, row: String): Set<Pair<Int, Int>> {
        if (row.isBlank()) return setOf()
        val rowInfo = row.foldRightIndexed(RowInfo(rowNo, -1, setOf())) { index, char, rowInfo -> rowInfo.add(char, index) }
        return rowInfo.positions.map { Pair(rowNo, it) }.toSet()
    }    
    
    fun visibleFromTop(colNo: Int, col: String): Set<Pair<Int, Int>> {
        if (col.isBlank()) return setOf()
        val rowInfo = col.foldIndexed(RowInfo(colNo, -1, setOf())) { index, rowInfo, char -> rowInfo.add(char, index) }
        return rowInfo.positions.map { Pair(it, colNo) }.toSet()
    }

    fun visibleFromBottom(colNo: Int, col: String): Set<Pair<Int, Int>> {
        if (col.isBlank()) return setOf()
        val rowInfo = col.foldRightIndexed(RowInfo(colNo, -1, setOf())) { index, char, rowInfo -> rowInfo.add(char, index) }
        return rowInfo.positions.map { Pair(it, colNo) }.toSet()
    }
    
    fun part1(): Int {
        val union = visibleFromLeft.union(visibleFromRight).union(visibleFromTop).union(visibleFromBottom)
        println("Union: ${union.sortedWith(compareBy<Pair<Int, Int>> { it.first }.thenBy { it.second })}")
        return union.size
    }
    
    val pairs = (input.indices).map { column ->
        (transposed.indices).map { row ->
            Pair(row, column)
        }
    }.flatten()
    
    fun part2(): Int {
        val associateWith = pairs.associateWith { sightLeft(it) * sightRight(it) * sightDown(it) * sightUp(it) }
        val entry = associateWith.maxWith { a, b -> a.value.compareTo(b.value) }
        println("Max pair is ${entry.key} = ${entry.value}")
        return entry.value
    }
    
    fun calculateScenicScore(pair: Pair<Int, Int>): Int {
        val up = sightUp(pair)
        val left = sightLeft(pair)
        val down = sightDown(pair)
        val right = sightRight(pair)
        println("[up=$up][left=$left][down=$down][right=$right]")
        val sum = up * left * down * right
        println("sum = $sum")
        return sum
    }

    fun sightUp(position: Pair<Int, Int>): Int {
        val height = inputInts[position.first][position.second]
        val consideredTrees = transposedInts[position.second].take(position.first)
        val count = consideredTrees.takeLastWhile { it < height }.count()
        if (count == consideredTrees.count()) return count
        return count + 1
    }

    fun sightDown(position: Pair<Int, Int>): Int {
        val height = inputInts[position.first][position.second]
        val consideredTrees = transposedInts[position.second].drop(position.first + 1)
        val count = consideredTrees.takeWhile { it < height }.count()
        if (count == consideredTrees.count()) return count
        return count + 1
    }

    fun sightRight(position: Pair<Int, Int>): Int {
        val height = inputInts[position.first][position.second]
        val consideredTrees = inputInts[position.first].drop(position.second + 1)
        val count = consideredTrees.takeWhile { it < height }.count()
        if (count == consideredTrees.count()) return count
        return count + 1
    }

    fun sightLeft(position: Pair<Int, Int>): Int {
        val height = inputInts[position.first][position.second]
        val consideredTrees = inputInts[position.first].take(position.second)
        val count = consideredTrees.takeLastWhile { it < height }.count()
        if (count == consideredTrees.count()) return count
        return count + 1
    }
}

data class RowInfo(val rowNo: Int, val highestNumber: Int, val positions: Set<Int>) {
    fun add(input: Char, index: Int): RowInfo {
        val inputDigit = input.digitToInt()
        if (highestNumber == 9 || inputDigit <= highestNumber) return this
        return this.copy(highestNumber = inputDigit, positions = positions + index)
    }
}


private fun transpose(input: List<String>): List<String> {
    val columns = input.first().length - 1
    return (0..columns).map { column -> input.map { string -> string[column] }.joinToString("") }
}

fun main() {
    val testInput = Day8::class.java.getResourceAsStream("/day8_test.txt").bufferedReader().readLines()
    val input = Day8::class.java.getResourceAsStream("/day8_input.txt").bufferedReader().readLines()

    val day8test = Day8(testInput)
    val day8input = Day8(input)
    println("From left: ${day8test.visibleFromLeft}")
    println("From right: ${day8test.visibleFromRight}")
    println("From top: ${day8test.visibleFromTop}")
    println("From bottom: ${day8test.visibleFromBottom}")

    println("Part 1 test result: ${day8test.part1()}")
    println("Part 1 result: ${day8input.part1()}")
    
    println("Part 2 test result: ${day8test.part2()}")
    println("Part 2 result: ${day8input.part2()}")
    
//    println(day8test.sightUp(Pair(3,2)))
//    println(day8test.sightLeft(Pair(3,2)))
//    println(day8test.sightDown(Pair(3,2)))
//    println(day8test.sightRight(Pair(3,2)))
//    
//    println("1,1=" + day8test.calculateScenicScore(Pair(1,1)))
//    println("1,2=" + day8test.calculateScenicScore(Pair(1,2)))
//    println("3,2=" + day8test.calculateScenicScore(Pair(3,2)))
}

/*
abc
def
ghi
 */
