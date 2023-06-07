package com.idata.core;

/**
 * 集成底层所有组件，然后触发同步行为
 */
public abstract class Syncer implements Runnable {

    @Override
    public void run() {
        this.sync();
    }

    public abstract void sync();

}
