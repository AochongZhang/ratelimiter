package ratelimiter;

import util.TimeUtils;

/**
 * @author Aochong Zhang
 * @date 2020-09-15 23:03
 */
public class TokenBucketRateLimiter implements RateLimiter {
    /** 桶容量 */
    private int capacity;

    /** 当前令牌数量 */
    private int tokens;

    /** 令牌放入速度 个/s */
    private int rate;

    /** 上次放入令牌时间 */
    private long lastTime;

    public TokenBucketRateLimiter(int capacity, int rate) {
        this.capacity = capacity;
        this.rate = rate;
        this.tokens = 0;
        this.lastTime = System.currentTimeMillis();
    }

    @Override
    public boolean check() {
        long currentTime = System.currentTimeMillis();
        tokens = Math.min(capacity, tokens + (int) (currentTime - lastTime) * rate / 1000);
        lastTime = currentTime;
        if (tokens >= 1) {
            tokens--;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "令牌桶限流 上次放入令牌时间:" + TimeUtils.timestampToDateString(lastTime)
                + " 当前时间:" + TimeUtils.timestampToDateString(System.currentTimeMillis())
                + " 令牌桶容量:" + capacity
                + " 当前令牌数量:" + tokens
                + " 放入令牌速度:" + rate + "个/s";
    }
}
