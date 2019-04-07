package com.nexuslink.alphrye;

/**
 * RecyclerView的数据Model的基类
 * @author Yuanrui
 * @date 2019/3/31
 */
public abstract class SimpleModel {
    /**
     * 卡片类型标志
     */
    public int mCardType;

    /**
     * 标志不能被长按拖动
     */
    public boolean mImmobile;

    public SimpleModel() {
        mCardType = getCardType();
    }

    /**
     * 获取卡片类型
     * 对应CardConstant中类型
     * @return
     */
    protected abstract int getCardType();

    /**
     * 对应Item
     * @return
     */
    protected abstract SimpleItem createItem();
}
