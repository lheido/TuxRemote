package com.tuxremote.app;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

//Jsch for SSH2
//import com.jcraft.jsch.Channel;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, SeekBar.OnSeekBarChangeListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // Volume
        final View view = getLayoutInflater().inflate(R.layout.volume_action_view, null);
        if(view != null) {
            mVolumeControls = (SeekBar) view.findViewById(R.id.volume_control);
            if (mVolumeControls != null) {
                // Set the max range of the SeekBar to the max volume stream type
                mVolumeControls.setMax(100);
                // Bind the OnSeekBarChangeListener
                mVolumeControls.setOnSeekBarChangeListener(this);
            } else {
                Toast.makeText(this, "Error findViewById for volume action view", Toast.LENGTH_SHORT).show();
            }
            // Apply the custom View to the ActionBar
            getSupportActionBar().setCustomView(view, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, AppFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
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
            getMenuInflater().inflate(R.menu.main, menu);
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
            return true;
        }
        if(id == R.id.action_global_volume){
            // Toggle the custom View's visibility
            mShowingControls = !mShowingControls;
            getSupportActionBar().setDisplayShowCustomEnabled(mShowingControls);
            // Set the progress to the current volume level of the stream
            mVolumeControls.setProgress(currentVolume);
        }
        if(id == R.id.action_restart){
//            TuxRemoteUtils.CMD_RESTART
            return true;
        }
        if(id == R.id.action_shutdown){
//            TuxRemoteUtils.CMD_SHUTDOWN
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        currentVolume = i;
//        Log.v("onProgressChanged",""+i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Toast.makeText(this, "volume = "+currentVolume, Toast.LENGTH_SHORT).show();
        // Remove the SeekBar from the ActionBar
        //mShowingControls = false;
        //getSupportActionBar().setDisplayShowCustomEnabled(false);
    }
}
