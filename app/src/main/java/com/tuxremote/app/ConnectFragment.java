package com.tuxremote.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ConnectFragment extends Fragment {
    private ArrayList<Server> servers;
    private ListView listView;
    private ConnectAdapter adapter;

    public static ConnectFragment newInstance() {
        return new ConnectFragment();
    }

    public ConnectFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.connect_fragment, container, false);
        if(rootView != null) {
            listView = (ListView) rootView.findViewById(R.id.list);
            adapter = new ConnectAdapter(getActivity().getApplicationContext(), servers);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    // connect at this server <position>
                }
            });
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                    // manage server info at <position>
                    return true;
                }
            });
        }
        return rootView;
    }

    private static class ConnectAdapter extends BaseAdapter {

        private final Context context;
        private final ArrayList<Server> list;

        ConnectAdapter(Context c, ArrayList<Server> servers){
            context = c;
            list = servers;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            Server server = (Server) this.getItem(position);
            ViewHolder holder;
            if(convertView == null)
            {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.server, viewGroup, false);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.ip = (TextView) convertView.findViewById(R.id.ip_address);
                convertView.setTag(holder);
            }
            else
                holder = (ViewHolder) convertView.getTag();

            holder.name.setText(server.getName());
            holder.ip.setText(server.getIp());
            return convertView;
        }

        private class ViewHolder {
            public TextView name;
            public TextView ip;
        }
    }
}
