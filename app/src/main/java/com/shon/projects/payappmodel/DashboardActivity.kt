package com.shon.projects.payappmodel

import android.app.Activity
import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.shon.projects.payappmodel.utils.glide.PushReceiver
import com.shon.projects.payappmodel.utils.glide.networking.ApiClient
import com.shon.projects.payappmodel.utils.glide.networking.ProfileDataModel
import com.shon.projects.payappmodel.utils.glide.networking.TransactionDataModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dashboard_bottom_sheet.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DashboardActivity : AppCompatActivity() {


    private val dataList = ArrayList<TransactionDataModel>()
    private var PRIVATE_MODE = 0
    lateinit var sharedPref: SharedPreferences

    companion object {
        const val CHANNEL_ID = "PAY_CHANNEL"
        const val PREF_NAME = "mamo-pay"
        const val PAY_BALANCE = "pay_balance"
        const val PAY_BALANCE_DEDUCTION = "pay_balance_deduction"
        const val INTENT_SEND_MONEY = 111
        const val AMOUNT_PROCESS = "amount_process"
        const val ACTION_SNOOZE = "OK"
        const val NOTIFICATION_ID = 0
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPref = getSharedPreferences(PREF_NAME, PRIVATE_MODE)


        balance_value_txt.text = "AED " + sharedPref.getInt(PAY_BALANCE, 0).toString()

        val typeface = Typeface.createFromAsset(assets, "fonts/OpenSans-Regular.ttf")

        findViewById<TextView>(R.id.balance_text).typeface = typeface

        val sheetBehavior =
            BottomSheetBehavior.from(findViewById<ConstraintLayout>(R.id.bottom_sheet))




        sheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // React to state change
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events
            }
        })


        // Loads animals into the ArrayList
        //addAnimals()
        getProfileData()
        getHistoryData()

        // Creates a vertical Layout Manager
        history_list.layoutManager = LinearLayoutManager(this)


        history_list.adapter = TransactionHistoryAdapter(dataList, this)

        send_money_btn.setOnClickListener { goToSendMoney() }
        add_money_btn.setOnClickListener { addMoneyMock() }

    }

    private fun goToSendMoney() {
        val intent = Intent(this, SendMoneyActivity::class.java)
        intent.putExtra("BALANCE", sharedPref.getInt(PAY_BALANCE, 0))
        startActivityForResult(intent, INTENT_SEND_MONEY)
    }


    private fun getHistoryData() {
        progressBar.visibility = View.VISIBLE
        val call: Call<List<TransactionDataModel>> = ApiClient.getClient.getPhotos()
        call.enqueue(object : Callback<List<TransactionDataModel>> {

            override fun onResponse(
                call: Call<List<TransactionDataModel>>?,
                response: Response<List<TransactionDataModel>>?
            ) {

                response?.body()?.let { dataList?.addAll(it) }
                history_list?.adapter?.notifyDataSetChanged()
                progressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<TransactionDataModel>>?, t: Throwable?) {
                progressBar.visibility = View.GONE
            }

        })
    }

    private fun getProfileData() {
        val call: Call<ProfileDataModel> = ApiClient.getClient.getUserData()
        call.enqueue(object : Callback<ProfileDataModel> {

            override fun onResponse(
                call: Call<ProfileDataModel>?,
                response: Response<ProfileDataModel>?
            ) {

                val profileDataModel = response?.body()!!
                calculateBalance(balance = profileDataModel.balance)
                //sharedPref?.edit().putInt(PAY_BALANCE, profileDataModel.balance).apply()
            }

            override fun onFailure(call: Call<ProfileDataModel>?, t: Throwable?) {
            }

        })
    }

    //kept 20000 default value as i am returning static 20000 from server and this
    // functionality is to calculate intial and app transaction balance
    private fun calculateBalance(balance: Int = 20000) {
        val deductionAmount = sharedPref.getInt(PAY_BALANCE_DEDUCTION, 0)
        val amount: Int = balance.minus(deductionAmount)
        sharedPref.edit().putInt(PAY_BALANCE, amount).apply()
        balance_value_txt.text = "AED " + amount.toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            INTENT_SEND_MONEY -> {
                if (resultCode == Activity.RESULT_OK) {
                    val accountDeductionValue: Int? = data?.getIntExtra(AMOUNT_PROCESS, 0)
                    if (accountDeductionValue != null) {
                        calculateBalance()
                    }

                }
            }
        }
    }

    //TODO: interviewer review -  add money mock
    fun addMoneyMock() {
        addBalance()
        createNotificationChannel()
    }

    //TODO: interviewer review -  add money mock fake deposit
    private fun addBalance() {
        val amount = sharedPref.getInt(PAY_BALANCE_DEDUCTION, 0)
        val additionAmount: Int = amount.minus(100)
        sharedPref.edit().putInt(PAY_BALANCE_DEDUCTION, additionAmount).apply()
        sharedPref.edit().putInt(PAY_BALANCE, sharedPref.getInt(PAY_BALANCE, 0).plus(100)).apply()
        balance_value_txt.text = "AED " + sharedPref.getInt(PAY_BALANCE, 0)
    }


    private fun createNotificationChannel() { // Create the NotificationChannel, but only on API 26+ because
// the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.channel_name)
            val description = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
// or other notification behaviors after this
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
        sendNotification("title", NOTIFICATION_ID)
    }

    //TODO: interviewer review -  add money mock notification
    private fun sendNotification(
        title: String?,
        notificationId: Int
    ) { // Create an explicit intent for an Activity in your app

        val snoozeIntent = Intent(this, PushReceiver::class.java)
        snoozeIntent.action = ACTION_SNOOZE
        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, notificationId)
        Log.e("PUSH", snoozeIntent.extras.toString())
        Log.e(
            "PUSH",
            "snoozeIntent id: " + snoozeIntent.getIntExtra(
                EXTRA_NOTIFICATION_ID,
                -1
            )
        )
        val snoozePendingIntent =
            PendingIntent.getBroadcast(this, notificationId, snoozeIntent, 0)
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_mamo_small)
            .setContentTitle("Greetings from MamoPay")
            .setContentText("100 AED has been added to your wallet by Rosy")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(false) // Add the action button
            .addAction(
                R.drawable.ic_launcher_foreground, getString(R.string.snooze),
                snoozePendingIntent
            )
        val notificationManager = NotificationManagerCompat.from(this)
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build())
    }

}








