package com.petopia.loginsignup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CompletedTransactionActivity extends MainActivity {

    String user_id;
    private List<CompletedTransaction> transactionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_transaction);

        user_id = sharedPreferences.getString("user_id", "");

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CompletedTransactionAdapter adapter = new CompletedTransactionAdapter(transactionList, this);
        recyclerView.setAdapter(adapter);

        String apiUrl = "https://pawsomematch.online/android/completed_transaction.php?buyer_id=" + user_id;
        new FetchTransactionData(transactionList, adapter).execute(apiUrl);
    }

    public class FetchTransactionData extends AsyncTask<String, Void, Void> {
        private List<CompletedTransaction> transactionList;
        private CompletedTransactionAdapter adapter;

        public FetchTransactionData(List<CompletedTransaction> transactionList, CompletedTransactionAdapter adapter) {
            this.transactionList = transactionList;
            this.adapter = adapter;
        }

        @Override
        protected Void doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                inputStream.close();
                connection.disconnect();

                String json = stringBuilder.toString();
                parseJson(json);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        private void parseJson(String json) throws JSONException {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String petId = jsonObject.getString("pet_ID");
                String petName = jsonObject.getString("pet_name");
                String seller = jsonObject.getString("seller");
                String reference = jsonObject.getString("reference");
                double price = jsonObject.getDouble("price");
                String orderDate = jsonObject.getString("order_date");

                CompletedTransaction transaction = new CompletedTransaction(petId, petName, seller, reference, price, orderDate);
                transactionList.add(transaction);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
        }
    }

}