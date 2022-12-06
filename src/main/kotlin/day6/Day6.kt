package day6

class Day6(val input: String) {

    private val listOfWindows = (0..input.length-4).map { input.substring(it, it+4) }
    private val messageMarkerWindows = (0..input.length-14).map { input.substring(it, it+14) }
    
    fun startOfPacket(): Int {
        return listOfWindows.indexOfFirst { !it.containsDuplicates() } + 4
    }

    fun startOfMessage(): Int {
        return messageMarkerWindows.indexOfFirst { !it.containsDuplicates() } + 14
    }
    
    override fun toString(): String {
        return input
    }
}

fun String.containsDuplicates(): Boolean {
    return this.toCharArray().distinct().size != this.toCharArray().size
}

fun main() {
    
    val test1 = Day6("mjqjpqmgbljsphdztnvjfqwrcgsmlb")
    val test2 = Day6("bvwbjplbgvbhsrlpgdmjqwftvncz")
    val test3 = Day6("nppdvjthqldpwncqszvftbrmjlhg")
    val test4 = Day6("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")
    val test5 = Day6("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")
    
    check(test1.startOfPacket() == 7)
    check(test2.startOfPacket() == 5)
    check(test3.startOfPacket() == 6)
    check(test4.startOfPacket() == 10)
    check(test5.startOfPacket() == 11)    

    check(test1.startOfMessage() == 19)
    check(test2.startOfMessage() == 23)
    check(test3.startOfMessage() == 23)
    check(test4.startOfMessage() == 29)
    check(test5.startOfMessage() == 26)
    
    val input = Day6::class.java.getResourceAsStream("/day6_input.txt").bufferedReader().readLines().first()
    val day6 = Day6(input)
    println("Part1 = ${day6.startOfPacket()}")
    println("Part2 = ${day6.startOfMessage()}")
}
