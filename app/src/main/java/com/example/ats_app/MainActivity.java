package com.example.ats_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    //툴바*
    private ImageView ivMenu;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    //


    //툴바 뒤로가기
    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
        else{

        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivMenu = findViewById(R.id.iv_menu);
        drawerLayout = findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigation);

        Menu menu = navigationView.getMenu();


        //액션바 변경하기(들어갈 수 있는 타입 : Toolbar type*
        setSupportActionBar((androidx.appcompat.widget.Toolbar) toolbar);

        ivMenu.setOnClickListener(new View.OnClickListener() {                       //메뉴 터치시 메뉴 드로어 열림

            @Override
            public void onClick(View v) {
                MenuItem first = menu.findItem(R.id.action_login);
                first.setTitle("로그인");
                first.setIcon(R.drawable.ic_baseline_login_24);
                MenuItem second = menu.findItem(R.id.action_logup);
                second.setIcon(R.drawable.ic_baseline_person_add_24);
                second.setTitle("회원가입");

                drawerLayout.openDrawer(GravityCompat.START);

            }
        });

        //툴바* 이것도 비로그인시 만 먼저
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);             //메뉴바 안의 항목들 터치리스너
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.action_login) {
                   Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                   startActivity(intent);
                } else if (id == R.id.action_logup) {
                    Intent intent = new Intent(getBaseContext(), LogupActivity.class);
                    startActivity(intent);
                }

                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });

    }


}