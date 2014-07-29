package com.tuxremote.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CmdListViewAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Command> items;

    static class ViewHolder {
        public TextView text;
        public ImageView image;
    }

    public CmdListViewAdapter(Context context, ArrayList<Command> items){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.row_cmd, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.cmd_name);
            holder.image = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        // fill data
        Command cmd = this.items.get(position);
        holder.text.setText(cmd.getName());
        cmd.setIconToView(context, holder.image);

        return rowView;
    }
}