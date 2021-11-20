package com.example.ats_app;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SearchAdapter extends BaseAdapter {

    private List<Store> names;
    private Context context;

    public SearchAdapter(Context context, List<Store> names){
        this.context = context;
        this.names = names;
    }

    public void setList(List<String> naems){
        this.names = names;

        if(names.size() == 0){
            //names.add("검색어를 입력하세요");
        }
    }
    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int i) {
        return names.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.listview, null);
        TextView tv = v.findViewById(R.id.label);
        ImageView img = v.findViewById(R.id.img);

        switch (names.get(i).getType()){
            case "cafe":
                img.setImageResource(R.drawable.cafe);
                break;
            case "restaurant":
                img.setImageResource(R.drawable.restaurant);
                break;
            case "bar":
                img.setImageResource(R.drawable.soool);
                break;
            case "etc":
                img.setImageResource(R.drawable.etc);
                break;
        }

        tv.setText(names.get(i).getStoreName());
        return v;
    }
}
