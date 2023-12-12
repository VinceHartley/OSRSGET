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
        //get mapping data
        val setupWorker = OneTimeWorkRequest
            .Builder(SetupWorker::class.java)
            .setConstraints(constraints)
            .addTag(setupWorkerTag)
            .build()


        // get instant data
        val periodicRequest = PeriodicWorkRequestBuilder<PollWorker>(1, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(requireContext())
            .enqueue(setupWorker)

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


        // todo update this so this will update faster than 15 minutes
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
                itemListViewModel.getFilterCriteria().observe(viewLifecycleOwner) {
                    itemListViewModel.applySettings(it)
                }

                itemListViewModel.filteredItems.collect{ items ->
                   binding.itemRecyclerView.adapter = ItemListAdapter(items)
               }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}