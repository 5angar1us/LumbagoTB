package com.example.TradeBoot.trade;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public final class ExtendedExecutor extends ThreadPoolExecutor {

    static final Logger log =
            LoggerFactory.getLogger(ExtendedExecutor.class);

    public ExtendedExecutor(int nThreads) {
        super(nThreads, // core threads
                nThreads, // max threads
                5L, // timeout
                TimeUnit.MINUTES, // timeout units
                new LinkedBlockingQueue<Runnable>() // work queue
        );
    }

    protected void afterExecute(Runnable runnable, Throwable throwable) {
        super.afterExecute(runnable, throwable);
        if (throwable == null && runnable instanceof Future<?>) {
            try {
                Future<?> future = (Future<?>) runnable;
                if (future.isDone()) {
                    future.get();
                }
            } catch (CancellationException ce) {
                throwable = ce;
            } catch (ExecutionException ee) {
                throwable = ee.getCause();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        if (throwable != null) {
            log.error(throwable.getMessage(), throwable);
        }
    }

}
