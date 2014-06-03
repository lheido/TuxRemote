package com.tuxremote.app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lheido on 04/06/14.
 */
public class CmdListViewAdapter extends BaseAdapter {
    private final Activity context;
    private final ArrayList<Command> items;

    static class ViewHolder {
        public TextView text;
        public ImageView image;
    }

    public CmdListViewAdapter(Activity context, ArrayList<Command> items){
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
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.rowlayout, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) rowView.findViewById(R.id.label);
            viewHolder.image = (ImageView) rowView.findViewById(R.id.icon);
            rowView.setTag(viewHolder);
        }

        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();
        Command cmd = this.items.get(position);
        holder.text.setText(cmd.getName());
        cmd.setIconToView(context, holder.image);

        return rowView;
    }
}