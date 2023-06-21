package com.example.p3l_mobile_10838.PresensiMember

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
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
import com.example.p3l_mobile_10838.model.HistoriBoookingKelasMember
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class KonfirmasiPresensiMemberAdapter (private var historys: List<HistoriBoookingKelasMember>, context: Context): RecyclerView.Adapter<KonfirmasiPresensiMemberAdapter.ViewHolder>() {
    private val context: Context

    companion object{
        private val STATUS = arrayOf(
            "Hadir",
            "Tidak Hadir",
        )
    }

    init {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KonfirmasiPresensiMemberAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_konfirmasi_presensi_member_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: KonfirmasiPresensiMemberAdapter.ViewHolder, position: Int) {
        val data = historys[position]
        val preferences = context.getSharedPreferences("session", Context.MODE_PRIVATE)

        setExposedDropDownMenu(holder)

        holder.tvKodeBooking.text = "${data.KODE_BOOKING_KELAS} - ${data.NAMA_KELAS}"
        holder.tvTanggalBook.text = "ID Member: ${data.TANGGAL_JADWAL_HARIAN}"
        holder.tvTanggalMelakukan.text = "Nama Member: ${data.TANGGAL_MELAKUKAN_BOOKING}"
        holder.tvStatusBooking.text = "${data.STATUS_PRESENSI_KELAS} - ${data.WAKTU_KONFIRMASI_PRESENSI_KELAS}"
        if(holder.tvStatusBooking.text == "null - null"){
            holder.tvStatusBooking.text = "Belum dikonfirmasi/Tidak Hadir"
        }

        holder.iconCheck.setOnClickListener {
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin konfirmasi presensi member ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Iya"){ _, _ ->
                    if (context is KonfirmasiPresensiMemberActivity){
                        context.update(data.KODE_BOOKING_KELAS,holder.edStatus.text.toString())
                    }
                }
                .show()
        }
    }

    override fun getItemCount(): Int {
        return historys.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var tvKodeBooking: TextView
        var tvTanggalBook: TextView
        var tvTanggalMelakukan: TextView
        var tvStatusBooking: TextView
        var iconCheck: ImageButton
        var cvBook: CardView
        var edStatus: AutoCompleteTextView

        init {
            tvKodeBooking = view.findViewById(R.id.tv_kodeBooking_konfirmasiPresensiMemberAdapter)
            tvTanggalBook = view.findViewById(R.id.tv_tanggalbookingkelas_konfirmasiPresensiMemberAdapter)
            tvTanggalMelakukan = view.findViewById(R.id.tv_tanggalmelakukanbookingKelas_konfirmasiPresensiMemberAdapter)
            tvStatusBooking = view.findViewById(R.id.tv_statuskonfirmasipresensi_konfirmasiPresensiMemberAdapter)
            iconCheck = view.findViewById(R.id.icon_check)
            cvBook = view.findViewById(R.id.cv_konfirmasiPresensiMemberAdapter)
            edStatus = view.findViewById(R.id.ed_status)
        }

    }

    fun setExposedDropDownMenu(holder: KonfirmasiPresensiMemberAdapter.ViewHolder){
        val adapterslot: ArrayAdapter<String> = ArrayAdapter<String>(context as KonfirmasiPresensiMemberActivity,
            R.layout.item_list_status, KonfirmasiPresensiMemberAdapter.STATUS
        )
        holder.edStatus.setAdapter(adapterslot)
    }
}