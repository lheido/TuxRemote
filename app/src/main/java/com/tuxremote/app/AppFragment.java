package com.tuxremote.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

//import com.manuelpeinado.fadingactionbar.FadingActionBarHelperBase;
//import com.manuelpeinado.fadingactionbar.extras.actionbarcompat.FadingActionBarHelper;
//import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AppFragment extends Fragment {
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
    private Context context;
//    private FadingActionBarHelperBase mFadingHelper;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AppFragment newInstance(App app) {
        AppFragment fragment = new AppFragment();
        Bundle args = new Bundle();
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cmds = new ArrayList<Command>();
        cmds.addAll(loadFromConfigFile(appName, appHexaId));
//        View view = mFadingHelper.createView(inflater);
//        ImageView img = (ImageView) view.findViewById(R.id.image_header);
//        Picasso.with(getActivity().getApplicationContext()).load(TuxRemoteUtils.DEFAULT_ICON_APP).fit().centerInside().into(img);
        View view = inflater.inflate(R.layout.app_fragment, container, false);
        if(view != null) {
            listView = (ListView) view.findViewById(R.id.cmd_list);
            adapter = new AppAdapter(getActivity().getApplicationContext(), cmds);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    // connect at this server <position>
                }
            });
        }
        return view;
    }

    private static ArrayList<Command> loadFromConfigFile(String appName, String appHexaId) {
        ArrayList<Command> list = new ArrayList<Command>();
        //retrieve commands list for appName into config file
        //use static methode Command.createCmdsList(cmd_string_arrayList)
        //or use static methode Command.newCommand(cmd_string_line_from_config_file)
        //use static methode Command.cmdClose(appHexaId) to add close command at the end of list
        list.add(Command.cmdClose(appHexaId));
        return list;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        appName = getArguments().getString(ARG_APP_NAME);
        appHexaId = getArguments().getString(ARG_HEXAID);
        appTitle = getArguments().getString(ARG_TITLE);
        appPid = getArguments().getString(ARG_PID);
        ((MainActivity) activity).onSectionAttached(appName);
        context = ((MainActivity) activity).getApplicationContext();
//        mFadingHelper = new FadingActionBarHelper()
//                .actionBarBackground(R.drawable.ab_background)
//                .headerLayout(R.layout.app_header)
//                .contentLayout(R.layout.app_cmds)
//                .lightActionBar(false);
//        mFadingHelper.initActionBar(activity);
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
                convertView = LayoutInflater.from(context).inflate(R.layout.row_cmd, viewGroup, false);
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
