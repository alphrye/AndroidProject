package com.nexuslink.alphrye;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.View;

public class EquidistantDecoration extends RecyclerView.ItemDecoration {

    private static final String TYPE_SPACE_BOTH = "ItemSpace";
    private static final String TYPE_SPACE_HORIZONTAL = "HorizontalItemSpace";
    private static final String TYPE_SPACE_VERTICLE = "VerticleItemSpace";

    /**
     * 水平间距
     */
    public int mItemHorizontalSpace;

    /**
     * 垂直间距
     */
    public int mItemVerticleSpace;

    /**
     * 列数
     */
    public int columnCount;

    /**
     * 左边距百分比表（右边距反着取）
     */
    private List<Float> mMarginPercentList;

    /**
     * 水平竖直不等间距构造
     * @param columnCount
     * @param horizontalItemSpace
     * @param verticleItemSpace
     */
    public EquidistantDecoration( int columnCount, int horizontalItemSpace, int verticleItemSpace) {
        if (!isColumnCountValid(columnCount)
                || !isItemSpaceValid(horizontalItemSpace, TYPE_SPACE_HORIZONTAL)
                || !isItemSpaceValid(horizontalItemSpace, TYPE_SPACE_VERTICLE)) {
            return;
        }
        this.mItemHorizontalSpace = horizontalItemSpace;
        this.mItemVerticleSpace = verticleItemSpace;
        init(columnCount);
    }

    /**
     * 水平竖直等间距构造
     * @param itemSpace
     * @param columnCount
     */
    public EquidistantDecoration(int columnCount, int itemSpace) {
        if (!isColumnCountValid(columnCount)
                || !isItemSpaceValid(itemSpace, TYPE_SPACE_BOTH)) {
            return;
        }
        this.mItemHorizontalSpace = itemSpace;
        this.mItemVerticleSpace = itemSpace;
        init(columnCount);
    }

    private void init(int count) {
        this.columnCount = count;
        mMarginPercentList = new ArrayList<>();
        createMarginPercentList(count);
    }

    /**
     * 创建百分比表
     * @param count
     */
    private void createMarginPercentList(int count) {
        mMarginPercentList.add(0f);
        float spacePercent = 1 - 1.0f / columnCount;
        for (int pos = 1; pos < count; pos++) {
            float preMarginLeftPercent = mMarginPercentList.get(pos - 1);
            mMarginPercentList.add(spacePercent - preMarginLeftPercent);
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        int curColumn = pos % columnCount;
        outRect.left = (int) (mMarginPercentList.get(curColumn) * mItemHorizontalSpace);
        outRect.right = (int) (mMarginPercentList.get(columnCount - 1 - curColumn) * mItemHorizontalSpace);
        outRect.bottom = mItemHorizontalSpace;
    }

    /**
     * 检查列数有效性
     * @param count
     * @return
     */
    private boolean isColumnCountValid (int count) {
        if (count <= 0) {
            throw new RuntimeException("columnCount must > 0");
        }
        return true;
    }

    /**
     * 检查间距有效性
     * @param space
     * @param type
     * @return
     */
    private boolean isItemSpaceValid (int space, String type) {
        if (space < 0) {
            throw new RuntimeException(type + " must >= 0");
        }
        return true;
    }
}
