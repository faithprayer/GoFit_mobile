package com.example.p3l_mobile_10838.PresensiMember

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.p3l_mobile_10838.BookingKelas.BookingKelasActivity
import com.example.p3l_mobile_10838.R
import com.example.p3l_mobile_10838.model.JadwalInstruktur
import org.w3c.dom.Text

class JadwalInstrukturAdapter (private var instructors: List<JadwalInstruktur>, context: Context): RecyclerView.Adapter<JadwalInstrukturAdapter.ViewHolder>() {
    private val context: Context

    init {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JadwalInstrukturAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_jadwal_instruktur_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: JadwalInstrukturAdapter.ViewHolder, position: Int) {
        val data = instructors[position]
        val preferences = context.getSharedPreferences("session", Context.MODE_PRIVATE)
        holder.tvKelas.text = data.NAMA_KELAS
        holder.tvInstruktur.text = data.NAMA_INSTRUKTUR
        holder.tvTanggal.text = data.TANGGAL_JADWAL_HARIAN
        holder.tvHari.text = data.HARI_KELAS
        holder.tvKeterangan.text = data.STATUS_JADWAL_HARIAN
        holder.tvJamMulai.text = data.JAM_MULAI
        holder.tvJamSelesai.text = data.JAM_SELESAI

        holder.cvSchedule.setOnClickListener {
            if (context is JadwalInstrukturActivity){
                val intent = Intent(context,KonfirmasiPresensiMemberActivity::class.java)
                preferences.edit()
                    .putString("tanggal_jadwal_harian",data.TANGGAL_JADWAL_HARIAN)
                    .apply()
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return instructors.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var tvKelas: TextView
        var tvInstruktur: TextView
        var tvKeterangan: TextView
        var tvHari: TextView
        var tvTanggal: TextView
        var tvJamMulai: TextView
        var tvJamSelesai: TextView
        var cvSchedule: CardView

        init {
            tvKelas = view.findViewById(R.id.tv_nama_kelas_jadwalinsturktur)
            tvInstruktur = view.findViewById(R.id.tv_nama_instruktur_jadwalinstruktur)
            tvKeterangan = view.findViewById(R.id.tv_keterangan_jadwal_jadwalinstruktur)
            tvHari = view.findViewById(R.id.tv_hari_jadwal_jadwalinstruktur)
            tvTanggal = view.findViewById(R.id.tv_tanggal_jadwal_harian_jadwalinstruktur)
            tvJamMulai = view.findViewById(R.id.tv_jamMulai_jadwalinstruktur)
            tvJamSelesai = view.findViewById(R.id.tv_jamSelesai_jadwalinstruktur)
            cvSchedule = view.findViewById(R.id.cv_jadwalInstruktur)
        }

    }
}