package com.example.martha.gameguide.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.martha.gameguide.R;
import com.example.martha.gameguide.model.NavDrawerItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martha on 5/9/2016.
 */
public class NavigationDrawerAdapter extends BaseAdapter{
    private Context context;
    private List<NavDrawerItemModel> drawerList;

    public NavigationDrawerAdapter(Context context) {
        this.context = context;
        this.drawerList = new ArrayList<>();
    }

    public void setDrawerItems(ArrayList<NavDrawerItemModel> list){
        drawerList.clear();
        drawerList.addAll(list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.nav_drawer_item, null);
        }
        ImageView drawerItemIcon = (ImageView) convertView.findViewById(R.id.nav_item_icon);
        TextView drawerItemTitle = (TextView) convertView.findViewById(R.id.nav_item_title);

        drawerItemIcon.setImageResource(drawerList.get(position).getIcon());
        drawerItemTitle.setText(drawerList.get(position).getTitle());
        return convertView;
    }

    @Override
    public int getCount() {
        return drawerList.size();
    }

    @Override
    public Object getItem(int position) {
        return drawerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
