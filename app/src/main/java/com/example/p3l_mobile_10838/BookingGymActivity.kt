package com.example.p3l_mobile_10838

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.p3l_mobile_10838.api.IzinInstrukturApi.Companion.GET_ALL_URL
import com.example.p3l_mobile_10838.api.MemberApi
import com.example.p3l_mobile_10838.databinding.ActivityBookingGymBinding
import com.example.p3l_mobile_10838.model.BookingGym
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets

class BookingGymActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingGymBinding
    private var queue: RequestQueue? = null
    private lateinit var sharedPreferences: SharedPreferences

    private var srBooking: SwipeRefreshLayout? = null
    private var adapter: BookingGymAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookingGymBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE)

        val id = sharedPreferences.getString("id", null)
        queue = Volley.newRequestQueue(this)

        srBooking = findViewById(R.id.sr_booking)

        binding.srBooking.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            if(id != null){
                allDataBooking(id)
            }
        })

        if(id != null){
            allDataBooking(id)
        }
        binding.buttonCreate.setOnClickListener {
            val intent = Intent(this@BookingGymActivity, AddBookingGym::class.java)
            startActivity(intent)
        }
        binding.buttonHome.setOnClickListener {
            val intent = Intent(this@BookingGymActivity, HomeActivity::class.java)
            startActivity(intent)
        }

    }

    private fun allDataBooking(id: String) {
        binding.srBooking.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, MemberApi.GET_ALL_GYM_URL+ id, Response.Listener { response ->
                    var jo = JSONObject(response.toString())
                    var schedule = arrayListOf<BookingGym>()
                    var id: Int = jo.getJSONArray("data").length()

                    for (i in 0 until id) {
                        var data = BookingGym(
                            jo.getJSONArray("data").getJSONObject(i).getString("KODE_BOOKING_GYM"),
                            jo.getJSONArray("data").getJSONObject(i).getString("ID_MEMBER"),
                            jo.getJSONArray("data").getJSONObject(i).getString("SLOT_WAKTU_GYM"),
                            jo.getJSONArray("data").getJSONObject(i).getString("STATUS_PRESENSI_GYM"),
                            jo.getJSONArray("data").getJSONObject(i).getString("TANGGAL_BOOKING_GYM"),
                            jo.getJSONArray("data").getJSONObject(i).getString("TANGGAL_MELAKUKAN_BOOKING"),
                            jo.getJSONArray("data").getJSONObject(i).getString("WAKTU_KONFIRMASI_PRESENSI"),
                        )
                        schedule.add(data)
                    }
                    var data_array: Array<BookingGym> = schedule.toTypedArray()

                    val layoutManager = LinearLayoutManager(this)
                    val adapter: BookingGymAdapter = BookingGymAdapter(schedule, this)
                    val rvPermission: RecyclerView = findViewById(R.id.rv_booking)

                    rvPermission.layoutManager = layoutManager
                    rvPermission.setHasFixedSize(true)
                    rvPermission.adapter = adapter

                    binding.srBooking.isRefreshing = false

                    if (!data_array.isEmpty()) {
                        MotionToast.createToast(this,
                            "Data success",
                            "Get Data successfully!",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    } else {
                        MotionToast.createToast(this,
                            "Data failed",
                            "Get Data Failed!",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this , www.sanju.motiontoast.R.font.helvetica_regular))
                    }

                },
                Response.ErrorListener { error ->
                    binding.srBooking.isRefreshing = true
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
                        binding.srBooking.isRefreshing = false
                    } catch (e: Exception) {
                        Toast.makeText(this@BookingGymActivity, e.message, Toast.LENGTH_SHORT).show()
                        MotionToast.darkToast(
                            this,"Notification Display!",
                            e.message.toString(),
                            MotionToastStyle.INFO,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                        binding.srBooking.isRefreshing = false
                    }

                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Authorization"] = "Bearer " + sharedPreferences.getString("token", null);
                return headers
            }
        }
        queue!!.add(stringRequest)


    }
    fun cancelBookingGym(id: String) {
        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE, MemberApi.BATALGYM + id, Response.Listener { response ->
                var jo = JSONObject(response.toString())
//                var history = arrayListOf<HistoryBookingClass>()
                if (jo.getJSONObject("data") != null) {
                    MotionToast.createToast(this,
                        "Cancel Gym success",
                        "Canceled Gym successfully!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    val intent = Intent(this@BookingGymActivity, BookingGymActivity::class.java)
                    startActivity(intent)
                }else {
                    MotionToast.createToast(this,
                        "Cancel Gym failed",
                        "Canceled Gym Failed!",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this , www.sanju.motiontoast.R.font.helvetica_regular))
                }

            }, Response.ErrorListener { error ->
                binding.srBooking.isRefreshing = true
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
                    binding.srBooking.isRefreshing = false
                } catch (e: Exception){
                    MotionToast.darkColorToast(this,
                        "Notification Error",
                        "e.message",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    binding.srBooking.isRefreshing = false
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