package com.app.checking.ui.messages_Student;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private Paint paint;

    public DividerItemDecoration() {
        paint = new Paint();
        paint.setColor(Color.BLACK); // Set the border color
        paint.setStrokeWidth(2); // Set the border thickness
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        int childCount = parent.getChildCount();

        // Loop through all child views in the RecyclerView
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            // Get the bottom and left edges of the child view
            int top = child.getBottom();
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            // Draw the border
            c.drawLine(left, top, right, top, paint);
        }
    }
}
