package com.ahmettekin.instacloneparse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;


public class UploadActivity extends AppCompatActivity {

    ImageView imageView;
    EditText commentText;
    Uri imageData;
    Bitmap selectedImage;
    ParseObject object;
    String comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        imageView = findViewById(R.id.imageView);
        commentText = findViewById(R.id.commentText);

    }

    public void upload(View view) {

        if (imageData == null) {
            Toast.makeText(getApplicationContext(), "resim seçmediniz", Toast.LENGTH_LONG).show();
        }
        else {

            comment = commentText.getText().toString();

            if (comment.matches("")) {
                //Toast.makeText(UploadActivity.this,"uyarı",Toast.LENGTH_LONG).show();
                AlertDialog.Builder alert = new AlertDialog.Builder(UploadActivity.this);
                alert.setTitle("Uyarı");
                alert.setMessage("Yorum olmadan kaydetmek istiyor musunuz?");
                alert.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();

                        ParseFile parseFile = new ParseFile("image.png", bytes);
                        Date currentTime = Calendar.getInstance().getTime();

                        object = new ParseObject("Posts");
                        object.put("image", parseFile);
                        object.put("comment", comment);
                        object.put("username", ParseUser.getCurrentUser().getUsername());
                        object.put("date", currentTime);
                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                if (e != null) {
                                    Toast.makeText(UploadActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                } else {

                                    Toast.makeText(UploadActivity.this, "post uploaded", Toast.LENGTH_LONG).show();
                                    Intent intent2 = new Intent(UploadActivity.this, FeedActivity.class);
                                    startActivity(intent2);


                                }
                            }
                        });

                    }
                });

                alert.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "kaydedilmedi!", Toast.LENGTH_LONG).show();
                    }
                });
                alert.show();
            }

            else {

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();

                ParseFile parseFile = new ParseFile("image.png", bytes);
                Date currentTime = Calendar.getInstance().getTime();

                object = new ParseObject("Posts");
                object.put("image", parseFile);
                object.put("comment", comment);
                object.put("username", ParseUser.getCurrentUser().getUsername());
                object.put("date", currentTime);
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e != null) {
                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        } else {

                            Toast.makeText(getApplicationContext(), "post uploaded", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), FeedActivity.class);
                            startActivity(intent);


                        }
                    }
                });

            }
        }

}
    public void selectImage(View view) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==1 && grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 2 && resultCode == RESULT_OK && data!=null){
            imageData = data.getData();
            try {
                if(Build.VERSION.SDK_INT>=28){
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), imageData);
                    selectedImage= ImageDecoder.decodeBitmap(source);
                    imageView.setImageBitmap(selectedImage);

                } else {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageData);

                    imageView.setImageBitmap(selectedImage);

                }



            }

            catch (IOException e) {
                e.printStackTrace();
            }


        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}