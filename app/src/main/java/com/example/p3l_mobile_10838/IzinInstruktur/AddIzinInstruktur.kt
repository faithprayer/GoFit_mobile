package com.example.p3l_mobile_10838.IzinInstruktur

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.p3l_mobile_10838.HomeActivity
import com.example.p3l_mobile_10838.R
import com.example.p3l_mobile_10838.api.IzinInstrukturApi
import com.example.p3l_mobile_10838.databinding.ActivityAddIzinInstrukturBinding
import com.example.p3l_mobile_10838.model.IzinInstruktur
import com.google.gson.Gson
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets

class AddIzinInstruktur : AppCompatActivity() {
    private var etIdIzinInstruktur: EditText? = null
    private var etNamaInstruktur: EditText? = null
    private var edTglIzin: EditText? = null
    private var etKeteranganIzin: EditText? = null
    private var etTanggalKonfirmasiIzin: EditText? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityAddIzinInstrukturBinding

    companion object{
        const val LAUNCH_ADD_ACTIVITY = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddIzinInstrukturBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        queue = Volley.newRequestQueue(this)

        supportActionBar?.hide()
        sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE)

        val id = sharedPreferences.getInt("id", 0)
        queue = Volley.newRequestQueue(this)

        setDropdownSchedule(id)

        binding.btnSave.setOnClickListener{
            storePermission(id)
        }
        binding.btnCancel.setOnClickListener {
            val intent = Intent(this@AddIzinInstruktur, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    fun setDropdownSchedule(id: Int) {
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, IzinInstrukturApi.GETJADWALHARIANINSTRUKTUR + id, Response.Listener { response ->
                var jo = JSONObject(response.toString())
                var schedule = arrayListOf<String>()
                var id : Int = jo.getJSONArray("data").length()

                for(i in 0 until id) {
                    schedule.add(jo.getJSONArray("data").getJSONObject(i).getString("TANGGAL_JADWAL_HARIAN"))
                }

                val adapter: ArrayAdapter<String> = ArrayAdapter(this,R.layout.item_list_dropdown,R.id.dataList, schedule)
                binding.etTanggalIzinInstruktur.setAdapter(adapter)

                if (!schedule.isEmpty()) {
                    MotionToast.darkColorToast(this,
                        "Notification Success!",
                        "Succesfully Create Instructor Permission!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))

                }else {
                }

            }, Response.ErrorListener { error ->
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

                } catch (e: Exception){
                    Toast.makeText(this@AddIzinInstruktur, e.message, Toast.LENGTH_SHORT).show()
                    MotionToast.darkToast(
                        this,"Notification Display!",
                        e.message.toString(),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
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


    private fun storePermission(id: Int){
        val permisson = IzinInstruktur(
            id,
            binding.etTanggalIzinInstruktur.text.toString(),
            binding.inputLayoutStatus.getEditText()?.getText().toString(),
            null,
            null,
            null,
            binding.inputLayoutNamaInstruktur.getEditText()?.getText().toString(),

        )

        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, IzinInstrukturApi.ADD_IZIN_INSTRUKTUR, Response.Listener { response ->
                val gson = Gson()
                var permission = gson.fromJson(response, IzinInstruktur::class.java)
                var resJO = JSONObject(response.toString())

                if(permission != null) {
                    MotionToast.darkColorToast(this,"Notification Success!",
                        "Succesfully Create Permission!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    val intent = Intent(this@AddIzinInstruktur, IzinInstrukturActivity::class.java)
//                    sharedPreferences.edit()
//                        .putInt("id",userobj.getInt("ID_PEGAWAI"))
//                        .putString("role","MO")
//                        .putString("token",token)
//                        .apply()
                    startActivity(intent)
                }
                else {
                    MotionToast.darkColorToast(this,"Notification Failed!",
                        "Failed Create Permission",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                }
                return@Listener
            }, Response.ErrorListener { error ->
                try {
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    MotionToast.darkColorToast(this,"Notification Error!",
                        errors.getString("message"),
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                }catch (e: java.lang.Exception) {
                    Toast.makeText(this@AddIzinInstruktur, e.message,
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
                    val requestBody = gson.toJson(permisson)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8;"
                }
            }
        queue!!.add(stringRequest)
    }
}