package com.example.TradeBoot.trade.tradeloop;

import com.example.TradeBoot.api.extentions.RequestExcpetions.Uncecked.*;
import com.example.TradeBoot.trade.model.*;
import com.example.TradeBoot.trade.tradeloop.interfaces.ICloseOrders;
import com.example.TradeBoot.trade.tradeloop.interfaces.ITradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalTradeLoop {

    static final Logger log =
            LoggerFactory.getLogger(LocalTradeLoop.class);

    public LocalTradeLoop(ITradeService tradeService, ICloseOrders closeOrders, WorkStatus globalWorkStatus) {
        this.tradeService = tradeService;
        this.closeOrders = closeOrders;
        this.globalWorkStatus = globalWorkStatus;
    }

    //generall

    private WorkStatus globalWorkStatus;

    private ITradeService tradeService;

    private ICloseOrders closeOrders;

    final int MAX_CLOSE_ATTEMPTS_COUNT = 5;


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
                       var isWorkedAtLeastOnce =  tradeService.trade();
                       if(isWorkedAtLeastOnce){
                           log.debug("Close orders");
                       }
                       else {
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
                //4 9
                sleep((long) (closeAttemptsCount * Math.pow(closeAttemptsCount, 2.45)) + 3);
                sleepAfterCloseOrdersInMS = 30;
            } catch (OrderAlreadyQueuedForCancellationException e) {
                sleep(closeAttemptsCount * 300);
            }  catch(DoNotSendMoreThanExeption e){
                log.error(e.getMessage());
                sleep(100);
            } catch (UnknownErrorRequestByFtxException e) {
                globalWorkStatus.setNeedStop(true);
                log.error(e.getMessage(), e);
                sleep(1000);
            } catch (UnceckedIOException | BadRequestByFtxException e) {
                sleep(closeAttemptsCount * 150);

            }
            catch (Exception e){
                globalWorkStatus.setNeedStop(true);
                log.error(e.getMessage(), e);
                sleep((long) (closeAttemptsCount * Math.pow(closeAttemptsCount, 2.45)) + 3);
            }
            finally {
                state = ETradeState.CLOSE_ORDERS;
            }
        }

        if (isNeedThrow) {
            throw new UnknownErrorRequestByFtxException(closeAttemptsCount + " attempts to close orders ended in failure");
        }

        if(sleepAfterCloseOrdersInMS > 0){
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
            log.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

}
