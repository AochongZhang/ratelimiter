package ratelimiter;

import util.TimeUtils;

/**
 * 限流 漏桶算法
 *
 *   |  |  |  |  |  |
 *   v  v  v  v  v  v
 * +------------------+
 * |   leaky bucket   |
 * |                  |
 * |                  |
 * |      water       |
 * +------------------+
 *       |  |  |
 *       v  v  v
 *
 * @author Aochong Zhang
 * @date 2020-09-15 21:13
 */
public class LeakyBucketRateLimiter implements RateLimiter {
    /** 桶容量 */
    private int capacity;

    /** 当前水量 */
    private int water;

    /** 漏出速度 个/s */
    private int rate;

    /** 上次进水时间 */
    private long lastTime;

    public LeakyBucketRateLimiter(int capacity, int rate) {
        this.capacity = capacity;
        this.rate = rate;
        this.water = 0;
        this.lastTime = System.currentTimeMillis();
    }

    @Override
    public boolean check() {
        long currentTime = System.currentTimeMillis();
        // 计算当前水量
        water = Math.max(0, (int) (water - (currentTime - lastTime) * rate / 1000));
        lastTime = currentTime;
        if (water < capacity) {
            water++;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "漏桶限流 上次进水时间:" + TimeUtils.timestampToDateString(lastTime)
                + " 当前时间:" + TimeUtils.timestampToDateString(System.currentTimeMillis())
                + " 漏桶容量:" + capacity
                + " 当前水量:" + water
                + " 漏水速度:" + rate + "个/s";
    }
}
