package com.example.TradeBoot.trade.services;

import com.example.TradeBoot.api.services.IPositionsService;
import com.example.TradeBoot.api.services.IWalletService;

import java.math.BigDecimal;

public interface VolumeVisitor {
    BigDecimal getVolume(String marketName);

    public class FutureVolumeVisitor implements VolumeVisitor {

        IPositionsService positionsService;

        public FutureVolumeVisitor(IPositionsService iPositionsService) {
            positionsService = iPositionsService;
        }

        @Override
        public BigDecimal getVolume(String marketName) {
            var position = positionsService.getPositionByMarketOrTrow(marketName);
            var volume = position.getNetSize();
            return volume;
        }
    }

    public class CoinVolumeVisitor implements VolumeVisitor {

        private IWalletService walletService;

        public CoinVolumeVisitor(IWalletService walletService) {
            this.walletService = walletService;
        }

        @Override
        public BigDecimal getVolume(String marketName) {
            var balance = walletService.getBalanceByMarketOrTrow(marketName);
            var volume = balance.getTotal();
            return volume;
        }
    }
}
