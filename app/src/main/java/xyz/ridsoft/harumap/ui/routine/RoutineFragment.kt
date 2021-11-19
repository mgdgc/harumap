package xyz.ridsoft.harumap.ui.routine

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import xyz.ridsoft.harumap.database.DBHelper
import xyz.ridsoft.harumap.worker.DataManager
import xyz.ridsoft.harumap.R
import xyz.ridsoft.harumap.databinding.FragmentRoutineBinding
import java.util.*

class RoutineFragment : Fragment() {

    private lateinit var binding: FragmentRoutineBinding
    private lateinit var adapter: RoutineAdapter

    var onRoutineStateChangedListener: (() -> Unit)? = null

    private lateinit var dataManager: DataManager

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

        dataManager = DataManager(requireContext())
        initRecyclerView()
    }

    private fun initRecyclerView() {
        // Initialize recyclerview
        adapter = RoutineAdapter(requireContext())
        adapter.data = DBHelper(requireContext()).getRoutines()
        adapter.onStateChangedListener = { id, complete ->
            // Update db
            // Get calendar
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val day = cal.get(Calendar.DAY_OF_YEAR)

            val task = DBHelper(requireContext()).getTask(year, day)
            task.routines[id] = complete

            DBHelper(requireContext()).update(task)

            dataManager.initTasks()
            onRoutineStateChangedListener?.let { it() }
        }
        adapter.onLongClickListener = { routine, position ->
            val alert = AlertDialog.Builder(requireContext())
            alert.setTitle(R.string.routine_delete_title)
                .setMessage(R.string.routine_delete_message)
                .setPositiveButton(R.string.cancel) { d, _ -> d.dismiss() }
                .setNegativeButton(R.string.delete) { d, _ ->
                    routine.enabled = false

                    val dbHelper = DBHelper(requireContext())
                    dbHelper.update(routine)

                    adapter.remove(position)

                    dataManager.initRoutines()
                    onRoutineStateChangedListener?.let { it() }

                    d.dismiss()
                }
                .show()
        }

        binding.rvRoutine.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRoutine.adapter = adapter
    }

    fun reloadRoutine() {
        adapter.data = DBHelper(requireContext()).getRoutines()
    }

}