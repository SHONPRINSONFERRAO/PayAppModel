package com.shon.projects.payappmodel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment


class SendMoneyConfirmation : Fragment() {

    private var amountToSend: Long? = 0
    internal lateinit var callback: OnSendSelectedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View =
            inflater.inflate(R.layout.fragment_send_money_confirmation, container, false)
        amountToSend = arguments?.getLong("transaction_amount")
        callback.setToolbar(getString(R.string.confirm_transfer))
        callback.onSendSelected(amountToSend!!.toLong(), "Steve Pickles")
        view.findViewById<TextView>(R.id.amount_text).text = "AED " + amountToSend.toString()
        return view
    }

    fun setOnSendSelectedListener(callback: OnSendSelectedListener) {
        this.callback = callback
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnSendSelectedListener {
        fun onSendSelected(transaction_amount: Long, user: String)
        fun setToolbar(text: String)
    }

}
