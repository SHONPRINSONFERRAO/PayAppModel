package com.shon.projects.payappmodel


import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.send_money_amount_fragment.*


class SendMoneyFragment : Fragment() {

    internal lateinit var callback: OnNextSelectedListener
    val parentActivity: SendMoneyActivity = SendMoneyActivity()
    lateinit var transaction_amount: EditText
    lateinit var balance_amount: TextView
    var wallet_amount: Int? = 0
    lateinit var next_btn: Button
    lateinit var options: ChipGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.send_money_amount_fragment, container, false)
        callback.setToolbar(getString(R.string.enter_amount))
        transaction_amount = view.findViewById(R.id.transaction_amount)
        balance_amount = view.findViewById(R.id.balance_amount)
        wallet_amount = arguments?.getInt("available_balance")
        balance_amount.text = wallet_amount.toString()
        //balance_amount.text =  wallet_amount.toString()
        next_btn = view.findViewById(R.id.next_btn)
        options = view.findViewById(R.id.options)
        transaction_amount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (text?.length != 0 && wallet_amount!! < text.toString().toLong()) {
                    balance_amount.setTextColor(Color.RED)
                    error_text.visibility = View.VISIBLE
                    next_btn.isEnabled = false
                } else {
                    balance_amount.setTextColor(
                        ResourcesCompat.getColor(resources, R.color.text_color_grey, null)
                    )
                    error_text.visibility = View.GONE
                    if (options.checkedChipId > -1) {
                        next_btn.isEnabled = true
                    }
                }

            }
        })
        transaction_amount.requestFocus()

        options.setOnCheckedChangeListener { group, checkedId ->
            val transaction_amount = transaction_amount.text.toString()
            if (transaction_amount.isNotEmpty() && wallet_amount!! >= transaction_amount.toLong() && transaction_amount.toLong() > 0) {
                next_btn.isEnabled = true
            }
            if (checkedId == -1) {
                next_btn.isEnabled = false
            }
        }


        next_btn.setOnClickListener {
            /*  val frag = SendMoneyConfirmation()
              val bundle = Bundle()
              bundle.putLong("transaction_amount", transaction_amount.text.toString().toLong())
              frag.arguments = bundle
              val fragmentTransaction = parentActivity.supportFragmentManager.beginTransaction()
              fragmentTransaction.replace(R.id.myFragment, frag)
              fragmentTransaction.commit()*/
            callback.onNextSelected(transaction_amount.text.toString().toLong())
        }

        return view
    }


    fun setOnNextSelectedListener(callback: OnNextSelectedListener) {
        this.callback = callback
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnNextSelectedListener {
        fun onNextSelected(transaction_amount: Long)
        fun setToolbar(text: String)
    }


}