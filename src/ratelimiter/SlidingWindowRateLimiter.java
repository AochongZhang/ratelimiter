package ratelimiter;

import util.TimeUtils;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 限流 滑动窗口计数器算法
 *
 *       SlidingWindow ->
 *      +---------------+
 *      |               |
 *  +---+---+---+---+---+---+---+---+
 *  |   |   |   |   |   |   |   |   |
 *  |   |   |   |   |   |   |   |   |
 *  |   |   |   |   |   |   |   |   |
 *  |   |   |   |100|100|   |   |   |
 *  |   |   |   |   |   |   |   |   |
 *  |   |   |   |   |   |   |   |   |
 *  |   |   |   |   |   |   |   |   |
 *  +---+---+---+---+---+---+---+---+
 *
 *  |       |       |       |       |
 *  +-------+-------+-------+-------+
 *  1s     1.5s    2s     2.5s     3s
 *
 * @author Aochong Zhang
 * @date 2020-09-15 16:19
 */
public class SlidingWindowRateLimiter implements RateLimiter, Runnable {
    /** 限流数量 */
    private int limit;

    /** 滑动窗口 */
    private LinkedList<AtomicInteger> rollingWindow = new LinkedList<>();

    /** 滑动窗口大小 */
    private int rollingWindowSize;

    /** 限流时间 */
    private long timeInterval;

    public SlidingWindowRateLimiter(int limit, int rollingWindowSize, long timeInterval) {
        this.limit = limit;
        this.rollingWindowSize = rollingWindowSize;
        this.timeInterval = timeInterval;
        initWindow();
    }

    private void initWindow() {
        for (int i = 0; i < rollingWindowSize; i++) {
            rollingWindow.addLast(new AtomicInteger(0));
        }
    }

    @Override
    public boolean check() {
        if (getTotalCount() >= limit) {
            return false;
        }
        rollingWindow.getLast().getAndIncrement();
        return true;
    }

    private int getTotalCount() {
        return rollingWindow.stream().mapToInt(AtomicInteger::get).sum();
    }

    @Override
    public void run() {
        for (;;) {
            try {
                Thread.sleep(timeInterval / rollingWindowSize);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            rollingWindow.removeFirst();
            rollingWindow.addLast(new AtomicInteger(0));
        }
    }

    @Override
    public String toString() {
        return "滑动窗口计数器限流 滑动窗口:" + rollingWindow
                + " 当前时间:" + TimeUtils.timestampToDateString(System.currentTimeMillis())
                + " 限制数量:" + limit
                + " 当前数量:" + getTotalCount();
    }
}
