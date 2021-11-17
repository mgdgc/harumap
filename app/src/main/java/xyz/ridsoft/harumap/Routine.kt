package xyz.ridsoft.harumap

import java.lang.StringBuilder

data class Routine(val id: Int, var content: String) {
    companion object {
        const val SQL_CREATE = "CREATE TABLE IF NOT EXISTS ROUTINE (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "content TEXT," +
                "enabled INTEGER DEFAULT 1," +
                "notification INTEGER DEFAULT 0" +
                ");"
    }

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

data class Task(
    val _id: String,
    var year: Int,
    var day: Int,
    var routines: MutableMap<Int, Boolean>
) {
    companion object {
        const val SQL_CREATE = "CREATE TABLE IF NOT EXISTS TASK (" +
                "_id TEXT PRIMARY KEY," +
                "year INTEGER," +
                "day INTEGER," +
                "routines TEXT DEFAULT \"{}\"" +
                ");"

        fun generateKey(year: Int, day: Int): String {
            val builder = StringBuilder()
            val date = day.toString()
            builder.append(year.toString())
            for (i in 0 until 3 - date.length) {
                builder.append("0")
            }
            builder.append(day)

            return builder.toString()
        }
    }

}