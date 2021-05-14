package com.example.antenna.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import com.example.antenna.login.LoginActivity
import com.example.antenna.R
import kotlinx.android.synthetic.main.fragment_info.*

class InfoFragment : Fragment() {

    @Nullable
    override fun onCreateView(
            inflater: LayoutInflater,
            @Nullable container: ViewGroup?,
            @Nullable savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button4.setOnClickListener {
            activity?.let {
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }
}