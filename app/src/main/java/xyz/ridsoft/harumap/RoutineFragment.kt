package xyz.ridsoft.harumap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import xyz.ridsoft.harumap.databinding.FragmentRoutineBinding
import java.util.*

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

        initTask()
        initRecyclerView()
    }

    private fun initTask() {
        // Get calendar
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val week = cal.get(Calendar.WEEK_OF_YEAR)
        dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)

        dbHelper = DBHelper(requireContext())
        task = dbHelper.getTask(year, week)
    }

    private fun initRecyclerView() {
        // Initialize recyclerview
        adapter = RoutineAdapter(requireContext())
        adapter.onStateChangedListener = { id, complete ->
            // Update preference
            val routinePref =
                requireContext().getSharedPreferences(SharedPreferenceKeys.KEY_ROUTINES, 0)
            val edit = routinePref.edit()
            edit.putBoolean(id.toString(), complete)
            edit.apply()

            // Update db
            task.routines[id] = complete

            dbHelper.update(task)

        }

        binding.rvRoutine.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRoutine.adapter = adapter
    }
}