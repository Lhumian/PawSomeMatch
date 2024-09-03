package com.petopia.loginsignup;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PetSellingF extends Fragment {

    ImageView petBtn, btn_browse;
    TextView nameTv;

    ListView listView;
    SellingAdapter adapter;
    public static ArrayList<SellPets> petsSellArrayList = new ArrayList<>();
    String url = "https://pawsomematch.online/android/retrievesellingpets.php";
    SellPets pets;
    private TextView noPetMessage;

    Button btnOrder;
    Button btnSale;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pet_selling, container, false);
        retrieveEmail();

        listView = view.findViewById(R.id.selling_list);
        adapter = new SellingAdapter(requireContext(), petsSellArrayList);
        listView.setAdapter(adapter);

        noPetMessage = view.findViewById(R.id.noPetMessage);

        getViews(view);


        return view;
    }
    private void retrieveEmail() {
        String email = HomeActivity.emailTv.getText().toString().trim();
        // Create a new RequestQueue
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        // Define the URL for the PHP file that will retrieve the pet data from the database
        String url = "https://pawsomematch.online/android/retrievesellingpets.php";
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
        petsSellArrayList.clear();
        try {
            JSONObject jsonObject = new JSONObject(response);
            String success = jsonObject.getString("success");
            JSONArray jsonArray = jsonObject.getJSONArray("selling");

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
                    float price = Float.parseFloat(object.getString("price"));
                    String description = object.getString("description");

                    pets = new SellPets(pet_ID, pet_name, category, breed, age, weight, vaccine_status, gender, color, description, breedtype, price);
                    petsSellArrayList.add(pets);
                    adapter.notifyDataSetChanged();
                }
            }     if (noPetMessage != null) {
                // The view exists, so set its visibility
                if (petsSellArrayList.isEmpty()) {
                    listView.setVisibility(View.GONE); // Hide the ListView
                    noPetMessage.setVisibility(View.VISIBLE); // Show the "NO PET REGISTERED" message
                } else {
                    adapter.notifyDataSetChanged(); // Notify the adapter of data change
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void getViews(View view) {
        // Find the buttons within the fragment's view hierarchy
        petBtn = view.findViewById(R.id.btn_post);
        petBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), AddPost.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btn_browse = view.findViewById(R.id.btn_browse);
        btn_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), BrowsePet.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
}