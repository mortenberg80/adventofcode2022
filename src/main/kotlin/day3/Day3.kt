package day3

class Day3 {
    fun getOrganizer(filename: String): RucksackOrganizer {
        val readLines = this::class.java.getResourceAsStream(filename).bufferedReader().readLines()

        return RucksackOrganizer(readLines)
    }
}

data class RucksackOrganizer(val input: List<String>) {
    val rucksacks = input.map { Rucksack(it) }

    val elfGroups = createElfGroups(listOf(), rucksacks)

    private fun createElfGroups(accumulator: List<ElfGroup>, input: List<Rucksack>): List<ElfGroup> {
        if (input.isEmpty()) return accumulator

        return createElfGroups(accumulator + ElfGroup(input.take(3)), input.drop(3))
    }

    val part1Score = rucksacks.sumOf { it.score() }
    val part2Score = elfGroups.sumOf { it.score() }
}

data class ElfGroup(val rucksacks: List<Rucksack>) {

    val badge = rucksacks[0].types.intersect(rucksacks[1].types).intersect(rucksacks[2].types).first()

    fun score() = badge.day3Score()

    override fun toString(): String {
        return "[${rucksacks[0].input}][${rucksacks[1].input}][${rucksacks[2].input}][badge=$badge]"
    }
}

data class Rucksack(val input: String) {
    private val compartment1 = input.take(input.length / 2)
    private val compartment2 = input.takeLast(input.length / 2)

    private val duplicate = compartment1.toList().toSet().intersect(compartment2.toList().toSet()).first()

    val types = input.toList().toSet()

    fun score(): Int {
        return duplicate.day3Score()
    }
    override fun toString(): String {
        return "[Compartment1=$compartment1][Compartment2 = $compartment2][duplicate = $duplicate][score = ${score()}]"
    }
}

fun Char.day3Score(): Int {
    return if (this.isLowerCase()) {
        this.toInt() - 96
    } else {
        this.toInt() - 38
    }
}



fun main() {
    val rucksackOrganizerTest = Day3().getOrganizer("/day3_test.txt")
    val rucksackOrganizerInput = Day3().getOrganizer("/day3_input.txt")

    //rucksackOrganizer.rucksacks.forEach { println(it) }
    println("Part 1 score: ${rucksackOrganizerTest.part1Score}")
    println("Part 1 score: ${rucksackOrganizerInput.part1Score}")
    println("Part 2 score: ${rucksackOrganizerTest.part2Score}")
    println("Part 2 score: ${rucksackOrganizerInput.part2Score}")

    //println("ElfGroups: ${rucksackOrganizerTest.elfGroups}")
}
