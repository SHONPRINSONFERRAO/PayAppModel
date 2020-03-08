package com.shon.projects.payappmodel

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


class SendMoneyActivity : AppCompatActivity(), SendMoneyFragment.OnNextSelectedListener,
    SendMoneyConfirmation.OnSendSelectedListener, SendMoneySuccess.OnSuccessSelectedListener {

    companion object {
        const val PREF_NAME = "mamo-pay"
        const val PAY_BALANCE = "pay_balance"
        const val PAY_BALANCE_DEDUCTION = "pay_balance_deduction"
        const val AMOUNT_PROCESS = "amount_process"
    }

    private var PRIVATE_MODE = 0
    lateinit var sharedPref: SharedPreferences

    var available_balance = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_money)
        sharedPref = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        available_balance = intent.getIntExtra("BALANCE", 0)
        setSupportActionBar(findViewById(R.id.send_money_toolbar))
        val bundle = Bundle()
        bundle.putInt("available_balance", available_balance)
        val frag = SendMoneyFragment()
        frag.arguments = bundle
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.myFragment, frag)
        fragmentTransaction.commit()
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onNextSelected(transaction_amount: Long) {
        val sendMoneyConfirmationFragment = SendMoneyConfirmation()
        val transaction = supportFragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putLong("transaction_amount", transaction_amount)
        sendMoneyConfirmationFragment.arguments = bundle
        transaction.replace(R.id.myFragment, sendMoneyConfirmationFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override
    fun onAttachFragment(fragment: Fragment) {
        if (fragment is SendMoneyFragment) {
            fragment.setOnNextSelectedListener(this)
        } else if (fragment is SendMoneyConfirmation) {
            fragment.setOnSendSelectedListener(this)
        } else if (fragment is SendMoneySuccess) {
            fragment.setOnSuccessSelectedListener(this)
        }
    }

    override fun onSendSelected(transaction_amount: Long, name: String) {
        val sendMoneySuccess = SendMoneySuccess()
        val transaction = supportFragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putLong("transaction_amount", transaction_amount)
        bundle.putString("user_name", name)
        sendMoneySuccess.arguments = bundle
        transaction.replace(R.id.myFragment, sendMoneySuccess)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onSuccessSelected(transaction_amount: Long?) {
        sharedPref?.edit().putInt(
            PAY_BALANCE_DEDUCTION,
            sharedPref.getInt(PAY_BALANCE_DEDUCTION, 0).plus(transaction_amount!!.toInt())
        ).apply()
        //sharedPref?.edit().putInt(PAY_BALANCE, sharedPref.getInt(PAY_BALANCE, 0).minus(transaction_amount!!.toInt())).apply()
        val resultIntent = Intent()
        resultIntent.putExtra(
            AMOUNT_PROCESS,
            sharedPref.getInt(PAY_BALANCE, 0).minus(transaction_amount!!.toInt())
        )
        setResult(Activity.RESULT_OK, resultIntent)
        this.finish()
    }

    override
    fun setToolbar(text: String) {
        supportActionBar.let {
            it?.title = text
            it?.setDisplayHomeAsUpEnabled(true)
            it?.setDisplayHomeAsUpEnabled(true)
        }
    }

}
