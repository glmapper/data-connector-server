package com.idata.connectors.mysql.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import javax.sql.DataSource;
import java.util.List;


/**
 * <p>
 * RowObjectManager
 * </p>
 *
 * @author mailto:glmapper_2018@163.com glmapper
 * @date 2023/6/13 10:38 AM
 * @since 1.0
 */
public class RowObjectManager<T> {

    private final DataSource targetDataSource;

    private final Disruptor<RowObjectEvent> disruptor;

    private RingBuffer<RowObjectEvent> ringBuffer;

    private final RowObjectThreadFactory threadFactory = new RowObjectThreadFactory();

    private boolean allowDiscard = false;

    public RowObjectManager(int queueSize, DataSource targetDataSource) {
        this.targetDataSource = targetDataSource;
        // 使用这个计算来保证realQueueSize是2的次幂（返回当前 大于等于queueSize的最小的2的次幂数 ）
        int realQueueSize = 1 << (32 - Integer.numberOfLeadingZeros(queueSize - 1));
        //构建disruptor，使用的是 ProducerType.MULTI
        //等待策略是 BlockingWaitStrategy
        this.disruptor = new Disruptor<RowObjectEvent>(new RowObjectEventFactory(),
                realQueueSize, threadFactory, ProducerType.MULTI, new BlockingWaitStrategy());

        //绑定消费者
        RowObjectEventHandler consumer = new RowObjectEventHandler(this.targetDataSource);
        this.disruptor.handleEventsWith(consumer);
    }

    /**
     * 启动 disruptor
     *
     * @param workerName
     */
    public void start(final String workerName) {
        this.threadFactory.setWorkName(workerName);
        this.ringBuffer = this.disruptor.start();
    }

    /**
     * 添加到 disruptor 队列
     *
     * @param rowObjList
     * @return
     */
    public boolean pushToQueue(List<T> rowObjList) {
        long sequence = 0L;
        if (allowDiscard) {
            try {
                sequence = ringBuffer.tryNext();
            } catch (Throwable e) {
                e.printStackTrace();
                return false;
            }
        } else {
            sequence = ringBuffer.next();
        }
        try {
            RowObjectEvent event = ringBuffer.get(sequence);
            event.setRowObject(rowObjList);
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
        ringBuffer.publish(sequence);
        return true;
    }
}
