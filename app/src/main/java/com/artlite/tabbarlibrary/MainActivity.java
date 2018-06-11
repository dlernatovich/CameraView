package com.artlite.tabbarlibrary;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.artlite.bslibrary.annotations.FindViewBy;
import com.artlite.bslibrary.managers.BSTransferManager;
import com.artlite.bslibrary.ui.activity.BSActivity;
import com.google.android.cameraview.CameraView;
import com.google.android.cameraview.GCameraView;
import com.google.android.cameraview.GCameraViewV1;

public class MainActivity extends BSActivity implements GCameraViewV1.OnCameraCallback {

    /**
     * Instance of the {@link CameraView}
     */
    @FindViewBy(id = R.id.camera_view)
    GCameraViewV1 cameraView;

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
        this.cameraView.configure(this);
        this.cameraView.setFacing(GCameraViewV1.Facing.BACK);
        this.cameraView.setFlash(GCameraViewV1.Flash.AUTO);
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

    /**
     * Method which provide the action when the flash the changed
     *
     * @param cameraView instance of the {@link GCameraView}
     * @param flash      instance of the {@link GCameraView.Flash}
     */
    @Override
    public void cameraViewFlashChanged(@NonNull GCameraViewV1 cameraView,
                                       @NonNull GCameraViewV1.Flash flash) {

    }

    /**
     * Method which provide the action when the flash the changed
     *
     * @param cameraView instance of the {@link GCameraView}
     * @param facing     instance of the {@link GCameraView.Facing}
     */
    @Override
    public void cameraViewFacingChanged(@NonNull GCameraViewV1 cameraView,
                                        @NonNull GCameraViewV1.Facing facing) {

    }

    /**
     * Method which provide the action when the flash the changed
     *
     * @param cameraView instance of the {@link GCameraView}
     * @param uri        instance of the {@link Uri}
     */
    @Override
    public void cameraViewPictureTaken(@NonNull GCameraViewV1 cameraView,
                                       @NonNull Uri uri) {
        BSTransferManager.put(ImageActivity.class, uri);
        this.startActivity(ImageActivity.class);
    }

    /**
     * Method which provide the getting of the camera icon
     *
     * @return {@link Integer} value of the camera icon
     */
    @Nullable
    @Override
    public Integer cameraViewIconCamera() {
        return null;
    }

    /**
     * Method which provide the getting of the flash auto icon
     *
     * @return {@link Integer} value of the camera icon
     */
    @Nullable
    @Override
    public Integer cameraViewIconFlashAuto() {
        return null;
    }

    /**
     * Method which provide the getting of the flash on icon
     *
     * @return {@link Integer} value of the camera icon
     */
    @Nullable
    @Override
    public Integer cameraViewIconFlashOn() {
        return null;
    }

    /**
     * Method which provide the getting of the flash off icon
     *
     * @return {@link Integer} value of the camera icon
     */
    @Nullable
    @Override
    public Integer cameraViewIconFlashOff() {
        return null;
    }
}
