package com.example.p3l_mobile_10838

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.p3l_mobile_10838.api.MemberApi
import com.example.p3l_mobile_10838.databinding.ActivityAddBookingGymBinding
import com.example.p3l_mobile_10838.model.BookingGym
import com.google.gson.Gson
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class AddBookingGym : AppCompatActivity() {

    companion object{
        private val SLOT_LIST = arrayOf(
            "07:00-09:00",
            "09:00-11:00",
            "11:00-13:00",
            "13:00-15:00",
            "15:00-17:00",
            "17:00-19:00",
            "19:00-21:00")
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityAddBookingGymBinding
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBookingGymBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()
        sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE)

        val id = sharedPreferences.getString("id",null)
        queue = Volley.newRequestQueue(this)

        setExposedDropDownMenu()

        val cal = Calendar.getInstance()
        val myYear = cal.get(Calendar.YEAR)
        val myMonth = cal.get(Calendar.MONTH)
        val myDay = cal.get(Calendar.DAY_OF_MONTH)

        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)

        binding.tglBookingGymInput.setOnFocusChangeListener { view, b ->
            val datePicker = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    var date: String = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);
                    binding.tglBookingGymInput.setText(date)
                }, myYear, myMonth, myDay
            )
            if (b) {
                datePicker.show()
            } else {
                datePicker.hide()
            }
        }

        binding.btnSave.setOnClickListener {
            if (id != null) {
                storeBookingGym(id)
            }
        }

        binding.btnCancel.setOnClickListener {
            val intent = Intent(this@AddBookingGym, BookingGymActivity::class.java)
            startActivity(intent)
        }
    }

    fun setExposedDropDownMenu(){
        val adapterslot: ArrayAdapter<String> = ArrayAdapter<String>(this,
            R.layout.item_list_dropdown,R.id.dataList, SLOT_LIST)
        binding.SlotWaktuGymInput.setAdapter(adapterslot)
    }

    private fun storeBookingGym(id: String){
        val booking = BookingGym(
        "",
            id,
            binding.layoutSlotWaktuGym.getEditText()?.getText().toString(),
            null,
            binding.layoutTglBookingGym.getEditText()?.getText().toString(),
            null,
            null,
        )

        val stringRequest: StringRequest =
            object : StringRequest(Request.Method.POST, MemberApi.STOREDATAGYM, Response.Listener { response ->
                val gson = Gson()
                var booking_gym = gson.fromJson(response, BookingGymActivity::class.java)

                if(booking_gym!= null) {
                    MotionToast.darkColorToast(this,"Notification Success!",
                        "Succesfully Create Booking Gym!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    val intent = Intent(this@AddBookingGym, BookingGymActivity::class.java)
                    startActivity(intent)
                }
                else {
                    MotionToast.darkColorToast(this,"Notification Failed!",
                        "Failed Create Booking Gym",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                }
                return@Listener
            }, Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    MotionToast.darkColorToast(this,"Notification Error!",
                        errors.getString("message"),
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                }catch (e: java.lang.Exception) {
                    Toast.makeText(this@AddBookingGym, e.message,
                        Toast.LENGTH_LONG).show();
                }
            }) {
                @kotlin.jvm.Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    headers["Authorization"] = "Bearer " + sharedPreferences.getString("token",null);
                    return headers
                }

                @kotlin.jvm.Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(booking)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8;"
                }
            }
        queue!!.add(stringRequest)
    }
}