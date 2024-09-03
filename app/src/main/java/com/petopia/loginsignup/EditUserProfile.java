package com.petopia.loginsignup;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;

public class EditUserProfile extends MainActivity {

    ImageView backBtn;
    TextView userID;
    EditText eEmail, eNumber;
    private static final int GALLERY_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 201;
    private Uri imageUri;

    ImageView profileImage;

    String UserID;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        init();
        setContentView(R.layout.activity_edit_user_profile);

        getViews();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading image...");
        progressDialog.setCancelable(false);

        backBtn = findViewById(R.id.backBtn);
        profileImage = findViewById(R.id.profileImage);

        UserID = HomeActivity.userIdTv.getText().toString();
        new FetchImageUrlTask().execute(UserID);



        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditUserProfile.this, Settings.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        TextView editPictureTextView = findViewById(R.id.editPicture);
        editPictureTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Choose between gallery or camera
                showPictureDialog();
            }
        });

        Button saveBtn = findViewById(R.id.saveBtn);
        eNumber = findViewById(R.id.eNumber);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEmail = eEmail.getText().toString();
                String userId = sharedPreferences.getString("user_id", "");
                String newNumber = eNumber.getText().toString(); // Initialize newNumber

                if (!newEmail.isEmpty() && !userId.isEmpty() && !newNumber.isEmpty()) {
                    new UpdateEmailAndNumberTask(userId, newEmail, newNumber).execute();
                } else {
                    Toast.makeText(EditUserProfile.this, "User ID, Email, and User Number cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private class FetchImageUrlTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String userId = params[0];
            String imageUrl = "";

            try {
                String imageUrlFetchUrl = "https://pawsomematch.online/android/get_profile_image.php?UserID=" + userId;

                URL url = new URL(imageUrlFetchUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                int responseCode = urlConnection.getResponseCode();
                Log.d(TAG, "Response code: " + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    StringBuilder response = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    reader.close();
                    inputStream.close();

                    imageUrl = response.toString();
                } else {
                    Log.e(TAG, "Error fetching image URL. Response code: " + responseCode);
                }
            } catch (IOException e) {
                Log.e(TAG, "Error fetching image URL: " + e.getMessage());
            }

            return imageUrl;
        }

        @Override
        protected void onPostExecute(String imageUrl) {
            if (!imageUrl.isEmpty()) {
                String imageUrl1 = "https://pawsomematch.online/android/" + imageUrl;
                Picasso.get().load(imageUrl1).into(profileImage);
                Log.d(TAG, "Image URL: " + imageUrl1);
            } else {
                Log.d(TAG, "Error Retrieving Photo");
            }
        }
    }



    private class UpdateEmailAndNumberTask extends AsyncTask<Void, Void, String> {
        private String userId;
        private String newEmail;
        private String newNumber;

        public UpdateEmailAndNumberTask(String userId, String newEmail, String newNumber) {
            this.userId = userId;
            this.newEmail = newEmail;
            this.newNumber = newNumber; // Add this line
        }



        @Override
        protected String doInBackground(Void... voids) {
            String serverResponse = "";
            String userId = sharedPreferences.getString("user_id", "");

            try {
                String boundary = UUID.randomUUID().toString();
                URL url = new URL("https://pawsomematch.online/android/user_edit_email_num.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes("--" + boundary + "\r\n");
                outputStream.writeBytes("Content-Disposition: form-data; name=\"user_id\"\r\n\r\n");
                outputStream.writeBytes(userId + "\r\n");

                outputStream.writeBytes("--" + boundary + "\r\n");
                outputStream.writeBytes("Content-Disposition: form-data; name=\"new_email\"\r\n\r\n");
                outputStream.writeBytes(newEmail + "\r\n");

                outputStream.writeBytes("--" + boundary + "\r\n");
                outputStream.writeBytes("Content-Disposition: form-data; name=\"new_number\"\r\n\r\n");
                outputStream.writeBytes(newNumber + "\r\n");

                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read server response
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    StringBuilder responseBuilder = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }

                    reader.close();
                    serverResponse = responseBuilder.toString();
                } else {
                    serverResponse = "Error updating email. Response code: " + responseCode;
                }

                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                serverResponse = "Exception: " + e.getMessage();
            }

            return serverResponse;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(EditUserProfile.this, result, Toast.LENGTH_SHORT).show();
            // Access the instance variables here
            eEmail.setText(newEmail);
            eNumber.setText(newNumber);
        }
    }



    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                openGallery();
                                break;
                            case 1:
                                checkCameraPermissionAndOpenCamera(); // Check camera permission before opening camera
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    private void checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                // Permission denied, show a message or take appropriate action
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE) {
                imageUri = data.getData();
                // Handle the selected image here
                uploadImageToServer();
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageUri = getImageUri(photo);
                // Handle the captured image here
                uploadImageToServer();
            }
        }
    }

    private Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(
                getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    private void uploadImageToServer() {
        if (imageUri != null) {
            progressDialog.show();
            String userId = sharedPreferences.getString("user_id", ""); // Fetch user_id from shared preferences
            new UploadImageTask(userId).execute();
        } else {
            // Handle error case where imageUri is null
        }
    }

    private class UploadImageTask extends AsyncTask<Void, Void, String> {

        private String userId;

        public UploadImageTask(String userId) {
            this.userId = userId;
        }


        @Override
        protected String doInBackground(Void... voids) {
            String serverResponse = "";
            try {
                String boundary = UUID.randomUUID().toString();
                URL url = new URL("https://pawsomematch.online/android/user_editprof.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes("--" + boundary + "\r\n");
                outputStream.writeBytes("Content-Disposition: form-data; name=\"user_id\"\r\n\r\n");
                outputStream.writeBytes(userId + "\r\n");

                outputStream.writeBytes("--" + boundary + "\r\n");
                outputStream.writeBytes("Content-Disposition: form-data; name=\"profile_photos\"; filename=\"" + imageUri.getLastPathSegment() + "\"\r\n");
                outputStream.writeBytes("Content-Type: image/jpeg\r\n\r\n");

                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                int bytesRead;
                byte[] dataBuffer = new byte[4096];
                while ((bytesRead = inputStream.read(dataBuffer)) != -1) {
                    outputStream.write(dataBuffer, 0, bytesRead);
                }
                outputStream.writeBytes("\r\n");
                outputStream.writeBytes("--" + boundary + "--\r\n");

                inputStream.close();
                outputStream.flush();
                outputStream.close();



                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    serverResponse = "Image uploaded successfully";


                } else {
                    serverResponse = "Error uploading image. Response code: " + responseCode;

                }
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();

                serverResponse = "Exception: " + e.getMessage();
            }

            return serverResponse;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            Toast.makeText(EditUserProfile.this, result, Toast.LENGTH_SHORT).show();
        }


    }

    private void getViews(){
        userID = findViewById(R.id.userIdxD);
        userID.setText(sharedPreferences.getString("user_id",""));
        eEmail = findViewById(R.id.eEmail);
        eEmail.setText(sharedPreferences.getString("email",""));
        // Add debug log to check the value of UserNum
        String userNum = sharedPreferences.getString("UserNum", "");
        Log.d("UserNum", "UserNum from SharedPreferences: " + userNum);

        eNumber = findViewById(R.id.eNumber);
        eNumber.setText(userNum);
    }
}