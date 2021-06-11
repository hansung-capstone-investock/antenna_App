package com.example.antenna.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.example.antenna.R


class ClickFragment : Fragment() {
    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater,
            @Nullable container: ViewGroup?,
            @Nullable savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_antenna, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /*val years = resources.getStringArray(R.array.spinner_code)
        val months = DateFormatSymbols().months*/

        super.onViewCreated(view, savedInstanceState)
    }

//    private fun setupSpinnerYear() {
//        val years = resources.getStringArray(R.array.spinner_year)
//        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
//        binding.spinnerYear.adapter = adapter
//    }

    /*private fun setupSpinnerMonth() {
        val months = DateFormatSymbols().months
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, months)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMonth.adapter = adapter
    }*/
}
