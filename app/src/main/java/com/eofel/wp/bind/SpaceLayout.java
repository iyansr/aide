package com.eofel.wp.bind;

import android.support.v7.widget.GridLayoutManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

public class SpaceLayout extends GridLayoutManager {
	
	private int columnWidth;
	private boolean hashChanged = true;

	public SpaceLayout(Context context, int ColumnWidth) {
		super(context, 1);
		setColumnWidth(ColumnWidth);
	}

	public void setColumnWidth(int mColumnWidth) {
		if (mColumnWidth > 0 && mColumnWidth != columnWidth) {
			columnWidth = mColumnWidth;
			hashChanged = true;
		}
	}

	@Override
	public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {

		if (hashChanged && columnWidth > 0) {
			int totalSpace;
            if (getOrientation() == VERTICAL) {
                totalSpace = getWidth() - getPaddingRight() - getPaddingLeft();
            } else {
                totalSpace = getHeight() - getPaddingTop() - getPaddingBottom();
            }
            int spanCount = Math.max(1, totalSpace / columnWidth);
            setSpanCount(spanCount);
            hashChanged = false;
		}
		super.onLayoutChildren(recycler, state);

	}
	
}
