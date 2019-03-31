package com.example.bt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class book_info_page extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    Button button;

    public String book_name;

    private static final String TAG = "book_info_page";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_info_page);

        Intent intent=getIntent();
        book_name= intent.getExtras().getString("title");
        final String author= intent.getExtras().getString("author");
        String rating= intent.getExtras().getString("rating");
        final String genre= intent.getExtras().getString("genre");
        final String img_url= intent.getExtras().getString("small_image_url");
        final String id= intent.getExtras().getString("id");


        Log.d(TAG,"Selected Book :"+ book_name);
        Log.d(TAG,"Author is :"+ author);
        Log.d(TAG,"Genre is :"+ genre);
        Log.d(TAG,"Rating is :"+ rating);
        Log.d(TAG,"ID is :"+ id);

        imageView = (ImageView) findViewById(R.id.imageView);
        Picasso.with(this).load(img_url).resize(350,500).into(imageView);

    textView=(TextView) findViewById(R.id.textView2);
    textView.setText("\nTitle\t "+book_name + "\nBy Author\t "+author + "\nGenre\t "+genre + "\nRated\t "+rating);

    button= (Button) findViewById(R.id.button2);

    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
            String strDate = "Current Time : " + mdformat.format(calendar.getTime());

            FirebaseDatabase database= FirebaseDatabase.getInstance();
            DatabaseReference mref= database.getReference("issued_books");
            FirebaseAuth mauth = FirebaseAuth.getInstance();
            FirebaseUser user=mauth.getCurrentUser();
            String uid=user.getUid();

            ArrayList<String> book_info = new ArrayList<>();
            book_info.add(book_name);
            book_info.add(author);
            book_info.add(genre);
            book_info.add(img_url);
            book_info.add(id);
            book_info.add(strDate);


            mref.child(uid).push().setValue(book_info);
            Toast.makeText(getApplicationContext(), "Thanks for Issuing  "+book_name+ " at time: "+ strDate , Toast.LENGTH_LONG).show();
            Intent launchactivity = new Intent(getApplicationContext(), searchbuk.class);
            startActivity(launchactivity);

        }
    });

    }

}
