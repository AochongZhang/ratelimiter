package ratelimiter;

import util.TimeUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 限流 固定窗口计数器算法
 *
 *  固定窗口问题
 *  限制每秒100次，1.5到2.5中出现200次
 *
 *  +-----------+-----------+
 *  |           |           |
 *  |           |           |
 *  |        100|100        |
 *  |           |           |
 *  |           |           |
 *  +-----------+-----------+
 *
 *  |     |     |     |     |
 *  +-----+-----+-----+-----+
 * 1s    1.5s   2s   2.5s   3s
 *
 * @author Aochong Zhang
 * @date 2020-09-15 15:26
 */
public class FixedWindowRateLimiter implements RateLimiter {
    /** 限流数量 */
    private int limit;

    /** 限流时间内请求数量 */
    private AtomicInteger count = new AtomicInteger(0);

    /** 限流时间 */
    private long timeInterval;

    /** 开始时间 */
    private long startTime;

    public FixedWindowRateLimiter(int limit, long timeInterval) {
        this.limit = limit;
        this.timeInterval = timeInterval;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public boolean check() {
        long currentTime = System.currentTimeMillis();
        // 超过限制时间
        if (currentTime - startTime > timeInterval) {
            count = new AtomicInteger(1);
            startTime = currentTime;
            return true;
        }
        // 在限制时间内, 判断请求数量
        if (count.get() < limit) {
            count.incrementAndGet();
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "固定窗口计数器限流 限流开始时间:" + TimeUtils.timestampToDateString(startTime)
                + " 当前时间:" + TimeUtils.timestampToDateString(System.currentTimeMillis())
                + " 限制数量:" + limit
                + " 当前数量:" + count;
    }
}
