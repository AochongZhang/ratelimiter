package ratelimiter;

/**
 * @author Aochong Zhang
 * @date 2020-09-15 20:52
 */
public class RateLimiterDemo {
    public static void main(String[] args) throws InterruptedException {
//        fixedWindowRateLimiterDemo();
//        slidingWindowRateLimiterDemo();
        leakyBucketRateLimiterDemo();
    }

    public static void fixedWindowRateLimiterDemo() throws InterruptedException {
        FixedWindowRateLimiter fixedWindowRateLimiter = new FixedWindowRateLimiter(10, 10 * 1000);
        System.out.println(fixedWindowRateLimiter);
        Thread.sleep(9 * 1000);
        for (int i = 0; i < 10; i++) {
            System.out.println("不限流 " + fixedWindowRateLimiter.check());
            System.out.println(fixedWindowRateLimiter);
            Thread.sleep(100);
        }
        for (int i = 0; i < 10; i++) {
            System.out.println("不限流 " + fixedWindowRateLimiter.check());
            System.out.println(fixedWindowRateLimiter);
            Thread.sleep(100);
        }
    }

    public static void slidingWindowRateLimiterDemo() throws InterruptedException {
        SlidingWindowRateLimiter slidingWindowRateLimiter = new SlidingWindowRateLimiter(10, 5, 10 * 1000);
        Thread rollingWindowThread = new Thread(slidingWindowRateLimiter);
        rollingWindowThread.setDaemon(true);
        rollingWindowThread.start();

        System.out.println(slidingWindowRateLimiter);
        Thread.sleep(9000);
        for (int i = 0; i < 10; i++) {
            System.out.println("不限流 " + slidingWindowRateLimiter.check());
            System.out.println(slidingWindowRateLimiter);
            Thread.sleep(100);
        }
        for (int i = 0; i < 10; i++) {
            System.out.println("限流 " + slidingWindowRateLimiter.check());
            System.out.println(slidingWindowRateLimiter);
            Thread.sleep(100);
        }
    }

    public static void leakyBucketRateLimiterDemo() throws InterruptedException {
        LeakyBucketRateLimiter leakyBucketRateLimiter = new LeakyBucketRateLimiter(10, 1);
        System.out.println(leakyBucketRateLimiter);
        for (int i = 0; i < 10; i++) {
            System.out.println("不限流 " + leakyBucketRateLimiter.check());
            System.out.println(leakyBucketRateLimiter);
        }
        System.out.println("限流 " + leakyBucketRateLimiter.check());
        System.out.println(leakyBucketRateLimiter);
        Thread.sleep(2000);
        System.out.println("不限流 " + leakyBucketRateLimiter.check());
        System.out.println(leakyBucketRateLimiter);
        System.out.println("不限流 " + leakyBucketRateLimiter.check());
        System.out.println(leakyBucketRateLimiter);
        System.out.println("限流 " + leakyBucketRateLimiter.check());
        System.out.println(leakyBucketRateLimiter);
    }
}
