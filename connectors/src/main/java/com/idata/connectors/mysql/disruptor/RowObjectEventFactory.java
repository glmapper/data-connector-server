package com.idata.connectors.mysql.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * <p>
 *  todo desc
 * </p>
 *
 * @author mailto:glmapper_2018@163.com glmapper
 * @date 2023/6/13 10:35 AM
 * @since 1.0
 */
public class RowObjectEventFactory implements EventFactory<RowObjectEvent> {
    @Override
    public RowObjectEvent newInstance() {
        return new RowObjectEvent();
    }
}
