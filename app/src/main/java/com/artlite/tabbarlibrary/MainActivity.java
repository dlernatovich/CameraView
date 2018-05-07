package com.artlite.tabbarlibrary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.artlite.bslibrary.annotations.FindViewBy;
import com.artlite.bslibrary.ui.activity.BSActivity;
import com.google.android.cameraview.CameraView;
import com.google.android.cameraview.GCameraView;

public class MainActivity extends BSActivity {

    /**
     * Instance of the {@link CameraView}
     */
    @FindViewBy(id = R.id.camera_view)
    GCameraView cameraView;

    /**
     * Method which provide the getting of the layout ID for the current Activity
     *
     * @return layout ID for the current Activity
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * Method which provide the action when Activity is created
     *
     * @param bundle
     */
    @Override
    protected void onCreateActivity(@Nullable Bundle bundle) {

    }

    /**
     * Method which provide the action when Activity is created (post creation)
     * Use it if you create any callback inside the activity like
     * <b>final |CallbackType| callback = new |CallbackType|</b>
     *
     * @param bundle
     */
    @Override
    protected void onActivityPostCreation(@Nullable Bundle bundle) {
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
    }

    /**
     * Overriden method for the OnClickListener
     *
     * @param v current view
     */
    @Override
    public void onClick(View v) {

    }

    /**
     * Method which provide the save image
     *
     * @param data array of the {@link Byte} data
     */
    protected void saveImage(byte[] data) {
    }
}
