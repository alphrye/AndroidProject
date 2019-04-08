package com.nexuslink.alphrye.model;

import com.nexuslink.alphrye.CardConstant;
import com.nexuslink.alphrye.SimpleItem;
import com.nexuslink.alphrye.SimpleModel;
import com.nexuslink.alphrye.item.FeedItem;

public class FeedModel extends SimpleModel {

    public FeedUserBean user;
    public FeedContentBean content;
    public String update_time;

    @Override
    protected int getCardType() {
        return CardConstant.TYPE_FEED;
    }

    @Override
    protected SimpleItem createItem() {
        return new FeedItem();
    }

    public static class FeedUserBean {

        public String user_name;
        public String user_avatar;
    }

    public static class FeedContentBean {

        public String text;
    }
}
