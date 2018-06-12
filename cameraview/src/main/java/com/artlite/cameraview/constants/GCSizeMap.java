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

import android.support.v4.util.ArrayMap;

import com.artlite.cameraview.models.GCAspectRatio;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A collection class that automatically groups {@link GCSize}s by their {@link GCAspectRatio}s.
 * IMPLEMENTATION NON USE OF THIS CLASS USE THE {@link com.artlite.cameraview.common.GCCameraView}
 */
public class GCSizeMap {

    /**
     * Instance of the {@link ArrayMap}
     */
    protected final ArrayMap<GCAspectRatio, SortedSet<GCSize>> ratios = new ArrayMap<>();

    /**
     * Add a new {@link GCSize} to this collection.
     *
     * @param size The size to add.
     * @return {@code true} if it is added, {@code false} if it already exists and is not added.
     */
    public boolean add(GCSize size) {
        for (GCAspectRatio ratio : ratios.keySet()) {
            if (ratio.matches(size)) {
                final SortedSet<GCSize> sizes = ratios.get(ratio);
                if (sizes.contains(size)) {
                    return false;
                } else {
                    sizes.add(size);
                    return true;
                }
            }
        }
        // None of the existing ratio matches the provided size; add a new key
        SortedSet<GCSize> sizes = new TreeSet<>();
        sizes.add(size);
        ratios.put(GCAspectRatio.of(size.getWidth(), size.getHeight()), sizes);
        return true;
    }

    /**
     * Removes the specified aspect ratio and all sizes associated with it.
     *
     * @param ratio The aspect ratio to be removed.
     */
    public void remove(GCAspectRatio ratio) {
        ratios.remove(ratio);
    }

    /**
     * Method which provide the getting of the {@link Set} of the {@link GCAspectRatio}
     *
     * @return {@link Set} of the {@link GCAspectRatio}
     */
    public Set<GCAspectRatio> getRatios() {
        return ratios.keySet();
    }

    public SortedSet<GCSize> sizes(GCAspectRatio ratio) {
        return ratios.get(ratio);
    }

    public void clear() {
        ratios.clear();
    }

    public boolean isEmpty() {
        return ratios.isEmpty();
    }

}
