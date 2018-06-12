/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.artlite.cameraview.apis;

import android.annotation.SuppressLint;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.SparseArrayCompat;
import android.view.SurfaceHolder;

import com.artlite.cameraview.apis.abs.GCBaseAPI;
import com.artlite.cameraview.constants.GCConstants;
import com.artlite.cameraview.constants.GCSize;
import com.artlite.cameraview.constants.GCSizeMap;
import com.artlite.cameraview.models.GCAspectRatio;
import com.artlite.cameraview.views.abs.GCBaseCameraView;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class which provide the implementation of the Camera V1 API implementation
 * IMPLEMENTATION NON USE OF THIS CLASS USE THE {@link com.artlite.cameraview.common.GCCameraView}
 */
@SuppressWarnings("deprecation")
public class GCCameraAPI extends GCBaseCameraView {

    /**
     * {@link Integer} constants of the invalid camera ID
     */
    private static final int INVALID_CAMERA_ID = -1;

    /**
     * Constant of the {@link SparseArrayCompat}
     */
    private static final SparseArrayCompat<String> FLASH_MODES = new SparseArrayCompat<>();

    /**
     * Initialization of the flash modes
     */
    static {
        FLASH_MODES.put(GCConstants.FLASH_OFF, Camera.Parameters.FLASH_MODE_OFF);
        FLASH_MODES.put(GCConstants.FLASH_ON, Camera.Parameters.FLASH_MODE_ON);
        FLASH_MODES.put(GCConstants.FLASH_TORCH, Camera.Parameters.FLASH_MODE_TORCH);
        FLASH_MODES.put(GCConstants.FLASH_AUTO, Camera.Parameters.FLASH_MODE_AUTO);
        FLASH_MODES.put(GCConstants.FLASH_RED_EYE, Camera.Parameters.FLASH_MODE_RED_EYE);
    }

    /**
     * {@link Integer} value of the camera ID
     */
    private int cameraID;

    /**
     * Instance of the {@link AtomicBoolean}
     */
    private final AtomicBoolean isPictureCaptureInProgress = new AtomicBoolean(false);

    /**
     * Instance of the {@link Camera}
     */
    protected Camera camera;

    /**
     * Instance of the {@link Camera.Parameters}
     */
    private Camera.Parameters parameters;

    /**
     * Instance of the {@link Camera.CameraInfo}
     */
    private final Camera.CameraInfo cameraInfo = new Camera.CameraInfo();

    /**
     * Instance of the {@link GCSizeMap}
     */
    private final GCSizeMap previewSizes = new GCSizeMap();

    /**
     * Instance of the {@link GCSizeMap}
     */
    private final GCSizeMap pictureSizes = new GCSizeMap();

    /**
     * Instance of the {@link GCAspectRatio}
     */
    private GCAspectRatio aspectRatio;

    /**
     * {@link Boolean} value which provide to define if we need to preview
     */
    private boolean isShowingPreview;

    /**
     * {@link Boolean} value if the camera have the auto focus
     */
    private boolean isHaveAutoFocus;

    /**
     * {@link Integer} value of the facing
     */
    private int facing;

    /**
     * {@link Integer} value of the flash
     */
    private int flash;

    /**
     * {@link Integer} value of the display orientation
     */
    private int displayOrientation;

    /**
     * Constructor which provide the create {@link GCCameraAPI} with parameters
     *
     * @param callback instance of the {@link Callback}
     * @param preview  instance of the {@link GCBaseAPI}
     */
    public GCCameraAPI(@Nullable Callback callback,
                       @NonNull GCBaseAPI preview) {
        super(callback, preview);
        preview.setCallback(new GCBaseAPI.Callback() {
            @Override
            public void onSurfaceChanged() {
                if (camera != null) {
                    setUpPreview();
                    adjustCameraParameters();
                }
            }
        });
    }

    /**
     * Method which provide the start session
     *
     * @return {@link Boolean} value if the session is started
     */
    @Override
    public boolean start() {
        chooseCamera();
        openCamera();
        if (preview.isReady()) {
            setUpPreview();
        }
        isShowingPreview = true;
        camera.startPreview();
        return true;
    }

