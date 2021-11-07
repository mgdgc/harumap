package xyz.ridsoft.harumap

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import xyz.ridsoft.harumap.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize fragments
        initFragments()
    }

    private fun initFragments() {
        supportFragmentManager.beginTransaction().replace(R.id.layoutMainHeatmap, HeatmapFragment())
            .commit()
        supportFragmentManager.beginTransaction().replace(R.id.layoutMainRoutine, RoutineFragment())
            .commit()
    }
}