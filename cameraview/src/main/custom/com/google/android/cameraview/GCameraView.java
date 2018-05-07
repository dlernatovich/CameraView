package com.google.android.cameraview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.Date;

/**
 * Class which provide the create of the View for the camera
 */
public class GCameraView extends FrameLayout implements View.OnClickListener {

    /**
     * Enum which provide the facing
     */
    public enum Facing {
        BACK(0), FRONT(1);

        /**
         * {@link Integer} value of the {@link Facing}
         */
        protected final int value;

        /**
         * Method which provide to create the {@link Facing} from {@link Integer} value
         *
         * @param value {@link Integer} value
         */
        Facing(int value) {
            this.value = value;
        }
    }

    /**
     * Enum with the flash value
     */
    public enum Flash {
        OFF(0, R.drawable.ic_gflash_off),
        ON(1, R.drawable.ic_gflash_on),
        AUTO(3, R.drawable.ic_gflash_auto);
        /**
         * {@link Integer} value of the {@link Facing}
         */
        protected final int value;

        /**
         * {@link Integer} value of the drawable
         */
        @DrawableRes
        protected final int drawable;

        /**
         * Method which provide to create the {@link Flash} from {@link Integer} value
         *
         * @param value {@link Integer} value
         */
        Flash(int value, @DrawableRes int drawable) {
            this.value = value;
            this.drawable = drawable;
        }
    }

    /**
     * Interface which provide the camera callback
     */
    public interface OnCameraCallback {

        /**
         * Method which provide the action when the flash the changed
         *
         * @param cameraView instance of the {@link GCameraView}
         * @param flash      instance of the {@link Flash}
         */
        void cameraViewFlashChanged(@NonNull GCameraView cameraView,
                                    @NonNull Flash flash);

        /**
         * Method which provide the action when the flash the changed
         *
         * @param cameraView instance of the {@link GCameraView}
         * @param facing     instance of the {@link Facing}
         */
        void cameraViewFacingChanged(@NonNull GCameraView cameraView,
                                     @NonNull Facing facing);

        /**
         * Method which provide the action when the flash the changed
         *
         * @param cameraView instance of the {@link GCameraView}
         * @param uri        instance of the {@link Uri}
         */
        void cameraViewPictureTaken(@NonNull GCameraView cameraView,
                                    @NonNull Uri uri);

        /**
         * Method which provide the getting of the camera icon
         *
         * @return {@link Integer} value of the camera icon
         */
        @DrawableRes
        @Nullable
        Integer cameraViewIconCamera();

        /**
         * Method which provide the getting of the flash auto icon
         *
         * @return {@link Integer} value of the camera icon
         */
        @DrawableRes
        @Nullable
        Integer cameraViewIconFlashAuto();

        /**
         * Method which provide the getting of the flash on icon
         *
         * @return {@link Integer} value of the camera icon
         */
        @DrawableRes
        @Nullable
        Integer cameraViewIconFlashOn();

        /**
         * Method which provide the getting of the flash off icon
         *
         * @return {@link Integer} value of the camera icon
         */
        @DrawableRes
        @Nullable
        Integer cameraViewIconFlashOff();

    }

    /**
     * Instance of {@link View}
     */
    protected View baseView;

    /**
     * Instance of the {@link CameraView}
     */
    protected CameraView cameraView;

    /**
     * Instance of the {@link ImageView}
     */
    protected ImageView imageFlash;

    /**
     * Instance of the {@link ImageView}
     */
    protected ImageView imageCamera;

    /**
     * Instance of the {@link OnCameraCallback}
     */
    protected OnCameraCallback callback;

    /**
     * Instance of the {@link CameraView.Callback}
     */
    protected CameraView.Callback cameraCallback;

    /**
     * Constructor which provide the create {@link View} from
     *
     * @param context instance of {@link Context}
     */
    public GCameraView(Context context) {
        super(context);
        onInitializeView(context, null);
    }

