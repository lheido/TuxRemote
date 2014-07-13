package com.tuxremote.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lheido on 25/05/14.
 */
public class AppListViewAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<App> items;

    static class ViewHolder {
        public TextView text;
        public ImageView image;
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
        View rowView = convertView;
        // reuse views
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.rowlayout, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.label);
            holder.image = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        // fill data
        App app = this.items.get(position);
        holder.text.setText(app.getTitle());
        app.setIconToView(context, holder.image);

        return rowView;
    }
}
