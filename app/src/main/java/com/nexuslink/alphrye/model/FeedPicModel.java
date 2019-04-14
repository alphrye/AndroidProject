package com.nexuslink.alphrye.model;

import com.nexuslink.alphrye.CardConstant;
import com.nexuslink.alphrye.SimpleItem;
import com.nexuslink.alphrye.SimpleModel;
import com.nexuslink.alphrye.item.FeedPicItem;

public class FeedPicModel extends SimpleModel {

    public String pic_url;

    public FeedPicModel(String picUrl) {
        this.pic_url = picUrl;
    }

    @Override
    protected int getCardType() {
        return CardConstant.TYPE_FEED_PIC;
    }

    @Override
    protected SimpleItem createItem() {
        return new FeedPicItem();
    }
}
