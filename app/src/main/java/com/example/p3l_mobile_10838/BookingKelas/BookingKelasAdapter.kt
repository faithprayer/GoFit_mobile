package com.example.p3l_mobile_10838.BookingKelas

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.p3l_mobile_10838.BookingGymActivity
import com.example.p3l_mobile_10838.R
import com.example.p3l_mobile_10838.api.MemberApi
import com.example.p3l_mobile_10838.model.BookingKelas
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class BookingKelasAdapter (private var historys: List<BookingKelas>, context: Context):
    RecyclerView.Adapter<BookingKelasAdapter.ViewHolder>() {
    private val context: Context


    init {
        this.context = context
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.activity_booking_kelas_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = historys[position]

        holder.tvKodeBooking.text = "${data.KODE_BOOKING_KELAS}  | ${data.NAMA_KELAS}"
        holder.tvNamaInstruktur.text = "Instruktur : ${data.NAMA_INSTRUKTUR}"
        holder.tvTanggalBook.text = "Tanggal Kelas: ${data.TANGGAL_JADWAL_HARIAN}"
        holder.tvTanggalMelakukan.text = "Tanggal Booking: ${data.TANGGAl_MELAKUKAN_BOOKING}"
        holder.tvStatusBooking.text = "${data.STATUS_PRESENSI_KELAS} - ${data.WAKTU_KONFIRMASI_PRESENSI_KELAS}"
        if(holder.tvStatusBooking.text == "null - null"){
            holder.tvStatusBooking.text = "Status : Belum dikonfirmasi"
        }
        holder.icon_delete.setOnClickListener {
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Are you sure want to cancel this booked class?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Yes"){ _, _ ->
                    if (context is BookingKelasActivity){
                        context.cancelBookingKelas(data.KODE_BOOKING_KELAS)
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
        var tvNamaInstruktur: TextView
        var tvTanggalBook: TextView
        var tvTanggalMelakukan: TextView
        var tvStatusBooking: TextView
        var icon_delete: ImageButton
        var cvBook: CardView

        init {
            tvKodeBooking = view.findViewById(R.id.tv_kode_kelas)
            tvNamaInstruktur = view.findViewById(R.id.tv_nama_instrukutur)
            tvTanggalBook = view.findViewById(R.id.tv_tanggal_kelas)
            tvTanggalMelakukan = view.findViewById(R.id.tv_tanggal_dibooking_kelas)
            tvStatusBooking = view.findViewById(R.id.tv_status_konfirmasi_kelas)
            icon_delete = view.findViewById(R.id.icon_delete)
            cvBook = view.findViewById(R.id.cv_book)
        }

    }
}