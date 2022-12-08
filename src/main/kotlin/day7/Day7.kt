package day7

import day6.Day6

class Day7(input: List<String>) {
    val commands = parseCommands(listOf(), input)
    
    private val pathMap = buildMap()
    
    private fun parseCommands(initial: List<Command>, input: List<String>): List<Command> {
        if (input.isEmpty()) return initial
        val line = input.first().split(" ")
        val commandInput = line[1]
        val tail = input.drop(1)
        if (commandInput == "cd") return parseCommands(initial + Command.Cd(line[2]), tail)
        if (commandInput == "ls") {
            val results = tail.takeWhile { !it.startsWith("$") }
            return parseCommands(initial + Command.Ls(results), tail.drop(results.size))
        }
        println("Should not reach this..?")
        return initial
    }
    
    private fun buildMap(): Map<String, Pair<Int, List<String>>> {
        val resultMap = mutableMapOf<String, Pair<Int, List<String>>>()
        var currentDir: List<String> = listOf()
        commands.forEach {
            when (it) {
                is Command.Cd -> {
                    currentDir = when (it.path) {
                        "/" -> {
                            listOf()
                        }
                        ".." -> {
                            currentDir.dropLast(1)
                        }
                        else -> {
                            currentDir.plus(it.path)
                        }
                    }
                }
                is Command.Ls -> {
                    val currnetPath = computePath(currentDir)
                    val dirs = it.result.filter { c ->  c.startsWith("dir") }.map { c -> c.split(" ")[1] }.map { path -> computePath(currentDir + path) }
                    val size = it.result.filter { c -> !c.startsWith("dir") }.map { c -> c.split(" ")[0].toInt() }.sum()
                    resultMap[currnetPath] = Pair(size, dirs)
                }
            }
        }
        return resultMap
    }

    private fun computePath(currentDir: List<String>): String {
        if (currentDir.isEmpty()) return "/"
        return "/" + currentDir.joinToString("/")
    }

    fun part1(): Int {
        val scoreMap = scoreMap()
        return scoreMap.filter { it.second < 100000 }.sumOf { it.second }
    }

    private fun scoreMap(): List<Pair<String, Int>> {
        return pathMap.keys.map { it to computeScore(it) }
    }

    fun part2(): Int {
        val totalSpace = 70000000
        val neededSpace = 30000000
        val usedSpace = computeScore("/")
        val availableSpace = totalSpace - usedSpace
        val toBeDeleted = neededSpace - availableSpace
        
        //println("UsedSpace = $usedSpace, toBeDeleted=$toBeDeleted")
        
        return scoreMap().map { it.second }.filter { it > toBeDeleted }.minBy { it }
    }
    
    private fun computeScore(path: String): Int {
        val pathScore = pathMap[path]!!
        val dirScore = pathScore.first
        val subdirScore = pathScore.second.sumOf { computeScore(it) }

        return dirScore.plus(subdirScore)
    }
}


sealed class Command {
    data class Cd(val path: String): Command()
    data class Ls(val result: List<String>): Command()
}

    fun main() {
        val testInput = Day6::class.java.getResourceAsStream("/day7_test.txt").bufferedReader().readLines()
        val input = Day6::class.java.getResourceAsStream("/day7_input.txt").bufferedReader().readLines()
    
        val day7Test = Day7(testInput)
//        println(day7Test.commands)
//        println(day7Test.buildMap())
        println("Part 1 test answer: ${day7Test.part1()}")
        println("Part 2 test answer: ${day7Test.part2()}")
        
        val day7Input = Day7(input)
//        println(day7Input.commands)
//        println(day7Input.buildMap())
        println("Part 1 answer: ${day7Input.part1()}")
        println("Part 2 answer: ${day7Input.part2()}")
}

