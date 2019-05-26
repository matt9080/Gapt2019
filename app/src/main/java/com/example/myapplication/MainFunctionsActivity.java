package com.example.myapplication;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainFunctionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainfunction);

        BottomNavigationView navigationView = findViewById(R.id.buttom_nav); //load bottom toolbar

        //initialize fragments
        final LessonsFragment lessonsFragment = new LessonsFragment();
        final HomeFragment homeFragment = new HomeFragment();
        final ProfileFragment profileFragment = new ProfileFragment();

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) { //change fragment according to icon pressed
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

        navigationView.setSelectedItemId(R.id.home);        //default fragment is home fragment
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }
}