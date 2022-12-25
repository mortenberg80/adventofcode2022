package day10

class Day10(private val input: List<String>) {
    
    private val commands = parseCommands(input)

    private fun parseCommands(input: List<String>): List<Command> {
        return input.flatMap { parseCommand(it) }
    }

    private fun parseCommand(input: String): List<Command> {
        if (input == "noop") return listOf(Command.Noop)
        val split = input.split(" ")
        if (split[0] == "addx") return listOf(Command.AddFirstCycle(split[1].toInt()), Command.AddSecondCycle(split[1].toInt()))
        return listOf()
    }

    fun part1(): Int {
        val _20 = commands.take(19).fold(1) { initial, command -> initial + command.compute() } * 20
        val _60 = commands.take(59).fold(1) { initial, command -> initial + command.compute() } * 60
        val _100 = commands.take(99).fold(1) { initial, command -> initial + command.compute() } * 100
        val _140 = commands.take(139).fold(1) { initial, command -> initial + command.compute() } * 140
        val _180 = commands.take(179).fold(1) { initial, command -> initial + command.compute() } * 180
        val _220 = commands.take(219).foldIndexed(1) { i, initial, command -> initial + command.compute() } * 220
        return _20 + _60 + _100 + _140 + _180 + _220
    }
    
    fun part2(): String {
        val emptyRow = (0 until 40).joinToString("") { "." }
        val firstRow = Row(cyclePosition = 0, currentRow = emptyRow, 1)
        val row1 = commands.take(40).fold(firstRow) { row, command -> row.handleCommand(command)}
        val secondRow = Row(cyclePosition = 0, currentRow = emptyRow, registerX = row1.registerX)
        val row2 = commands.drop(40).take(40).fold(secondRow) { row, command -> row.handleCommand(command)}
        val third = Row(cyclePosition = 0, currentRow = emptyRow, registerX = row2.registerX)
        val row3 = commands.drop(80).take(40).fold(third) { row, command -> row.handleCommand(command)}
        val fourth = Row(cyclePosition = 0, currentRow = emptyRow, row3.registerX)
        val row4 = commands.drop(120).take(40).fold(fourth) { row, command -> row.handleCommand(command)}
        val fifth = Row(cyclePosition = 0, currentRow = emptyRow, registerX = row4.registerX)
        val row5 = commands.drop(160).take(40).fold(fifth) { row, command -> row.handleCommand(command)}
        val sixth = Row(cyclePosition = 0, currentRow = emptyRow, registerX = row5.registerX)
        val row6 = commands.drop(200).take(40).fold(sixth) { row, command -> row.handleCommand(command)}

        return "\n${row1.currentRow}\n" +
                "${row2.currentRow}\n" +
                "${row3.currentRow}\n" +
                "${row4.currentRow}\n" +
                "${row5.currentRow}\n" +
                "${row6.currentRow}\n"
    }
}

sealed class Command {
    abstract fun compute(): Int
    object Noop : Command() {
        override fun compute(): Int = 0
    }

    data class AddFirstCycle(val value: Int) : Command() {
        override fun compute(): Int = 0
    }

    data class AddSecondCycle(val value: Int) : Command() {
        override fun compute(): Int = value
    }
}


data class Row(val cyclePosition: Int, val currentRow: String, val registerX: Int) {
    fun handleCommand(command: Command): Row {
        val nextRow = drawPixel()
        val nextCyclePosition = cyclePosition + 1
        val nextRegisterX = registerX + command.compute()
        return Row(nextCyclePosition, nextRow, nextRegisterX)
    }
    
    fun drawPixel(): String {
        return if ((registerX - 1..registerX + 1).contains(cyclePosition)) currentRow.replaceRange(cyclePosition, cyclePosition+1, "#")
        else currentRow
    }
}


data class Row2(val cyclePosition: Int, val currentRow: String, val registerX: Int) {
    fun handleCommand(command: Command): Row {
        val nextRow = drawPixel()
        val nextCyclePosition = cyclePosition + 1
        val nextRegisterX = registerX + command.compute()
        return Row(nextCyclePosition, nextRow, nextRegisterX)
    }

    fun drawPixel(): String {
        return if ((registerX - 1..registerX + 1).contains(cyclePosition)) currentRow.replaceRange(cyclePosition, cyclePosition+1, "#")
        else currentRow
    }
}

fun main() {
    val testInput = Day10::class.java.getResourceAsStream("/day10_test.txt").bufferedReader().readLines()
    val input = Day10::class.java.getResourceAsStream("/day10_input.txt").bufferedReader().readLines()

    val day10test = Day10(testInput)
    val day10input = Day10(input)

    println("Day10 part 1 test result: ${day10test.part1()}")
    println("Day10 part 1 result: ${day10input.part1()}")
    println("Day10 part 2 test result: ${day10test.part2()}")
    println("Day10 part 2 result: ${day10input.part2()}")
}
