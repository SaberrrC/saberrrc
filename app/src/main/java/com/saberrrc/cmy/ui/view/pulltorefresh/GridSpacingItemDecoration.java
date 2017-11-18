package com.saberrrc.cmy.ui.view.pulltorefresh;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int     spanCount;
    private int     spacingH;
    private int     spacingV;
    private boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this(spanCount, spacing, spacing, includeEdge);
    }

    public GridSpacingItemDecoration(int spanCount, int spacingH, int spacingV, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacingH = spacingH;
        this.spacingV = spacingV;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (spacingV < 0 && spacingH < 0) {
            return;
        }
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            if (spacingV >= 0) {
                outRect.left = spacingV - column * spacingV / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacingV / spanCount; // (column + 1) * ((1f / spanCount) * spacing)
            }

            if (spacingH >= 0) {
                if (position < spanCount) { // top edge
                    outRect.top = spacingH;
                }
                outRect.bottom = spacingH; // item bottom
            }
        } else {
            if (spacingV >= 0) {
                outRect.left = column * spacingV / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacingV - (column + 1) * spacingV / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            }
            if (spacingH >= 0) {
                if (position >= spanCount) {
                    outRect.top = spacingH; // item top
                }
            }
        }
    }
}