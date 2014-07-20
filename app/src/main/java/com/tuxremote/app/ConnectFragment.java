package com.tuxremote.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ConnectFragment extends Fragment {
    private ArrayList<Server> servers;

    public static ConnectFragment newInstance() {
        return new ConnectFragment();
    }

    public ConnectFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.connect_fragment, container, false);
        StickyListHeadersListView stickyList = (StickyListHeadersListView) rootView.findViewById(R.id.list);
        Adapter adapter = new Adapter(getActivity().getApplicationContext(), servers);
        stickyList.setAdapter(adapter);
        return rootView;
    }

    private static class Adapter extends BaseAdapter implements StickyListHeadersAdapter {

        private final Context context;
        private final ArrayList<Server> list;

        Adapter(Context c, ArrayList<Server> liste){
            context = c;
            list = liste;
        }

        @Override
        public View getHeaderView(int i, View view, ViewGroup viewGroup) {
            return null;
        }

        @Override
        public long getHeaderId(int i) {
            return 0;
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return null;
        }
    }
}
