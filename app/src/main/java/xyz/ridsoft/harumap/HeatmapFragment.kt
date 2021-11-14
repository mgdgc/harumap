package xyz.ridsoft.harumap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import xyz.ridsoft.harumap.databinding.FragmentHeatmapBinding

class HeatmapFragment : Fragment() {

    private lateinit var binding: FragmentHeatmapBinding
    private lateinit var adapter: HeatmapAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHeatmapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize recyclerview
        initRecyclerView()
    }

    private fun initRecyclerView() {
        // Initialize adapter
        adapter = HeatmapAdapter(requireContext())

        // Initialize recyclerview
        binding.rvHeatMapHorizontal.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHeatMapHorizontal.adapter = adapter
    }
}