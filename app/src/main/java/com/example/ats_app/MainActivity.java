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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.naver.maps.map.NaverMapSdk;

public class MainActivity extends AppCompatActivity {

    private MapFragment map;

    //툴바*
    private ImageView ivMenu;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    //


    //파이어베이스
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

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
        map.onDestroy();
    }
    @Override
    public void onResume() {

        super.onResume();
        //다시 메인화면으로 왔을때 맵 초기화 db에서 값 다시가져오게 하기
        map = new MapFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainerView, map).commit();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ivMenu = findViewById(R.id.iv_menu);
        drawerLayout = findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigation);
        firebaseAuth=firebaseAuth.getInstance();

        Menu menu = navigationView.getMenu();
        db = FirebaseFirestore.getInstance();


        //액션바 변경하기(들어갈 수 있는 타입 : Toolbar type*
        setSupportActionBar((androidx.appcompat.widget.Toolbar) toolbar);

        ivMenu.setOnClickListener(new View.OnClickListener() {                       //메뉴 터치시 메뉴 드로어 열림
            //로그인 버튼쪽만 있게 함 *
            @Override
            public void onClick(View v) {
                //유저가 로그인 되어있을 경우 이프문은 파베 연결시 추가
                if(firebaseAuth.getCurrentUser() != null){
                    //메뉴추가
                    MenuItem first = menu.findItem(R.id.action_login);
                    first.setTitle("프로필");
                    first.setIcon(R.drawable.profile);
                    MenuItem second = menu.findItem(R.id.action_logup);
                    second.setTitle("로그아웃");
                    second.setIcon(R.drawable.sign_out);

                }
                //비로그인인 경우
                else{
                    MenuItem first = menu.findItem(R.id.action_login);
                    first.setTitle("로그인");
                    first.setIcon(R.drawable.ic_baseline_login_24);
                    MenuItem second = menu.findItem(R.id.action_logup);
                    second.setIcon(R.drawable.ic_baseline_person_add_24);
                    second.setTitle("회원가입");
                }

                drawerLayout.openDrawer(GravityCompat.START);

            }
        });

        //툴바
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);             //메뉴바 안의 항목들 터치리스너
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_login) {
                    if(item.getTitle().equals("로그인")){
                        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                    else if(item.getTitle().equals("프로필")){
                        Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
                        startActivity(intent);
                    }

                } else if (id == R.id.action_logup) {
                    if(item.getTitle().equals("회원가입")){
                        Intent intent = new Intent(getBaseContext(), LogupActivity.class);
                        startActivity(intent);
                    }
                    else if(item.getTitle().equals("로그아웃")){
                        firebaseAuth.signOut();
                        Toast.makeText(getApplicationContext(), "로그아웃", Toast.LENGTH_SHORT).show();
                    }

                }
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });

        //맵 프래그먼트 화면에 출력
        map = new MapFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainerView, map).commit();


    }


}