package com.example.binlookup

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.binlookup.history.BinEntity
import com.example.binlookup.model.BinInfo
import com.google.gson.Gson
import net.cachapa.expandablelayout.ExpandableLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter(val context: Context, val list: List<BinEntity>) :
    RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return MyViewHolder(item)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val entity = list[position]

        holder.bin.text = "BIN: ${entity.bin}"

        val binInfo = try {
            Gson().fromJson(entity.jsonOfData, BinInfo::class.java)
        } catch (e: Exception) {
            null
        }

        binInfo?.let {
            holder.scheme.text = "${it.scheme ?: "N/A"}"
            holder.bank.text = "${it.bank?.name ?: "N/A"}"
            holder.time.text = formatTime(entity.searchTime)
            holder.bin.text = "Bin: ${entity.bin}"
            Glide.with(context)
                .load("https://flagsapi.com/${binInfo.country.alpha2.uppercase()}/flat/64.png")
                .centerCrop().into(holder.flag)
            holder.btn_web.setOnClickListener {
                if (binInfo.bank.url == "" || binInfo.bank.url == null) {
                    Toast.makeText(
                        holder.view.context,
                        "Web url is not available for this bank",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val url = "https://${binInfo.bank.url}"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = url.toUri()
                    it.context.startActivity(intent)
                }
            }
            holder.btn_location.setOnClickListener {
                if (binInfo.country.latitude == 0 || binInfo.country.latitude == null) {
                    Toast.makeText(
                        holder.view.context,
                        "Location is not available for this bank",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val url =
                        "https://www.google.com/maps/search/?api=1&query=${binInfo.country.latitude},${binInfo.country.latitude}"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    it.context.startActivity(intent)
                }
            }
            holder.btn_call.setOnClickListener {
                if (binInfo.bank.phone == "" || binInfo.bank.phone == null) {
                    Toast.makeText(
                        holder.view.context,
                        "Phone number is not available for this bank",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val intent = Intent(Intent.ACTION_DIAL) // ✅ safer; no permission needed
                    intent.data = "tel:${binInfo.bank.phone}".toUri()
                    it.context.startActivity(intent)
                }
            }
        }



        holder.view.setOnClickListener {
           holder.expand.toggle()
        }
    }

    private fun formatTime(lng: Long): String {
        val date = Date(lng)
        val format = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        return format.format(date)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var scheme = view.findViewById<TextView>(R.id.scheme_name)
        var bin = view.findViewById<TextView>(R.id.item_bin)
        var flag = view.findViewById<ImageView>(R.id.item_flag)
        var time = view.findViewById<TextView>(R.id.time)
        val view = view
        var bank = view.findViewById<TextView>(R.id.bank_name)
        var btn_call = view.findViewById<ImageButton>(R.id.btn_call)
        var btn_location = view.findViewById<ImageButton>(R.id.btn_location)
        var btn_web = view.findViewById<ImageButton>(R.id.btn_web)
        val expand: ExpandableLayout = view.findViewById(R.id.expand)


    }
}