    /**
     * Method which provide the stop session
     */
    @Override
    public void stop() {
        if (camera != null) {
            camera.stopPreview();
        }
        isShowingPreview = false;
        releaseCamera();
    }

    /**
     * Method which provide the setting up of the preview
     */
    @SuppressLint("NewApi")
    public void setUpPreview() {
        try {
            if (preview.getOutputClass() == SurfaceHolder.class) {
                final boolean needsToStopPreview = isShowingPreview && Build.VERSION.SDK_INT < 14;
                if (needsToStopPreview) {
                    camera.stopPreview();
                }
                camera.setPreviewDisplay(preview.getSurfaceHolder());
                if (needsToStopPreview) {
                    camera.startPreview();
                }
            } else {
                camera.setPreviewTexture((SurfaceTexture) preview.getSurfaceTexture());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method which provide the checking if the session is opened
     *
     * @return {@link Boolean} value if the session is opened
     */
    @Override
    public boolean isCameraOpened() {
        return camera != null;
    }

    /**
     * Method which provide the setting of the facing
     *
     * @param facing {@link Integer} value of the facing
     */
    @Override
    public void setFacing(int facing) {
        if (this.facing == facing) {
            return;
        }
        this.facing = facing;
        if (isCameraOpened()) {
            stop();
            start();
        }
    }

    /**
     * Method which provide the getting of the {@link Integer} value of the facing
     *
     * @return {@link Integer} value of the facing
     */
    @Override
    public int getFacing() {
        return facing;
    }

    /**
     * Method which provide the getting of the {@link Set} of the supported {@link GCAspectRatio}
     *
     * @return {@link Set} of the supported {@link GCAspectRatio}
     */
    @Override
    public Set<GCAspectRatio> getAspectRatios() {
        GCSizeMap idealAspectRatios = previewSizes;
        for (GCAspectRatio aspectRatio : idealAspectRatios.getRatios()) {
            if (pictureSizes.sizes(aspectRatio) == null) {
                idealAspectRatios.remove(aspectRatio);
            }
        }
        return idealAspectRatios.getRatios();
    }

    /**
     * Method which provide the setting of the {@link GCAspectRatio}
     *
     * @param ratio instance of the {@link GCAspectRatio}
     * @return {@link Boolean} value if it set
     */
    @Override
    public boolean setAspectRatio(GCAspectRatio ratio) {
        if (aspectRatio == null || !isCameraOpened()) {
            // Handle this later when camera is opened
            aspectRatio = ratio;
            return true;
        } else if (!aspectRatio.equals(ratio)) {
            final Set<GCSize> sizes = previewSizes.sizes(ratio);
            if (sizes == null) {
                throw new UnsupportedOperationException(ratio + " is not supported");
            } else {
                aspectRatio = ratio;
                adjustCameraParameters();
                return true;
            }
        }
        return false;
    }

    /**
     * Method which provide the getting of the {@link GCAspectRatio}
     *
     * @return instance of the {@link GCAspectRatio}
     */
    @Override
    public GCAspectRatio getAspectRatio() {
        return aspectRatio;
    }

    /**
     * Method which provide the setting of the auto focus
     *
     * @param autoFocus {@link Boolean} value of the auto focus
     */
    @Override
    public void setAutoFocus(boolean autoFocus) {
        if (isHaveAutoFocus == autoFocus) {
            return;
        }
        if (setAutoFocusInternal(autoFocus)) {
            camera.setParameters(parameters);
        }
    }

    /**
     * Method which provide the getting of the {@link Boolean} value of the auto focus
     *
     * @return {@link Boolean} value of the auto focus
     */
    @Override
    public boolean getAutoFocus() {
        if (!isCameraOpened()) {
            return isHaveAutoFocus;
        }
        String focusMode = parameters.getFocusMode();
        return focusMode != null && focusMode.contains("continuous");
    }

    /**
     * Method which provide the setting {@link Integer} value of the flash
     *
     * @param flash {@link Integer} value of the flash
     */
    @Override
    public void setFlash(int flash) {
        if (flash == this.flash) {
            return;
        }
        if (setFlashInternal(flash)) {
            camera.setParameters(parameters);
        }
    }

    /**
     * Method which provide the getting {@link Integer} value of the flash
     *
     * @return {@link Integer} value of the flash
     */
    @Override
    public int getFlash() {
        return flash;
    }

    /**
     * Method which provide the taking picture
     */
    @Override
    public void takePicture() {
        if (!isCameraOpened()) {
            throw new IllegalStateException(
                    "Camera is not ready. Call start() before takePicture().");
        }
        if (getAutoFocus()) {
            camera.cancelAutoFocus();
            camera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    takePictureInternal();
                }
            });
        } else {
            takePictureInternal();
        }
    }

