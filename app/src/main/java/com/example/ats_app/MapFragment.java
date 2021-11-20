package com.example.ats_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.ats_app.geocoding.GeoPointer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {


    private MapView mapView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private ArrayList<Store> list;
    private NaverMap naverMap;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public MapFragment() {
    }

    public static MapFragment newInstance()
    {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        NaverMapSdk.getInstance(getContext()).setClient(
                new NaverMapSdk.NaverCloudPlatformClient("e8z6cu0818"));

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE); //현재위치 반환 구현체
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,  @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_map,
                container, false);

        mapView = (MapView) rootView.findViewById(R.id.navermap);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap)
    {
        this.naverMap = naverMap;
        UiSettings settings = naverMap.getUiSettings();

        //현재위치 설정
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        settings.setLocationButtonEnabled(true);
        //배경 지도 선택
        naverMap.setMapType(NaverMap.MapType.Basic);

        //건물 표시
        naverMap.setLayerGroupEnabled(naverMap.LAYER_GROUP_BUILDING, true);

        //db의 상점들 출력 마커
        list = new ArrayList<>();
        class BackgroundTask extends AsyncTask<ArrayList<Store> , Store, ArrayList<Store>> {

            private int cnt = 0;

            public BackgroundTask() {
                super();
            }

            @Override
            protected void onCancelled(ArrayList<Store> stores) {
                super.onCancelled(stores);
            }

            @Override
            protected ArrayList<Store> doInBackground(ArrayList<Store>... vectors) {

                db.collection("store")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        vectors[0].add(document.toObject(Store.class));
                                        publishProgress(vectors[0].get(vectors[0].size()-1));
                                    }
                                } else {

                                }
                            }
                        });

                return vectors[0];
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onProgressUpdate(Store ... values) {
                cnt++;
                Store store = new Store();
                store = values[0];
                list.add(store);
                String address = store.getAddress().toString();

                Store finalStore = store;
                GeoPointer.OnGeoPointListener listener = new GeoPointer.OnGeoPointListener() {
                    @Override
                    public void onPoint(GeoPointer.Point[] p) {
                        int sCnt = 0, fCnt = 0;
                        for (GeoPointer.Point point : p) {
                            if (point.havePoint) sCnt++;
                            else fCnt++;
                            finalStore.setP(new LatLng(point.getY(),point.getX()));
                            createMarker(new LatLng(point.getY(),point.getX()), finalStore);
                        }
                        Log.d("TEST_CODE", String.format("성공 : %s, 실패 : %s", sCnt, fCnt));
                    }

                    @Override
                    public void onProgress(int progress, int max) {
                        Log.d("TEST_CODE", String.format("좌표를 얻어오는중 %s / %s", progress, max));
                    }
                };

                GeoPointer geoPointer = new GeoPointer(getContext(), listener);
                geoPointer.execute(address);
            }

            @Override
            protected void onPostExecute(ArrayList<Store> stores) {

                Log.v("finish", String.valueOf(stores.size()));
                for(int i = 0; i < stores.size(); i++){
                    Log.v("finish", stores.get(i).getAddress().toString());
                }
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }
        }


        BackgroundTask bt = new BackgroundTask();
        bt.execute(list);

    }

    @Override
    public void onStart()
    {
        super.onStart();
        mapView.onStart();

    }

    @Override
    public void onResume()
    {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public void createMarker(LatLng loc, Store store){
        Marker marker = new Marker();
        marker.setOnClickListener(new Overlay.OnClickListener() {


            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                if(auth.getCurrentUser() != null) {
                    Intent intent = new Intent(getContext(), BoardActivity.class);
                    Log.v("marker click", store.getAddress());
                    Log.v("marker click", String.valueOf(store.getPositionIndex().length));

                    intent.putExtra("address", store.getAddress());
                    intent.putExtra("buisnessName", store.getBusinessName());
                    intent.putExtra("detailAddress", store.getDetailAddress());
                    intent.putExtra("id", store.getId());
                    intent.putExtra("introduce", store.getIntroduce());
                    intent.putExtra("phone", store.getPhone());
                    intent.putExtra("positionIndex", store.getPositionIndex().toString());
                    intent.putExtra("storeName", store.getStoreName());
                    intent.putExtra("totalSeat", store.getTotalSeat().toString());
                    intent.putExtra("type", store.getType());

                    startActivity(intent);
                }
                else{
                    Toast.makeText(getContext(), "로그인이 필요한 서비스입니다.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        marker.setPosition(loc);
        marker.setMap(this.naverMap);
        if(store.getType().equals("cafe")){        //카페
            marker.setIcon(OverlayImage.fromResource(R.drawable.cafe));
            marker.setIconTintColor(Color.BLUE);
        }
        else if(store.getType().equals("restaurant")){      //식당
            marker.setIcon(OverlayImage.fromResource(R.drawable.restaurant));
            marker.setIconTintColor(Color.RED);
        }
        else if(store.getType().equals("bar")){       //주점
            marker.setIcon(OverlayImage.fromResource(R.drawable.soool));
            marker.setIconTintColor(Color.GREEN);
        }
        else if(store.getType().equals("etc")){
            marker.setIcon(OverlayImage.fromResource(R.drawable.etc));
            marker.setIconTintColor(Color.YELLOW);
        }

        marker.setCaptionText(store.getStoreName() + "\n" + "남은 자리 : " + String.valueOf(store.getTotalSeat()));
        marker.setCaptionTextSize(15);

    }
}