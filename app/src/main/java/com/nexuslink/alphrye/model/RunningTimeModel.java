package com.nexuslink.alphrye.model;

import com.nexuslink.alphrye.CardConstant;
import com.nexuslink.alphrye.SimpleItem;
import com.nexuslink.alphrye.SimpleModel;
import com.nexuslink.alphrye.item.RunningTimeItem;

public class RunningTimeModel extends SimpleModel {

    public String mTitle;

    public RunningTimeModel(String mTitle) {
        this.mTitle = mTitle;
    }

    @Override
    protected int getCardType() {
        return CardConstant.TYPE_RUNNING_TIME;
    }

    @Override
    protected SimpleItem createItem() {
        return new RunningTimeItem();
    }
}
