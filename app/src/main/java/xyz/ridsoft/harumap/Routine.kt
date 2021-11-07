package xyz.ridsoft.harumap

data class Routine(val id: Int, var content: String) {
    var enabled: Boolean = true
    var notification: Boolean = true

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Routine

        if (id != other.id) return false
        if (content != other.content) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + content.hashCode()
        return result
    }
}

data class Task(val _id: Int, var id: Int, var week: Int, var dayOfWeek: Int, var complete: Boolean)

class Week(var week: Int, var done: Array<Int>, var tasks: Array<Int>)