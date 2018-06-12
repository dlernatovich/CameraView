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

package com.artlite.cameraview.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;

import com.artlite.cameraview.constants.GCSize;

/**
 * Immutable class for describing proportional relationship between width and height.
 * IMPLEMENTATION NON USE OF THIS CLASS USE THE {@link com.artlite.cameraview.common.GCCameraView}
 */
public class GCAspectRatio implements Comparable<GCAspectRatio>, Parcelable {

    /**
     * Instance of the {@link SparseArrayCompat}
     */
    private final static SparseArrayCompat<SparseArrayCompat<GCAspectRatio>> sCache
            = new SparseArrayCompat<>(16);

    /**
     * {@link Integer} value of the x
     */
    private final int x;

    /**
     * {@link Integer} value of the y
     */
    private final int y;

    /**
     * Returns an instance of {@link GCAspectRatio} specified by {@code x} and {@code y} values.
     * The values {@code x} and {@code} will be reduced by their greatest common divider.
     *
     * @param x The width
     * @param y The height
     * @return An instance of {@link GCAspectRatio}
     */
    public static GCAspectRatio of(int x, int y) {
        int gcd = gcd(x, y);
        x /= gcd;
        y /= gcd;
        SparseArrayCompat<GCAspectRatio> arrayX = sCache.get(x);
        if (arrayX == null) {
            GCAspectRatio ratio = new GCAspectRatio(x, y);
            arrayX = new SparseArrayCompat<>();
            arrayX.put(y, ratio);
            sCache.put(x, arrayX);
            return ratio;
        } else {
            GCAspectRatio ratio = arrayX.get(y);
            if (ratio == null) {
                ratio = new GCAspectRatio(x, y);
                arrayX.put(y, ratio);
            }
            return ratio;
        }
    }

    /**
     * Parse an {@link GCAspectRatio} from a {@link String} formatted like "4:3".
     *
     * @param s The string representation of the aspect ratio
     * @return The aspect ratio
     * @throws IllegalArgumentException when the format is incorrect.
     */
    public static GCAspectRatio parse(String s) {
        int position = s.indexOf(':');
        if (position == -1) {
            throw new IllegalArgumentException("Malformed aspect ratio: " + s);
        }
        try {
            int x = Integer.parseInt(s.substring(0, position));
            int y = Integer.parseInt(s.substring(position + 1));
            return GCAspectRatio.of(x, y);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Malformed aspect ratio: " + s, e);
        }
    }

    /**
     * Constructor which provide the create of the {@link GCAspectRatio} with parameters
     *
     * @param x The width
     * @param y The height
     */
    private GCAspectRatio(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Method which provide the getting {@link Integer} value of the x
     *
     * @return {@link Integer} value of the x
     */
    public int getX() {
        return x;
    }

    /**
     * Method which provide the getting {@link Integer} value of the y
     *
     * @return {@link Integer} value of the y
     */
    public int getY() {
        return y;
    }

    /**
     * Method which provide the checking matches of the {@link GCSize}
     *
     * @param size instance of the {@link GCSize}
     * @return {@link Boolean} value if it match
     */
    public boolean matches(GCSize size) {
        int gcd = gcd(size.getWidth(), size.getHeight());
        int x = size.getWidth() / gcd;
        int y = size.getHeight() / gcd;
        return this.x == x && this.y == y;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param object the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj argument;
     */
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (object instanceof GCAspectRatio) {
            GCAspectRatio ratio = (GCAspectRatio) object;
            return x == ratio.x && y == ratio.y;
        }
        return false;
    }

    /**
     * Returns a string representation of the object. In general, the
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return x + ":" + y;
    }

    /**
     * Method which provide the convert {@link GCAspectRatio} to {@link Float}
     *
     * @return instance of the {@link Float}
     */
    public float toFloat() {
        return (float) x / y;
    }

    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hash tables such as those provided by
     * {@link java.util.HashMap}.
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        // assuming most sizes are <2^16, doing a rotate will give us perfect hashing
        return y ^ ((x << (Integer.SIZE / 2)) | (x >>> (Integer.SIZE / 2)));
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * @param another the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(@NonNull GCAspectRatio another) {
        if (equals(another)) {
            return 0;
        } else if (toFloat() - another.toFloat() > 0) {
            return 1;
        }
        return -1;
    }

    /**
     * Method which provide the inverse of the {@link GCAspectRatio}
     *
     * @return The inverse of this {@link GCAspectRatio}.
     */
    public GCAspectRatio inverse() {
        //noinspection SuspiciousNameCombination
        return GCAspectRatio.of(y, x);
    }

    /**
     * Method which provide the gcd action
     *
     * @param a instance of the {@link Integer}
     * @param b instance of the {@link Integer}
     * @return instance of the {@link Integer}
     */
    private static int gcd(int a, int b) {
        while (b != 0) {
            int c = b;
            b = a % b;
            a = c;
        }
        return a;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(x);
        dest.writeInt(y);
    }

    /**
     * Instance of the {@link Parcelable.Creator}
     */
    public static final Parcelable.Creator<GCAspectRatio> CREATOR
            = new Parcelable.Creator<GCAspectRatio>() {

        @Override
        public GCAspectRatio createFromParcel(Parcel source) {
            int x = source.readInt();
            int y = source.readInt();
            return GCAspectRatio.of(x, y);
        }

        @Override
        public GCAspectRatio[] newArray(int size) {
            return new GCAspectRatio[size];
        }
    };

}
