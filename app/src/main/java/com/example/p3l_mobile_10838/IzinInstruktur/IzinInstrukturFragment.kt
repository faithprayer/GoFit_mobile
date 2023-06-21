package com.example.p3l_mobile_10838.IzinInstruktur

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.example.p3l_mobile_10838.Instruktur.HomeInstrukturFrag
import com.example.p3l_mobile_10838.R

class IzinInstrukturFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_izin_instruktur, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnAdd: Button = view.findViewById(R.id.btnAdd)
        val btnHome: Button = view.findViewById(R.id.button_home)

        btnHome.setOnClickListener {
            val move = Intent(this@IzinInstrukturFragment.context, HomeInstrukturFrag::class.java)
            startActivity(move)
        }

        btnAdd.setOnClickListener(View.OnClickListener {
            val move = Intent(this@IzinInstrukturFragment.context, IzinInstrukturActivity::class.java)
            startActivity(move)
        })
    }
}