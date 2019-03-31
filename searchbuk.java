package com.example.bt;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class searchbuk extends AppCompatActivity {

    EditText ed1;
    String book;
    ImageButton img,img1,img2,img3,img4,img5,img6,img7,img8,img9,img10,img11,img12;
    TextView v1,v2,v3,v4,v5,v6,v7,v8;
    private static final String TAG = "searchbuk";
    public ArrayList<String> titles = new ArrayList<>();
    public ArrayList<String> authors = new ArrayList<>();
    public ArrayList<String> genres = new ArrayList<>();
    public ArrayList<String> urls = new ArrayList<>();
    public ArrayList<String> ids = new ArrayList<>();
    public      ArrayList<String>  all_books= new ArrayList<>();


    public HashMap<String,String> rec_based_on_author = new HashMap<>();
    public HashMap<String,String> rec_based_on_genre = new HashMap<>();
    public HashMap<String,String> top_rated = new HashMap<>();

    public FirebaseDatabase database= FirebaseDatabase.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchbuk);

        ed1= (EditText) findViewById(R.id.search_field);
        img= (ImageButton) findViewById(R.id.search_btn);

        FirebaseAuth mauth = FirebaseAuth.getInstance();
        FirebaseUser user=mauth.getCurrentUser();
        String uid=user.getUid();
        DatabaseReference mref= database.getReference("issued_books/"+uid);

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                titles=new ArrayList<>();
                authors=new ArrayList<>();
                genres=new ArrayList<>();
                urls=new ArrayList<>();
                Map<String,String> issued_books= (HashMap<String, String>) dataSnapshot.getValue();
                if(issued_books == null ){
                    Log.d(TAG,"No past History of the User !");
                }
                else{
                    Collection<String> issued = issued_books.values();
                    ArrayList<String> info = new ArrayList<String>(issued);
                 //   Log.d(TAG,"List :"+ info);

                    for(Object s : info){
                        ArrayList<String> al = (ArrayList) s;
                        titles.add(al.get(0));
                        authors.add(al.get(1));
                        genres.add(al.get(2));
                        urls.add(al.get(3));
                        ids.add(al.get(4));

                    }

                }

                Log.d(TAG,"Information of Past Issued Books"+ authors+genres+urls);

                Log.d(TAG,"Past Issued Books by this user are :"+ titles);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        FirebaseDatabase database1= FirebaseDatabase.getInstance();
        DatabaseReference mbooks= database1.getReference("books");
        mbooks.addValueEventListener(new ValueEventListener() {

            HashMap<String,ArrayList<String>> hashMap= new HashMap<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    if( authors.contains(ds.child("author").getValue().toString())){
                        if(!titles.contains(ds.getKey())){
                            ArrayList<String> something = new ArrayList<>();
                            something.add(ds.child("genre").getValue().toString());
                            something.add(ds.child("author").getValue().toString());
                            something.add(ds.child("average_rating").getValue().toString());
                            something.add(ds.child("small_image_url").getValue().toString());
                            something.add(ds.child("id").getValue().toString());
                            hashMap.put( ds.getKey(), something);

                            rec_based_on_author.put(ds.getKey(),ds.child("small_image_url").getValue().toString());
                        }

                    }
                }

                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    if( genres.contains(ds.child("genre").getValue().toString())){
                        if(!titles.contains(ds.getKey())){
                            ArrayList<String> something = new ArrayList<>();
                            something.add(ds.child("genre").getValue().toString());
                            something.add(ds.child("author").getValue().toString());
                            something.add(ds.child("average_rating").getValue().toString());
                            something.add(ds.child("small_image_url").getValue().toString());
                            something.add(ds.child("id").getValue().toString());
                            rec_based_on_genre.put(ds.getKey(),ds.child("small_image_url").getValue().toString() );
                            hashMap.put( ds.getKey(), something);
                        }

                    }
                }
                for(DataSnapshot ds: dataSnapshot.getChildren()) {

                    try
                    {double val= Double.parseDouble(ds.child("average_rating").getValue().toString());
                        if(val > 3.50){
                            if(!titles.contains(ds.getKey())){
                                ArrayList<String> something = new ArrayList<>();
                                something.add(ds.child("genre").getValue().toString());
                                something.add(ds.child("author").getValue().toString());
                                something.add(ds.child("average_rating").getValue().toString());
                                something.add(ds.child("small_image_url").getValue().toString());
                                something.add(ds.child("id").getValue().toString());
                                rec_based_on_genre.put(ds.getKey(),ds.child("small_image_url").getValue().toString() );
                                hashMap.put( ds.getKey(), something);
                                top_rated.put(ds.getKey(), ds.child("small_image_url").getValue().toString());
                            }
                        }}
                    catch (NumberFormatException n){
                        continue;}

                }

                Log.d(TAG,"Recommendations hashmap :"+ hashMap);
                Log.d(TAG,"Recommendations :"+ rec_based_on_author+rec_based_on_genre+top_rated);


                    Random  random = new Random();
                    Random r= new Random();
              /*  try {
                    img1 = (ImageButton) findViewById(R.id.image1);
                    String rk1 = urls.get(r.nextInt(urls.size()));
                    Picasso.with(searchbuk.this).load(rk1).resize(100, 150).into(img1);
                    urls.remove(rk1);

                    img2 = (ImageButton) findViewById(R.id.image2);
                    String rk2 = urls.get(r.nextInt(urls.size()));
                    Picasso.with(searchbuk.this).load(rk2).resize(100, 150).into(img2);
                    urls.remove(rk2);

                    img3 = (ImageButton) findViewById(R.id.image3);
                    String rk3 = urls.get(r.nextInt(urls.size()));
                    Picasso.with(searchbuk.this).load(rk3).resize(100, 150).into(img3);
                    urls.remove(rk3);

                    img4 = (ImageButton) findViewById(R.id.image4);
                    String rk4 = urls.get(r.nextInt(urls.size()));
                    Picasso.with(searchbuk.this).load(rk4).resize(100, 150).into(img4);
                    urls.remove(rk4);
                }
                catch (IllegalArgumentException exc){} */

                try {
                    rec_based_on_author.putAll(rec_based_on_genre);
                    ArrayList<String> keys  = new ArrayList<String>(rec_based_on_author.keySet());

                    ArrayList<String> keys2  = new ArrayList<String>(top_rated.keySet());

                    img5 = (ImageButton) findViewById(R.id.image5);
                    v1= (TextView) findViewById(R.id.textView01);

                    String  randomKey = keys.get( random.nextInt(keys.size()));
                    img5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          //  Log.d(TAG, "dk"+ hashMap.keySet() );
                          //  Log.d(TAG, "dk"+ hashMap.values() );
                            for(String entry : hashMap.keySet()){
                                if(hashMap.get(entry).get(3).equals(rec_based_on_author.get(randomKey))){
                                   Intent intent=new Intent(getApplicationContext(),book_info_page.class);
                                    intent.putExtra("title",randomKey);
                                    intent.putExtra("genre", hashMap.get(entry).get(0));
                                    intent.putExtra("author",hashMap.get(entry).get(1));
                                    intent.putExtra("rating",hashMap.get(entry).get(2) );
                                    intent.putExtra("small_image_url",hashMap.get(entry).get(3));
                                    intent.putExtra("id",hashMap.get(entry).get(4));
                                    startActivity(intent);
                                }
                            }
                        }
                    });
                    Log.d(TAG,"Random test :"+ rec_based_on_author.get(randomKey) + randomKey);
                    keys.remove(randomKey);
                    Picasso.with(searchbuk.this).load(rec_based_on_author.get(randomKey)).resize(100, 150).into(img5);
                    v1.setText(randomKey);

                    img6 = (ImageButton) findViewById(R.id.image6);
                    v2 = (TextView) findViewById(R.id.textView02);
                    String  randomKey01 = keys.get( random.nextInt(keys.size()));
                    img6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for(String entry : hashMap.keySet()){
                                if(hashMap.get(entry).get(3).equals(rec_based_on_author.get(randomKey01))){
                                    Intent intent=new Intent(getApplicationContext(),book_info_page.class);
                                    intent.putExtra("title",randomKey01);
                                    intent.putExtra("genre", hashMap.get(entry).get(0));
                                    intent.putExtra("author",hashMap.get(entry).get(1));
                                    intent.putExtra("rating",hashMap.get(entry).get(2) );
                                    intent.putExtra("small_image_url",hashMap.get(entry).get(3));
                                    intent.putExtra("id",hashMap.get(entry).get(4));
                                    startActivity(intent);
                                }
                            }
                        }
                    });
                    Log.d(TAG,"Random test :"+ rec_based_on_author.get(randomKey01) + randomKey01);
                    keys.remove(randomKey01);
                    Picasso.with(searchbuk.this).load(rec_based_on_author.get(randomKey01)).resize(100, 150).into(img6);
                    v2.setText(randomKey01);

                    img7 = (ImageButton) findViewById(R.id.image7);
                    v3= (TextView) findViewById(R.id.textView03);
                    String  randomKey02 = keys.get( random.nextInt(keys.size()));
                    img7.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for(String entry : hashMap.keySet()){
                                if(hashMap.get(entry).get(3).equals(rec_based_on_author.get(randomKey02))){
                                    Intent intent=new Intent(getApplicationContext(),book_info_page.class);
                                    intent.putExtra("title",randomKey02);
                                    intent.putExtra("genre", hashMap.get(entry).get(0));
                                    intent.putExtra("author",hashMap.get(entry).get(1));
                                    intent.putExtra("rating",hashMap.get(entry).get(2) );
                                    intent.putExtra("small_image_url",hashMap.get(entry).get(3));
                                    intent.putExtra("id",hashMap.get(entry).get(4));
                                    startActivity(intent);
                                }
                            }
                        }
                    });
                    Log.d(TAG,"Random test :"+ rec_based_on_author.get(randomKey02) + randomKey02);
                    keys.remove(randomKey02);
                    Picasso.with(searchbuk.this).load(rec_based_on_author.get(randomKey02)).resize(100, 150).into(img7);
                    v3.setText(randomKey02);

                    img8 = (ImageButton) findViewById(R.id.image8);
                    v4= (TextView) findViewById(R.id.textView04);
                    String  randomKey03 = keys.get( random.nextInt(keys.size()));
                    img8.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for(String entry : hashMap.keySet()){
                                if(hashMap.get(entry).get(3).equals(rec_based_on_author.get(randomKey03))){
                                    Intent intent=new Intent(getApplicationContext(),book_info_page.class);
                                    intent.putExtra("title",randomKey03);
                                    intent.putExtra("genre", hashMap.get(entry).get(0));
                                    intent.putExtra("author",hashMap.get(entry).get(1));
                                    intent.putExtra("rating",hashMap.get(entry).get(2) );
                                    intent.putExtra("small_image_url",hashMap.get(entry).get(3));
                                    intent.putExtra("id",hashMap.get(entry).get(4));
                                    startActivity(intent);
                                }
                            }
                        }
                    });
                    Log.d(TAG,"Random test :"+ rec_based_on_author.get(randomKey03) + randomKey03);
                    keys.remove(randomKey03);
                    Picasso.with(searchbuk.this).load(rec_based_on_author.get(randomKey03)).resize(100, 150).into(img8);
                    v4.setText(randomKey03);


                    img9 = (ImageButton) findViewById(R.id.image9);
                    v5= (TextView) findViewById(R.id.textView05);
                    String  randomKey2 = keys2.get( random.nextInt(keys2.size()));
                    img9.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for(String entry : hashMap.keySet()){
                                if(hashMap.get(entry).get(3).equals(top_rated.get(randomKey2))){
                                    Intent intent=new Intent(getApplicationContext(),book_info_page.class);
                                    intent.putExtra("title",randomKey2);
                                    intent.putExtra("genre", hashMap.get(entry).get(0));
                                    intent.putExtra("author",hashMap.get(entry).get(1));
                                    intent.putExtra("rating",hashMap.get(entry).get(2) );
                                    intent.putExtra("small_image_url",hashMap.get(entry).get(3));
                                    intent.putExtra("id",hashMap.get(entry).get(4));
                                    startActivity(intent);
                                }
                            }
                        }
                    });
                    Log.d(TAG,"Random test :"+ top_rated.get(randomKey2) + randomKey2);
                    keys2.remove(randomKey2);
                    Picasso.with(searchbuk.this).load(top_rated.get(randomKey2)).resize(100, 150).into(img9);
                    v5.setText(randomKey2);

                    img10 = (ImageButton) findViewById(R.id.image10);
                    v6= (TextView) findViewById(R.id.textView06);
                    String  randomKey3 = keys2.get( random.nextInt(keys2.size()));
                    img10.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for(String entry : hashMap.keySet()){
                                if(hashMap.get(entry).get(3).equals(top_rated.get(randomKey3))){
                                    Intent intent=new Intent(getApplicationContext(),book_info_page.class);
                                    intent.putExtra("title",randomKey3);
                                    intent.putExtra("genre", hashMap.get(entry).get(0));
                                    intent.putExtra("author",hashMap.get(entry).get(1));
                                    intent.putExtra("rating",hashMap.get(entry).get(2) );
                                    intent.putExtra("small_image_url",hashMap.get(entry).get(3));
                                    intent.putExtra("id",hashMap.get(entry).get(4));
                                    startActivity(intent);
                                }
                            }
                        }
                    });
                    Log.d(TAG,"Random test :"+ top_rated.get(randomKey3) + randomKey3);
                    keys2.remove(randomKey3);
                    Picasso.with(searchbuk.this).load(top_rated.get(randomKey3)).resize(100, 150).into(img10);
                    v6.setText(randomKey3);


                    img11 = (ImageButton) findViewById(R.id.image11);
                    v7= (TextView) findViewById(R.id.textView07);
                    String  randomKey4 = keys2.get( random.nextInt(keys2.size()));
                    img11.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for(String entry : hashMap.keySet()){
                                if(hashMap.get(entry).get(3).equals(top_rated.get(randomKey4))){
                                    Intent intent=new Intent(getApplicationContext(),book_info_page.class);
                                    intent.putExtra("title",randomKey4);
                                    intent.putExtra("genre", hashMap.get(entry).get(0));
                                    intent.putExtra("author",hashMap.get(entry).get(1));
                                    intent.putExtra("rating",hashMap.get(entry).get(2) );
                                    intent.putExtra("small_image_url",hashMap.get(entry).get(3));
                                    intent.putExtra("id",hashMap.get(entry).get(4));
                                    startActivity(intent);
                                }
                            }
                        }
                    });
                    Log.d(TAG,"Random test :"+ top_rated.get(randomKey4) + randomKey4);
                    keys2.remove(randomKey4);
                    Picasso.with(searchbuk.this).load(top_rated.get(randomKey4)).resize(100, 150).into(img11);
                    v7.setText(randomKey4);

                    img12 = (ImageButton) findViewById(R.id.image12);
                    v8= (TextView) findViewById(R.id.textView08);
                    String  randomKey5 = keys2.get( random.nextInt(keys2.size()));
                    img12.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for(String entry : hashMap.keySet()){
                                if(hashMap.get(entry).get(3).equals(top_rated.get(randomKey5))){
                                    Intent intent=new Intent(getApplicationContext(),book_info_page.class);
                                    intent.putExtra("title",randomKey5);
                                    intent.putExtra("genre", hashMap.get(entry).get(0));
                                    intent.putExtra("author",hashMap.get(entry).get(1));
                                    intent.putExtra("rating",hashMap.get(entry).get(2) );
                                    intent.putExtra("small_image_url",hashMap.get(entry).get(3));
                                    intent.putExtra("id",hashMap.get(entry).get(4));
                                    startActivity(intent);

                                }
                            }
                        }
                    });
                    Log.d(TAG,"Random test :"+ top_rated.get(randomKey5) + randomKey5);
                    keys2.remove(randomKey5);
                    Picasso.with(searchbuk.this).load(top_rated.get(randomKey5)).resize(100, 150).into(img12);
                    v8.setText(randomKey5);
                }
                catch (IllegalArgumentException ar){

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        img.setOnClickListener(v -> {

            book= ed1.getText().toString().trim();
            search_for_book(book);

        });

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.item1:

                Toast.makeText(getApplicationContext(),"My profile",Toast.LENGTH_LONG).show();
                return true;
            case R.id.item2:
                Intent intent= new Intent(getApplicationContext(),help.class);
                startActivity(intent);
               // Toast.makeText(getApplicationContext(),"Help",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void search_for_book(final String book){

        String book2;
        ArrayList<String> all_books2 = new ArrayList<>();

        if (TextUtils.isEmpty(book)){
            Toast.makeText(getApplicationContext(), "Search Field cannot be empty !", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        DatabaseReference mref2= database2.getReference("books");

        mref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    all_books.add(childSnapshot.getKey());
                   // Log.d(TAG,"Key value is:" + clubkey);
            //        all_books.add(clubkey);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Log.d(TAG,"Key value in allbooks2 is:" + all_books2);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mref= database.getReference("books").child(book);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();

               /* title= book;
                genre= map.get("genre");
                author=map.get("author");
                rating=map.get("average_rating");

                Log.d(TAG,"Selected Book :"+ title);
                Log.d(TAG,"Author is :"+ author);
                Log.d(TAG,"Genre is :"+ genre);
                Log.d(TAG,"Rating is :"+ rating);*/

                Intent intent=new Intent(getApplicationContext(),book_info_page.class);
                intent.putExtra("title",book);
                intent.putExtra("genre", map.get("genre"));
                intent.putExtra("author", map.get("author"));
                intent.putExtra("rating", map.get("average_rating"));
                intent.putExtra("small_image_url", map.get("small_image_url"));
                intent.putExtra("id", map.get("id"));
                startActivity(intent);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG,"Failed to read value.", databaseError.toException());
            }
        });

    }


}
