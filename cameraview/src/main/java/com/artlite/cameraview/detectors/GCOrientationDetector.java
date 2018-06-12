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

package com.artlite.cameraview.detectors;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.Surface;


/**
 * Monitors the value returned from {@link Display#getRotation()}.
 * IMPLEMENTATION NON USE OF THIS CLASS USE THE {@link com.artlite.cameraview.common.GCCameraView}
 */
public abstract class GCOrientationDetector {

    /**
     * Instance of the {@link OrientationEventListener}
     */
    private final OrientationEventListener mOrientationEventListener;

    /**
     * Mapping from Surface.Rotation_n to degrees.
     */
    static final SparseIntArray DISPLAY_ORIENTATIONS = new SparseIntArray();

    /**
     * Init of the display orientations
     */
    static {
        DISPLAY_ORIENTATIONS.put(Surface.ROTATION_0, 0);
        DISPLAY_ORIENTATIONS.put(Surface.ROTATION_90, 90);
        DISPLAY_ORIENTATIONS.put(Surface.ROTATION_180, 180);
        DISPLAY_ORIENTATIONS.put(Surface.ROTATION_270, 270);
    }

    /**
     * Instance of the {@link Display}
     */
    protected Display mDisplay;

    /**
     * {@link Integer} value of the last knows display orientation
     */
    private int previousOrientation = 0;

    /**
     * Constructor which provide the create the {@link GCOrientationDetector} from the
     * instance of the {@link Context}
     *
     * @param context instance of the {@link Context}
     */
    public GCOrientationDetector(Context context) {
        mOrientationEventListener = new OrientationEventListener(context) {
            /**
             * This is either Surface.Rotation_0, _90, _180, _270, or -1 (invalid).
             */
            private int lastOrientation = -1;

            /**
             * Called when the orientation of the device has changed.
             * orientation parameter is in degrees, ranging from 0 to 359.
             * orientation is 0 degrees when the device is oriented in its natural position,
             * 90 degrees when its left side is at the top, 180 degrees when it is upside down,
             * and 270 degrees when its right side is to the top.
             * {@link #ORIENTATION_UNKNOWN} is returned when the device is close to flat
             * and the orientation cannot be determined.
             *
             * @param orientation The new orientation of the device.
             *
             *  @see #ORIENTATION_UNKNOWN
             */
            @Override
            public void onOrientationChanged(int orientation) {
                if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN ||
                        mDisplay == null) {
                    return;
                }
                final int rotation = mDisplay.getRotation();
                if (lastOrientation != rotation) {
                    lastOrientation = rotation;
                    dispatchOrientationChanged(DISPLAY_ORIENTATIONS.get(rotation));
                }
            }
        };
    }

    /**
     * Method which provide the enable of the {@link Display}
     *
     * @param display instance of the {@link Display}
     */
    public void enable(Display display) {
        mDisplay = display;
        mOrientationEventListener.enable();
        // Immediately dispatch the first callback
        dispatchOrientationChanged(DISPLAY_ORIENTATIONS.get(display.getRotation()));
    }

    /**
     * Method which provide the disable of the {@link Display}
     */
    public void disable() {
        mOrientationEventListener.disable();
        mDisplay = null;
    }

    /**
     * Method which provide the getting of the {@link Integer} value of the previous orientation
     *
     * @return {@link Integer} value of the previous orientation
     */
    public int getPreviousOrientation() {
        return previousOrientation;
    }

    /**
     * Method which provide the dispatch orientation changed
     *
     * @param orientation {@link Integer} value of the orientation changed
     */
    protected void dispatchOrientationChanged(int orientation) {
        previousOrientation = orientation;
        onOrientationChanged(orientation);
    }

    /**
     * Called when display orientation is changed.
     *
     * @param orientation One of 0, 90, 180, and 270.
     */
    public abstract void onOrientationChanged(int orientation);

}
