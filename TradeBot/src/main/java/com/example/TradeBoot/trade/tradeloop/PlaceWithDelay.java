package com.example.TradeBoot.trade.tradeloop;

import com.example.TradeBoot.api.domain.orders.OrderToPlace;
import com.example.TradeBoot.api.domain.orders.PlacedOrder;
import com.example.TradeBoot.api.services.IOrdersService;
import com.example.TradeBoot.trade.tradeloop.interfaces.IPlaceOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlaceWithDelay implements IPlaceOrder {



    static final Logger log =
            LoggerFactory.getLogger(PlaceWithDelay.class);

    public PlaceWithDelay(IOrdersService ordersService) {
        this.ordersService = ordersService;
    }

    private long lastRequestTime;

    private final long MINIMUM_DELAY_MS = 105;

    private IOrdersService ordersService;

    @Override
    public PlacedOrder place(OrderToPlace order) {
        var currentTime = System.currentTimeMillis();
        var delayBetweenLastRequest = currentTime - lastRequestTime;

        if (delayBetweenLastRequest < MINIMUM_DELAY_MS) {

            var sleepTime = MINIMUM_DELAY_MS - delayBetweenLastRequest;

            sleep(sleepTime);
        }

        var placedOrder = ordersService.placeOrder(order);

        lastRequestTime = System.currentTimeMillis();

        return placedOrder;
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
