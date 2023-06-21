package com.example.p3l_mobile_10838.Member

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.p3l_mobile_10838.HomeActivity
import com.example.p3l_mobile_10838.Instruktur.HistoriInstruktur
import com.example.p3l_mobile_10838.MainActivity
import com.example.p3l_mobile_10838.R
import com.example.p3l_mobile_10838.api.LoginApi
import com.example.p3l_mobile_10838.api.MemberApi
import com.example.p3l_mobile_10838.databinding.FragmentHomeMemberBinding
import com.example.p3l_mobile_10838.databinding.FragmentProfileMemberBinding
import com.example.p3l_mobile_10838.model.Auth
import com.example.p3l_mobile_10838.model.Member
import com.google.gson.Gson
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets

class ProfileMemberFrag : Fragment() {
    private var _binding: FragmentProfileMemberBinding? = null

    private val binding get() = _binding!!

    private var queue: RequestQueue? = null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileMemberBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = (activity as HomeActivity).getSharedPreferences()

        val id = sharedPreferences.getString("id", null)

        queue = Volley.newRequestQueue(activity)

        getProfileById(id)


        binding.btnHistoriMember.setOnClickListener{
            val move = Intent(activity, HistoriMember::class.java)
            startActivity(move)
        }

        binding.btnLogoutProfileMember.setOnClickListener{
            logout()
        }
    }

    private fun getProfileById(id: String?){

        val stringRequest: StringRequest = object :
            StringRequest(
                Method.GET, MemberApi.GET_BY_ID_URL + id,
                { response ->

                    var jo = JSONObject(response.toString())
                    var member = arrayListOf<Member>()
                    var id : Int = jo.getJSONArray("data").length()

                    for(i in 0 until id) {
                        var data = Member(
                            jo.getJSONArray("data").getJSONObject(i).getString("ID_MEMBER"),
                            jo.getJSONArray("data").getJSONObject(i).getString("NAMA_MEMBER"),
                            jo.getJSONArray("data").getJSONObject(i).getString("EMAIL_MEMBER"),
                            jo.getJSONArray("data").getJSONObject(i).getString("MASA_AKTIVASI"),
                            jo.getJSONArray("data").getJSONObject(i).getString("SISA_DEPOSIT_MEMBER"),
                            jo.getJSONArray("data").getJSONObject(i).getString("DEPO_SISA")
                        )
                        member.add(data)
                    }
                    binding.viewUsernameMember.setText(jo.getJSONArray("data").getJSONObject(0).getString("NAMA_MEMBER"))
                    binding.viewEmailMember.setText(jo.getJSONArray("data").getJSONObject(0).getString("EMAIL_MEMBER"))
                    if(jo.getJSONArray("data").getJSONObject(0).getString("MASA_AKTIVASI") == "null"){
                        binding.viewMasaAktivasiMember.setText("Belum Aktivasi")
                    }else{
                        binding.viewMasaAktivasiMember.setText(jo.getJSONArray("data").getJSONObject(0).getString("MASA_AKTIVASI"))
                    }

                    binding.viewSisaDepositMember.setText("Rp."+jo.getJSONArray("data").getJSONObject(0).getString("SISA_DEPOSIT_MEMBER").toString())
                    binding.viewDepoSisa.setText(""+jo.getJSONArray("data").getJSONObject(0).getString("DEPO_SISA").toString())


                    MotionToast.createToast(context as Activity,
                        "Profile Data success",
                        "Get Profile Data Member successfully!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular))
                },
                Response.ErrorListener{ error ->
                    try{
                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        MotionToast.darkColorToast(context as Activity,"Notification Login!",
                            errors.getString("message"),
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular))

                    } catch (e: Exception){
                        Toast.makeText(activity, e.message,
                            Toast.LENGTH_LONG).show();
                    }
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = java.util.HashMap<String, String>()
                headers["Authorization"] = "Bearer " + sharedPreferences.getString("token",null);
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)

    }

    private fun logout(){
        val auth = Auth(
            "",
            "")

        val stringRequest: StringRequest =
            object : StringRequest(Request.Method.POST, LoginApi.LOGOUT_URL, Response.Listener { response ->
                val gson = Gson()
                var user_logout = gson.fromJson(response, Auth::class.java)


                if(user_logout != null) {

                    val intent = Intent(activity, MainActivity::class.java)
                    MotionToast.darkColorToast(context as Activity,"Notification Logout!",
                        "Succesfully Logout!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular))
                    sharedPreferences.edit()
                        .putInt("id",-1)
                        .putString("id", null)
                        .putString("role",null)
                        .putString("token",null)
                        .apply()
                    startActivity(intent)
                }
                else {
                    MotionToast.darkColorToast(context as Activity,"Notification Logout!",
                        "Failed Logout",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular))
                }
                return@Listener
            }, Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    MotionToast.darkColorToast(context as Activity,"Notification Login!",
                        errors.getString("message"),
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(context as Activity, www.sanju.motiontoast.R.font.helvetica_regular))
                }catch (e: Exception) {
                    Toast.makeText(activity, e.message,
                        Toast.LENGTH_LONG).show()
                }
            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    headers["Authorization"] = "Bearer " + sharedPreferences.getString("token",null);
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(auth)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8;"
                }
            }
        queue!!.add(stringRequest)
    }
}