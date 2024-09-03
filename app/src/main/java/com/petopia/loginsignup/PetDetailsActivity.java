package com.petopia.loginsignup;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.petopia.loginsignup.settings.About;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PetDetailsActivity extends MainActivity {

    private TextView tvid, tvuserid, tvpetname, tvspecies, tvbreed, tvage, tvweight, tvvax, tvgen, tvcolor,etDescription;
    ImageView backBtn;
    private int position;
    private ImageView imgPhoto, imgCertification, imgVaccine;
    private Uri selectedCertificationUri, selectedVaccineUri, selectedPhotoUri;

    private static final int REQUEST_PERMISSION = 1;
    private static final int REQUEST_CERTIFICATION_PICKER = 2;
    private static final int REQUEST_VACCINE_PICKER = 3;
    private static final int REQUEST_PHOTO_PICKER = 4;
    private String petId;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_details);

        backBtn = findViewById(R.id.backBtn);
        tvid = findViewById(R.id.txtid);
        tvpetname = findViewById(R.id.txtpetname);
        tvspecies = findViewById(R.id.txtspecies);
        tvage = findViewById(R.id.txtage);
        tvweight = findViewById(R.id.txtweight);
        /*tvvax = findViewById(R.id.txtvax);*/
        tvgen = findViewById(R.id.txtgender);
        tvbreed = findViewById(R.id.txtbreed);
        tvcolor = findViewById(R.id.txtcolor);

        etDescription = findViewById(R.id.etDescription);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading image...");
        progressDialog.setCancelable(false);

        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");

        tvid.setText(ProfileF.petsArrayList.get(position).getPet_ID());
        petId = ProfileF.petsArrayList.get(position).getPet_ID();
        tvpetname.setText(ProfileF.petsArrayList.get(position).getPet_Name());
        tvbreed.setText(ProfileF.petsArrayList.get(position).getBreed());
        tvcolor.setText(ProfileF.petsArrayList.get(position).getColor());
        tvspecies.setText(ProfileF.petsArrayList.get(position).getCategory());

        tvage.setText(ProfileF.petsArrayList.get(position).getAge());
        tvweight.setText("" + ProfileF.petsArrayList.get(position).getWeight());
        /*tvvax.setText("Vaccine Status: " + ProfileF.petsArrayList.get(position).getVaccine_status());*/
        tvgen.setText(ProfileF.petsArrayList.get(position).getGender());

        /*imgCertification = findViewById(R.id.imgCertification);
        imgVaccine = findViewById(R.id.imgVaccine);*/
        imgPhoto = findViewById(R.id.imgPhoto);

        ImageView btnUploadCertification = findViewById(R.id.btnUploadCertification);
        btnUploadCertification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PetDetailsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    pickCertificationImage();
                } else {
                    ActivityCompat.requestPermissions(PetDetailsActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CERTIFICATION_PICKER); // Request permission when the button is clicked
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        ImageView btndesc = findViewById(R.id.btndesc);

        btndesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PetDetailsActivity.this, PetDescription.class);
                intent.putExtra("petId", petId);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        ImageView btnUploadVaccine = findViewById(R.id.btnUploadVaccine);
        btnUploadVaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PetDetailsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    pickVaccineImage();
                } else {
                    ActivityCompat.requestPermissions(PetDetailsActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_VACCINE_PICKER); // Request permission when the button is clicked
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        });

        ImageView btnUploadPhoto = findViewById(R.id.btnUploadPhoto);
        btnUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PetDetailsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    pickPhotoImage();
                } else {
                    ActivityCompat.requestPermissions(PetDetailsActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_PHOTO_PICKER); // Request permission when the button is clicked
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        });


        // Load the previously uploaded images (if any)
        /*loadCertificationImage();
        loadVaccineImage();*/
        loadProfilePhotoImage();
        fetchDescriptionFromDatabase();

        ImageView btnParent = findViewById(R.id.btnParent);
        btnParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PetDetailsActivity.this, ParentBreedActivity.class);
                String category = ProfileF.petsArrayList.get(position).getCategory();
                String breed = ProfileF.petsArrayList.get(position).getBreed();
                intent.putExtra("petId", petId);
                intent.putExtra("category", category);
                intent.putExtra("breed", breed);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void loadCertificationImage() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                return getImageUrlFromDatabase("certification");
            }

            @Override
            protected void onPostExecute(String imageUrl) {
                if (!imageUrl.isEmpty()) {
                    Picasso.get().load(imageUrl).into(imgCertification);
                }
            }
        }.execute();
    }

    private void loadVaccineImage() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                return getImageUrlFromDatabase("vaccine");
            }

            @Override
            protected void onPostExecute(String imageUrl) {
                if (!imageUrl.isEmpty()) {
                    Picasso.get().load(imageUrl).into(imgVaccine);
                }
            }
        }.execute();
    }

    private void loadProfilePhotoImage() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                return getImageUrlFromDatabase("photo");
            }

            @Override
            protected void onPostExecute(String imageUrl) {
                if (!imageUrl.isEmpty()) {
                    Picasso.get().load(imageUrl).into(imgPhoto);
                }
            }
        }.execute();
    }



    private String getImageUrlFromDatabase(String imageType) {
        String imageUrl = "https://pawsomematch.online/android/pictures/";

        // Make an HTTP request to your PHP script passing petId and imageType
        // Handle the response and extract the image URL

        String phpUrl = "https://pawsomematch.online/android/retrieveuploadedphoto.php";
        String postData = "pet_id=" + petId + "&image_type=" + imageType;

        try {
            URL url = new URL(phpUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(postData.getBytes());
            os.flush();
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String responseLine;
            StringBuilder response = new StringBuilder();

            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            br.close();

            // Extract the image URL from the response
            String imageUrlFromResponse = response.toString();

            if (!imageUrlFromResponse.isEmpty()) {
                return imageUrl + imageUrlFromResponse;
            } else {
                return ""; // Return an empty string if no image URL found
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ""; // Return an empty string on error
        }
    }

    private void pickCertificationImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CERTIFICATION_PICKER);
    }

    private void pickVaccineImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_VACCINE_PICKER);
    }

    private void pickPhotoImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PHOTO_PICKER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Handle permission for btnUploadCertification
                pickCertificationImage();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CERTIFICATION_PICKER) {
            // Handle permission for btnUploadCertification
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickCertificationImage();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_VACCINE_PICKER) {
            // Handle permission for btnUploadVaccine
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickVaccineImage();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_PHOTO_PICKER) {
            // Handle permission for btnUploadPhoto
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickPhotoImage();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CERTIFICATION_PICKER) {
                if (data != null) {
                    selectedCertificationUri = data.getData();
                    uploadImage(selectedCertificationUri, imgCertification, "certification");
                } else {
                    Toast.makeText(this, "Failed to pick an image", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == REQUEST_VACCINE_PICKER) {
                if (data != null) {
                    selectedVaccineUri = data.getData();
                    uploadImage(selectedVaccineUri, imgVaccine, "vaccine");
                } else {
                    Toast.makeText(this, "Failed to pick an image", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == REQUEST_PHOTO_PICKER) {
                if (data != null) {
                    selectedPhotoUri = data.getData();
                    selectedPhotoUri = data.getData();
                    uploadImage(selectedPhotoUri, imgPhoto, "photo");
                } else {
                    Toast.makeText(this, "Failed to pick an image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String generateUniqueName(String uploadType) {
        String ext = ".jpg"; // Use the appropriate image extension
        return uploadType + "_" + UUID.randomUUID().toString() + ext;
    }


    private void uploadImage(Uri imageUri, ImageView imageView, String uploadType) {
        progressDialog.show(); // Show the loading indicator

        String url = "https://pawsomematch.online/android/testupload.php"; // Replace with the actual URL of your PHP script

        OkHttpClient client = new OkHttpClient();

        String filename = generateUniqueName(uploadType);  // Generate a unique filename

        // Get the file path from the URI using the ContentResolver
        String filePath = getFilePathFromUri(imageUri);
        if (filePath == null) {
            progressDialog.dismiss(); // Dismiss the loading indicator
            Toast.makeText(this, "Failed to get file path", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(filePath);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("pet_id", petId)
                .addFormDataPart("image_type", uploadType)
                .addFormDataPart("photo", filename + ".jpg", RequestBody.create(MediaType.parse("image/*"), file))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss(); // Dismiss the loading indicator
                        Toast.makeText(PetDetailsActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                progressDialog.dismiss(); // Dismiss the loading indicator

                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PetDetailsActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                            /*Picasso.get().load(imageUri).into(imageView);*/
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PetDetailsActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


    private String getFilePathFromUri(Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            filePath = cursor.getString(columnIndex);
            cursor.close();
        }
        return filePath;
    }

    private void savePetDetails() {

        OkHttpClient client = new OkHttpClient();

        JSONObject params = new JSONObject();
        try {
            params.put("pet_id", petId);
            // Add other parameters you want to send
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestBody body = RequestBody.create(MediaType.parse("application/json"), params.toString());

        Request request = new Request.Builder()
                .url("https://pawsomematch.online/android/testupload.php")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("SavePetDetails", "Request failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.d("SavePetDetails", "Request successful: " + responseBody);
                } else {
                    Log.e("SavePetDetails", "Request unsuccessful: " + response.code());
                }
            }
        });
    }

    private void fetchDescriptionFromDatabase() {
        OkHttpClient client = new OkHttpClient();
        String phpUrl = "https://pawsomematch.online/android/retrieve_description.php"; // Replace with the actual URL

        // Create a JSON object to send the pet_id
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("pet_id", petId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        Request request = new Request.Builder()
                .url(phpUrl)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseBody = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            etDescription.setText(responseBody);
                        }
                    });
                }
            }
        });
    }


    private void saveDescriptionToDatabase() {
        String description = etDescription.getText().toString();

        OkHttpClient client = new OkHttpClient();
        JSONObject params = new JSONObject();
        try {
            params.put("pet_id", petId);
            params.put("pet_description", description);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), params.toString());

        String phpUrl = "https://pawsomematch.online/android/add_description.php"; // Replace with the actual URL

        Request request = new Request.Builder()
                .url(phpUrl)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Handle the response after saving the description, if needed
                }
            }
        });
    }

}