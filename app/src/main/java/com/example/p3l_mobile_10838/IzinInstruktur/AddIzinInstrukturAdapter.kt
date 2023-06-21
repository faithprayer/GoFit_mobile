package com.example.p3l_mobile_10838.IzinInstruktur

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.p3l_mobile_10838.R
import com.example.p3l_mobile_10838.databinding.ActivityAddIzinInstrukturAdapterBinding
import com.example.p3l_mobile_10838.model.IzinInstruktur
import java.time.format.DateTimeFormatter

class AddIzinInstrukturAdapter (private var izinInstrukturList: List<IzinInstruktur>, context: Context) :
    RecyclerView.Adapter<AddIzinInstrukturAdapter.ViewHolder>() {
    private var filteredizinInstrukturList: MutableList<IzinInstruktur>
    private val context: Context

    init{
        filteredizinInstrukturList = ArrayList(izinInstrukturList)
        this.context=context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_add_izin_instruktur_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredizinInstrukturList.size
    }

    fun setIzinInstrukturList(izinInstrukturList: Array<IzinInstruktur>){
        this.izinInstrukturList = izinInstrukturList.toList()
        filteredizinInstrukturList = izinInstrukturList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){

        val data = izinInstrukturList[position]
        val izinInstruktur = filteredizinInstrukturList[position]
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//        holder.tvTanggalIzinInstruktur.text = izinInstruktur.TANGGAL_IZIN_INSTRUKTUR.format(formatter)
           holder.tvTanggalIzinInstruktur.text = "Tanggal Izin : ${data.TANGGAL_IZIN_INSTRUKTUR}"
        holder.tvTanggalPengajuan.text = "Tanggal Pengajuan : ${data.TANGGAL_MELAKUKAN_IZIN}"
        holder.tvKeteranganIzin.text = "Keterangan Izin: ${data.KETERANGAN_IZIN}"
        holder.tvStatusIzin.text = "Status Izin : ${data.TANGGAL_KONFIRMASI_IZIN} - ${data.STATUS_IZIN}"
        if(holder.tvStatusIzin.text == "null - null"){
            holder.tvStatusIzin.text = "Belum Dikonfirmasi"
        }




        holder.cvIzinInstruktur.setOnClickListener{
            Toast.makeText(context,data.STATUS_IZIN, Toast.LENGTH_SHORT).show()
        }
    }



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var tvIdIzinInstruktur: TextView
        var tvIdInstruktur: TextView
        var tvNamaInstruktur: TextView
        var tvTanggalIzinInstruktur: TextView
        var tvTanggalPengajuan: TextView
        var tvStatusIzin: TextView
        var tvKeteranganIzin: TextView
        var tvTanggalKonfirmasiIzin: TextView
        var cvIzinInstruktur: CardView

        init{

            tvIdIzinInstruktur = itemView.findViewById(R.id.tvIdIzinInstruktur)
            tvIdInstruktur = itemView.findViewById(R.id.tvIdInstruktur)
            tvNamaInstruktur = itemView.findViewById(R.id.tvNamaInstruktur)
            tvTanggalIzinInstruktur = itemView.findViewById(R.id.tvTanggalIzinInstruktur)
            tvKeteranganIzin = itemView.findViewById(R.id.tvKeteranganIzin)
            tvTanggalPengajuan = itemView.findViewById(R.id.tvTanggalMelakukanIzin)
            tvStatusIzin = itemView.findViewById(R.id.tvStatusIzin)
            tvTanggalKonfirmasiIzin = itemView.findViewById(R.id.tvTanggalKonfirmasiIzin)
            cvIzinInstruktur = itemView.findViewById(R.id.cv_izin)
        }
    }
}