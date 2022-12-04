package day1

class Day1 {
    fun getElfStore(filename: String): ElfStore {
        val readLines = this::class.java.getResourceAsStream(filename).bufferedReader().readLines()

        return readLines.fold(ElfStore()) { acc, next -> acc.handle(next) }.finalize()
    }
}

class ElfStore() {
    val elves = mutableListOf<Int>()

    var currentScore = 0

    fun add(value: Int): ElfStore {
        currentScore += value
        return this
    }

    fun next(): ElfStore {
        elves.add(currentScore)
        currentScore = 0
        return this
    }

    fun handle(next: String): ElfStore {
        if (next.isEmpty()) {
            return this.next()
        } else {
            return this.add(next.toInt())
        }
    }

    fun max(): Int {
        return elves.max()
    }

    fun sumOfTopThree(): Int {
        return elves.sortedDescending().take(3).sum()
    }

    fun finalize(): ElfStore {
        elves.add(currentScore)
        currentScore = 0
        return this
    }
}

fun main() {
    val elfStoreDay1Test = Day1().getElfStore("/day1_test.txt")
    val elfStoreDay1 = Day1().getElfStore("/day1_input.txt")
    println("Day 1: (test) = " + elfStoreDay1Test.max())
    println("Day 1: = " + elfStoreDay1.max())

    println("Day 2: Sum top three (test) = " + elfStoreDay1Test.sumOfTopThree())
    println("Day 2: Sum top three = " + elfStoreDay1.sumOfTopThree())
}

