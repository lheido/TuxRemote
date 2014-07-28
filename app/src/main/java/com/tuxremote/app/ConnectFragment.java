package com.tuxremote.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tuxremote.app.TuxeRemoteSsh.SshSession;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ConnectFragment extends Fragment {
    private ArrayList<Server> servers;
    private ListView listView;
    private ConnectAdapter adapter;
    private MainActivity act;

    public static ConnectFragment newInstance() {
        return new ConnectFragment();
    }

    public ConnectFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.connect_fragment, container, false);
        if(rootView != null) {
            listView = (ListView) rootView.findViewById(R.id.list);
//            servers = new ArrayList<Server>();
            servers = retrieveServersList();
            adapter = new ConnectAdapter(getActivity().getApplicationContext(), servers);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    // connect at this server <position>
                    Server s = servers.get(position);
                    TestConnexion test = new TestConnexion(act,s);
                    test.execute();
                }
            });
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                    // manage server info at <position>
                    Server s = servers.get(position);
                    manageServer(s);
                    return false;
                }
            });
        }
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        act = (MainActivity) activity;
    }

    public void save_server(Server server){
        SharedPreferences pref = TuxRemoteUtils.getPref(getActivity().getApplicationContext());
        String str = server.getIp()+
                TuxRemoteUtils.PREF_SPLIT+
                server.getUserId()+
                TuxRemoteUtils.PREF_SPLIT;
        if(server.getPassword() != null && pref.getBoolean("save_password", true))
            str += server.getPassword();
        pref.edit().putString(server.getName(), str).commit();
    }

    private ArrayList<Server> retrieveServersList(){
        ArrayList<Server> serversList = new ArrayList<Server>();
        SharedPreferences pref = TuxRemoteUtils.getPref(getActivity().getApplicationContext());
        Set<String> list = pref.getStringSet(TuxRemoteUtils.SERVERS_LIST, new HashSet<String>());
        for(String name : list){
            String[] data = pref.getString(name, "").split(TuxRemoteUtils.PREF_SPLIT);
            Server s = new Server(name, data[0], data[1], (data.length == 3) ? data[2]: null);
            serversList.add(s);
        }
        return serversList;
    }

    public void prefRemoveServer(String name){
        SharedPreferences pref = TuxRemoteUtils.getPref(getActivity().getApplicationContext());
        pref.edit().putStringSet(name, null).commit();
    }

    public void prefUpdateServersList(){
        SharedPreferences pref = TuxRemoteUtils.getPref(getActivity().getApplicationContext());
        Set<String> set = new HashSet<String>();
        for (Server server : servers){
            set.add(server.getName());
        }
        pref.edit().putStringSet(TuxRemoteUtils.SERVERS_LIST, set).commit();
    }

    public void removeAllServers(){
        SharedPreferences pref = TuxRemoteUtils.getPref(getActivity().getApplicationContext());
        SharedPreferences.Editor editor = pref.edit();
        for (Server server : servers){
            editor.putStringSet(server.getName(), null);
        }
        editor.putStringSet(TuxRemoteUtils.SERVERS_LIST, null);
        editor.commit();
        servers.clear();
        adapter.notifyDataSetChanged();
    }

    private void manageServer(final Server server){
        final String name = server.getName();
        TuxRemoteUtils.TuxRemoteDialog dialog = new TuxRemoteUtils.TuxRemoteDialog(
                Global.getStaticContext(), R.layout.manage_server,
                "Server : "+server.getName()) {
            @Override
            public void customInit(){
                EditText entryName = (EditText)this.findViewById(R.id.entry_name);
                entryName.setText(name);
                EditText entryIp = (EditText)this.findViewById(R.id.entry_ip);
                entryIp.setText(server.getIp());
                EditText entryUserId = (EditText)this.findViewById(R.id.entry_user_id);
                entryUserId.setText(server.getUserId());
                EditText entryPassword = (EditText)this.findViewById(R.id.entry_password);
                if(server.getPassword() != null)
                    entryPassword.setText(server.getPassword());
            }

            @Override
            public void customCancel() {}

            @Override
            public void customOk() {
                EditText entryName = (EditText)this.findViewById(R.id.entry_name);
                EditText entryIp = (EditText)this.findViewById(R.id.entry_ip);
                EditText entryUserId = (EditText)this.findViewById(R.id.entry_user_id);
                EditText entryPassword = (EditText)this.findViewById(R.id.entry_password);
                server.setName(entryName.getText().toString());
                server.setIp(entryIp.getText().toString());
                server.setUserId(entryUserId.getText().toString());
                server.setPassword(entryPassword.getText().toString());
                save_server(server);
                if(server.getName().equals(name))
                    prefRemoveServer(name);
                adapter.notifyDataSetChanged();
                prefUpdateServersList();
            }
        };
        dialog.show();
    }

    public void add(Server server) {
        servers.add(server);
        adapter.notifyDataSetChanged();
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

    public static class TestConnexion extends AsyncTask<Void, Void, Boolean> {

        private final Server server;
        private WeakReference<MainActivity> act;

        TestConnexion(MainActivity activity, Server s){
            server = s;
            link(activity);
        }

        private void link(MainActivity activity) {
            act = new WeakReference<MainActivity>(activity);
        }

        @Override
        protected void onPreExecute(){
            if(act.get() != null) {
                act.get().setProgressBarIndeterminateVisibility(true);
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Global.session = new SshSession(server.getUserId(), server.getIp(), null);
            if(server.getPassword() != null)
                Global.session.setPassword(server.getPassword());
            return Global.session.connect();
        }

        @Override
        protected void onPostExecute (Boolean result) {
            String message = null;
            if(result){
                 message = "Connexion réussie";
            }else{
                message = "Echec connexion";
            }
            Toast.makeText(Global.getStaticContext(), message, Toast.LENGTH_SHORT).show();
            if(act.get() != null) {
                act.get().setProgressBarIndeterminateVisibility(false);
            }
        }
    }
}
