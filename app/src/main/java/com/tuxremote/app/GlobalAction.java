package com.tuxremote.app;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by lheido on 26/05/14.
 */
public class GlobalAction extends ActionProvider {
    Context context;

    public GlobalAction(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View onCreateActionView() {
        // Inflate the action view to be shown on the action bar.
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View view = layoutInflater.inflate(R.layout.global_action_provider, null);
        return view;
    }
}
