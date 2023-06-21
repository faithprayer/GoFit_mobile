package com.example.p3l_mobile_10838.Instruktur

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
import com.example.p3l_mobile_10838.IzinInstruktur.IzinInstrukturActivity
import com.example.p3l_mobile_10838.MainActivity
import com.example.p3l_mobile_10838.R
import com.example.p3l_mobile_10838.api.InstrukturApi
import com.example.p3l_mobile_10838.api.LoginApi
import com.example.p3l_mobile_10838.databinding.FragmentHomeInstrukturBinding
import com.example.p3l_mobile_10838.databinding.FragmentProfileInstrukturBinding
import com.example.p3l_mobile_10838.databinding.FragmentProfileMemberBinding
import com.example.p3l_mobile_10838.model.Auth
import com.example.p3l_mobile_10838.model.Instruktur
import com.google.gson.Gson
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets

class ProfileInstrukturFrag : Fragment() {
    private var _binding: FragmentProfileInstrukturBinding? = null

    private val binding get() = _binding!!

    private var queue: RequestQueue? = null
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_profile_instruktur, container, false)
        _binding = FragmentProfileInstrukturBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = (activity as HomeActivity).getSharedPreferences()

        val id = sharedPreferences.getInt("id", 0)

        queue = Volley.newRequestQueue(activity as HomeActivity)

        getProfileById(id)

        binding.btnHistroiInstruktur.setOnClickListener{
            val move = Intent(activity, HistoriInstruktur::class.java)
            startActivity(move)
        }

        binding.btnLogoutProfileInstruktur.setOnClickListener{
            logout()
        }
    }

    private fun getProfileById(id:Int){

        val stringRequest: StringRequest = object :
            StringRequest(
                Method.GET, InstrukturApi.GET_BY_ID_URL + id,
                { response ->
                    val jsonObject = JSONObject(response.toString())
                    val json = jsonObject.getJSONObject("data")
                    val Instruktur = Gson().fromJson(json.toString(), Instruktur::class.java)

                    binding!!.viewUsernameInstruktur.setText(Instruktur.NAMA_INSTRUKTUR)
                    binding!!.viewEmailInstruktur.setText( Instruktur.EMAIL_INSTRUKTUR)
//                    binding!!.view.setText("Jenis Kelamin : " + Instruktur.JENIS_KELAMIN_INSTRUKTUR)
                    binding!!.viewNomorTeleponInstruktur.setText(Instruktur.TELEPON_INSTRUKTUR)
                    binding!!.viewWaktuTerlambatInstruktur.setText( Instruktur.JUMLAH_TERLAMBAT)
                    if(Instruktur.JUMLAH_TERLAMBAT.isNullOrEmpty()){
                        binding!!.viewWaktuTerlambatInstruktur.setText("0");
                    }

                    MotionToast.createToast(context as Activity,
                        "Profile Data success",
                        "Get Profile Data Instructor successfully!",
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