    /**
     * Constructor which provide the create {@link View} from
     *
     * @param context instance of {@link Context}
     * @param attrs   instance of {@link AttributeSet}
     */
    public GCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        onInitializeView(context, attrs);
    }

    /**
     * Constructor which provide the create {@link View} from
     *
     * @param context      instance of {@link Context}
     * @param attrs        instance of {@link AttributeSet}
     * @param defStyleAttr attribute style
     */
    public GCameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onInitializeView(context, attrs);
    }

    /**
     * Method which provide the {@link View} initializing
     *
     * @param context instance of {@link Context}
     */
    private void onInitializeView(Context context, @Nullable final AttributeSet attrs) {
        this.inflateView(context, getLayoutId());
        try {
            this.onLinkInterface();
        } catch (Exception ex) {
            Log.e(GCameraView.class.getSimpleName(), ex.toString());
        }
    }

    /**
     * Method which provide the inflating of the view
     *
     * @param context  current context
     * @param layoutID layout id
     */
    private void inflateView(Context context, @LayoutRes int layoutID) {
        LayoutInflater inflater = LayoutInflater.from(context);
        baseView = inflater.inflate(layoutID, this);
    }

    /**
     * Method which provide the getting of the layout ID
     *
     * @return layout ID
     */
    @LayoutRes
    protected int getLayoutId() {
        return R.layout.layout_gcamera_view;
    }

    /**
     * Method which provide interface linking
     */
    protected void onLinkInterface() {
        this.cameraView = findViewById(R.id.camera);
        this.imageFlash = findViewById(R.id.image_flash);
        this.imageCamera = findViewById(R.id.image_camera);
    }

    /**
     * Method which provide the creating of the {@link View}
     */
    protected void onCreateView() {
        if (this.cameraView != null) {
            this.cameraView.removeCallback(getCameraCallback());
            this.cameraView.addCallback(getCameraCallback());
        }
        if (this.imageFlash != null) {
            this.imageFlash.setOnClickListener(this);
        }
        if (this.imageCamera != null) {
            this.imageCamera.setOnClickListener(this);
        }
        this.setFlash(Flash.AUTO);
    }

    // Start / Stop

    /**
     * Method which provide the {@link CameraView} starting
     */
    public final void start() {
        if (this.cameraView != null) {
            this.onCreateView();
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest
                        .permission.CAMERA}, 0b1010);
                return;
            }
            if (ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest
                        .permission.WRITE_EXTERNAL_STORAGE}, 0b1010);
                return;
            }
            if (this.cameraView.isCameraOpened()) {
                this.cameraView.stop();
            }
            this.cameraView.start();
        }
    }

    /**
     * Method which provide the {@link CameraView} starting
     */
    public final void stop() {
        if (this.cameraView != null) {
            this.cameraView.stop();
        }
    }

    // Facing

    /**
     * Method which provide the setting facing
     *
     * @param facing instance of the {@link Facing}
     */
    public final void setFacing(Facing facing) {
        if (this.cameraView != null) {
            this.cameraView.setFacing(facing.value);
            if (callback != null) {
                callback.cameraViewFacingChanged(this, getFacing());
            }
        }
    }

    /**
     * Method which provide the getting of the {@link Facing}
     *
     * @return instance of the {@link Facing}
     */
    @NonNull
    public final Facing getFacing() {
        Facing facing = Facing.BACK;
        if (this.cameraView != null) {
            int facingValue = this.cameraView.getFacing();
            if (facingValue == Facing.FRONT.value) {
                facing = Facing.FRONT;
            }
        }
        return facing;
    }

    // Flash

    /**
     * Method which provide the setting facing
     *
     * @param flash instance of the {@link Flash}
     */
    public final void setFlash(Flash flash) {
        if (this.cameraView != null) {
            this.cameraView.setFlash(flash.value);
            if (callback != null) {
                callback.cameraViewFlashChanged(this, getFlash());
            }
            Integer flashIcon = null;
            if (this.callback != null) {
                if (flash == Flash.AUTO) {
                    flashIcon = this.callback.cameraViewIconFlashAuto();
                } else if (flash == Flash.ON) {
                    flashIcon = this.callback.cameraViewIconFlashOn();
                } else if (flash == Flash.OFF) {
                    flashIcon = this.callback.cameraViewIconFlashOff();
                }
            }
            if (flashIcon == null) {
                flashIcon = flash.drawable;
            }
            if (this.imageFlash != null) {
                this.imageFlash.setImageResource(flashIcon);
            }
        }
    }

    /**
     * Method which provide the getting of the {@link Flash}
     *
     * @return instance of the {@link Flash}
     */
    @NonNull
    public final Flash getFlash() {
        Flash flash = Flash.OFF;
        if (this.cameraView != null) {
            int facingValue = this.cameraView.getFlash();
            if (facingValue == Flash.ON.value) {
                flash = Flash.ON;
            } else if (facingValue == Flash.AUTO.value) {
                flash = Flash.AUTO;
            }
        }
        return flash;
    }

    /**
     * Method which provide the switching of the flash
     */
    public final void switchFlash() {
        Flash flash = getFlash();
        if (flash == Flash.OFF) {
            flash = Flash.AUTO;
        } else if (flash == Flash.ON) {
            flash = Flash.OFF;
        } else if (flash == Flash.AUTO) {
            flash = Flash.ON;
        }
        setFlash(flash);
    }

    // Make photo

    /**
     * Method which provide the making photo
     */
    public final void makePhoto() {
        if (this.cameraView != null) {
            this.cameraView.takePicture();
        }
    }

    // On click

    /**
     * Called when a view has been clicked.
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.image_flash) {
            this.switchFlash();
        } else if (view.getId() == R.id.image_camera) {
            this.makePhoto();
        }
    }

    // Get callback

    /**
     * Method which provide the getting of the {@link CameraView.Callback}
     *
     * @return instance of the {@link CameraView.Callback}
     */
    @NonNull
    protected final CameraView.Callback getCameraCallback() {
        if (this.cameraCallback == null) {
            this.cameraCallback = new CameraView.Callback() {
                /**
                 * Called when a picture is taken.
                 *
                 * @param cameraView The associated {@link CameraView}.
                 * @param data       JPEG data.
                 */
                @Override
                public void onPictureTaken(CameraView cameraView, byte[] data) {
                    onPictureProcessing(data);
                }
            };
        }
        return cameraCallback;
    }

    // Take picture

    /**
     * Method which provide the picture taking
     *
     * @param data array of the {@link Byte}
     */
    protected void onPictureProcessing(byte[] data) {
        new SaveFileTask(data, this.callback, this).execute();
    }

    /**
     * Method which provide the configuring of the {@link GCameraView} with callback
     *
     * @param callback instance of the {@link OnCameraCallback}
     */
    public final void configure(@Nullable OnCameraCallback callback) {
        this.callback = callback;
        // Update camera icon
        if ((this.imageCamera != null)
                && (this.callback != null)
                && (this.callback.cameraViewIconCamera() != null)) {
            this.imageCamera.setImageResource(this.callback.cameraViewIconCamera());
        }
        // Update flash icons
        if (this.callback != null) {
            this.setFlash(this.getFlash());
        }
    }

    /**
     * Instance of the {@link SaveFileTask}
     */
    protected class SaveFileTask extends AsyncTask<Void, Void, Uri> {

        /**
         * Array of the {@link Byte}
         */
        private final byte[] bytes;

        /**
         * Instance of the {@link WeakReference}
         */
        private final WeakReference<OnCameraCallback> callbackWeakReference;

        /**
         * Instance of the {@link WeakReference}
         */
        private final WeakReference<GCameraView> viewWeakReference;

        /**
         * Constructor which provide the create of the {@link SaveFileTask} from parameters
         *
         * @param bytes      array of the {@link Byte}
         * @param callback   instance of the {@link OnCameraCallback}
         * @param cameraView instance of the {@link GCameraView}
         */
        public SaveFileTask(@NonNull byte[] bytes,
                            @Nullable OnCameraCallback callback,
                            @Nullable GCameraView cameraView) {
            this.bytes = bytes;
            this.callbackWeakReference = new WeakReference<>(callback);
            this.viewWeakReference = new WeakReference<>(cameraView);
        }

        /**
         * Override this method to perform a computation on a background thread.
         *
         * @param voids The parameters of the task.
         * @return A result, defined by the subclass of this task.
         */
        @Override
        protected Uri doInBackground(Void... voids) {
            Uri uri = null;
            String root = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).toString();
            File myDir = new File(root);
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            @SuppressLint("DefaultLocale") String fileName = String.format("photo_%d", new Date().getTime());
            String imageName = String.format("%s.jpg", fileName);
            File file = new File(myDir, imageName);
            if (file.exists()) file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                out.write(this.bytes);
                out.flush();
                out.close();
                uri = Uri.fromFile(file);
            } catch (Exception e) {
                e.printStackTrace();
                uri = null;
            }
            return uri;
        }

        /**
         * Method which provide the post execute functional
         *
         * @param uri instance of the {@link Uri}
         */
        @Override
        protected void onPostExecute(Uri uri) {
            final OnCameraCallback cameraCallback = (this.callbackWeakReference == null)
                    ? null : this.callbackWeakReference.get();
            final GCameraView cameraView = (this.viewWeakReference == null)
                    ? null : this.viewWeakReference.get();
            if ((cameraCallback != null)
                    && (uri != null)
                    && (cameraView != null)) {
                cameraCallback.cameraViewPictureTaken(cameraView, uri);
            }
        }
    }

}
