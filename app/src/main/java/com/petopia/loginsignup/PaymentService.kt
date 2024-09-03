package com.petopia.loginsignup

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.adyen.checkout.dropin.service.DropInService
import com.adyen.checkout.dropin.service.DropInServiceResult
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject

class PaymentService : DropInService() {
    // Submitting a payment request
    override fun makePaymentsCall(paymentComponentJson: JSONObject): DropInServiceResult {
        // Extract paymentMethod JSON object from paymentComponentJson
        val paymentMethodJson = paymentComponentJson.getJSONObject("paymentMethod")

        val sharedPreferences = getSharedPreferences("Amount", Context.MODE_PRIVATE)
        val selectedPrice = sharedPreferences.getFloat("selectedPrice", 0.0f)
        val sharedPreferences1 = getSharedPreferences("OrderData", Context.MODE_PRIVATE)
        val selectedreference = sharedPreferences1.getString("reference", "")

        // Make the payment request to Adyen's /payments endpoint
        val client = OkHttpClient()
        val requestBody = JSONObject().apply {
            put("merchantAccount", "STICollegeGensan249ECOM")
            put("amount", JSONObject().apply {
                put("currency", "PHP")
                put("value", selectedPrice)
            })
            put("reference", selectedreference)
            put("returnUrl", "adyencheckout://your.package.name")
            put("paymentMethod", paymentMethodJson)
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val request = Request.Builder()
            .url("https://checkout-test.adyen.com/v70/payments") // Replace with the actual endpoint
            .addHeader("x-api-key", "AQEuhmfuXNWTK0Qc+iSDpk0bqOiXTYxIKpZES2RYlCzyom0yAEokIXOeCZiP8PES1BDBXVsNvuR83LVYjEgiTGAH-rHBYaoPbeZ/rzNdGRTBp9uu4G0pW+87IsZFhgTRS37s=-k]q%?wX^E94GHgm4") // Replace with your actual API key
            .post(requestBody.toString().toRequestBody(mediaType))
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string()

        return if (response.isSuccessful) {
            val responseJson = JSONObject(responseBody)
            if (responseJson.has("action")) {
                // Handle action response
                val actionJsonObject = responseJson.getJSONObject("action")
                val actionType = actionJsonObject.getString("type")
                if (actionType.equals("redirect", ignoreCase = true)) {
                    val redirectUrl = actionJsonObject.getString("url")

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl))
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)

                    val intent1 = Intent(this@PaymentService, OrderSummaryGcash::class.java)
                    intent1.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent1)
                    DropInServiceResult.Action("{\"actionType\": \"wait\"}")
                } else {
                    // Handle other action types if needed
                    DropInServiceResult.Action(actionType)
                }
            } else {
                // Handle successful payment response
                val resultCode = responseJson.getString("resultCode")
                // Handle the resultCode ("Authorised", "Refused", etc.)
                val intent = Intent(this, OrderSummaryGcash::class.java)
                startActivity(intent)
                DropInServiceResult.Finished(resultCode)
            }
        } else {
            // Handle API error
            DropInServiceResult.Action("{\"actionType\": \"error\"}")
        }
    }

    // Submitting additional payment details
    override fun makeDetailsCall(actionComponentJson: JSONObject): DropInServiceResult {
        try {
            // Create a JSONObject with the necessary details
            val detailsJson = JSONObject().apply {
                put("redirectResult", "eyJ0cmFuc1N0YXR1cyI6IlkifQ==")
                // Add other details as needed based on your requirements
            }

            // Create the request body
            val requestBody = JSONObject().apply {
                put("details", detailsJson)
            }.toString().toRequestBody("application/json".toMediaType())

            // Make the /payments/details POST request
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://checkout-test.adyen.com/v70/payments/details") // Use the correct Adyen endpoint
                .addHeader("x-API-key", "AQEuhmfuXNWTK0Qc+iSDpk0bqOiXTYxIKpZES2RYlCzyom0yAEokIXOeCZiP8PES1BDBXVsNvuR83LVYjEgiTGAH-rHBYaoPbeZ/rzNdGRTBp9uu4G0pW+87IsZFhgTRS37s=-k]q%?wX^E94GHgm4") // Replace with your actual API key
                .addHeader("content-type", "application/json")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful) {
                // Handle the successful response as needed
                // Parse and process the responseBody JSON
                val responseJson = JSONObject(responseBody)
                if (responseJson.has("resultCode")) {
                    val resultCode = responseJson.getString("resultCode")
                    return DropInServiceResult.Finished(resultCode)
                } else {
                    // Handle other response data as needed
                    return DropInServiceResult.Finished("Unknown")
                }
            } else {
                // Handle API error
                // Handle non-successful response here
                return DropInServiceResult.Action("{\"actionType\": \"error\"}")
            }
        } catch (e: Exception) {
            // Handle exceptions, e.g., network errors
            // Handle exceptions here
            return DropInServiceResult.Action("{\"actionType\": \"error\"}")
        }
    }
}

