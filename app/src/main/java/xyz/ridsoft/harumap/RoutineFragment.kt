package xyz.ridsoft.harumap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import xyz.ridsoft.harumap.databinding.FragmentRoutineBinding

class RoutineFragment : Fragment() {

    private lateinit var binding: FragmentRoutineBinding
    private lateinit var adapter: RoutineAdapter

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

        // Initialize recyclerview
        adapter = RoutineAdapter(requireContext())
        adapter.onStateChangedListener = { id, complete ->
            DataManager.tasks[id]?.complete = complete
            // update db
            val dbHelper = DBHelper(requireContext())
//            dbHelper.update()
        }

        binding.rvRoutine.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRoutine.adapter = adapter
    }
}