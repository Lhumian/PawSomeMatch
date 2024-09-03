package com.petopia.loginsignup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.UUID
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val payButton: Button = findViewById(R.id.payBtn)
        payButton.setOnClickListener {
            makeTestPayment()
        }
    }

    private fun makeTestPayment() {
        val url = "https://checkout-test.adyen.com/v70/payments"
        val apiKey = "AQEuhmfuXNWTK0Qc+iSDpk0bqOiXTYxIKpZES2RYlCzyom0yAEokIXOeCZiP8PES1BDBXVsNvuR83LVYjEgiTGAH-rHBYaoPbeZ/rzNdGRTBp9uu4G0pW+87IsZFhgTRS37s=-k]q%?wX^E94GHgm4"
        val merchantAccount = "STICollegeGensan249ECOM"

        val idempotencyKey = UUID.randomUUID().toString() // Generate a unique idempotency key

        val requestBody = JSONObject().apply {
            put("merchantAccount", merchantAccount)
            put("reference", "My first Adyen test payment")
            put("amount", JSONObject().apply {
                put("value", 1000)
                put("currency", "EUR")
            })
            put("paymentMethod", JSONObject().apply {
                put("type", "scheme")
                put("encryptedCardNumber", "test_4111111111111111")
                put("encryptedExpiryMonth", "test_03")
                put("encryptedExpiryYear", "test_2030")
                put("encryptedSecurityCode", "test_737")
            })
        }

        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .addHeader("x-api-key", apiKey)
            .addHeader("idempotency-key", idempotencyKey)
            .addHeader("content-type", "application/json")
            .post(requestBody.toString().toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                println(responseBody)
                // Parse the response JSON to extract the resultCode and handle it as needed
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }
}
