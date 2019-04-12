package com.nexuslink.alphrye.model;

import com.nexuslink.alphrye.CardConstant;
import com.nexuslink.alphrye.SimpleItem;
import com.nexuslink.alphrye.SimpleModel;
import com.nexuslink.alphrye.item.RunningDataItem;

public class RunningDataModel extends SimpleModel {

    public String mTitle;

    public String mData;

    public RunningDataModel(String mTitle, String mData) {
        this.mTitle = mTitle;
        this.mData = mData;
    }

    @Override
    protected int getCardType() {
        return CardConstant.TYPE_RUNNING_DATA;
    }

    @Override
    protected SimpleItem createItem() {
        return new RunningDataItem();
    }
}
