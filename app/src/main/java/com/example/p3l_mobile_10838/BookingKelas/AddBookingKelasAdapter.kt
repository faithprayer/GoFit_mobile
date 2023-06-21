package com.example.p3l_mobile_10838.BookingKelas

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.p3l_mobile_10838.HomeActivity
import com.example.p3l_mobile_10838.R
import com.example.p3l_mobile_10838.model.JadwalHarian
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class AddBookingKelasAdapter (private var historys: List<JadwalHarian>, context: Context):
    RecyclerView.Adapter<AddBookingKelasAdapter.ViewHolder>() {

    private val context: Context

    init {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_jadwalharian_adapter, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = historys[position]
        val preferences = context.getSharedPreferences("session", Context.MODE_PRIVATE)

        holder.tvKelas.text = data.NAMA_KELAS
        holder.tvTanggalKelas.text = data.TANGGALJADWAL_HARIAN
        holder.tvInstruktur.text = data.NAMA_INSTRUKTUR
        holder.tvKeteranganKelas.text = data.STATUS_JADWAL_HARIAN

        holder.tvHari.text = data.HARI_JADWAL_UMUM
        holder.tvTarif.text = "Rp.${data.TARIF.toString()}"

        holder.cvKelas.setOnClickListener {
            if(holder.tvKeteranganKelas.text == "Libur") {
                Toast.makeText(context,"Kelas ditiadakan", Toast.LENGTH_SHORT).show()
            }else {
                if(!(preferences.getString("booking",null).isNullOrEmpty())){

                    val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)

                    materialAlertDialogBuilder.setTitle("Confirmation")
                        .setMessage("Are you sure to booked this class?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes"){ _, _ ->
                            if (context is AddBookingKelas){
                                context.getSharedPreferences("session",Context.MODE_PRIVATE).getString("id",null)
                                    ?.let { it1 -> context.bookingClass(it1,data.ID_KELAS,data.TANGGALJADWAL_HARIAN) }
                            }
                        }
                        .show()
                }else if((preferences.getString("status",null).isNullOrEmpty())){
                    MotionToast.createToast(
                        Activity(),
                        "Notification",
                        "Login Terlebih Dahulu",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(Activity() , www.sanju.motiontoast.R.font.helvetica_regular))
                }else{

                }
            }
        }
    }
    override fun getItemCount(): Int {
        return historys.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var tvKelas: TextView
        var tvInstruktur: TextView
        var tvKeteranganKelas: TextView
        var tvTanggalKelas: TextView
        var tvHari: TextView
        var tvTarif: TextView
        var cvKelas: CardView

        init {
            tvKelas = view.findViewById(R.id.tv_NamaKelas)
            tvInstruktur = view.findViewById(R.id.tv_NamaInstruktur)
            tvKeteranganKelas = view.findViewById(R.id.tv_KeteranganJadwalHarian)
            tvTanggalKelas = view.findViewById(R.id.tv_tanggalHarian)
            tvHari = view.findViewById(R.id.tv_HariJadwal)
            tvTarif = view.findViewById(R.id.tv_Tarif)
            cvKelas = view.findViewById(R.id.cv_jadwal)
        }
    }


}