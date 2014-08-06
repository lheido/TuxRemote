package com.tuxremote.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AppListViewAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<App> items;

    static class ViewHolder {
        public ImageView image;
        public TextView app_name;
        public TextView window_title;
    }

    public AppListViewAdapter(Context context, ArrayList<App> items){
        this.context = context;
        this.items = items;
    }

    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int position){
        return this.items.get(position);
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // reuse views
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.row_app, parent, false);
            holder.app_name = (TextView) convertView.findViewById(R.id.app_name);
            holder.window_title = (TextView) convertView.findViewById(R.id.window_title);
            holder.image = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        // fill data
        App app = this.items.get(position);
        holder.app_name.setText(app.getName());
        holder.window_title.setText(app.getTitle());
//        app.setIconToView(context, holder.image);

        return convertView;
    }
}
