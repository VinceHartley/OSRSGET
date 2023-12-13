
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.hartleyv.android.osrsget.ItemGraphViewModel
import com.hartleyv.android.osrsget.databinding.FragmentLineChartBinding
import kotlinx.coroutines.launch

class LineChartFragment : Fragment() {

    private var _binding: FragmentLineChartBinding? = null
    private val binding get() = _binding!!

    private val itemGraphViewModel: ItemGraphViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLineChartBinding.inflate(inflater, container, false)
        val view = binding.root

        val args = LineChartFragmentArgs.fromBundle(requireArguments())
        val item = args.combinedItemInfo

        itemGraphViewModel.setItemId(item.itemId)
        itemGraphViewModel.getItemTimeseries()



        val lineChart: LineChart = binding.lineChart


        viewLifecycleOwner.lifecycleScope.launch {
            itemGraphViewModel.itemPrices.collect {
                val entries1 = ArrayList<Entry>()
                val entries2 = ArrayList<Entry>()
                var count = 0
                for (price in it) {
                    if (price.timestamp != null && price.highPrice != null) {
                        entries1.add(Entry(count.toFloat(), price.highPrice.toFloat()))
                    }
                    if(price.timestamp != null && price.lowPrice != null) {
                        entries2.add(Entry(count.toFloat(), price.lowPrice.toFloat()))
                    }
                    count += 1
                }


                Log.e("HERE", entries1.toString())
                Log.e("HERE", entries2.toString())
                // Create a LineDataSet
                val dataSet1 = LineDataSet(entries1, "Label1")
                val dataSet2 = LineDataSet(entries2, "Label2")

                // Customize the appearance of the chart
                dataSet1.color = resources.getColor(android.R.color.holo_red_dark)
                dataSet1.valueTextColor = resources.getColor(android.R.color.black)

                dataSet2.color = resources.getColor(android.R.color.holo_green_dark)
                dataSet2.valueTextColor = resources.getColor(android.R.color.black)

                // Create a list of LineDataSets
                val dataSets = ArrayList<ILineDataSet>()
                dataSets.add(dataSet1)
                dataSets.add(dataSet2)

                // Create a LineData object from the list of LineDataSets
                val lineData = LineData(dataSets)

                // Set the data to the chart
                lineChart.data = lineData

                // Set chart description
                val description = Description()
                description.text = "Line Chart Example"
                lineChart.description = description


            }
        }




        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        return view
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // this fragment should only appear horizontally
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
