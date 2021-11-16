package xyz.ridsoft.harumap

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import xyz.ridsoft.harumap.databinding.FragmentRoutineBinding
import java.util.*
import kotlin.collections.ArrayList

class RoutineFragment : Fragment() {

    private lateinit var binding: FragmentRoutineBinding
    private lateinit var adapter: RoutineAdapter
    private lateinit var dbHelper: DBHelper
    private lateinit var task: Task
    private var dayOfWeek: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRoutineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        initRecyclerView()
    }

    private fun initData() {
        // Get calendar
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val week = cal.get(Calendar.WEEK_OF_YEAR)
        dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)

        // Initialize DB
        dbHelper = DBHelper(requireContext())
        // Initialize Task
        task = dbHelper.getTask(year, week)
    }

    private fun initRecyclerView() {
        // Initialize recyclerview
        adapter = RoutineAdapter(requireContext())
        reloadData()
        adapter.onStateChangedListener = { id, complete ->
            // Update db
            task.routines[id] = complete
            dbHelper.update(task)
        }

        binding.rvRoutine.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRoutine.adapter = adapter
    }

    fun reloadData() {
        adapter.data = dbHelper.getRoutines()
    }

}