package com.tuxremote.app;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
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
    public static final String TAG="ConnectFragment";
    private ArrayList<Server> servers;
    private ListView listView;
    private ConnectAdapter adapter;
    private MainActivity act;
    private OnConnectCallbacks mCallbacks;

    public static ConnectFragment newInstance() {
        return new ConnectFragment();
    }

    public ConnectFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.connect_fragment, container, false);
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            NavigationDrawerFragment frag = (NavigationDrawerFragment) fragmentManager.findFragmentById(R.id.navigation_drawer);
            if (frag != null && frag.isDrawerOpen()) {
                frag.closeDrawer();
            }
        }
        if(rootView != null) {
            listView = (ListView) rootView.findViewById(R.id.list);
            servers = new ArrayList<Server>();
            adapter = new ConnectAdapter(Global.getStaticContext(), servers);
            listView.setAdapter(adapter);
            retrieveServersList();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    // connect at this server <position>
                    Server s = servers.get(position);
                    connexionTask test = new connexionTask(act,s);
                    test.execTask();
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
        try {
            mCallbacks = (OnConnectCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OnConnectCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
        act = null;
    }

    public void save_server(Server server){
        SharedPreferences pref = TuxRemoteUtils.getPref(Global.getStaticContext());
        String str = server.getIp()+
                TuxRemoteUtils.PREF_SPLIT+
                server.getUserId()+
                TuxRemoteUtils.PREF_SPLIT;
        if(server.getPassword() != null && pref.getBoolean("save_password", true))
            str += server.getPassword();
        pref.edit().putString(server.getName(), str).commit();
    }

    private void retrieveServersList(){
        SharedPreferences pref = TuxRemoteUtils.getPref(Global.getStaticContext());
        Set<String> list = pref.getStringSet(TuxRemoteUtils.SERVERS_LIST, new HashSet<String>());
        for(String name : list){
            String[] data = pref.getString(name, "").split(TuxRemoteUtils.PREF_SPLIT);
            try {
                if(data.length > 1) {
                    Server s = new Server(name, data[0], data[1], (data.length == 3) ? data[2] : null);
                    add(s);
                }else{
                    //serveur pas bon
                    Toast.makeText(Global.getStaticContext(), "serveur pas bon", Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                Toast.makeText(Global.getStaticContext(), "retrieveServerList\n\n"+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void prefRemoveServer(String name){
        SharedPreferences pref = TuxRemoteUtils.getPref(Global.getStaticContext());
        pref.edit().putStringSet(name, null).commit();
    }

    public void prefUpdateServersList(){
        SharedPreferences pref = TuxRemoteUtils.getPref(Global.getStaticContext());
        Set<String> set = new HashSet<String>();
        for (Server server : servers){
            set.add(server.getName());
        }
        pref.edit().putStringSet(TuxRemoteUtils.SERVERS_LIST, set).commit();
    }

    public void removeAllServers(){
        SharedPreferences pref = TuxRemoteUtils.getPref(Global.getStaticContext());
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
                server.setAvailable(false);
                adapter.notifyDataSetChanged();
                save_server(server);
                if(!server.getName().equals(name))
                    prefRemoveServer(name);
                adapter.notifyDataSetChanged();
                prefUpdateServersList();
                TestConnexionTask task = new TestConnexionTask((MainActivity)getActivity(), server) {
                    @Override
                    protected void onPostExecute(Boolean result) {
                        server.setAvailable(result);
                        try {
                            session.disconnect();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                };
                task.execTask();
            }
        };
        dialog.show();
    }

    public void add(Server server) {
        servers.add(server);
        TestConnexionTask task = new TestConnexionTask((MainActivity)getActivity(), server) {
            @Override
            protected void onPostExecute(Boolean result) {
                server.setAvailable(result);
                try {
                    session.disconnect();
                }catch (Exception e){
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        };
        task.execTask();
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
                holder.available = (View) convertView.findViewById(R.id.available);
                convertView.setTag(holder);
            }
            else
                holder = (ViewHolder) convertView.getTag();

            holder.name.setText(server.getName());
            holder.ip.setText(server.getIp());
            holder.available.setBackgroundColor(context.getResources().getColor(server.isAvailable() ? R.color.green : R.color.black));
            return convertView;
        }

        private class ViewHolder {
            public TextView name;
            public TextView ip;
            public View available;
        }
    }

    private class connexionTask extends AsyncTask<Void, Void, Boolean> {

        private final Server server;
        private WeakReference<MainActivity> act;

        connexionTask(MainActivity activity, Server s){
            server = s;
            link(activity);
        }

        private void link(MainActivity activity) {
            act = new WeakReference<MainActivity>(activity);
        }

        @Override
        protected void onPreExecute(){
            if(act.get() != null) {
                try {
                    act.get().setProgressBarIndeterminateVisibility(true);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Global.session = new SshSession(server.getUserId(), server.getIp(), null, 22);
            if(server.getPassword() != null)
                Global.session.setPassword(server.getPassword());
            return Global.session.connect();
        }

        @Override
        protected void onPostExecute (Boolean result) {
            String message = null;
            if(result){
                message = "Connexion réussie";
                if (mCallbacks != null) {
                    mCallbacks.onConnect(server);
                }
            }else{
                message = "Echec connexion";
            }
            Toast.makeText(Global.getStaticContext(), message, Toast.LENGTH_SHORT).show();
            if(act.get() != null) {
                try {
                    act.get().setProgressBarIndeterminateVisibility(false);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        public void execTask() {
            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
                executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                execute();
            }
        }
    }

    /**
     * Interface onConnect implemented by MainActivity
     */
    public static interface OnConnectCallbacks{
        /**
         * Called when server item is selected and session.connect() return true.
         */
        void onConnect(Server server);
    }

    private abstract class TestConnexionTask extends AsyncTask<Void, Void, Boolean>{
        protected Server server;
        protected WeakReference<MainActivity> actRef;
        protected SshSession session;

        public TestConnexionTask(MainActivity activity, Server s) {
            server = s;
            link(activity);
        }

        private void link(MainActivity activity) {
            actRef = new WeakReference<MainActivity>(activity);
        }

        @Override
        protected void onPreExecute(){

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            session = new SshSession(server.getUserId(), server.getIp(), null, 22);
            if(server.getPassword() != null)
                session.setPassword(server.getPassword());
            return session.connect();
        }

        @Override
        abstract protected void onPostExecute (Boolean result);

        public void execTask() {
            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
                executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                execute();
            }
        }
    }
}
