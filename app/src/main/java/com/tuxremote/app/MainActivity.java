package com.tuxremote.app;

import android.app.FragmentManager;
import android.content.Context;
import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tuxremote.app.TuxeRemoteSsh.BashReturn;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        SeekBar.OnSeekBarChangeListener, ConnectFragment.OnConnectCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    /** Volume control **/
    /** Used to control the volume for a given stream type */
    private SeekBar mVolumeControls;
    /** True is the volume controls are showing, false otherwise */
    private boolean mShowingControls;
    private int currentVolume = 0;

    protected App currentApp = null;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        context = this;
        Global.setContext(context);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        if(!Global.userIsConnected()) disconnectFragment();

        // Volume
        final View view = getLayoutInflater().inflate(R.layout.volume_action_view, null);
        if(view != null) {
            mVolumeControls = (SeekBar) view.findViewById(R.id.volume_control);
            if (mVolumeControls != null) {
                mVolumeControls.setMax(100);
                mVolumeControls.setOnSeekBarChangeListener(this);
            } else {
                Toast.makeText(this, "Error findViewById for volume action view", Toast.LENGTH_SHORT).show();
            }
            // Apply the custom View to the ActionBar
            getSupportActionBar().setCustomView(view, new ActionBar.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position, App app) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, AppFragment.newInstance(app))
                .commit();
        currentApp = app;
        setActionBarTitle(app.getName());
    }

    public void disconnectFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, ConnectFragment.newInstance())
                .commit();
    }

    @Override
    public void onConnect(Server server){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, EmptyFragment.newInstance())
                .commit();
        Global.setUserIsConnected(true);
    }

    public void onSectionAttached(String name) {
        setActionBarTitle(name);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            int layout;
            if(Global.userIsConnected())
                layout = R.menu.main;
            else
                layout = R.menu.disconnect;
            getMenuInflater().inflate(layout, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent;
            intent = new Intent(this, Preference.class);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
            return true;
        }
        else if(id == R.id.action_global_volume){
            // Toggle the custom View's visibility
            mShowingControls = !mShowingControls;
            getSupportActionBar().setDisplayShowCustomEnabled(mShowingControls);
            // Set the progress to the current volume level
            mVolumeControls.setProgress(currentVolume);
        }
//        else if(id == R.id.action_restart){
//            final MainActivity act = this;
//            TuxRemoteUtils.TuxRemoteDialog alert = new TuxRemoteUtils.TuxRemoteDialog(this, R.layout.alert_dialog, "Redémarrer"){
//                @Override
//                public void customInit() {
//                    TextView label = (TextView)findViewById(R.id.alert_label);
//                    label.setText("Voulez vous vraiment, vraiment, mais alors vraiment redémarrer le serveur?");
//                }
//
//                @Override
//                public void customCancel() {}
//
//                @Override
//                public void customOk() {
//                    Log.v("RESTART", "Test Command");
//                    SSHAsyncTask task = new SSHAsyncTask(act, new Command("restart", TuxRemoteUtils.CMD_RESTART, null));
//                    task.execute();
//                }
//            };
//            alert.show();
//            return true;
//        }
        else if(id == R.id.action_shutdown){
            final MainActivity act = this;
            TuxRemoteUtils.TuxRemoteDialog alert = new TuxRemoteUtils.TuxRemoteDialog(this, R.layout.alert_dialog, "Eteindre"){
                @Override
                public void customInit() {
                    TextView label = (TextView)findViewById(R.id.alert_label);
                    label.setText("Voulez vous vraiment, vraiment, mais alors vraiment éteindre le serveur? après faut le rallumer à la main t'es conscient de ça au moins??");
                }

                @Override
                public void customCancel() {}

                @Override
                public void customOk() {
                    SSHAsyncTask task = new SSHAsyncTask(act, new Command("shutDown", TuxRemoteUtils.CMD_SHUTDOWN, null));
                    task.execute();
                }
            };
            alert.show();
            return true;
        }
        else if(id == R.id.action_deconnexion){
            Global.session.disconnect();
            Global.setUserIsConnected(false);
            disconnectFragment();
            restoreActionBar();
            mShowingControls = false;
            getSupportActionBar().setDisplayShowCustomEnabled(false);
            Toast.makeText(this, "Déconnexion", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(id == R.id.action_remove_all_server){
            FragmentManager fragmentManager = getFragmentManager();
            Fragment frag = fragmentManager.findFragmentById(R.id.container);
            if(frag != null && frag.getClass() == ConnectFragment.class) {
                ((ConnectFragment)frag).removeAllServers();
            }
            return true;
        }
        else if(id == R.id.action_add_server){
            TuxRemoteUtils.TuxRemoteDialog newServerDialog = new TuxRemoteUtils.TuxRemoteDialog(
                    context, R.layout.new_server, "Nouveau serveur") {
                @Override
                public void customInit() {}

                @Override
                public void customCancel() {}

                @Override
                public void customOk() {
                    EditText entryName = (EditText)this.findViewById(R.id.entry_name);
                    EditText entryIp = (EditText)this.findViewById(R.id.entry_ip);
                    EditText entryUserId = (EditText)this.findViewById(R.id.entry_user_id);
                    EditText entryPassword = (EditText)this.findViewById(R.id.entry_password);

                    Server server = new Server(entryName.getText().toString(),
                            entryIp.getText().toString(),
                            entryUserId.getText().toString(),
                            entryPassword.getText().toString().isEmpty()?null:entryPassword.getText().toString());

                    FragmentManager fragmentManager = getFragmentManager();
                    Fragment frag = fragmentManager.findFragmentById(R.id.container);
                    if(frag != null && frag.getClass() == ConnectFragment.class) {
                        ((ConnectFragment)frag).save_server(server);
                        ((ConnectFragment)frag).add(server);
                        ((ConnectFragment)frag).prefUpdateServersList();
                    }
                }
            };
            newServerDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        currentVolume = i;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
//        Toast.makeText(this, "volume = "+currentVolume, Toast.LENGTH_SHORT).show();
        String cmd = TuxRemoteUtils.CMD_VOLUME;
        if(currentVolume == 0) cmd += "mute 0%";
        else cmd += "unmute "+currentVolume+"%";
        SSHAsyncTask task = new SSHAsyncTask(this, new Command("volume", cmd, null));
        task.execTask();
        // Remove the SeekBar from the ActionBar
        //mShowingControls = false;
        //getSupportActionBar().setDisplayShowCustomEnabled(false);
    }

    public void setActionBarTitle(String actionBarTitle) {
        mTitle = actionBarTitle;
        restoreActionBar();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(Global.userIsConnected()) {
            Global.session.disconnect();
            Global.setUserIsConnected(false);
            Toast.makeText(this, "Déconnexion", Toast.LENGTH_SHORT).show();
        }
    }
}
