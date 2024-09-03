package com.petopia.loginsignup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adyen.checkout.dropin.DropIn

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val result = DropIn.getDropInResultFromIntent(intent)
    }
}