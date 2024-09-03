package com.petopia.loginsignup;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Profile extends MainActivity {

    Button petBtn;
    TextView nameTv;

    ListView listView;
    MyAdapter adapter;
    public static ArrayList<Pets> petsArrayList = new ArrayList<>();

    String url = "https://pawsomematch.online/android/retrievepets.php";
    Pets pets;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = this;
        init();
        retrieveEmail();
        getViews();

        listView = findViewById(R.id.myListView);
        adapter = new MyAdapter(this, petsArrayList);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long pet_ID) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                ProgressDialog progressDialog = new ProgressDialog(view.getContext());

                CharSequence[] dialogItem = {"View Pet Data","Edit Pet Data","Delete Pet Data"};
                builder.setTitle(petsArrayList.get(position).getPet_Name());
                builder.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        switch (i){

                            case 0:
                                startActivity(new Intent(getApplicationContext(),PetDetailsActivity.class)
                                        .putExtra("position",position));
                                break;

                            case 1:
                                break;

                            case 2:
                                break;
                        }
                    }
                });

                builder.create().show();
            }
        });
        retrieveEmail();

        petBtn = (Button) findViewById(R.id.petBtn);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_match:
                    startActivity(new Intent(getApplicationContext(), Match.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;

                case R.id.bottom_paw_pound:
                    startActivity(new Intent(getApplicationContext(), PetSelling.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_profile:
                    return true;

                case R.id.bottom_message:
                    startActivity(new Intent(getApplicationContext(),   Messaging.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });
    }
    private void retrieveEmail() {
        String email = sharedPreferences.getString("email","");
        // Create a new RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);
        // Define the URL for the PHP file that will retrieve the pet data from the database
        String url = "https://pawsomematch.online/android/retrievepets.php";
        // Create a StringRequest object to send a POST request to the server
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server
                        processResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Display an error message
                Toast.makeText(Profile.this, "Error retrieving pet", Toast.LENGTH_SHORT).show();
                Log.e("Volley", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Add the email to the POST request parameters
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };
        queue.add(request);
    }
    private void processResponse(String response) {
        petsArrayList.clear();
        try {
            JSONObject jsonObject = new JSONObject(response);
            String success = jsonObject.getString("success");
            JSONArray jsonArray = jsonObject.getJSONArray("pet_reg2");

            if (success.equals("1")) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    String pet_ID = object.getString("pet_ID");
                    String pet_name = object.getString("pet_name");
                    String category = object.getString("category");
                    String breed = object.getString("breed");
                    String age = object.getString("age");
                    String weight = object.getString("weight");
                    String vaccine_status = object.getString("vaccine_status");
                    String gender = object.getString("gender");
                    String color = object.getString("color");
                    String breedtype = object.getString("breedtype");
                    String studfee = object.getString("studfee");
                    String shares = object.getString("shares");


                    pets = new Pets(pet_ID, pet_name, category, breed, age, weight, vaccine_status, gender, color, breedtype, studfee, shares);
                    petsArrayList.add(pets);
                    adapter.notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void getViews() {
        nameTv = findViewById(R.id.nameTv);
        nameTv.setText(sharedPreferences.getString("first_name", "") + " " + sharedPreferences.getString("middle_name", "") + " " + sharedPreferences.getString("last_name", ""));
        petBtn = findViewById(R.id.petBtn);
        petBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(context,PetRegister.class);
                startActivity(intent);
            }
        });
    }
}