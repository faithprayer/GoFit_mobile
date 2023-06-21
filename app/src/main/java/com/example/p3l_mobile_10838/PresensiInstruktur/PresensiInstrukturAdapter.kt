package com.example.p3l_mobile_10838.PresensiInstruktur

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.p3l_mobile_10838.R
import com.example.p3l_mobile_10838.databinding.ActivityPresensiInstrukturAdapterBinding
import com.example.p3l_mobile_10838.model.Instruktur
import com.example.p3l_mobile_10838.model.PresensiInstruktur
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PresensiInstrukturAdapter (private var instructors: List<PresensiInstruktur>, context: Context): RecyclerView.Adapter<PresensiInstrukturAdapter.ViewHolder>(){
    private val context: Context

    init {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresensiInstrukturAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_presensi_instruktur_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PresensiInstrukturAdapter.ViewHolder, position: Int) {
        val data = instructors[position]
        holder.tvKelas.text = data.NAMA_KELAS
        holder.tvInstruktur.text = data.NAMA_INSTRUKTUR
        holder.tvTanggal.text = data.TANGGAL_JADWAL_HARIAN
        holder.tvHari.text = data.HARI_JADWAL_UMUM
        holder.tvKeterangan.text = data.STATUS_JADWAL_HARIAN

        holder.btn_edit.setOnClickListener {
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin update jam mulai kelas?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Iya"){ _, _ ->
                    if (context is PresensiInstrukturActivity){
                        context.store(data.ID_INSTRUKTUR,data.TANGGAL_JADWAL_HARIAN)
                    }
                }
                .show()
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
        var cvSchedule: CardView
        var btn_edit: ImageButton


        init {
            tvKelas = view.findViewById(R.id.tv_kode_kelas)
            tvInstruktur = view.findViewById(R.id.tv_nama_instrukutur)
            tvKeterangan = view.findViewById(R.id.tv_status_jadwal_harian)
            tvHari = view.findViewById(R.id.tv_hari_kelas)
            tvTanggal = view.findViewById(R.id.tv_tanggal_jadwal_harian)
            cvSchedule = view.findViewById(R.id.cv_presensi_instruktur)
            btn_edit = view.findViewById(R.id.btn_edit)
        }

    }
}