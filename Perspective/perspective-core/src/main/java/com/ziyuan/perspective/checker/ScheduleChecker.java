
package com.ziyuan.perspective.checker;

import com.ziyuan.perspective.Constants;
import com.ziyuan.perspective.invokes.Branch;
import com.ziyuan.perspective.invokes.Ender;
import com.ziyuan.perspective.invokes.Trace;
import com.ziyuan.perspective.storages.Storage;
import com.ziyuan.perspective.util.StorageUtil;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * ScheduleChecker 这个checker用来检查超时的Trace
 *
 * @author ziyuan
 * @since 2017-02-20
 */
public final class ScheduleChecker implements Checker {

    /**
     * storage结构
     */
    private final Storage storage = StorageUtil.getStorage();

    /**
     * 分十个线程去检查
     */
    private final static int GROUP_COUNT = Constants.CHECKER_THREAD_NUM;

    private final ExecutorService executorService = Executors.newFixedThreadPool(Constants.CHECKER_THREAD_NUM, new ThreadFactory() {
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "Check worker Thread");
            return t;
        }
    });

    private final ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(2, new ThreadFactory() {
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "ScheduleChecker Thread");
            return t;
        }
    });

    /**
     * 开始检查,这里47秒检查一次,主要是防止跟收集线程冲突
     */
    public void checkBegin() {
        scheduled.scheduleAtFixedRate(new Runnable() {
            public void run() {
                check();
            }
        }, 1, 47, TimeUnit.SECONDS);
    }

    /**
     * 检查
     */
    private void check() {
        final long now = System.currentTimeMillis();
        List<Trace> tracing = storage.getTracing();
        if (CollectionUtils.isEmpty(tracing)) {
            return;
        }

        int count = (tracing.size() + GROUP_COUNT - 1) / GROUP_COUNT;
        CountDownLatch countDownLatch = new CountDownLatch(GROUP_COUNT);

        //分组处理,十个线程去检查
        for (int index = 0; index < GROUP_COUNT; index++) {
            int startIndex = index * count;
            int endIndex = 0;
            if ((index + 1) * count > tracing.size()) {
                endIndex = tracing.size();
            } else {
                endIndex = (index + 1) * count;
            }
            final List<Trace> needCheck;
            if (startIndex < endIndex) {
                needCheck = tracing.subList(startIndex, endIndex);
            } else {
                needCheck = new ArrayList<Trace>();
            }
            executorService.submit(new CheckerThread(needCheck, now));
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            //TODO 这里记录日志
        }
    }

    /**
     * 结束检查
     */
    public void shutDown() {
        scheduled.shutdown();
        executorService.shutdown();
    }

    /**
     * 检查trace超时的线程
     */
    class CheckerThread extends Thread {

        /**
         * 需要检查的trace集合
         */
        private List<Trace> traces;

        /**
         * 检查的时间点
         */
        private long now;

        public CheckerThread(List<Trace> traces, long now) {
            this.traces = traces;
            this.now = now;
        }

        @Override
        public void run() {
            for (Trace trace : traces) {
                if (trace.checkTimeOut(now)) {
                    //检查到长时间不返回，直接给主线程添加一个ender，强行返回并移除，防止发生泄漏
                    Branch traceMain = trace.getMainBranch();
                    Ender timeOutEnder = new Ender("Time out Exception Ender", trace.getTraceId(), traceMain.getBranchId());
                    trace.endOneBranch(traceMain.getBranchId(), timeOutEnder);
                    storage.endOneTrace(trace);
                }
            }
        }
    }
}
