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

package com.artlite.cameraview.surface;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.artlite.cameraview.apis.abs.GCBaseAPI;
import com.google.android.cameraview.R;

/**
 * Class which provide the surface view preview
 * IMPLEMENTATION NON USE OF THIS CLASS USE THE {@link com.artlite.cameraview.common.GCCameraView}
 */
public class GCSurfaceViewPreview extends GCBaseAPI {

    /**
     * Instance of the {@link SurfaceView}
     */
    protected final SurfaceView surfaceView;

    /**
     * Constructor which provide the create the {@link GCSurfaceViewPreview} with parameters
     *
     * @param context instance of the {@link Context}
     * @param parent  instance of the {@link ViewGroup}
     */
    public GCSurfaceViewPreview(@NonNull Context context,
                                @NonNull ViewGroup parent) {
        final View view = View.inflate(context, R.layout.surface_view, parent);
        surfaceView = (SurfaceView) view.findViewById(R.id.surface_view);
        final SurfaceHolder holder = surfaceView.getHolder();
        //noinspection deprecation
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder h) {
            }

            @Override
            public void surfaceChanged(SurfaceHolder h, int format, int width, int height) {
                setSize(width, height);
                if (!ViewCompat.isInLayout(surfaceView)) {
                    dispatchSurfaceChanged();
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder h) {
                setSize(0, 0);
            }
        });
    }

    /**
     * Method which provide the getting of the {@link Surface}
     *
     * @return instance of the {@link Surface}
     */
    @Override
    public Surface getSurface() {
        return getSurfaceHolder().getSurface();
    }

    /**
     * Method which provide the getting of the {@link SurfaceHolder}
     *
     * @return instance of the {@link SurfaceHolder}
     */
    @Override
    public SurfaceHolder getSurfaceHolder() {
        return surfaceView.getHolder();
    }

    /**
     * Method which provide the getting of the {@link View}
     *
     * @return instance of the {@link View}
     */
    @Override
    public View getView() {
        return surfaceView;
    }

    /**
     * Method which provide the getting of the output {@link Class}
     *
     * @return instance of the {@link Class}
     */
    @Override
    public Class getOutputClass() {
        return SurfaceHolder.class;
    }

    /**
     * Method which provide the setting of the display orientation
     *
     * @param orientation {@link Integer} value of the display orientation
     */
    @Override
    public void setDisplayOrientation(int orientation) {
    }

    /**
     * Method which provide the checking if the {@link Surface} is ready
     *
     * @return {@link Boolean} value if it ready
     */
    @Override
    public boolean isReady() {
        return getWidth() != 0 && getHeight() != 0;
    }

}
