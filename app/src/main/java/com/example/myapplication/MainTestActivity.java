package com.example.myapplication;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainTestActivity extends AppCompatActivity {
    private static final String TAG = "ViewDatabase";

    private FirebaseDatabase mFirebaseDatabase;
    //private FirebaseAuth mAuth;
    //private FirebaseAuth.AuthStateListener nAuthListener;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintest);

        BottomNavigationView navigationView = findViewById(R.id.buttom_nav);

        final HomeFragment homeFragment = new HomeFragment();
        final LessonsFragment lessonsFragment = new LessonsFragment();
        final ProfileFragment profileFragment = new ProfileFragment();

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.home){
                    setFragment(homeFragment);
                    return true;
                }else if(id == R.id.lessons){
                    setFragment(lessonsFragment);
                    return true;
                }else if(id == R.id.profile){
                    setFragment(profileFragment);
                    return true;
                }
                return false;
            }
        });

        navigationView.setSelectedItemId(R.id.home);

    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }
}