    /**
     * Method which provide the take picture internal
     */
    public void takePictureInternal() {
        if (!isPictureCaptureInProgress.getAndSet(true)) {
            camera.takePicture(null, null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    isPictureCaptureInProgress.set(false);
                    callback.onPictureTaken(data);
                    camera.cancelAutoFocus();
                    camera.startPreview();
                }
            });
        }
    }

    /**
     * Method which provide the setting of the display orientation
     *
     * @param orientation {@link Integer} value of the display orientation
     */
    @Override
    public void setDisplayOrientation(int orientation) {
        if (displayOrientation == orientation) {
            return;
        }
        displayOrientation = orientation;
        if (isCameraOpened()) {
            parameters.setRotation(calcCameraRotation(orientation));
            camera.setParameters(parameters);
            final boolean needsToStopPreview = isShowingPreview && Build.VERSION.SDK_INT < 14;
            if (needsToStopPreview) {
                camera.stopPreview();
            }
            camera.setDisplayOrientation(calcDisplayOrientation(orientation));
            if (needsToStopPreview) {
                camera.startPreview();
            }
        }
    }

    /**
     * Method which provide the choosing camera
     */
    private void chooseCamera() {
        for (int i = 0, count = Camera.getNumberOfCameras(); i < count; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == facing) {
                cameraID = i;
                return;
            }
        }
        cameraID = INVALID_CAMERA_ID;
    }

    /**
     * Method which provide the open camera
     */
    private void openCamera() {
        if (camera != null) {
            releaseCamera();
        }
        camera = Camera.open(cameraID);
        parameters = camera.getParameters();
        // Supported preview sizes
        previewSizes.clear();
        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            previewSizes.add(new GCSize(size.width, size.height));
        }
        // Supported picture sizes;
        pictureSizes.clear();
        for (Camera.Size size : parameters.getSupportedPictureSizes()) {
            pictureSizes.add(new GCSize(size.width, size.height));
        }
        // GCAspectRatio
        if (aspectRatio == null) {
            aspectRatio = GCConstants.DEFAULT_ASPECT_RATIO;
        }
        adjustCameraParameters();
        camera.setDisplayOrientation(calcDisplayOrientation(displayOrientation));
        callback.onCameraOpened();
    }

    /**
     * Method which provide the choose of the {@link GCAspectRatio}
     *
     * @return instance of the {@link GCAspectRatio}
     */
    private GCAspectRatio chooseAspectRatio() {
        GCAspectRatio r = null;
        for (GCAspectRatio ratio : previewSizes.getRatios()) {
            r = ratio;
            if (ratio.equals(GCConstants.DEFAULT_ASPECT_RATIO)) {
                return ratio;
            }
        }
        return r;
    }

    /**
     * Method which provide the adjust camera parameters
     */
    protected void adjustCameraParameters() {
        SortedSet<GCSize> sizes = previewSizes.sizes(aspectRatio);
        if (sizes == null) { // Not supported
            aspectRatio = chooseAspectRatio();
            sizes = previewSizes.sizes(aspectRatio);
        }
        GCSize size = chooseOptimalSize(sizes);

        // Always re-apply camera parameters
        // Largest picture size in this ratio
        final GCSize pictureSize = pictureSizes.sizes(aspectRatio).last();
        if (isShowingPreview) {
            camera.stopPreview();
        }
        parameters.setPreviewSize(size.getWidth(), size.getHeight());
        parameters.setPictureSize(pictureSize.getWidth(), pictureSize.getHeight());
        parameters.setRotation(calcCameraRotation(displayOrientation));
        setAutoFocusInternal(isHaveAutoFocus);
        setFlashInternal(flash);
        camera.setParameters(parameters);
        if (isShowingPreview) {
            camera.startPreview();
        }
    }

    /**
     * Method which provide the choose of the optimal size
     *
     * @param sizes instance of the {@link SortedSet}
     * @return instance of the {@link GCSize}
     */
    @SuppressWarnings("SuspiciousNameCombination")
    private GCSize chooseOptimalSize(SortedSet<GCSize> sizes) {
        if (!preview.isReady()) { // Not yet laid out
            return sizes.first(); // Return the smallest size
        }
        int desiredWidth;
        int desiredHeight;
        final int surfaceWidth = preview.getWidth();
        final int surfaceHeight = preview.getHeight();
        if (isLandscape(displayOrientation)) {
            desiredWidth = surfaceHeight;
            desiredHeight = surfaceWidth;
        } else {
            desiredWidth = surfaceWidth;
            desiredHeight = surfaceHeight;
        }
        GCSize result = null;
        for (GCSize size : sizes) { // Iterate from small to large
            if (desiredWidth <= size.getWidth() && desiredHeight <= size.getHeight()) {
                return size;

            }
            result = size;
        }
        return result;
    }

    /**
     * Method which provide the release of the camera
     */
    private void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
            callback.onCameraClosed();
        }
    }

    /**
     * Calculate display orientation
     *
     * @param degrees Screen orientation in degrees
     * @return Number of degrees required to rotate preview
     */
    private int calcDisplayOrientation(int degrees) {
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            return (360 - (cameraInfo.orientation + degrees) % 360) % 360;
        } else {  // back-facing
            return (cameraInfo.orientation - degrees + 360) % 360;
        }
    }

    /**
     * Calculate camera rotation
     *
     * @param degrees Screen orientation in degrees
     * @return Number of degrees to rotate image in order for it to view correctly.
     */
    private int calcCameraRotation(int degrees) {
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            return (cameraInfo.orientation + degrees) % 360;
        } else {  // back-facing
            final int landscapeFlip = isLandscape(degrees) ? 180 : 0;
            return (cameraInfo.orientation + degrees + landscapeFlip) % 360;
        }
    }

    /**
     * Test if the supplied orientation is in landscape.
     *
     * @param degrees Orientation in degrees (0,90,180,270)
     * @return True if in landscape, false if portrait
     */
    private boolean isLandscape(int degrees) {
        return (degrees == GCConstants.LANDSCAPE_90 ||
                degrees == GCConstants.LANDSCAPE_270);
    }

    /**
     * Method which provide the setting of the auto focus
     *
     * @return {@code true} if {@link #parameters} was modified.
     */
    private boolean setAutoFocusInternal(boolean autoFocus) {
        isHaveAutoFocus = autoFocus;
        if (isCameraOpened()) {
            final List<String> modes = parameters.getSupportedFocusModes();
            if (autoFocus && modes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            } else if (modes.contains(Camera.Parameters.FOCUS_MODE_FIXED)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_FIXED);
            } else if (modes.contains(Camera.Parameters.FOCUS_MODE_INFINITY)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
            } else {
                parameters.setFocusMode(modes.get(0));
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method which provide the setting flash internal
     *
     * @return {@code true} if {@link #parameters} was modified.
     */
    private boolean setFlashInternal(int flash) {
        if (isCameraOpened()) {
            List<String> modes = parameters.getSupportedFlashModes();
            String mode = FLASH_MODES.get(flash);
            if (modes != null && modes.contains(mode)) {
                parameters.setFlashMode(mode);
                this.flash = flash;
                return true;
            }
            String currentMode = FLASH_MODES.get(this.flash);
            if (modes == null || !modes.contains(currentMode)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                this.flash = GCConstants.FLASH_OFF;
                return true;
            }
            return false;
        } else {
            this.flash = flash;
            return false;
        }
    }

}
