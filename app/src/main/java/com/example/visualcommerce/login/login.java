package com.example.visualcommerce.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.visualcommerce.CurrentUser;
import com.example.visualcommerce.MainActivity;
import com.example.visualcommerce.R;
import com.example.visualcommerce.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class login extends AppCompatActivity {

    TabLayout tabView;
    ViewPager viewPager;
    FloatingActionButton fb, twitter, instagram;
    float v = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Paper.init(this);
        String userName=Paper.book().read(CurrentUser.userName);
        String password=Paper.book().read(CurrentUser.password);

        if (userName !="" && password !=""){
            if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)){
                allowAccess(userName,password);
            }
        }

        tabView = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.view_pager);
        fb = findViewById(R.id.facebook);
        instagram = findViewById(R.id.instagram);
        twitter = findViewById(R.id.twitter);

        tabView.addTab(tabView.newTab().setText("LOGIN"));
        tabView.addTab(tabView.newTab().setText("SIGN UP"));
        tabView.setTabGravity(TabLayout.GRAVITY_FILL);

        final loginAdapter adapter = new loginAdapter(getSupportFragmentManager(), this, tabView.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabView));
        fb.setTranslationY(300);
        twitter.setTranslationY(300);
        instagram.setTranslationY(300);
        tabView.setTranslationY(300);

        fb.setAlpha(v);
        twitter.setAlpha(v);
        instagram.setAlpha(v);
        tabView.setAlpha(v);

        fb.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        twitter.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        instagram.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        tabView.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(100).start();
        
    }

    private void allowAccess(String userName, String password) {
        final DatabaseReference root= FirebaseDatabase.getInstance().getReference();
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("users").child(userName).exists()) {
                    User userData= snapshot.child("users").child(userName).getValue(User.class);
                    if (userData.getUsername().equals(userName)) {
                        if (userData.getPassword().equals(password)){
                            Toast.makeText(login.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(login.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(login.this, "Password is not matched", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(login.this, "User name is not matched", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(login.this, "This User name is not exists", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}