package day4

class Day4 {
    fun getOrganizer(filename: String): SupplyOrganizer {
        val readLines = this::class.java.getResourceAsStream(filename).bufferedReader().readLines()

        return SupplyOrganizer(readLines)
    }
}

class SupplyOrganizer(input: List<String>) {
    val elfPairs = input.map { ElfPair(it) }

    val fullyContained = elfPairs.count { it.fullyContains }
    val overlapped = elfPairs.count { it.overlaps }
}

data class ElfPair(val input: String) {
    private val firstSection = AssignedSection(input.split(",").first())
    private val secondSection = AssignedSection(input.split(",").last())

    val fullyContains = firstSection.sections.intersect(secondSection.sections) == firstSection.sections
            || secondSection.sections.intersect(firstSection.sections) == secondSection.sections

    val overlaps = firstSection.sections.intersect(secondSection.sections).isNotEmpty()

    override fun toString(): String {
        return "ElfPair[${firstSection.range}][${secondSection.range}][fullyContains=$fullyContains][overlaps=$overlaps]"
    }
}

data class AssignedSection(val input: String) {
    val from = input.split("-")[0].toInt()
    val to = input.split("-")[1].toInt()
    val range = (from..to)
    val sections = range.toSet()

    override fun toString(): String {
        return "AssignedSection[$range]"
    }

}

fun main() {
    val organizerTest = Day4().getOrganizer("/day4_test.txt")
    val organizerInput = Day4().getOrganizer("/day4_input.txt")
    //println("elfpairs: ${organizerTest.elfPairs}")

    organizerInput.elfPairs.filter { !it.fullyContains }.forEach { println(it)}

    println("Part 1 test: ${organizerTest.fullyContained}")
    println("Part 1 input: ${organizerInput.fullyContained}")
    println("Part 1 test: ${organizerTest.overlapped}")
    println("Part 1 input: ${organizerInput.overlapped}")
}
