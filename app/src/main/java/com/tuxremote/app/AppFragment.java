package com.tuxremote.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A placeholder fragment containing a simple view.
 */
public class AppFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_APP_NAME = "app_name";
    private static final String ARG_HEXAID = "hexaId";
    private static final String ARG_TITLE = "window_title";
    private static final String ARG_PID = "app_pid";
    private AppAdapter adapter;
    private ListView listView;
    private ArrayList<Command> cmds;
    private String appName;
    private String appHexaId;
    private String appTitle;
    private String appPid;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AppFragment newInstance(int sectionNumber, App app) {
        AppFragment fragment = new AppFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString(ARG_APP_NAME, app.getName());
        args.putString(ARG_HEXAID, app.getHexaId());
        args.putString(ARG_TITLE, app.getTitle());
        args.putString(ARG_PID, app.getPid());
        fragment.setArguments(args);
        return fragment;
    }

    public AppFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        appName = getArguments().getString(ARG_APP_NAME);
        appHexaId = getArguments().getString(ARG_HEXAID);
        appTitle = getArguments().getString(ARG_TITLE);
        appPid = getArguments().getString(ARG_PID);
        cmds.clear();
        cmds.addAll(loadFromConfigFile(appName, appHexaId));
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        if(rootView != null){
            listView = (ListView) rootView.findViewById(R.id.list);
            adapter = new AppAdapter(getActivity().getApplicationContext(), cmds);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    // connect at this server <position>
                }
            });
        }
        return rootView;
    }

    private static ArrayList<Command> loadFromConfigFile(String appName, String appHexaId) {
        ArrayList<Command> list = new ArrayList<Command>();
        //retrieve commands list for appName into config file
        //use static methode Command.createCmdsList(cmd_string_arrayList)
        //or use static methode Command.newCommand(cmd_string_line_from_config_file)
        //use static methode Command.cmdClose(appHexaId) to add close command at the end of list
        return list;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    private static class AppAdapter extends BaseAdapter {

        private final Context context;
        private final ArrayList<Command> list;

        AppAdapter(Context c, ArrayList<Command> cmds){
            context = c;
            list = cmds;
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
            Command cmd = (Command) this.getItem(position);
            ViewHolder holder;
            if(convertView == null)
            {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.command, viewGroup, false);
                holder.name = (TextView) convertView.findViewById(R.id.cmd_name);
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                convertView.setTag(holder);
            }
            else
                holder = (ViewHolder) convertView.getTag();

            holder.name.setText(cmd.getName());
            cmd.setIconToView(context, holder.icon);
            return convertView;
        }

        private class ViewHolder {
            public TextView name;
            public ImageView icon;
        }
    }
}
