package com.nexuslink.alphrye.model;

import com.amap.api.track.query.entity.Track;
import com.nexuslink.alphrye.CardConstant;
import com.nexuslink.alphrye.SimpleItem;
import com.nexuslink.alphrye.SimpleModel;
import com.nexuslink.alphrye.item.RideHistoryItem;

public class RideHistoryModel extends SimpleModel {

    public Track mTrack;

    public RideHistoryModel(Track curTrack) {
        mTrack = curTrack;
    }

    @Override
    protected int getCardType() {
        return CardConstant.TYPE_RIDE_HISTORY;
    }

    @Override
    protected SimpleItem createItem() {
        return new RideHistoryItem();
    }
}
