package com.nexuslink.alphrye.model;

public class FlashlightChangeEvent {
    private boolean status;
    public FlashlightChangeEvent(boolean status){
        this.status=status;
    }
    public boolean getStatus() {
        return status;
    }
 
    public void setMessage(boolean status) {
        this.status = status;
    }
}