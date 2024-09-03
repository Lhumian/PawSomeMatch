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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MatchActivityF extends Fragment {

    private ListView listView;
    private MyAdapter adapter;
    private ArrayList<Pets> petsArrayList = new ArrayList<>();
    private Pets pets;
    private TextView noPetMessage;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_activity, container, false);

        retrievePetsFromServer();

        listView = view.findViewById(R.id.myListView);
        noPetMessage = view.findViewById(R.id.noPetMessage);

        adapter = new MyAdapter(requireContext(), petsArrayList);
        listView.setAdapter(adapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long pet_ID) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                ProgressDialog progressDialog = new ProgressDialog(requireContext());

                CharSequence[] dialogItem = {"Select Preference"};
                builder.setTitle(petsArrayList.get(position).getPet_Name());
                builder.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Intent intent = new Intent(view.getContext(), Matching.class);

                        intent.putExtra("position1", position);
                        intent.putExtra("petId", petsArrayList.get(position).getPet_ID());
                        intent.putExtra("petname", petsArrayList.get(position).getPet_Name());
                        intent.putExtra("gender", petsArrayList.get(position).getGender());
                        intent.putExtra("category", petsArrayList.get(position).getCategory());
                        intent.putExtra("breed", petsArrayList.get(position).getBreed());
                        switch (i) {
                            case 0:
                                intent.putExtra("preSelectedCategory", petsArrayList.get(position).getCategory());
                                intent.putExtra("preSelectedBreed", petsArrayList.get(position).getBreed());
                                break;

                            case 1:
                                // Handle case 1
                                break;

                            case 2:
                                // Handle case 2
                                break;
                        }
                        requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        requireContext().startActivity(intent);
                    }
                });
                builder.create().show();
            }
        });

        return view;
    }

    private void retrievePetsFromServer() {
        String email = HomeActivity.emailTv.getText().toString().trim();
        // Create a new RequestQueue
        RequestQueue queue = Volley.newRequestQueue(requireContext());
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
                Toast.makeText(getContext(), "Error retrieving pet", Toast.LENGTH_SHORT).show();
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
            if (petsArrayList.isEmpty()) {
                listView.setVisibility(View.GONE);
                noPetMessage.setVisibility(View.VISIBLE);
            } else {
                listView.setVisibility(View.VISIBLE);
                noPetMessage.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
