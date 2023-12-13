package com.hartleyv.android.osrsget

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.hartleyv.android.osrsget.databinding.FragmentItemListBinding
import com.hartleyv.android.osrsget.entities.CombinedItemListInfo
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit



private const val TAG = "ItemListFragment"
private const val POLL_WORK = "POLL_WORK"

class ItemListFragment: Fragment() {
    private val itemListViewModel: ItemListViewModel by viewModels({requireParentFragment()})

    private var _binding: FragmentItemListBinding? = null

    private val binding
        get() = checkNotNull(_binding) {
            "cannot access binding because it is null"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        val setupWorkerTag = "SETUP WORKER"
        val setupWorker = OneTimeWorkRequest
            .Builder(SetupWorker::class.java)
            .setConstraints(constraints)
            .addTag(setupWorkerTag)
            .build()


        val periodicRequest = PeriodicWorkRequestBuilder<PollWorker>(1, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()


        // setup worker initially pings the wiki API to get fresh data
        WorkManager.getInstance(requireContext())
            .enqueue(setupWorker)

        // the periodic worker updates the data every 15 minutes
        WorkManager.getInstance(requireContext())
            .getWorkInfosByTagLiveData(setupWorkerTag)
            .observeForever { workInfos ->
                if (workInfos != null && !workInfos.isEmpty()) {
                    val latestWorkInfo = workInfos[0]
                    if (latestWorkInfo.state == WorkInfo.State.SUCCEEDED) {
                        itemListViewModel.setupWorkerFinished = true
                    }
                }
            }


        WorkManager.getInstance(requireContext())
            .enqueueUniquePeriodicWork(
                POLL_WORK,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicRequest
            )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemListBinding.inflate(inflater, container, false)

        binding.itemRecyclerView.layoutManager = LinearLayoutManager(context)

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // apply filter settings on change
                itemListViewModel.getFilterCriteria().observe(viewLifecycleOwner) {
                    itemListViewModel.applySettings(it)
                }

                // view list changes
                itemListViewModel.filteredItems.collect{ items ->
                    val adapter = ItemListAdapter(items, {clickedItem -> navigateToLineChartFragment(clickedItem)})
                   binding.itemRecyclerView.adapter = adapter
               }
            }
        }

        //this fragment should always be vertical
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        return binding.root
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // this fragment should only appear vertically
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_item_list, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // show settings fragment
        return when (item.itemId) {
            R.id.filter_settings -> {
                showSettingsFragment()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun showSettingsFragment() {
        viewLifecycleOwner.lifecycleScope.launch {

            findNavController().navigate(
                ItemListFragmentDirections.showSettingsFragment()
            )

        }

    }


    private fun navigateToLineChartFragment(item: CombinedItemListInfo) {
        viewLifecycleOwner.lifecycleScope.launch {
            findNavController().navigate(
                ItemListFragmentDirections.showItemData(item)
            )

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}