package com.artlite.tabbarlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.artlite.bslibrary.ui.view.BSView;

/**
 * Method which provide the segmentation control
 */
public final class AppleSegmentationView extends BSView {

    /**
     * Constructor which provide the create {@link BSView} from
     *
     * @param context instance of {@link Context}
     */
    public AppleSegmentationView(Context context) {
        super(context);
    }

    /**
     * Constructor which provide the create {@link BSView} from
     *
     * @param context instance of {@link Context}
     * @param attrs   instance of {@link AttributeSet}
     */
    public AppleSegmentationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Constructor which provide the create {@link BSView} from
     *
     * @param context      instance of {@link Context}
     * @param attrs        instance of {@link AttributeSet}
     * @param defStyleAttr attribute style
     */
    public AppleSegmentationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Method which provide the getting of the layout ID
     *
     * @return layout ID
     */
    @Override
    protected int getLayoutId() {
        return 0;
    }

    /**
     * Method which provide interface linking
     */
    @Override
    protected void onLinkInterface() {

    }

    /**
     * Method which provide the creating of the {@link View}
     */
    @Override
    protected void onCreateView() {

    }
}
