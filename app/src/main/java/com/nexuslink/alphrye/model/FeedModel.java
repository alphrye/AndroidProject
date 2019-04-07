package com.nexuslink.alphrye.model;

import com.nexuslink.alphrye.CardConstant;
import com.nexuslink.alphrye.SimpleItem;
import com.nexuslink.alphrye.SimpleModel;
import com.nexuslink.alphrye.item.FeedItem;

public class FeedModel extends SimpleModel {
    @Override
    protected int getCardType() {
        return CardConstant.TYPE_FEED;
    }

    @Override
    protected SimpleItem createItem() {
        return new FeedItem();
    }
}
