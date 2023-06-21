package com.example.p3l_mobile_10838.BookingKelas

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.p3l_mobile_10838.HomeActivity
import com.example.p3l_mobile_10838.R
import com.example.p3l_mobile_10838.api.BookingKelasApi
import com.example.p3l_mobile_10838.api.MemberApi
import com.example.p3l_mobile_10838.databinding.ActivityBookingKelasBinding
import com.example.p3l_mobile_10838.model.BookingKelas
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets

class BookingKelasActivity : AppCompatActivity(){
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityBookingKelasBinding
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingKelasBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()
        sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE)

        val id = sharedPreferences.getString("id",null)
        queue = Volley.newRequestQueue(this)

        binding.srBookingKelas.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener{
            if (id != null) {
                allData(id)
            }
        })
        if (id != null) {
            allData(id)
        }
        binding.buttonHome.setOnClickListener {
            val intent = Intent(this@BookingKelasActivity, HomeActivity::class.java)
            startActivity(intent)
        }

        binding.addBtnBookingkelas.setOnClickListener {
            val intent = Intent(this@BookingKelasActivity, AddBookingKelas::class.java)
            sharedPreferences.edit()
                .putString("booking","yes")
                .apply()
            startActivity(intent)
        }
    }


    fun cancelBookingKelas(id: String) {
        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE, MemberApi.DELETEDATABOOKINGKELAS + id, Response.Listener { response ->
                var jo = JSONObject(response.toString())

                if (jo.getJSONObject("data") != null) {
                    MotionToast.createToast(this,
                        "Cancel Class success",
                        "Canceled Class successfully!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this@BookingKelasActivity, www.sanju.motiontoast.R.font.helvetica_regular))
                    val intent = Intent(this@BookingKelasActivity, BookingKelasActivity::class.java)
                    startActivity(intent)
                }else {
                    MotionToast.createToast(this,
                        "Cancel Class failed",
                        "Canceled Class Failed!",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this , www.sanju.motiontoast.R.font.helvetica_regular))
                }

            }, Response.ErrorListener { error ->
                binding.srBookingKelas.isRefreshing = true
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    MotionToast.darkColorToast(this,
                        "Notification Error",
                        errors.getString("message"),
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    binding.srBookingKelas.isRefreshing = false
                } catch (e: Exception){
                    MotionToast.darkColorToast(this,
                        "Notification Error",
                        "e.message",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    binding.srBookingKelas.isRefreshing = false
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Authorization"] = "Bearer " + sharedPreferences.getString("token",null);
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    private fun allData(id: String) {
        binding.srBookingKelas.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, BookingKelasApi.GETDATABOOKINGKELAS + id, Response.Listener { response ->
                var jo = JSONObject(response.toString())
                var history = arrayListOf<BookingKelas>()
                var id : Int = jo.getJSONArray("data").length()

                for(i in 0 until id) {
                    var data = BookingKelas(
                        jo.getJSONArray("data").getJSONObject(i).getString("KODE_BOOKING_KELAS"),
                        jo.getJSONArray("data").getJSONObject(i).getString("NAMA_KELAS"),
                        jo.getJSONArray("data").getJSONObject(i).getString("NAMA_INSTRUKTUR"),
                        jo.getJSONArray("data").getJSONObject(i).getString("TANGGAL_JADWAL_HARIAN"),
                        jo.getJSONArray("data").getJSONObject(i).getString("TANGGAL_MELAKUKAN_BOOKING"),
                        jo.getJSONArray("data").getJSONObject(i).getString("WAKTU_KONFIRMASI_PRESENSI_KELAS"),
                        jo.getJSONArray("data").getJSONObject(i).getString("STATUS_PRESENSI_KELAS")
                    )
                    history.add(data)
                }
                var data_array: Array<BookingKelas> = history.toTypedArray()

                val layoutManager = LinearLayoutManager(this)
                val adapter : BookingKelasAdapter = BookingKelasAdapter(history,this)
                val rvPermission : RecyclerView = findViewById(R.id.rv_booking)

                rvPermission.layoutManager = layoutManager
                rvPermission.setHasFixedSize(true)
                rvPermission.adapter = adapter

                binding.srBookingKelas.isRefreshing = false

                if (!data_array.isEmpty()) {
                    MotionToast.createToast(this,
                        "Data success",
                        "Get Data successfully!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                }else {
                    MotionToast.createToast(this,
                        "Data failed",
                        "Get Data Failed!",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this , www.sanju.motiontoast.R.font.helvetica_regular))
                }

            }, Response.ErrorListener { error ->
                binding.srBookingKelas.isRefreshing = true
                try {

                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    MotionToast.darkColorToast(this,
                            "Notification Error",
                            errors.getString("message"),
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))

                    binding.srBookingKelas.isRefreshing = false
                } catch (e: Exception){
                    Toast.makeText(this@BookingKelasActivity, e.message, Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        this,"Notification Display!",
                        e.message.toString(),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    binding.srBookingKelas.isRefreshing = false
                }

            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Authorization"] = "Bearer " + sharedPreferences.getString("token",null);
                return headers
            }
        }
        queue!!.add(stringRequest)

        }
}