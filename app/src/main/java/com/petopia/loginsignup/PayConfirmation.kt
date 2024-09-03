package com.petopia.loginsignup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adyen.checkout.components.model.PaymentMethodsApiResponse
import com.adyen.checkout.components.model.payments.Amount
import com.adyen.checkout.core.api.Environment
import com.adyen.checkout.dropin.DropInConfiguration
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

// Import the toRequestBody extension function

class PayConfirmation : AppCompatActivity() {
    private lateinit var paymentMethodsLayout: LinearLayout
    val amount = Amount()
    private var selectedPrice: Double = 0.00
    private var selectedPetID: String? = null
    private var selectedPetName: String? = null
    private var selectedPetCategory: String? = null
    private var selectedPetBreed: String? = null
    private var selectedPetColor: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_confirmation)


        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        // Retrieve pet details from the intent
        val sharedPreferences1 = getSharedPreferences("SelectedPetData", MODE_PRIVATE)
        selectedPetID = sharedPreferences1.getString("selectedPetID", "")
        selectedPetName = sharedPreferences1.getString("selectedPetName", "")
        selectedPetCategory = sharedPreferences1.getString("selectedPetCategory", "")
        selectedPetBreed = sharedPreferences1.getString("selectedPetBreed", "")
        selectedPetColor = sharedPreferences1.getString("selectedPetColor", "")
        selectedPrice = sharedPreferences1.getFloat("selectedPrice", 0.0f).toDouble() // Convert to float


        val sharedPreferences = getSharedPreferences("Amount", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("selectedPrice", (selectedPrice * 100).toFloat()) // Convert to float and store
        editor.apply()

        paymentMethodsLayout = findViewById(R.id.paymentMethodsLayout)

        // Create an Amount object with the selected price
        amount.currency = "PHP"
        amount.value = (selectedPrice).toInt() // Convert price to minor units (cents) as Int

        // Configure the global Drop-in configuration
        val dropInConfiguration = DropInConfiguration.Builder(
            this,
            PaymentService::class.java,
            "test_NSVLLNOFWBBBLLH77YL3RR5QIANXYR6W"
        )
            .setAmount(amount) // Set the amount dynamically
            .setEnvironment(Environment.TEST) // Change to LIVE when ready
            .build()

        // Call a function to fetch available payment methods
        fetchAvailablePaymentMethods()
    }

    // Function to fetch available payment methods
    private fun fetchAvailablePaymentMethods() {
        val url = "https://checkout-test.adyen.com/v66/paymentMethods"

        // Construct the request body with required parameters
        val requestBody = JSONObject().apply {
            put("merchantAccount", "STICollegeGensan249ECOM")
            put("countryCode", "PH") // Set the country code for the Philippines
            put("amount", mapOf("currency" to "PHP", "value" to 15000L)) // Example amount
            put("recurringProcessingModel", "CardOnFile")
            // Add other necessary parameters like additionalPaymentMethod, etc.
        }

        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .addHeader(
                "x-api-key",
                "AQEuhmfuXNWTK0Qc+iSDpk0bqOiXTYxIKpZES2RYlCzyom0yAEokIXOeCZiP8PES1BDBXVsNvuR83LVYjEgiTGAH-rHBYaoPbeZ/rzNdGRTBp9uu4G0pW+87IsZFhgTRS37s=-k]q%?wX^E94GHgm4"
            ) // Replace with your actual API key
            .addHeader("content-type", "application/json")
            .post(requestBody.toString().toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val paymentMethodsApiResponse = PaymentMethodsApiResponse.SERIALIZER.deserialize(JSONObject(responseBody))

                    println("Response Body: $responseBody") // Debugging: Print the response body
                    val jsonResponse = JSONObject(responseBody)
                    val paymentMethods = jsonResponse.getJSONArray("paymentMethods")

                    // Update UI on the main (UI) thread
                    runOnUiThread {
                        for (i in 0 until paymentMethods.length()) {
                            val paymentMethod = paymentMethods.getJSONObject(i)

                            if (paymentMethod.getString("type") == "gcash") {

                                val button = Button(this@PayConfirmation)
                                button.text = "Pay with GCash"
                                button.setBackgroundColor(resources.getColor(R.color.mainprimary))
                                button.setTextColor(resources.getColor(R.color.mainaccent))

                                val layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                                button.layoutParams = layoutParams

                                button.setOnClickListener {
                                    val paymentMethodData = paymentMethod.toString()
                                    // Initialize Drop-in configuration for GCash
                                    // Remove the previous button.setOnClickListener block
                                    val dropInConfiguration = DropInConfiguration.Builder(
                                        this@PayConfirmation,
                                        PaymentService::class.java,
                                        "test_NSVLLNOFWBBBLLH77YL3RR5QIANXYR6W"
                                    )
                                        .setAmount(amount) // Set the amount dynamically
                                        .setEnvironment(Environment.TEST) // Change to LIVE when ready
                                        .build()

                                    val resultIntent = Intent(this@PayConfirmation, DeliveryGcashActivity::class.java) // Use DeliveryGcashActivity
                                    resultIntent.putExtra("paymentMethodData", paymentMethodData)
                                    resultIntent.putExtra("paymentMethodsApiResponse", paymentMethodsApiResponse) // Pass the response

                                    val sharedPreferences = getSharedPreferences("SelectedPet", Context.MODE_PRIVATE)
                                    val editor = sharedPreferences.edit()
                                    editor.putString("selectedPetID", selectedPetID)
                                    editor.putString("selectedPetName", selectedPetName)
                                    editor.putString("selectedPetCategory", selectedPetCategory)
                                    editor.putString("selectedPetBreed", selectedPetBreed)
                                    editor.putString("selectedPetColor", selectedPetColor)
                                    editor.putFloat("selectedPrice", selectedPrice.toFloat()) // Convert to float and store
                                    editor.apply()

                                    startActivity(resultIntent)
                                }
                                paymentMethodsLayout.addView(button)
                            }
                        }

                        // Add Cash On Delivery button
                        val codButton = Button(this@PayConfirmation)
                        codButton.text = "Cash On Delivery"
                        codButton.setBackgroundColor(resources.getColor(R.color.mainsecondary)) // Set the background color
                        codButton.setTextColor(resources.getColor(R.color.mainaccent)) // Set the text color

// Define the button's layout parameters (you can adjust these as needed)
                        val layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                        )
                        layoutParams.setMargins(0, 5, 0, 0)
                        codButton.layoutParams = layoutParams

                        codButton.setOnClickListener {
                            // Save the selected pet details in SharedPreferences
                            val sharedPreferences = getSharedPreferences("SelectedPet", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("selectedPetID", selectedPetID)
                            editor.putString("selectedPetName", selectedPetName)
                            editor.putString("selectedPetCategory", selectedPetCategory)
                            editor.putString("selectedPetBreed", selectedPetBreed)
                            editor.putString("selectedPetColor", selectedPetColor)
                            editor.putFloat("selectedPrice", selectedPrice.toFloat()) // Convert to float and store
                            editor.apply()

                            // Start the DeliveryActivity
                            val intent = Intent(this@PayConfirmation, DeliveryActivity::class.java)
                            startActivity(intent)
                        }
                        paymentMethodsLayout.addView(codButton)



                        val gcashmanualButton = Button(this@PayConfirmation)
                        gcashmanualButton.text = "GCash Manual"
                        gcashmanualButton.setBackgroundColor(resources.getColor(R.color.mainprimary)) // Set the background color
                        gcashmanualButton.setTextColor(resources.getColor(R.color.mainaccent)) // Set the text color

                        gcashmanualButton.setOnClickListener {
                            // Save the selected pet details in SharedPreferences
                            val sharedPreferences = getSharedPreferences("SelectedPet", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("selectedPetID", selectedPetID)
                            editor.putString("selectedPetName", selectedPetName)
                            editor.putString("selectedPetCategory", selectedPetCategory)
                            editor.putString("selectedPetBreed", selectedPetBreed)
                            editor.putString("selectedPetColor", selectedPetColor)
                            editor.putFloat("selectedPrice", selectedPrice.toFloat()) // Convert to float and store
                            editor.apply()

                            // Start the DeliveryActivity
                            val intent = Intent(this@PayConfirmation, GCashManualActivity::class.java)
                            startActivity(intent)
                        }
                        paymentMethodsLayout.addView(gcashmanualButton)

                    }
                } else {
                    runOnUiThread {
                        // Log the response status code for debugging
                        println("Response Code: ${response.code}")
                        // For example, show an error message to the user
                        Toast.makeText(
                            this@PayConfirmation,
                            "Failed to fetch payment methods",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    // For example, show an error message to the user
                    Toast.makeText(
                        this@PayConfirmation,
                        "Network error. Please check your connection.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}

