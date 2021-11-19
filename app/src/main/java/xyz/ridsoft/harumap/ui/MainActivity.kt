package xyz.ridsoft.harumap.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import xyz.ridsoft.harumap.R
import xyz.ridsoft.harumap.Routine
import xyz.ridsoft.harumap.database.DBHelper
import xyz.ridsoft.harumap.databinding.ActivityMainBinding
import xyz.ridsoft.harumap.ui.heatmap.HeatmapFragment
import xyz.ridsoft.harumap.ui.routine.RoutineFragment
import xyz.ridsoft.harumap.worker.DataManager
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: DBHelper

    private lateinit var heatmapFragment: HeatmapFragment
    private lateinit var routineFragment: RoutineFragment

    private lateinit var dataManager: DataManager

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
        dataManager = DataManager(this)
        dbHelper = DBHelper(this)
    }

    private fun initFragments() {
        heatmapFragment = HeatmapFragment()
        supportFragmentManager.beginTransaction().replace(R.id.layoutMainHeatmap, heatmapFragment)
            .commit()

        routineFragment = RoutineFragment()
        routineFragment.onRoutineStateChangedListener = {
            heatmapFragment.routineStateUpdated()
        }
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

                        dataManager.initTasks()
                        dataManager.initRoutines()
                        routineFragment.reloadRoutine()
                        heatmapFragment.routineStateUpdated()
                    }

                    // Dismiss dialog
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
                .show()
        }

        binding.ablMain.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (appBarLayout != null) {
                if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                    binding.fabRoutine.shrink()
                } else {
                    binding.fabRoutine.extend()
                }
            }
        })
    }
}