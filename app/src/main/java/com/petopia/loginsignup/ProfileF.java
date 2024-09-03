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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class ProfileF extends Fragment {

    ImageView petBtn;
    TextView nameTv;

    ListView listView;
    MyAdapter adapter;

    private SharedPreferences sharedPreferences;
    public static ArrayList<Pets> petsArrayList = new ArrayList<>();

    String url = "https://pawsomematch.online/android/retrievepets.php";
    Pets pets;
    private TextView noPetMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        retrieveEmail();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        listView = view.findViewById(R.id.myListView);
        adapter = new MyAdapter(getContext(), petsArrayList);

        listView.setAdapter(adapter);

        noPetMessage = view.findViewById(R.id.noPetMessage);

        if (noPetMessage != null) {
            if (petsArrayList.isEmpty()) {
                listView.setVisibility(View.GONE);
                noPetMessage.setVisibility(View.VISIBLE);
            } else {
                adapter.notifyDataSetChanged();
            }
        }

         listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             // Inside the onItemClick method in the listView.setOnItemClickListener block
             @Override
             public void onItemClick(AdapterView<?> parent, View view, final int position, long pet_ID) {
                 AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                 ProgressDialog progressDialog = new ProgressDialog(view.getContext());

                 builder.setTitle(petsArrayList.get(position).getPet_Name());

                 CharSequence[] dialogItems;
                 if (petsArrayList.get(position).getStudfee().equals("Stud Fee")) {
                     dialogItems = new CharSequence[]{"View Pet Data", "Edit Stud Fee", "Remove Pet"};
                 } else if (petsArrayList.get(position).getStudfee().equals("Share Offspring")) {
                     dialogItems = new CharSequence[]{"View Pet Data", "Edit Shares", "Remove Pet"};
                 } else {
                     dialogItems = new CharSequence[]{"View Pet Data", "Remove Pet"};
                 }

                 // Inside the onItemClick method in the listView.setOnItemClickListener block
                 builder.setItems(dialogItems, new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int i) {
                         switch (i) {
                             case 0:
                                 startActivity(new Intent(getContext(), PetDetailsActivity.class)
                                         .putExtra("position", position));
                                 break;
                             case 1:
                                 if (petsArrayList.get(position).getStudfee().equals("Share Offspring")) {
                                     // Handle the "Edit Stud Fee" option here
                                     startActivity(new Intent(getContext(), EditShares.class)
                                             .putExtra("position", position)
                                             .putExtra("pet_ID", petsArrayList.get(position).getPet_ID())
                                             .putExtra("shares", petsArrayList.get(position).getShares()));
                                 } else if (petsArrayList.get(position).getStudfee().equals("Stud Fee")) {
                                     // Handle the "Edit Shares" option here
                                     startActivity(new Intent(getContext(), EditStudFee.class)
                                             .putExtra("position", position)
                                             .putExtra("pet_ID", petsArrayList.get(position).getPet_ID())
                                             .putExtra("shares", petsArrayList.get(position).getShares()));
                                 }
                                 break;
                             case 2:
                                 // Handle the "Remove Pet" option here
                                 removePet(position);
                                 break;
                         }
                     }
                 });

                 builder.create().show();
             }

         });

        getViews(view);

        petBtn = (ImageView) view.findViewById(R.id.petBtn);

        return view;
    }

    private void retrieveEmail() {
        String email = HomeActivity.emailTv.getText().toString();
        // Create a new RequestQueue
        RequestQueue queue = Volley.newRequestQueue(getContext());
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
    private void getViews(View view) {
        nameTv = view.findViewById(R.id.nameTv);

        // Retrieve data from SharedPreferences and set it in TextView
        String firstName = sharedPreferences.getString("first_name", "");
        String middleName = sharedPreferences.getString("middle_name", "");
        String lastName = sharedPreferences.getString("last_name", "");

        nameTv.setText(firstName + " " + middleName + " " + lastName);

        petBtn = view.findViewById(R.id.petBtn);
        petBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PetRegister.class);
                startActivity(intent);
            }
        });
    }

    private void removePet(int position) {
        AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(getContext());
        confirmationDialog.setTitle("Confirm Pet Removal");
        confirmationDialog.setMessage("Are you sure you want to remove this pet?");
        confirmationDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User confirmed the removal, proceed with the removal process
                String user_ID = HomeActivity.userIdTv.getText().toString().trim();
                String pet_ID = petsArrayList.get(position).getPet_ID();

                // Create a RequestQueue
                RequestQueue queue = Volley.newRequestQueue(getContext());

                // Define the URL for your PHP script
                String url = "https://pawsomematch.online/android/remove_pet.php"; // Replace with your server's URL

                // Create a StringRequest to send a POST request to the server
                StringRequest request = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Handle the response from the server
                                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                                petsArrayList.remove(position);
                                adapter.notifyDataSetChanged();

                                refreshFragmentContent();
                                if (response.equals("Pet removed successfully")) {
                                    // Remove the pet from the local list
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle the error
                                Toast.makeText(getContext(), "Error removing pet", Toast.LENGTH_SHORT).show();
                                Log.e("Volley", error.toString());
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        // Add user_ID and pet_ID to the POST request parameters
                        Map<String, String> params = new HashMap<>();
                        params.put("user_ID", user_ID);
                        params.put("pet_ID", pet_ID);
                        return params;
                    }
                };

                // Add the request to the queue to execute it
                queue.add(request);
            }
        });
        confirmationDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User canceled the removal, do nothing
            }
        });
        confirmationDialog.create().show();
    }
    private void refreshFragmentContent() {
        // Clear the current list of pets
        petsArrayList.clear();
        // Notify the adapter that the data has changed
        adapter.notifyDataSetChanged();
        // Trigger the retrieval of pets again
        retrieveEmail();
    }


}
