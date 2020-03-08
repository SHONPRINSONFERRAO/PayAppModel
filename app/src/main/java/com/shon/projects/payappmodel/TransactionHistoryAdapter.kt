package com.shon.projects.payappmodel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shon.projects.payappmodel.utils.glide.networking.TransactionDataModel
import java.text.SimpleDateFormat
import java.util.*


class TransactionHistoryAdapter(val items: ArrayList<TransactionDataModel>, val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ListPlaceHolder(
            LayoutInflater.from(context).inflate(
                R.layout.dashboard_row_layout,
                parent,
                false
            )
        )
    }

    // Binds each animal in the ArrayList to a view
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val headerViewHolder = holder as ListPlaceHolder
        headerViewHolder.let {
            if (items[position].transactionType == "received") {
                it.arrowImage.setImageResource(R.drawable.ic_received)
                it.transactionAmount.text =
                    "AED " + items[position].amountTransaction.toString()
            } else {
                it.arrowImage.setImageResource(R.drawable.ic_sent)
                it.transactionAmount.text =
                    "- AED " + items[position].amountTransaction.toString()

            }

            it.userName.text = items[position].userName
            it.transactionType.text = items[position].transactionType


            if (position == 0 || items[position].timeStamp != items[position - 1].timeStamp) {
                println("TIME+ " + items[position].timeStamp.toString())

                it.dateText.visibility = View.VISIBLE
                it.dateText.text = timeStampToDate(items[position].timeStamp)
            } else {
                it.dateText.visibility = View.GONE
            }
        }
    }


}


class ListPlaceHolder(view: View) : RecyclerView.ViewHolder(view) {
    val userName: TextView = view.findViewById(R.id.user_name_text)
    val transactionType: TextView = view.findViewById(R.id.transaction_type)
    val transactionAmount: TextView = view.findViewById(R.id.transaction_amount)
    val arrowImage: ImageView = view.findViewById(R.id.arrow_image)
    val dateText: TextView = view.findViewById(R.id.date_text)

}


fun timeStampToDate(timeStamp: Long): String {
    val date = Date(timeStamp * 1000L)
    return SimpleDateFormat("DD MMMM").format(date);

}

