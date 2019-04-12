package com.nexuslink.alphrye.model;

import com.amap.api.services.help.Tip;
import com.nexuslink.alphrye.CardConstant;
import com.nexuslink.alphrye.SimpleItem;
import com.nexuslink.alphrye.SimpleModel;
import com.nexuslink.alphrye.item.SearchTipItem;

public class SearchTipModel extends SimpleModel{
    public Tip mTip;

    public SearchTipModel(Tip tip) {
        this.mTip = tip;
    }

    @Override
    protected int getCardType() {
        return CardConstant.TYPE_SEARCH_TIPS;
    }

    @Override
    protected SimpleItem createItem() {
        return new SearchTipItem();
    }
}
