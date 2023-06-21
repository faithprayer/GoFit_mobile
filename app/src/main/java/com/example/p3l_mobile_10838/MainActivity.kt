package com.example.p3l_mobile_10838

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.p3l_mobile_10838.api.LoginApi
import com.example.p3l_mobile_10838.model.Auth
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.lang.Exception
import java.nio.charset.StandardCharsets
import kotlin.jvm.Throws
class MainActivity : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var mainLayout: ConstraintLayout
    private lateinit var usernameInput : TextInputEditText
    private lateinit var PasswordInput : TextInputEditText
    lateinit var mBundle: Bundle

    private var queue: RequestQueue? = null

    var vUsername: String =""
    lateinit var vNoHandphone: String
    lateinit var vEmail: String
    lateinit var vTglLahir:String
    var vPassword: String =""

    var checkLogin = true

    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        val isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun",true)

        if(isFirstRun){
            startActivity(Intent(this@MainActivity,SplashScreen :: class.java))
            finish()
        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun",false).commit()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle("User Login")
        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences("session",Context.MODE_PRIVATE)
        queue = Volley.newRequestQueue(this)

        inputUsername = findViewById(R.id.inputLayoutUsername)
        inputPassword = findViewById(R.id.inputLayoutPassword)
        mainLayout = findViewById(R.id.loginLayout)

        usernameInput = findViewById(R.id.TextInputUsername)
        PasswordInput = findViewById(R.id.textInputPassword)

        val btnLogin: Button = findViewById(R.id.btnLogin)
        val textMoveForgotPass : TextView = findViewById(R.id.textMoveForgotPass)
        val btnInformasiUmum: Button = findViewById(R.id.btnInformasiUmum)

        getBundle()

        btnLogin.setOnClickListener (View.OnClickListener{
            inputUsername.setError(null)
            inputPassword.setError(null)
            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()

            if (username.isEmpty()) {
                inputUsername.setError("Username must be filled with text")
                checkLogin = false
            }

            if (password.isEmpty()) {
                inputPassword.setError("Password must be filled with text")
                checkLogin = false
            }

            if(!checkLogin){
                warningLogin()
                return@OnClickListener
            }else{
                Login()
            }
        })
        textMoveForgotPass.setOnClickListener(View.OnClickListener{
            MotionToast.createToast(this,
                "Success",
                "Move to Forgot password successfully!",
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
            val moveDaftar = Intent(this@MainActivity, ForgotPassword::class.java)
            startActivity(moveDaftar)
        })

        btnInformasiUmum.setOnClickListener (View.OnClickListener{
            MotionToast.createToast(this,
                "Success",
                "Move to General Information successfully!",
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
            val moveInformasiUmum = Intent(this@MainActivity, InformasiUmumActivity::class.java)
            startActivity(moveInformasiUmum)
        })
    }

    fun getBundle() {
        if(intent.getBundleExtra("register") != null){
            mBundle = intent.getBundleExtra("register")!!
            vUsername = mBundle.getString("nama")!!
            vNoHandphone = mBundle.getString("noHandphone")!!
            vEmail = mBundle.getString("email")!!
            vTglLahir = mBundle.getString("tanggalLahir")!!
            vPassword = mBundle.getString("password")!!
            usernameInput.setText(vUsername)
            PasswordInput.setText(vPassword)
        }

    }

    fun warningLogin() {
        val builder = AlertDialog.Builder(this)
        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            Toast.makeText(applicationContext,
                android.R.string.no, Toast.LENGTH_SHORT).show()
        }
        builder.setMessage("Username dan Password Salah")
        builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveButtonClick))
        builder.show()
    }

    private fun Login() {
        //  setLoading(true)

        val userModel = Auth(
            inputUsername.getEditText()?.getText().toString(),
            inputPassword.getEditText()?.getText().toString(),
        )
        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, LoginApi.LOGIN_URL, Response.Listener { response ->
                val gson = Gson()
                var jsonObj = JSONObject(response.toString())
                var userObjectData = jsonObj.getJSONObject("user")

                if(userObjectData.has("ID_MEMBER")){
                    val token = jsonObj.getString("access_token")
                    val move = Intent(this@MainActivity, HomeActivity::class.java)
                    MotionToast.createToast(this,
                        "Login Member success",
                        "Login successfully!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    sharedPreferences.edit()
                        .putString("id", userObjectData.getString("ID_MEMBER"))
                        .putString("role", "member")
                        .putString("token", token)
                        .apply()
                    startActivity(move)
                }else if(userObjectData.has("ID_PEGAWAI")){
                    val token = jsonObj.getString("access_token")
                    sharedPreferences.edit()
                        .putInt("id", userObjectData.getInt("ID_PEGAWAI"))
                        .putString("role", "Manajer Operasional")
                        .putString("token", token)
                        .apply()
                    MotionToast.createToast(this,
                        sharedPreferences.getString("role",null),
                        "",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    val move = Intent(this@MainActivity, HomeActivity::class.java)
                    startActivity(move)
                }else if(userObjectData.has("ID_INSTRUKTUR")){
                    val token = jsonObj.getString("access_token")
                    sharedPreferences.edit()
                        .putInt("id", userObjectData.getInt("ID_INSTRUKTUR"))
                        .putString("role", "instruktur")
                        .putString("token", token)
                        .apply()
                    MotionToast.createToast(this,
                        "Login Instructor success",
                        "Login successfully!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                    val move = Intent(this@MainActivity, HomeActivity::class.java)
                    startActivity(move)
                }

            }, Response.ErrorListener { error ->

                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@MainActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
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
                    val requestBody = gson.toJson(userModel)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        queue!!.add(stringRequest)
    }
}