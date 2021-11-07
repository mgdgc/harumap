package xyz.ridsoft.harumap

import android.content.Context

class DataManager(private val context: Context) {
    companion object {
        private var initialized = false
        var tasks: MutableMap<Int, Task> = mutableMapOf()
    }

    init {
        if (!initialized) {

        }
    }
}