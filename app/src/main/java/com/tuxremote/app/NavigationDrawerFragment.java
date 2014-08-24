package com.tuxremote.app;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.app.Fragment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tuxremote.app.TuxeRemoteSsh.BashReturn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.timroes.android.listview.EnhancedListView;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private static final long FIRST_DELAY = 0;
    private static final long PERIOD = 10;
    private static final TimeUnit UNIT = TimeUnit.SECONDS;

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private EnhancedListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;

    private ArrayList<App> listApp = null;
    private AppListViewAdapter adapter;
    private Context context;
    private ScheduledExecutorService scheduler = null;
    protected static Handler notify;
    private ProgressBar progress;
    private Handler notifyProgress;
    private boolean isProgress = false;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
        }
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        if(view != null) {
            mDrawerListView = (EnhancedListView) view.findViewById(R.id.app_list);
            progress = (ProgressBar) view.findViewById(R.id.progress);
            progress.setIndeterminate(true);
            progress.setVisibility(View.GONE);
//            mDrawerListView = (EnhancedListView) inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
            mDrawerListView.setDismissCallback(new EnhancedListView.OnDismissCallback() {
                @Override
                public EnhancedListView.Undoable onDismiss(EnhancedListView enhancedListView, final int position) {
                    final App current = listApp.get(position);
                    if (current.isStaticApp())
                        return null;
                    final App item = listApp.remove(position);
                    adapter.notifyDataSetChanged();
                    return new EnhancedListView.Undoable() {
                        @Override
                        public void undo() {
                            listApp.add(position, item);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void discard() {
                            Command cmd = Command.cmdClose(item.getHexaId());
                            SSHAsyncTask task = new SSHAsyncTask(cmd);
                            task.execTask();
                        }
                    };
                }
            });
            mDrawerListView.enableSwipeToDismiss();
            mDrawerListView.setSwipeDirection(EnhancedListView.SwipeDirection.END);
            mDrawerListView.setUndoHideDelay(3000);
            mDrawerListView.setRequireTouchBeforeDismiss(false);
            mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectItem(position);
                }
            });
            mDrawerListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(context, "long press", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            listApp = new ArrayList<App>();
            adapter = new AppListViewAdapter(getActivity().getApplicationContext(), listApp);
            mDrawerListView.setAdapter(adapter);

            notify = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    adapter.notifyDataSetChanged();
                }
            };
            notifyProgress = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    int visibility;
                    if(isProgress) visibility = View.GONE;
                    else visibility = View.VISIBLE;
                    isProgress = ! isProgress;
                    progress.setVisibility(visibility);
                }
            };
        }
        return view;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }
                getActivity().invalidateOptionsMenu();
//                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }
                getActivity().invalidateOptionsMenu();
//                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public void onResume(){
        super.onResume();
        try {
            if (Global.userIsConnected())
                updateAppList();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if(scheduler != null)
            scheduler.shutdown();
        mDrawerListView.discardUndo();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(scheduler != null && !scheduler.isShutdown())
            scheduler.shutdown();
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
//        if (mDrawerListView != null) {
//            mDrawerListView.setItemChecked(position, true);
//        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position, listApp.get(position));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity.getApplicationContext();
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            int layout;
            if(Global.userIsConnected())
                layout = R.menu.global;
            else
                layout = R.menu.disconnect;
            inflater.inflate(layout, menu); // or R.menu.global
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.action_kill_all) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(mFragmentContainerView);
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(mFragmentContainerView);
    }

    public void dowloadConfigFile(){
        try {
            SSHAsyncTask task = new SSHAsyncTask(new Command("downloadFile", "cat ~/.config/TuxRemote/config.xml", null)) {
                @Override
                protected void onPreExecute(){
                    try{
                        getActivity().setProgressBarIndeterminateVisibility(true);
                    }catch (Exception e){}
                }
                @Override
                protected void onProgressUpdate(BashReturn... prog) {
                    ArrayList<String> retour = prog[0].getBashReturn();
                    String content = "";
                    for (String line : retour) {
                        content += line;
                    }
                    TuxRemoteUtils.saveFileToInternalStorage(Global.getStaticContext(), "config.xml", content);
                    mCallbacks.volumeCmdsFromConfigFile(new ConfigXML(context).getStaticCommand("Volume"));
                    mCallbacks.shutdownCmdFromConfigFile(new ConfigXML(context).getStaticCommand("Shutdown"));
//                    savePNG();
                    updateAppList();
                }
                @Override
                protected void onPostExecute(Boolean result){
                    try{
                        getActivity().setProgressBarIndeterminateVisibility(false);
                    }catch (Exception e){}
                }
            };
            task.execTask();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void savePNG() {
        try {
            ArrayList<App> configList = new ConfigXML(context).getAppList();
            for (final App app : configList) {
                if(app.getIcon() != null) {
                    TuxRemoteUtils.scpTask task = new TuxRemoteUtils.scpTask(app.getIcon());
                    task.execTask();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateAppList(){
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(updateThread(), FIRST_DELAY, PERIOD, UNIT);
    }

    public Runnable updateThread(){
        return new Runnable() {
            public void run() {
                try {
                    listApp.clear();
                    notify.sendEmptyMessage(0);
                    notifyProgress.sendEmptyMessage(0);
                    ArrayList<App> configList = new ConfigXML(context).getAppList();
                    BashReturn retour = Global.session.SetCommand(TuxRemoteUtils.CMD_WMCTRL);
                    ArrayList<App> wmctrlList = TuxRemoteUtils.getAppListFromWmctrl(retour.getBashReturn());
                    for (App a : wmctrlList) {
                        if(isAvailableApp(a, configList)) {
                            listApp.add(a);
                        }
                    }
                    ArrayList<App> staticAppList = new ConfigXML(context).getStaticAppList();
                    for (App app : staticAppList){
                        listApp.add(app);
                    }
                    notify.sendEmptyMessage(0);
                    notifyProgress.sendEmptyMessage(0);
                    if(mCallbacks != null)
                        mCallbacks.testCurrentApp(listApp);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
    }

    public static boolean isAvailableApp(App a, ArrayList<App> configList){
        for (App appAvailable : configList){
            if(appAvailable.getWmctrlName().equals(a.getWmctrlName())) {
                a.merge(appAvailable);
                return true;
            }
        }
        return false;
    }

    public void clearScheduler() {
        if(scheduler != null && !scheduler.isShutdown()){
            scheduler.shutdown();
        }
    }

    public void clearAppList(){
        if(listApp != null){
            listApp.clear();
            adapter.notifyDataSetChanged();
        }
    }

    public void removeAt(int position) {
        listApp.remove(position);
        adapter.notifyDataSetChanged();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position, App app);
        void testCurrentApp(ArrayList<App> appList);

        void volumeCmdsFromConfigFile(HashMap<String, String> volume);

        void shutdownCmdFromConfigFile(HashMap<String, String> shutdown);
    }
}
