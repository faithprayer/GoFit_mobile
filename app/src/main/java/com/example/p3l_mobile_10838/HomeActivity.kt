package com.example.p3l_mobile_10838

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.p3l_mobile_10838.Instruktur.HomeInstrukturFrag
import com.example.p3l_mobile_10838.Instruktur.ProfileInstrukturFrag
import com.example.p3l_mobile_10838.ManajerOperasional.HomeManajerOperasionalFrag
import com.example.p3l_mobile_10838.Member.HomeMemberFrag
import com.example.p3l_mobile_10838.Member.ProfileMemberFrag
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sharedPreferences = getSharedPreferences("session", MODE_PRIVATE)
        getSupportActionBar()?.hide()

        var role = sharedPreferences.getString("role",null)


        if(role == "member"){
            setThatFragments(HomeMemberFrag())
            botNavigation.setOnItemSelectedListener {
                when(it){
                    R.id.nav_home ->{
                        setThatFragments(HomeMemberFrag())
                    }
                    R.id.nav_profile ->{
                        setThatFragments(ProfileMemberFrag())
                    }
                }
                true
            }
        }

        if(role == "Manajer Operasional"){
            setThatFragments(HomeManajerOperasionalFrag())
            botNavigation.setOnItemSelectedListener {
                when(it){
                    R.id.nav_home ->{
                        setThatFragments(HomeManajerOperasionalFrag())
                    }
                }
                true
            }
        }

        if(role == "instruktur"){
            setThatFragments(HomeInstrukturFrag())
            botNavigation.setOnItemSelectedListener {
                when(it){
                    R.id.nav_home ->{
                        setThatFragments(HomeInstrukturFrag())
                    }
                    R.id.nav_profile ->{
                        setThatFragments(ProfileInstrukturFrag())
                    }

                }
                true
            }
        }



    }

    private fun setThatFragments(fragment : Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.layout_fragment,fragment)
            addToBackStack(null)
            commit()
        }
    }

    fun getSharedPreferences(): SharedPreferences {
        return sharedPreferences
    }
}