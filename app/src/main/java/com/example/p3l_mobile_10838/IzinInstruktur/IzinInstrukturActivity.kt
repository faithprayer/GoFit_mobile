package com.example.p3l_mobile_10838.IzinInstruktur

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
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
import com.example.p3l_mobile_10838.api.IzinInstrukturApi
import com.example.p3l_mobile_10838.model.IzinInstruktur
import kotlinx.android.synthetic.main.activity_booking_gym.*
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets

class IzinInstrukturActivity: AppCompatActivity() {
    private var queue: RequestQueue? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var layoutLoading: LinearLayout? = null
    private var izinAdapter: IzinInstrukturAdapter? = null
    private var srIzin: SwipeRefreshLayout? = null

    companion object{
        const val LAUNCH_ADD_ACTIVITY = 123
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_izin_instruktur)

        queue = Volley.newRequestQueue(this)
        srIzin = findViewById(R.id.sr_Izin)
        layoutLoading = findViewById(R.id.layout_loading)
        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE)

        val id = sharedPreferences.getInt("id", -1)
        queue = Volley.newRequestQueue(this)

        srIzin?.setOnRefreshListener (SwipeRefreshLayout.OnRefreshListener {
            allDataIzin(id)
        })
        val btnHome = findViewById<ImageButton>(R.id.button_home)
        btnHome.setOnClickListener {
            val intent = Intent(this@IzinInstrukturActivity, HomeActivity::class.java)
            startActivity(intent)
        }

        val fabAdd = findViewById<Button>(R.id.button_create)
        fabAdd.setOnClickListener{
            val i = Intent(this@IzinInstrukturActivity, AddIzinInstruktur::class.java)
            startActivityForResult(i, LAUNCH_ADD_ACTIVITY)
        }
        allDataIzin(id)
    }

    private fun allDataIzin(id: Int) {
        srIzin!!.isRefreshing = true

        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, IzinInstrukturApi.GET_ALL_URL + id, Response.Listener{ response ->
                var jo = JSONObject(response.toString())
                var instrukturArray = arrayListOf<IzinInstruktur>()
                var id : Int = jo.getJSONArray("data").length()

                for(i in 0 until id) {
                    var data = IzinInstruktur(
                        jo.getJSONArray("data").getJSONObject(i).getInt("ID_INSTRUKTUR"),
                        jo.getJSONArray("data").getJSONObject(i).getString("TANGGAL_IZIN_INSTRUKTUR"),
                        jo.getJSONArray("data").getJSONObject(i).getString("KETERANGAN_IZIN"),
                        jo.getJSONArray("data").getJSONObject(i).getString("TANGGAL_MELAKUKAN_IZIN"),
                        jo.getJSONArray("data").getJSONObject(i).getString("STATUS_IZIN"),
                        jo.getJSONArray("data").getJSONObject(i).getString("TANGGAL_KONFIRMASI_IZIN"),
                        jo.getJSONArray("data").getJSONObject(i).getString("INSTRUKTUR_PENGGANTI"),
                    )
                    instrukturArray.add(data)
                }

                var dataArray: Array<IzinInstruktur> = instrukturArray.toTypedArray()


                val layoutManager = LinearLayoutManager(this)
                val adapter : IzinInstrukturAdapter = IzinInstrukturAdapter(instrukturArray,this)
                val rvPermission : RecyclerView = findViewById(R.id.rv_izin)

                rvPermission.layoutManager = layoutManager
                rvPermission.setHasFixedSize(true)
                rvPermission.adapter = adapter


                srIzin!!.isRefreshing = false

                if(!dataArray.isEmpty())
                    MotionToast.createToast(this,
                        "Data success",
                        "Get Data successfully!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                else
                    MotionToast.createToast(this,
                        "Data failed",
                        "Get Data Failed!",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this , www.sanju.motiontoast.R.font.helvetica_regular))

            }, Response.ErrorListener{ error ->
                srIzin!!.isRefreshing = true

                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    MotionToast.darkColorToast(this,
                        "Notification Error",
                        errors.getString("message"),
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                }catch(e: Exception){
                    MotionToast.darkToast(
                        this,"Notification Display!",
                        e.message.toString(),
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String>{
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer " + sharedPreferences.getString("token",null);
                headers["Accept"] = "application/json"
                return headers
            }
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8;"
            }
        }
        queue!!.add(stringRequest)
    }

    private fun setLoading(isLoading: Boolean){
        if(isLoading){
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading!!.visibility = View.VISIBLE
        }else{
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.GONE
        }
    }



}