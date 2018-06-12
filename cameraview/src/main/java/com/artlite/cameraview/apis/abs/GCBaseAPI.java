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

package com.artlite.cameraview.apis.abs;

import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;


/**
 * Encapsulates all the operations related to camera preview in a backward-compatible manner.
 * IMPLEMENTATION NON USE OF THIS CLASS USE THE {@link com.artlite.cameraview.common.GCCameraView}
 */
public abstract class GCBaseAPI {

    /**
     * Callback which provide the action with the surface changed
     */
    public interface Callback {

        /**
         * Method which provide the action when the surface was changed
         */
        void onSurfaceChanged();
    }

    /**
     * Instance of the {@link Callback}
     */
    protected Callback callback;

    /**
     * {@link Integer} value of the width
     */
    protected int width;

    /**
     * {@link Integer} value of the height
     */
    protected int height;

    /**
     * Method which provide the setting of the {@link Callback}
     *
     * @param callback instance of the {@link Callback}
     */
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    /**
     * Method which provide the getting of the {@link Surface}
     *
     * @return instance of the {@link Surface}
     */
    public abstract Surface getSurface();

    /**
     * Method which provide the getting of the {@link View}
     *
     * @return instance of the {@link View}
     */
    public abstract View getView();

    /**
     * Method which provide the getting of the output {@link Class}
     *
     * @return instance of the {@link Class}
     */
    public abstract Class getOutputClass();

    /**
     * Method which provide the setting of the display orientation
     *
     * @param orientation {@link Integer} value of the display orientation
     */
    public abstract void setDisplayOrientation(int orientation);

    /**
     * Method which provide the checking if the {@link Surface} is ready
     *
     * @return {@link Boolean} value if it ready
     */
    public abstract boolean isReady();

    /**
     * Method which provide the dispatching of the {@link Surface} changed
     */
    public void dispatchSurfaceChanged() {
        callback.onSurfaceChanged();
    }

    /**
     * Method which provide the getting of the {@link SurfaceHolder}
     *
     * @return instance of the {@link SurfaceHolder}
     */
    public SurfaceHolder getSurfaceHolder() {
        return null;
    }

    /**
     * Method which provide the getting of the surface texture
     *
     * @return instance of the texture {@link Object}
     */
    public Object getSurfaceTexture() {
        return null;
    }

    /**
     * Method which provide the setting of the buffer size
     *
     * @param width  {@link Integer} value of the width
     * @param height {@link Integer} value of the height
     */
    protected void setBufferSize(int width, int height) {
    }

    /**
     * Method which provide the setting of the size
     *
     * @param width  {@link Integer} value of the width
     * @param height {@link Integer} value of the height
     */
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Method which provide the getting of the {@link Integer} value of the width
     *
     * @return {@link Integer} value of the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Method which provide the getting of the {@link Integer} value of the height
     *
     * @return {@link Integer} value of the height
     */
    public int getHeight() {
        return height;
    }

}
