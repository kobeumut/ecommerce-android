package com.eteration.ecommerce.presentation.ui.filter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatCheckBox
import com.eteration.ecommerce.R
import com.eteration.ecommerce.domain.model.Filter
import com.eteration.ecommerce.domain.model.SortType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Bottom sheet for product filtering
 */
class FilterBottomSheet : BottomSheetDialogFragment() {

    private var onFilterAppliedListener: ((Filter) -> Unit)? = null

    // Sort options
    private lateinit var sortOldToNew: RadioButton
    private lateinit var sortNewToOld: RadioButton
    private lateinit var sortPriceLowToHigh: RadioButton
    private lateinit var sortPriceHighToLow: RadioButton

    // Brand options
    private lateinit var brandListView: LinearLayout
    private lateinit var modelListView: LinearLayout
    private lateinit var brandSearchEditText: EditText
    private lateinit var brandHonda: CheckBox
    private lateinit var brandVolvo: CheckBox
    private lateinit var brandApple: CheckBox
    private lateinit var brandSamsung: CheckBox
    private lateinit var brandHuawei: CheckBox

    // Model options
    private lateinit var modelSearchEditText: EditText
    private lateinit var impala: CheckBox
    private lateinit var focus: CheckBox
    private lateinit var alpine: CheckBox
    private lateinit var mustang: CheckBox
    private lateinit var golf: CheckBox
    private lateinit var model11: CheckBox
    private lateinit var model12Pro: CheckBox
    private lateinit var model13ProMax: CheckBox

    // Buttons
    private lateinit var closeButton: ImageView
    private lateinit var applyButton: Button

    private val selectedBrands = mutableSetOf<String>()
    private val selectedModels = mutableSetOf<String>()
    private var selectedSortType = SortType.OLD_TO_NEW

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupSortOptions()
        setupBrandOptions()
        setupModelOptions()
        setupSearchFields()
        setupButtons()
    }

    private fun initViews(view: View) {
        // Sort options
        sortOldToNew = view.findViewById(R.id.sort_old_to_new)
        sortNewToOld = view.findViewById(R.id.sort_new_to_old)
        sortPriceLowToHigh = view.findViewById(R.id.sort_price_low_to_high)
        sortPriceHighToLow = view.findViewById(R.id.sort_price_high_to_low)

        // Brand options
        brandListView = view.findViewById(R.id.brand_list)
        brandSearchEditText = view.findViewById(R.id.brand_search)
        brandApple  = AppCompatCheckBox(requireContext())
        brandSamsung = AppCompatCheckBox(requireContext())
        brandHuawei = AppCompatCheckBox(requireContext())
        brandVolvo= AppCompatCheckBox(requireContext())
        brandHonda = AppCompatCheckBox(requireContext())

        // Model options
        modelListView = view.findViewById(R.id.model_list)
        modelSearchEditText = view.findViewById(R.id.model_search)
        alpine = AppCompatCheckBox(requireContext())
        golf = AppCompatCheckBox(requireContext())
        mustang = AppCompatCheckBox(requireContext())
        model11 = AppCompatCheckBox(requireContext())
        model12Pro = AppCompatCheckBox(requireContext())
        model13ProMax = AppCompatCheckBox(requireContext())

        // Buttons
        closeButton = view.findViewById(R.id.close_button)
        applyButton = view.findViewById(R.id.apply_button)
    }

    private fun setupSortOptions() {
        sortOldToNew.isChecked = true

        val sortRadioButtons = listOf(
            sortOldToNew to SortType.OLD_TO_NEW,
            sortNewToOld to SortType.NEW_TO_OLD,
            sortPriceLowToHigh to SortType.PRICE_LOW_TO_HIGH,
            sortPriceHighToLow to SortType.PRICE_HIGH_TO_LOW
        )

        sortRadioButtons.forEach { (radioButton, sortType) ->
            radioButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedSortType = sortType
                    // Uncheck others
                    sortRadioButtons.forEach { (otherButton, _) ->
                        if (otherButton != radioButton) {
                            otherButton.isChecked = false
                        }
                    }
                }
            }
        }
    }
    //TODO: brands and model datas can be fetched from mockapi via dynamic mapping
    private fun setupBrandOptions() {
        val brandCheckBoxes = mapOf(
            brandVolvo to "Volvo",
            brandHonda to "Honda",
            brandApple to "Apple",
            brandSamsung to "Samsung",
            brandHuawei to "Huawei"
        )

        brandCheckBoxes.forEach { (checkBox, brand) ->
            checkBox.text = brand
            brandListView.addView(checkBox)
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedBrands.add(brand)
                } else {
                    selectedBrands.remove(brand)
                }
            }
        }
    }

    private fun setupModelOptions() {
        val modelCheckBoxes = mapOf(
            alpine to "Alpine",
            golf to "Golf",
            mustang to "Mustang",
            model11 to "11",
            model12Pro to "12 Pro",
            model13ProMax to "13 Pro Max"
        )

        modelCheckBoxes.forEach { (checkBox, model) ->
            checkBox.text = model
            modelListView.addView(checkBox)
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedModels.add(model)
                } else {
                    selectedModels.remove(model)
                }
            }
        }
    }

    private fun setupSearchFields() {
        // Brand search filter
        brandSearchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().lowercase()
                brandVolvo.visibility = if ("Volvo".contains(query)) View.VISIBLE else View.GONE
                brandHonda.visibility = if ("Honda".contains(query)) View.VISIBLE else View.GONE
                brandApple.visibility = if ("apple".contains(query)) View.VISIBLE else View.GONE
                brandSamsung.visibility = if ("samsung".contains(query)) View.VISIBLE else View.GONE
                brandHuawei.visibility = if ("huawei".contains(query)) View.VISIBLE else View.GONE
            }
        })

        // Model search filter
        modelSearchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().lowercase()
                focus.visibility = if ("focus".contains(query)) View.VISIBLE else View.GONE
                impala.visibility = if ("impala".contains(query)) View.VISIBLE else View.GONE
                model11.visibility = if ("11".contains(query)) View.VISIBLE else View.GONE
                model12Pro.visibility = if ("12 pro".contains(query)) View.VISIBLE else View.GONE
                model13ProMax.visibility = if ("13 pro max".contains(query)) View.VISIBLE else View.GONE
            }
        })
    }

    private fun setupButtons() {
        closeButton.setOnClickListener {
            dismiss()
        }

        applyButton.setOnClickListener {
            val filter = Filter(
                sortBy = selectedSortType,
                selectedBrands = selectedBrands.toSet(),
                selectedModels = selectedModels.toSet()
            )
            onFilterAppliedListener?.invoke(filter)

            dismiss()
        }
    }

    fun setOnFilterAppliedListener(listener: (Filter) -> Unit) {
        onFilterAppliedListener = listener
    }
}