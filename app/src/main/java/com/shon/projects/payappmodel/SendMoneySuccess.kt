package com.shon.projects.payappmodel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class SendMoneySuccess : Fragment() {

    internal lateinit var callback: OnSuccessSelectedListener
    var amount: Long = 0
    lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            amount = it.getLong("transaction_amount")
            name = it.getString("user_name").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_send_money_success, container, false)
        view.findViewById<TextView>(R.id.success_text).text =
            "You sent AED " + amount + " to \n" + name
        view.findViewById<Button>(R.id.dash_btn)
            .setOnClickListener { callback.onSuccessSelected(amount) }
        return view
    }

    fun setOnSuccessSelectedListener(callback: OnSuccessSelectedListener) {
        this.callback = callback
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnSuccessSelectedListener {
        fun onSuccessSelected(transaction_amount: Long?)
        fun setToolbar(text: String)
    }


}
