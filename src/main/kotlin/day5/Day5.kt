package day5

class Day5 {
    fun getOrganizer(filename: String): CrateOrganizer {
        val readLines = this::class.java.getResourceAsStream(filename).bufferedReader().readLines()

        return CrateOrganizer(readLines)
    }
}

/*
[N]         [C]     [Z]
[Q] [G]     [V]     [S]         [V]
[L] [C]     [M]     [T]     [W] [L]
[S] [H]     [L]     [C] [D] [H] [S]
[C] [V] [F] [D]     [D] [B] [Q] [F]
[Z] [T] [Z] [T] [C] [J] [G] [S] [Q]
[P] [P] [C] [W] [W] [F] [W] [J] [C]
[T] [L] [D] [G] [P] [P] [V] [N] [R]
 1   2   3   4   5   6   7   8   9

stackNumber

1 => 0-2
2 => 4-6
3 => 8-10
4 => 12-14
5 => 16-18
6 => 20-22
7 => 24-26
8 => 28-30
9 => 32-34

 */


data class CrateOrganizer(val input: List<String>) {
    val stackSize: Int
    val stacks: Map<Int, Stack>
    val commands: List<Command>

    init {
        println(input.first { it.startsWith(" 1 ") }.trim().replace("\\s{2,}".toRegex(), " ").split(" "))
        //  1   2   3   4   5   6   7   8   9 ==> 1 2 3 4 5 6 7 8 9 ==> 9
        stackSize = input.first { it.startsWith(" 1 ") }.trim().replace("\\s{2,}".toRegex(), " ").split(" ").size

        val cratesInput = input.takeWhile { !it.startsWith( " 1 ") }
        println(cratesInput)

        stacks = (1..stackSize)
            .map { stackNumber -> cratesInput.map { it.substring((stackNumber-1)*4, (stackNumber-1)*4 + 2) } }
            .map { crateInput -> crateInput.mapNotNull { Crate.fromString(it) }}
            .map { Stack(it) }
            .withIndex().associateBy({it.index + 1}, {it.value})
        
        commands = input.takeLastWhile { it.startsWith("move") }.map { Command.fromString(it) }
    }
    
    fun organizeWithCrateMover9000(): Stacks {
        return Stacks(commands.fold(stacks) { acc, command -> performCommand9000(acc, command) })
    }
    
    fun organizeWithCrateMover9001(): Stacks {
        return Stacks(commands.fold(stacks) { acc, command -> performCommand9001(acc, command) })
    }

    private fun performCommand9000(stacks: Map<Int, Stack>, command: Command): Map<Int, Stack> {
        if (command.repetitions == 0) return stacks
        //println("Moving ${command.repetitions} from ${command.from} to ${command.to}")

        val fromStack = stacks[command.from]!!
        val toStack = stacks[command.to]!!

        val movedCrates = fromStack.crates.take(1)

        val outputStack = stacks.toMutableMap()
        outputStack[command.from] = Stack(fromStack.crates.drop(1))
        outputStack[command.to] = Stack(movedCrates + toStack.crates)
        //println("Operation resulted in $outputStack")
        return performCommand9000(outputStack, Command(repetitions = command.repetitions -1, from = command.from, to = command.to))
    }

    private fun performCommand9001(stacks: Map<Int, Stack>, command: Command): Map<Int, Stack> {
        //println("Moving ${command.repetitions} from ${command.from} to ${command.to}")

        val fromStack = stacks[command.from]!!
        val toStack = stacks[command.to]!!

        val movedCrates = fromStack.crates.take(command.repetitions)

        val outputStack = stacks.toMutableMap()
        outputStack[command.from] = Stack(fromStack.crates.drop(command.repetitions))
        outputStack[command.to] = Stack(movedCrates + toStack.crates)
        //println("Operation resulted in $outputStack")
        return outputStack
    }

}

data class Stacks(val input: Map<Int, Stack>) {
    val topCrates = input.entries.sortedBy { it.key }.map { it.value }.map {it.topCrate()}.map { it.value }.joinToString("")
}

data class Stack(val crates: List<Crate>) {
    
    fun topCrate(): Crate = crates[0]
    
    override fun toString(): String {
        return crates.toString()
    }
}

data class Crate(val value: Char) {

    override fun toString(): String {
        return "[$value]"
    }
    companion object {
        fun fromString(input: String): Crate? {
            if (input.isNullOrBlank()) return null
            return Crate(input[1])
        }
    }
}

data class Command(val repetitions: Int, val from: Int, val to: Int) {

    override fun toString(): String {
        return "[repetitions=$repetitions][from=$from][to=$to]"
    }
    
    companion object {
        fun fromString(input: String): Command {
            val parts = input.split(" ")
            val repetitions = parts[1].toInt()
            val from = parts[3].toInt()
            val to = parts[5].toInt()
            return Command(repetitions, from, to)
        }
    }
}


fun main() {
    val testOrganizer = Day5().getOrganizer("/day5_test.txt")
    val inputOrganizer = Day5().getOrganizer("/day5_input.txt")

    println("Test stacks: ${testOrganizer.stackSize}")
    println("Test stacks: ${testOrganizer.stacks}")
    println("Test stacks: ${testOrganizer.commands}")
    
    val resultingStacks = testOrganizer.organizeWithCrateMover9000()
    println("Test stacks top crates: ${resultingStacks.topCrates}")
    val inputResultingStacks = inputOrganizer.organizeWithCrateMover9000()    
    println("Input stacks top crates: ${inputResultingStacks.topCrates}")
    
    val resultingStacks9001 = testOrganizer.organizeWithCrateMover9001()
    println("Test stacks top crates: ${resultingStacks9001.topCrates}")
    val inputResultingStacks9001 = inputOrganizer.organizeWithCrateMover9001()    
    println("Input stacks top crates: ${inputResultingStacks9001.topCrates}")
}
