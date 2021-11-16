package xyz.ridsoft.harumap

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import xyz.ridsoft.harumap.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: DBHelper

    private lateinit var routineFragment: RoutineFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)

        initData()

        // Initialize fragments
        initFragments()

        initView()
    }

    private fun initData() {
        DataManager(this)
        dbHelper = DBHelper(this)
    }

    private fun initFragments() {
        supportFragmentManager.beginTransaction().replace(R.id.layoutMainHeatmap, HeatmapFragment())
            .commit()

        routineFragment = RoutineFragment()
        supportFragmentManager.beginTransaction().replace(R.id.layoutMainRoutine, routineFragment)
            .commit()
    }

    private fun initView() {
        binding.fabRoutine.setOnClickListener {
            val editText = EditText(this)
            val alert = AlertDialog.Builder(this)
            alert.setTitle(R.string.routine_add_title)
                .setView(editText)
                .setPositiveButton(R.string.confirm) { dialog, _ ->
                    // Input
                    val content = editText.text.toString()

                    // If there's no content
                    if (content.isEmpty()) {
                        // Show reason
                        Snackbar.make(
                            binding.layoutMainRoutine,
                            R.string.routine_add_empty,
                            Snackbar.LENGTH_SHORT
                        )
                            .setAction(R.string.dismiss) { }
                            .show()
                    } else {
                        val routine = Routine(0, content)
                        dbHelper.insert(routine)
                        routineFragment.reloadData()
                    }

                    // Dismiss dialog
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }
}