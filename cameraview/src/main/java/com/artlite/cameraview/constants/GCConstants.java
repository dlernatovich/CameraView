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


import com.artlite.cameraview.models.GCAspectRatio;

/**
 * Interface which provide the constants
 * IMPLEMENTATION NON USE OF THIS CLASS USE THE {@link com.artlite.cameraview.common.GCCameraView}
 */
public interface GCConstants {

    /**
     * Instance of the {@link GCAspectRatio}
     */
    GCAspectRatio DEFAULT_ASPECT_RATIO = GCAspectRatio.of(4, 3);

    /**
     * {@link Integer} value of the facing
     */
    int FACING_BACK = 0;

    /**
     * {@link Integer} value of the facing
     */
    int FACING_FRONT = 1;

    /**
     * {@link Integer} value of the flash
     */
    int FLASH_OFF = 0;

    /**
     * {@link Integer} value of the flash
     */
    int FLASH_ON = 1;

    /**
     * {@link Integer} value of the flash
     */
    int FLASH_TORCH = 2;

    /**
     * {@link Integer} value of the flash
     */
    int FLASH_AUTO = 3;

    /**
     * {@link Integer} value of the flash
     */
    int FLASH_RED_EYE = 4;

    /**
     * {@link Integer} value of the landscape
     */
    int LANDSCAPE_90 = 90;

    /**
     * {@link Integer} value of the landscape
     */
    int LANDSCAPE_270 = 270;
}
