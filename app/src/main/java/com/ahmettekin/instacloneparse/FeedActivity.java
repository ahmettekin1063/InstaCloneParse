package com.ahmettekin.instacloneparse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;


import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


import java.util.ArrayList;

import java.util.List;

public class FeedActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> userNamesFromParse;
    ArrayList<String> userCommentFromParse;
    ArrayList<Bitmap> userImageFromParse;
    PostClass postClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        listView = findViewById(R.id.listview);

        userNamesFromParse =new ArrayList<>();
        userCommentFromParse = new ArrayList<>();
        userImageFromParse = new ArrayList<>();

        postClass = new PostClass(userNamesFromParse, userCommentFromParse,userImageFromParse,this);

        listView.setAdapter(postClass);
        download();
    }

    public void download(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");




        query.orderByDescending("date").findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e!=null){
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
                else{


                    if(objects.size()>0){
                        for(final ParseObject object : objects){

                            ParseFile parseFile = (ParseFile) object.get("image");

                            parseFile.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if(e==null&&data!= null){

                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);




                                        userImageFromParse.add(bitmap);
                                        userNamesFromParse.add(object.getString("username"));
                                        userCommentFromParse.add(object.getString("comment"));

                                        postClass.notifyDataSetChanged();

                                    }
                                }
                            });

                        }
                    }



                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.options,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if(item.getItemId()==R.id.addPost){

            Intent intent = new Intent(FeedActivity.this,UploadActivity.class);
            startActivity(intent);

        }
        if(item.getItemId()==R.id.signOut){
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if(e!=null){
                        Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }  else{
                        Toast.makeText(getApplicationContext(),"logged out",Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(), com.ahmettekin.instacloneparse.SignUpActivity.class);

                        startActivity(intent);


                    }
                }
            });

        }

        return super.onOptionsItemSelected(item);
    }
}