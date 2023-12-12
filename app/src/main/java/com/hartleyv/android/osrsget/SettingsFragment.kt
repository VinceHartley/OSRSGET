package com.hartleyv.android.osrsget

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hartleyv.android.osrsget.databinding.FragmentSettingsBinding

class SettingsFragment: Fragment() {
    private var _binding: FragmentSettingsBinding? = null

    private val binding
        get() = checkNotNull(_binding) {
            "cannot access binding because it is null"
        }


    private val itemListViewModel: ItemListViewModel by viewModels({requireParentFragment()})

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }


    private fun setupUserInputListeners() {
        binding.apply{

            searchTextBox.doOnTextChanged { text, _, _, _ ->
                updateFilterCriteria()
            }

            sortColumn.setOnCheckedChangeListener { _, _ ->
                updateFilterCriteria()
            }

            sortDirection.setOnCheckedChangeListener{ _, _ ->
                updateFilterCriteria()
            }

            buyPriceMin.doOnTextChanged { text, _, _, _ ->
                updateFilterCriteria()
            }

            buyPriceMax.doOnTextChanged { text, _, _, _ ->
                updateFilterCriteria()

            }

            sellPriceMin.doOnTextChanged { text, _, _, _ ->
                updateFilterCriteria()

            }

            sellPriceMax.doOnTextChanged { text, _, _, _ ->
                updateFilterCriteria()

            }

            volumeMin.doOnTextChanged { text, _, _, _ ->
                updateFilterCriteria()

            }

            volumeMax.doOnTextChanged { text, _, _, _ ->
                updateFilterCriteria()

            }

            marginMin.doOnTextChanged { text, _, _, _ ->
                updateFilterCriteria()

            }

            marginMax.doOnTextChanged { text, _, _, _ ->
                updateFilterCriteria()

            }

            buyLimitMin.doOnTextChanged { text, _, _, _ ->
                updateFilterCriteria()

            }

            buyLimitMax.doOnTextChanged { text, _, _, _ ->
                updateFilterCriteria()
            }

            resetButton.setOnClickListener {
                clearSettings()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showPreviousFilter()
        setupUserInputListeners()
    }

    private fun clearSettings() {
        var nullCriteria = FilterCriteria(null, null, null,
            null, null, null, null, null,
            null, null, null, null,null)

        itemListViewModel.updateFilterCriteria(
            nullCriteria
        )

        showPreviousFilter()
    }

    private fun showPreviousFilter() {
        var filterCriteria = itemListViewModel.getFilterCriteria().value

        binding.apply{
            when(filterCriteria?.orderColumn.toString().lowercase()) {
                "buy price" -> {
                    val toCheck = sortColumn.getChildAt(0) as RadioButton
                    toCheck.isChecked = true
                }

                "sell price" -> {
                    val toCheck = sortColumn.getChildAt(1) as RadioButton
                    toCheck.isChecked = true
                }

                "daily volume" -> {
                    val toCheck = sortColumn.getChildAt(2) as RadioButton
                    toCheck.isChecked = true
                }

                "margin" -> {
                    val toCheck = sortColumn.getChildAt(3) as RadioButton
                    toCheck.isChecked = true
                }
            }

            when(filterCriteria?.orderDirection.toString().lowercase()) {
                "descending" -> {
                    val toCheck = sortDirection.getChildAt(0) as RadioButton
                    toCheck.isChecked = true
                }

                "ascending" -> {
                    val toCheck = sortDirection.getChildAt(1) as RadioButton
                    toCheck.isChecked = true
                }

                else -> {
                    // assume descending
                    val descending = sortDirection.getChildAt(0) as RadioButton
                    val ascending = sortDirection.getChildAt(1) as RadioButton

                    descending.isChecked = true
                    ascending.isChecked = false
                }
            }

            searchTextBox.setText(filterCriteria?.searchTerm ?: "")
            buyPriceMin.setText(filterCriteria?.buyPriceMin?.toString() ?: "")
            buyPriceMax.setText(filterCriteria?.buyPriceMax?.toString() ?: "")
            sellPriceMin.setText(filterCriteria?.sellPriceMin?.toString() ?: "")
            sellPriceMax.setText(filterCriteria?.sellPriceMax?.toString() ?: "")
            volumeMin.setText(filterCriteria?.volumeMin?.toString() ?: "")
            volumeMax.setText(filterCriteria?.volumeMax?.toString() ?: "")
            marginMin.setText(filterCriteria?.marginMin?.toString() ?: "")
            marginMax.setText(filterCriteria?.marginMax?.toString() ?: "")
            buyLimitMin.setText(filterCriteria?.buyLimitMin?.toString() ?: "")
            buyLimitMax.setText(filterCriteria?.buyLimitMax?.toString() ?: "")

        }
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

    private fun updateFilterCriteria() {
        val selectedOrder = view!!.findViewById<RadioButton>(binding.sortColumn.checkedRadioButtonId)
        val selectedDirection = view!!.findViewById<RadioButton>(binding.sortDirection.checkedRadioButtonId)

        val newFilterCriteria = FilterCriteria(
            searchTerm = binding.searchTextBox.text?.toString(),
            orderColumn = selectedOrder?.text.toString(),
            orderDirection = selectedDirection?.text.toString(),
            buyPriceMin = binding.buyPriceMin.text.toString().toIntOrNull(),
            buyPriceMax = binding.buyPriceMax.text.toString().toIntOrNull(),
            sellPriceMin = binding.sellPriceMin.text.toString().toIntOrNull(),
            sellPriceMax =  binding.sellPriceMax.text.toString().toIntOrNull(),
            volumeMin =  binding.volumeMin.text.toString().toIntOrNull(),
            volumeMax = binding.volumeMax.text.toString().toIntOrNull(),
            marginMin = binding.marginMin.text.toString().toIntOrNull(),
            marginMax = binding.marginMax.text.toString().toIntOrNull(),
            buyLimitMin = binding.buyLimitMin.text.toString().toIntOrNull(),
            buyLimitMax = binding.buyLimitMax.text.toString().toIntOrNull()
        )

        itemListViewModel.updateFilterCriteria(newFilterCriteria)
    }
}