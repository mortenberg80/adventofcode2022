package day11

import java.lang.IllegalArgumentException

class Day11(val input: List<String>) {
    
    fun part1(): Long {
        val monkies = parseInput(input, true)
        println(monkies)
        val game = Game(monkies)
        (0 until 20).forEach { _ ->
            game.performRound(1)
        }
        
        println(game.monkies)
        
        return monkies.map { it.inspectCounter }.sortedDescending().take(2).reduce {a, b -> a*b}
    }

    fun part2(): Long {
        val monkies = parseInput(input, false)
        
        val lcm = monkies.map { it.divisor }.reduce { acc, i -> acc * i }.toLong()

        val game = Game(monkies)
        (0 until 10000).forEach { it ->
            game.performRound(lcm)
        }
        println(game.monkies)

        // 2147483647
        // 2713310158
        return monkies.map { it.inspectCounter }.sortedDescending().take(2).reduce {a, b -> a*b}
    }
    
    fun parseInput(input: List<String>, worryDivisor: Boolean): List<Monkey> {
        return input.chunked(7).map { toMonkey(it, worryDivisor) }
    }
    
    fun toMonkey(input: List<String>, worryDivisor: Boolean): Monkey {
        val id = Regex("""Monkey (\d+):""").find(input[0])!!.destructured.toList()[0].toInt()
        val startingItems = input[1].split(":")[1].trim().split(",").map { it.trim().toLong() }.toMutableList()
        val operationValues = "Operation: new = old ([*+]) ([a-zA-Z0-9]+)".toRegex().find(input[2])!!.destructured.toList()
        val operation = Operation.create(operationValues)
        val divisor = "Test: divisible by ([0-9]+)".toRegex().find(input[3])!!.destructured.toList()[0].toInt()
        val trueMonkey = "If true: throw to monkey ([0-9]+)".toRegex().find(input[4])!!.destructured.toList()[0].toInt()
        val falseMonkey = "If false: throw to monkey ([0-9]+)".toRegex().find(input[5])!!.destructured.toList()[0].toInt()
        
        return Monkey(id, startingItems, divisor, worryDivisor, operation::calculate) { input ->
            if (input) {
                trueMonkey
            } else {
                falseMonkey
            }
        }
    }
}

sealed class Operation {
    abstract fun calculate(old: Long): Long
    data class MultiplyWithNumber(val number: Long): Operation() {
        override fun calculate(old: Long): Long {
            return old * number
        }
    }

    object MultiplyWithOld: Operation() {
        override fun calculate(old: Long): Long {
            return old * old
        }
    }

    data class PlusWithNumber(val number: Long): Operation() {
        override fun calculate(old: Long): Long {
            return old + number
        }
    }

    object PlusWithOld: Operation() {
        override fun calculate(old: Long): Long {
            return old + old
        }
    }
    
    companion object {
        fun create(operationValues: List<String>): Operation {
            val operand = operationValues[0]
            val factor = operationValues[1]
            return when (operand) {
                "*" -> if (factor == "old") {
                    MultiplyWithOld
                } else {
                    MultiplyWithNumber(factor.toLong())
                }

                "+" -> if (factor == "old") {
                    PlusWithOld
                } else {
                    PlusWithNumber(factor.toLong())
                }

                else -> throw IllegalArgumentException("could not parse operation: $operationValues")
            }
        }
    }
}

class Game(val monkies: List<Monkey>) {
    fun performRound(lcm: Long) {
        monkies.forEach {
            it.performAction(monkies, lcm)
        }
    }
}

class Monkey(
    val id: Int,
    val items: MutableList<Long>,
    val divisor: Int,
    val worryDivisor: Boolean,
    val operation: (Long) -> Long,
    val monkeyChooser: (Boolean) -> Int
) {
    var inspectCounter = 0L
    
    fun performAction(monkies: List<Monkey>, lcm: Long) {
        val iterator = items.iterator()
        while (iterator.hasNext()) {
            inspectCounter++
            val item = iterator.next()
            val newWorryLevel = operation.invoke(item)
            val boredWorryLevel = if (worryDivisor) {
                newWorryLevel / 3
            } else {
                newWorryLevel % lcm
            }
            val monkeyReceiverId = monkeyChooser.invoke(boredWorryLevel % divisor == 0L)
            monkies[monkeyReceiverId].receive(boredWorryLevel)
            iterator.remove()
        }
    }
    
    fun receive(value: Long) {
        items.add(value)
    }

    override fun toString(): String {
        return "Monkey$id: inpsectCounter=$inspectCounter. '${items}'"
    }
}

fun main() {
    val testInput = Day11::class.java.getResourceAsStream("/day11_test.txt").bufferedReader().readLines()
    val input = Day11::class.java.getResourceAsStream("/day11_input.txt").bufferedReader().readLines()

    val day11test = Day11(testInput)
    val day11input = Day11(input)

    println("Day11 part 1 test result: ${day11test.part1()}")
    println("Day11 part 1 result: ${day11input.part1()}")
    println("Day11 part 2 test result: ${day11test.part2()}")
    println("Day11 part 2 result: ${day11input.part2()}")
}
