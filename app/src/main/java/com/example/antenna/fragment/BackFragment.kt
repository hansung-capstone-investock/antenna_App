package com.example.antenna.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import com.example.antenna.BackResult
import com.example.antenna.R
import kotlinx.android.synthetic.main.fragment_back.*

class BackFragment : Fragment() {
    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater,
            @Nullable container: ViewGroup?,
            @Nullable savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_back, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        backbutton.setOnClickListener {
            val intentBack = Intent(context, BackResult::class.java)

            startActivity(intentBack)
        }

        super.onViewCreated(view, savedInstanceState)
    }
}