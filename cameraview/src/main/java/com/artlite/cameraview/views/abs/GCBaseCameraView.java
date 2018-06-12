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

package com.artlite.cameraview.views.abs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.artlite.cameraview.apis.abs.GCBaseAPI;
import com.artlite.cameraview.models.GCAspectRatio;

import java.util.Set;

/**
 * Class which provide the camera view  implementation
 * IMPLEMENTATION NON USE OF THIS CLASS USE THE {@link com.artlite.cameraview.common.GCCameraView}
 *
 * @see com.artlite.cameraview.views.GCCameraImplementation
 */
public abstract class GCBaseCameraView {

    /**
     * Instance of the {@link Callback}
     */
    protected final Callback callback;

    /**
     * Instance of the {@link GCBaseAPI}
     */
    protected final GCBaseAPI preview;

    /**
     * Constructor which provide to create of the {@link GCBaseCameraView} with parameters
     *
     * @param callback instance of the {@link Callback}
     * @param preview  instance of the {@link GCBaseAPI}
     */
    public GCBaseCameraView(@NonNull Callback callback,
                            @NonNull GCBaseAPI preview) {
        this.callback = callback;
        this.preview = preview;
    }

    /**
     * Method which provide the getting of the {@link View}
     *
     * @return instance of the {@link View}
     */
    @Nullable
    public View getView() {
        if (preview != null) {
            return preview.getView();
        }
        return null;
    }

    /**
     * Method which provide the start session
     *
     * @return {@link Boolean} value if the session is started
     */
    public abstract boolean start();

    /**
     * Method which provide the stop session
     */
    public abstract void stop();

    /**
     * Method which provide the checking if the session is opened
     *
     * @return {@link Boolean} value if the session is opened
     */
    public abstract boolean isCameraOpened();

    /**
     * Method which provide the setting of the facing
     *
     * @param facing {@link Integer} value of the facing
     */
    public abstract void setFacing(int facing);

    /**
     * Method which provide the getting of the {@link Integer} value of the facing
     *
     * @return {@link Integer} value of the facing
     */
    public abstract int getFacing();

    /**
     * Method which provide the getting of the {@link Set} of the supported {@link GCAspectRatio}
     *
     * @return {@link Set} of the supported {@link GCAspectRatio}
     */
    public abstract Set<GCAspectRatio> getAspectRatios();

    /**
     * Method which provide the setting of the {@link GCAspectRatio}
     *
     * @param ratio instance of the {@link GCAspectRatio}
     * @return {@link Boolean} value if it set
     */
    public abstract boolean setAspectRatio(GCAspectRatio ratio);

    /**
     * Method which provide the getting of the {@link GCAspectRatio}
     *
     * @return instance of the {@link GCAspectRatio}
     */
    public abstract GCAspectRatio getAspectRatio();

    /**
     * Method which provide the setting of the auto focus
     *
     * @param autoFocus {@link Boolean} value of the auto focus
     */
    public abstract void setAutoFocus(boolean autoFocus);

    /**
     * Method which provide the getting of the {@link Boolean} value of the auto focus
     *
     * @return {@link Boolean} value of the auto focus
     */
    public abstract boolean getAutoFocus();

    /**
     * Method which provide the setting {@link Integer} value of the flash
     *
     * @param flash {@link Integer} value of the flash
     */
    public abstract void setFlash(int flash);

    /**
     * Method which provide the getting {@link Integer} value of the flash
     *
     * @return {@link Integer} value of the flash
     */
    public abstract int getFlash();

    /**
     * Method which provide the taking picture
     */
    public abstract void takePicture();

    /**
     * Method which provide the setting of the display orientation
     *
     * @param orientation {@link Integer} value of the display orientation
     */
    public abstract void setDisplayOrientation(int orientation);

    /**
     * Callback which provide the camera action listening
     */
    public interface Callback {

        /**
         * Method which provide the action when the camera was opened
         */
        void onCameraOpened();

        /**
         * Method which provide the action when the camera was closed
         */
        void onCameraClosed();

        /**
         * Method which provide the picture taken
         *
         * @param data {@link Byte} array of the picture data
         */
        void onPictureTaken(byte[] data);

    }

}
