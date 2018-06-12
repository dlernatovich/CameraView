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

package com.artlite.cameraview.constants;

import android.support.annotation.NonNull;

/**
 * Immutable class for describing width and height dimensions in pixels
 * IMPLEMENTATION NON USE OF THIS CLASS USE THE {@link com.artlite.cameraview.common.GCCameraView}
 */
public class GCSize implements Comparable<GCSize> {

    /**
     * {@link Integer} value of the width
     */
    private final int width;

    /**
     * {@link Integer} value of the width
     */
    private final int height;

    /**
     * Create a new immutable GCSize instance.
     *
     * @param width  The width of the size, in pixels
     * @param height The height of the size, in pixels
     */
    public GCSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Method which provide the getting {@link Integer} value of the width
     *
     * @return {@link Integer} value of the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Method which provide the getting {@link Integer} value of the height
     *
     * @return {@link Integer} value of the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Method which provide the equaling of the {@link Object}
     *
     * @param object instance of the {@link Object}
     * @return {@link Boolean} value of the equaling
     */
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (object instanceof GCSize) {
            GCSize size = (GCSize) object;
            return width == size.width && height == size.height;
        }
        return false;
    }

    /**
     * Method which provide the converting the {@link GCSize} to the {@link String}
     *
     * @return instance of the {@link String}
     */
    @Override
    public String toString() {
        return width + "x" + height;
    }

    /**
     * Method which provide the {@link Integer} value of the hash code
     *
     * @return {@link Integer} value of the hash code
     */
    @Override
    public int hashCode() {
        // assuming most sizes are <2^16, doing a rotate will give us perfect hashing
        return height ^ ((width << (Integer.SIZE / 2)) | (width >>> (Integer.SIZE / 2)));
    }

    /**
     * Method which provide the compare to functional
     *
     * @param another instance of the {@link GCSize}
     * @return {@link Boolean} value of the comparing
     */
    @Override
    public int compareTo(@NonNull GCSize another) {
        return width * height - another.width * another.height;
    }

}
