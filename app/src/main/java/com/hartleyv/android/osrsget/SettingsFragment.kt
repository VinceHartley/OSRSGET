package com.hartleyv.android.osrsget

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


    private val sharedViewModel: SharedViewModel by viewModels({requireParentFragment()})

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


        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupUserInputListeners()
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


        sharedViewModel.updateFilterCriteria(newFilterCriteria)
    }
}