package com.example.TradeBoot.trade.tradeloop;

import com.example.TradeBoot.api.extentions.ParseToJsonException;
import com.example.TradeBoot.api.extentions.ParseToModelException;
import com.example.TradeBoot.api.extentions.RequestExcpetions.*;
import com.example.TradeBoot.notification.EMessageType;
import com.example.TradeBoot.notification.INotificationService;
import com.example.TradeBoot.trade.model.*;
import com.example.TradeBoot.trade.tradeloop.interfaces.ICloseOrders;
import com.example.TradeBoot.trade.tradeloop.interfaces.ITradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalTradeLoop {

    static final Logger log =
            LoggerFactory.getLogger(LocalTradeLoop.class);

    public LocalTradeLoop(WorkStatus globalWorkStatus, ITradeService tradeService, ICloseOrders closeOrders, INotificationService notificationServices) {
        this.globalWorkStatus = globalWorkStatus;
        this.tradeService = tradeService;
        this.closeOrders = closeOrders;
        this.notificationServices = notificationServices;
    }

    private final WorkStatus globalWorkStatus;

    private final ITradeService tradeService;

    private final ICloseOrders closeOrders;

    private final INotificationService notificationServices;

    final int MAX_CLOSE_ATTEMPTS_COUNT = 5;

    final int DEFAULT_SLEEP_TIME_MS = 100;


    public void runTrade() {
        ETradeState state = ETradeState.TRADE;
        boolean localWorkStatus = true;
        boolean isNeedThrow = false;
        int closeAttemptsCount = 1;
        long sleepAfterCloseOrdersInMS = 0;

        while (localWorkStatus) {
            try {
                switch (state) {
                    case TRADE -> {
                        var isWorkedAtLeastOnce = tradeService.trade();
                        if (isWorkedAtLeastOnce) {
                            log.debug("Close orders");
                        } else {
                            localWorkStatus = false;
                        }

                    }
                    case CLOSE_ORDERS -> {

                        if (closeAttemptsCount <= MAX_CLOSE_ATTEMPTS_COUNT) {
                            closeAttemptsCount++;
                            log.debug(
                                    String.format("Close attempts %d", --closeAttemptsCount)
                            );

                            closeOrders.close();

                        } else {
                            isNeedThrow = true;
                        }
                        localWorkStatus = false;
                    }
                }
            } catch (UnexpectedErrorException | RetryRequestException e) {
                sleep((long) (closeAttemptsCount * Math.pow(closeAttemptsCount, 2.45)) + 3);
                sleepAfterCloseOrdersInMS = 30;

            } catch (OrderAlreadyQueuedForCancellationException e) {
                sleep(closeAttemptsCount * 300L);

            } catch (DoNotSendMoreThanException e) {
                sleep(DEFAULT_SLEEP_TIME_MS);

            } catch (UnknownErrorRequestByFtxException e) {
                globalWorkStatus.setNeedStop(true);
                log.error("UnknownErrorRequestByFtxExceptionMessage: " + e.getMessage(), e);
                notificationServices.sendMessage(EMessageType.API_UNKNOWN_ERROR, e.getMessage());
                sleep(1000);

            } catch (UnceckedIOException | BadRequestByFtxException e) {
                sleep(closeAttemptsCount * 150L);

            } catch (ParseToModelException | ParseToJsonException e) {
                globalWorkStatus.setNeedStop(true);
                var message = e.getClass().getSimpleName();
                log.error("ExceptionMessage: " + message, e);
                notificationServices.sendMessage(EMessageType.CONVERT_EXCEPTION, message);
                sleep(DEFAULT_SLEEP_TIME_MS);

            } catch (Exception e) {
                globalWorkStatus.setNeedStop(true);
                log.error("ExceptionMessage: " + e.getMessage(), e);
                notificationServices.sendMessage(EMessageType.INTERNAL_ERROR, e.getMessage());
                sleep((long) (closeAttemptsCount * Math.pow(closeAttemptsCount, 2.45)) + 3);

            } finally {
                state = ETradeState.CLOSE_ORDERS;
            }
        }

        if (isNeedThrow) {
            var errorMessage = closeAttemptsCount + " attempts to close orders ended in failure";
            notificationServices.sendMessage(EMessageType.TROUBLE_CLOSING_ORDERS, errorMessage);
            throw new UnknownErrorRequestByFtxException(errorMessage);
        }

        if (sleepAfterCloseOrdersInMS > 0) {
            sleep(sleepAfterCloseOrdersInMS);
        }
    }

    enum ETradeState {
        TRADE,
        CLOSE_ORDERS,
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            log.error("SleepErrorMessage: " + ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }

}
