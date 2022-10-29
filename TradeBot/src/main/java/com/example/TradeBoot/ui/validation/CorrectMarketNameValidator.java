package com.example.TradeBoot.ui.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CorrectMarketNameValidator implements ConstraintValidator<CorrectMarketName, Object> {


    @Override
    public void initialize(CorrectMarketName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = false;

        if (object == null) { // This should be validated by the not null validator (@NotNull).
            isValid = true;
        } else if (object instanceof String) {
            String string = new String(object.toString());

            isValid = Arrays.stream(allNamesString.split(",")).anyMatch(name -> name.equals(string));

            if (!isValid) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate("Market name isn't contains in hard code coin/spot name list").addConstraintViolation();
            }
        }

        return isValid;
    }

    private String allNamesString = "1INCH-PERP,1INCH-1230,1INCH/USD,AAPL-1230,AAPL/USD,AAVE-PERP,AAVE-1230,AAVE/USD,AAVE/USDT,ABNB-1230,ABNB/USD,ACB-1230,ACB/USD,ADA-PERP,ADA-1230,AGLD-PERP,AGLD/USD,AKRO/USD,AKRO/USDT,ALCX-PERP,ALCX/USD,ALEPH/USD,ALGO-PERP,ALGO-1230,ALGO/USD,ALGO/USDT,ALICE-PERP,ALICE/USD,ALPHA-PERP,ALPHA/USD,ALT-PERP,ALT-1230,AMC-1230,AMC/USD,AMD-1230,AMD/USD,AMPL-PERP,AMPL/USD,AMPL/USDT,AMZN-1230,AMZN/USD,APE-PERP,APE-1230,APE/USD,APEAMC/USD,APHA/USD,APT-PERP,APT/USD,APT/USDT,AR-PERP,ARKK-1230,ARKK/USD,ASD-PERP,ASD/USD,ATLAS-PERP,ATLAS/USD,ATOM-PERP,ATOM-1230,ATOM/USD,ATOM/USDT,AUDIO-PERP,AUDIO/USD,AUDIO/USDT,AURY/USD,AVAX-PERP,AVAX-1230,AVAX/BTC,AVAX/JPY,AVAX/TRY,AVAX/USD,AVAX/USDT,AXS-PERP,AXS-1230,AXS/USD,BABA-1230,BABA/USD,BADGER-PERP,BADGER/USD,BAL-PERP,BAL-1230,BAL/USD,BAL/USDT,BAND-PERP,BAND/USD,BAO/USD,BAR/USD,BAT-PERP,BAT/JPY,BAT/USD,BB-1230,BB/USD,BCH-PERP,BCH-1230,BCH/BTC,BCH/JPY,BCH/USD,BCH/USDT,BICO/USD,BILI-1230,BILI/USD,BIT-PERP,BIT/USD,BITO-1230,BITO/USD,BITW-1230,BITW/USD,BLT/USD,BNB-PERP,BNB-1230,BNB/BTC,BNB/USD,BNB/USDT,BNT-PERP,BNT/USD,BNTX-1230,BNTX/USD,BOBA-PERP,BOBA/USD,BOLSONARO2022,BRZ-PERP,BRZ/USD,BRZ/USDT,BSV-PERP,BSV-1230,BTC-PERP,BTC-MOVE-1029,BTC-MOVE-1030,BTC-MOVE-WK-1104,BTC-MOVE-WK-1111,BTC-MOVE-WK-1118,BTC-MOVE-WK-1125,BTC-1230,BTC-MOVE-2022Q4,BTC-0331,BTC-MOVE-2023Q1,BTC-MOVE-2023Q2,BTC/AUD,BTC/BRZ,BTC/EUR,BTC/JPY,BTC/TRY,BTC/TRYB,BTC/USD,BTC/USDT,BTT-PERP,BTT/USD,BYND-1230,BYND/USD,C98-PERP,C98/USD,CAD/USD,CAKE-PERP,CEL-PERP,CEL-1230,CEL/BTC,CEL/USD,CELO-PERP,CGC-1230,CGC/USD,CHR-PERP,CHR/USD,CHZ-PERP,CHZ-1230,CHZ/USD,CHZ/USDT,CITY/USD,CLV-PERP,CLV/USD,COIN/USD,COMP-PERP,COMP-1230,COMP/USD,COMP/USDT,CONV/USD,COPE/USD,CQT/USD,CREAM-PERP,CREAM/USD,CREAM/USDT,CRO-PERP,CRO/USD,CRON-1230,CRON/USD,CRV-PERP,CRV/USD,CUSDT/USD,CUSDT/USDT,CVC-PERP,CVC/USD,CVX-PERP,CVX/USD,DAI/USD,DAI/USDT,DASH-PERP,DAWN-PERP,DAWN/USD,DEFI-PERP,DEFI-1230,DENT-PERP,DENT/USD,DFL/USD,DKNG-1230,DKNG/USD,DMG/USD,DMG/USDT,DODO-PERP,DODO/USD,DOGE-PERP,DOGE-1230,DOGE/BTC,DOGE/JPY,DOGE/USD,DOGE/USDT,DOT-PERP,DOT-1230,DOT/BTC,DOT/JPY,DOT/USD,DOT/USDT,DYDX-PERP,DYDX/USD,EDEN-PERP,EDEN/USD,EGLD-PERP,EMB/USD,ENJ-PERP,ENJ/JPY,ENJ/USD,ENS-PERP,ENS/USD,EOS-PERP,EOS-1230,ETC-PERP,ETH-PERP,ETH-1230,ETH-0331,ETH/AUD,ETH/BRZ,ETH/BTC,ETH/EUR,ETH/JPY,ETH/TRY,ETH/USD,ETH/USDT,ETHE-1230,ETHE/USD,ETHW-PERP,ETHW/USD,EUL/USD,EUR/USD,EURT/EUR,EURT/USD,EURT/USDT,EXCH-PERP,EXCH-1230,FB-1230,FB/USD,FIDA-PERP,FIDA/USD,FIDA/USDT,FIL-PERP,FIL-1230,FLM-PERP,FLOW-PERP,FLUX-PERP,FRONT/USD,FRONT/USDT,FTM-PERP,FTM-1230,FTM/USD,FTT-PERP,FTT/BTC,FTT/JPY,FTT/USD,FTT/USDT,FTXDXY-PERP,FXS-PERP,FXS/USD,GAL-PERP,GAL/USD,GALA-PERP,GALA/USD,GALFAN/USD,GARI/USD,GBP/USD,GBTC-1230,GBTC/USD,GDX-1230,GDX/USD,GDXJ-1230,GDXJ/USD,GENE/USD,GLD-1230,GLD/USD,GLMR-PERP,GLXY/USD,GME-1230,GME/USD,GMT-PERP,GMT-1230,GMT/USD,GMX/USD,GODS/USD,GOG/USD,GOOGL-1230,GOOGL/USD,GRT-PERP,GRT-1230,GRT/USD,GST-PERP,GST/TRY,GST/USD,GST/USDT,GT/USD,HBAR-PERP,HBB/USD,HGET/USD,HGET/USDT,HMT/USD,HNT-PERP,HNT/USD,HNT/USDT,HOLY-PERP,HOLY/USD,HOOD/USD,HOT-PERP,HT-PERP,HT/USD,HUM/USD,HXRO/USD,HXRO/USDT,ICP-PERP,ICX-PERP,IMX-PERP,IMX/USD,INDI/USD,INJ-PERP,INTER/USD,IOST-PERP,IOTA-PERP,IP3/USD,JASMY-PERP,JET/USD,JOE/USD,JPY-PERP,JST/USD,KAVA-PERP,KBTT-PERP,KBTT/USD,KIN/USD,KLAY-PERP,KLUNC-PERP,KNC-PERP,KNC/USD,KNC/USDT,KSHIB-PERP,KSHIB/USD,KSM-PERP,KSOS-PERP,KSOS/USD,LDO-PERP,LDO/USD,LEO-PERP,LEO/USD,LINA-PERP,LINA/USD,LINK-PERP,LINK-1230,LINK/BTC,LINK/USD,LINK/USDT,LOOKS-PERP,LOOKS/USD,LRC-PERP,LRC/USD,LTC-PERP,LTC-1230,LTC/BTC,LTC/JPY,LTC/USD,LTC/USDT,LUA/USD,LUA/USDT,LUNA2-PERP,LUNC-PERP,MAGIC/USD,MANA-PERP,MANA/USD,MAPS-PERP,MAPS/USD,MAPS/USDT,MATH/USD,MATH/USDT,MATIC-PERP,MATIC-1230,MATIC/BTC,MATIC/USD,MBS/USD,MCB/USD,MEDIA-PERP,MEDIA/USD,MER/USD,MID-PERP,MID-1230,MINA-PERP,MKR-PERP,MKR/JPY,MKR/USD,MKR/USDT,MNGO-PERP,MNGO/USD,MOB-PERP,MOB/USD,MOB/USDT,MPLX/USD,MRNA-1230,MRNA/USD,MSOL/USD,MSTR-1230,MSTR/USD,MTA/USD,MTA/USDT,MTL-PERP,MTL/USD,MVDA10-PERP,MVDA25-PERP,MYC/USD,NEAR-PERP,NEAR-1230,NEAR/USD,NEAR/USDT,NEO-PERP,NEXO/USD,NFLX-1230,NFLX/USD,NIO-1230,NIO/USD,NOK-1230,NOK/USD,NVDA-1230,NVDA/USD,OKB-PERP,OKB-1230,OKB/USD,OMG-PERP,OMG-1230,OMG/JPY,OMG/USD,ONE-PERP,ONT-PERP,OP-PERP,OP-1230,ORBS/USD,ORCA/USD,OXY-PERP,OXY/USD,OXY/USDT,PAXG-PERP,PAXG/USD,PAXG/USDT,PENN-1230,PENN/USD,PEOPLE-PERP,PEOPLE/USD,PERP-PERP,PERP/USD,PFE-1230,PFE/USD,POLIS-PERP,POLIS/USD,PORT/USD,TRUMP2024,PRISM/USD,PRIV-PERP,PRIV-1230,PROM-PERP,PROM/USD,PSG/USD,PSY/USD,PTU/USD,PUNDIX-PERP,PUNDIX/USD,PYPL-1230,PYPL/USD,QI/USD,QTUM-PERP,RAY-PERP,RAY/USD,REAL/USD,REEF-PERP,REEF/USD,REN-PERP,REN/USD,RNDR-PERP,RNDR/USD,RON-PERP,ROOK/USD,ROOK/USDT,ROSE-PERP,RSR-PERP,RSR/USD,RUNE-PERP,RVN-PERP,SAND-PERP,SAND/USD,SC-PERP,SCRT-PERP,SECO-PERP,SECO/USD,SHIB-PERP,SHIB/USD,SHIT-PERP,SHIT-1230,SKL-PERP,SKL/USD,SLND/USD,SLP-PERP,SLP/USD,SLRS/USD,SLV-1230,SLV/USD,SNX-PERP,SNX/USD,SNY/USD,SOL-PERP,SOL-1230,SOL/BTC,SOL/JPY,SOL/TRY,SOL/USD,SOL/USDT,SOS-PERP,SOS/USD,SPA/USD,SPELL-PERP,SPELL/USD,SPY-1230,SPY/USD,SQ-1230,SQ/USD,SRM-PERP,SRM/USD,SRM/USDT,STARS/USD,STEP-PERP,STEP/USD,STETH/USD,STG-PERP,STG/USD,STMX-PERP,STMX/USD,STORJ-PERP,STORJ/USD,STSOL/USD,STX-PERP,SUN/USD,SUSHI-PERP,SUSHI-1230,SUSHI/BTC,SUSHI/USD,SUSHI/USDT,SWEAT/USD,SXP-PERP,SXP-1230,SXP/BTC,SXP/USD,SXP/USDT,SYN/USD,THETA-PERP,TLM-PERP,TLM/USD,TLRY-1230,TLRY/USD,TOMO-PERP,TOMO/USD,TOMO/USDT,TONCOIN-PERP,TONCOIN/USD,TRU-PERP,TRU/USD,TRU/USDT,TRX-PERP,TRX-1230,TRX/BTC,TRX/TRY,TRX/USD,TRX/USDT,TRYB-PERP,TRYB/USD,TSLA-1230,TSLA/BTC,TSLA/DOGE,TSLA/USD,TSM-1230,TSM/USD,TULIP/USD,UBER-1230,UBER/USD,UBXT/USD,UBXT/USDT,UMEE/USD,UNI-PERP,UNI-1230,UNI/BTC,UNI/USD,UNI/USDT,UNISWAP-PERP,UNISWAP-1230,USD/JPY,USD/TRY,USDT-PERP,USDT-1230,USDT/TRY,USDT/USD,USO-1230,USO/USD,USTC-PERP,VET-PERP,VGX/USD,WAVES-PERP,WAVES-1230,WAVES/USD,WAXL/USD,WBTC/BTC,WBTC/USD,WFLOW/USD,WNDR/USD,WRX/USD,WRX/USDT,WSB-1230,XAUT-PERP,XAUT/USD,XAUT/USDT,XEM-PERP,XLM-PERP,XMR-PERP,XPLA/USD,XRP-PERP,XRP-1230,XRP/BTC,XRP/JPY,XRP/USD,XRP/USDT,XTZ-PERP,XTZ-1230,YFI-PERP,YFI-1230,YFI/BTC,YFI/USD,YFI/USDT,YFII-PERP,YFII/USD,YGG/USD,ZEC-PERP,ZIL-PERP,ZM-1230,ZM/USD,ZRX-PERP,ZRX/USD,ADABEAR/USD,ADABULL/USD,ADAHALF/USD,ADAHEDGE/USD,ALGOBEAR/USD,ALGOBULL/USD,ALGOHALF/USD,ALGOHEDGE/USD,ALTBEAR/USD,ALTBULL/USD,ALTHALF/USD,ALTHEDGE/USD,ASDBEAR/USD,ASDBEAR/USDT,ASDBULL/USD,ASDBULL/USDT,ASDHALF/USD,ASDHEDGE/USD,ATOMBEAR/USD,ATOMBULL/USD,ATOMHALF/USD,ATOMHEDGE/USD,BALBEAR/USD,BALBEAR/USDT,BALBULL/USD,BALBULL/USDT,BALHALF/USD,BALHEDGE/USD,BCHBEAR/USD,BCHBEAR/USDT,BCHBULL/USD,BCHBULL/USDT,BCHHALF/USD,BCHHEDGE/USD,BEAR/USD,BEAR/USDT,BEARSHIT/USD,BNBBEAR/USD,BNBBEAR/USDT,BNBBULL/USD,BNBBULL/USDT,BNBHALF/USD,BNBHEDGE/USD,BSVBEAR/USD,BSVBEAR/USDT,BSVBULL/USD,BSVBULL/USDT,BSVHALF/USD,BSVHEDGE/USD,BULL/USD,BULL/USDT,BULLSHIT/USD,BVOL/USD,COMPBEAR/USD,COMPBEAR/USDT,COMPBULL/USD,COMPBULL/USDT,COMPHALF/USD,COMPHEDGE/USD,DEFIBEAR/USD,DEFIBEAR/USDT,DEFIBULL/USD,DEFIBULL/USDT,DEFIHALF/USD,DEFIHEDGE/USD,DOGEBEAR2021/USD,DOGEBULL/USD,DOGEHALF/USD,DOGEHEDGE/USD,EOSBEAR/USD,EOSBEAR/USDT,EOSBULL/USD,EOSBULL/USDT,EOSHALF/USD,EOSHEDGE/USD,ETCBEAR/USD,ETCBULL/USD,ETCHALF/USD,ETCHEDGE/USD,ETHBEAR/USD,ETHBEAR/USDT,ETHBULL/USD,ETHBULL/USDT,ETHHALF/USD,ETHHEDGE/USD,EXCHBEAR/USD,EXCHBULL/USD,EXCHHALF/USD,EXCHHEDGE/USD,GRTBEAR/USD,GRTBULL/USD,HALF/USD,HALFSHIT/USD,HEDGE/USD,HEDGESHIT/USD,HTBEAR/USD,HTBULL/USD,HTHALF/USD,HTHEDGE/USD,IBVOL/USD,KNCBEAR/USD,KNCBEAR/USDT,KNCBULL/USD,KNCBULL/USDT,KNCHALF/USD,KNCHEDGE/USD,LEOBEAR/USD,LEOBULL/USD,LEOHALF/USD,LEOHEDGE/USD,LINKBEAR/USD,LINKBEAR/USDT,LINKBULL/USD,LINKBULL/USDT,LINKHALF/USD,LINKHEDGE/USD,LTCBEAR/USD,LTCBEAR/USDT,LTCBULL/USD,LTCBULL/USDT,LTCHALF/USD,LTCHEDGE/USD,MATICBEAR2021/USD,MATICBULL/USD,MATICHALF/USD,MATICHEDGE/USD,MIDBEAR/USD,MIDBULL/USD,MIDHALF/USD,MIDHEDGE/USD,MKRBEAR/USD,MKRBULL/USD,OKBBEAR/USD,OKBBULL/USD,OKBHALF/USD,OKBHEDGE/USD,PAXGBEAR/USD,PAXGBULL/USD,PAXGHALF/USD,PAXGHEDGE/USD,PRIVBEAR/USD,PRIVBULL/USD,PRIVHALF/USD,PRIVHEDGE/USD,SUSHIBEAR/USD,SUSHIBULL/USD,SXPBEAR/USD,SXPBULL/USD,SXPHALF/USD,SXPHALF/USDT,SXPHEDGE/USD,THETABEAR/USD,THETABULL/USD,THETAHALF/USD,THETAHEDGE/USD,TOMOBEAR2021/USD,TOMOBULL/USD,TOMOHALF/USD,TOMOHEDGE/USD,TRXBEAR/USD,TRXBULL/USD,TRXHALF/USD,TRXHEDGE/USD,TRYBBEAR/USD,TRYBBULL/USD,TRYBHALF/USD,TRYBHEDGE/USD,UNISWAPBEAR/USD,UNISWAPBULL/USD,USDTBEAR/USD,USDTBULL/USD,USDTHALF/USD,USDTHEDGE/USD,VETBEAR/USD,VETBEAR/USDT,VETBULL/USD,VETBULL/USDT,VETHEDGE/USD,XAUTBEAR/USD,XAUTBULL/USD,XAUTHALF/USD,XAUTHEDGE/USD,XLMBEAR/USD,XLMBULL/USD,XRPBEAR/USD,XRPBEAR/USDT,XRPBULL/USD,XRPBULL/USDT,XRPHALF/USD,XRPHEDGE/USD,XTZBEAR/USD,XTZBEAR/USDT,XTZBULL/USD,XTZBULL/USDT,XTZHALF/USD,XTZHEDGE/USD,ZECBEAR/USD,ZECBULL/USD,1INCH-PERP,AAVE-PERP,ADA-PERP,AGLD-PERP,ALCX-PERP,ALGO-PERP,ALICE-PERP,ALPHA-PERP,ALT-PERP,AMPL-PERP,ANC-PERP,APE-PERP,APT-PERP,AR-PERP,ASD-PERP,ATLAS-PERP,ATOM-PERP,AUDIO-PERP,AVAX-PERP,AXS-PERP,BADGER-PERP,BAL-PERP,BAND-PERP,BAT-PERP,BCH-PERP,BIT-PERP,BNB-PERP,BNT-PERP,BOBA-PERP,BRZ-PERP,BSV-PERP,BTC-PERP,BTT-PERP,C98-PERP,CAKE-PERP,CEL-PERP,CELO-PERP,CHR-PERP,CHZ-PERP,CLV-PERP,COMP-PERP,CREAM-PERP,CRO-PERP,CRV-PERP,CVC-PERP,CVX-PERP,DASH-PERP,DAWN-PERP,DEFI-PERP,DENT-PERP,DODO-PERP,DOGE-PERP,DOT-PERP,DRGN-PERP,DYDX-PERP,EDEN-PERP,EGLD-PERP,ENJ-PERP,ENS-PERP,EOS-PERP,ETC-PERP,ETH-PERP,ETHW-PERP,EXCH-PERP,FIDA-PERP,FIL-PERP,FLM-PERP,FLOW-PERP,FLUX-PERP,FTM-PERP,FTT-PERP,FTXDXY-PERP,FXS-PERP,GAL-PERP,GALA-PERP,GLMR-PERP,GMT-PERP,GRT-PERP,GST-PERP,HBAR-PERP,HNT-PERP,HOLY-PERP,HOT-PERP,HT-PERP,ICP-PERP,ICX-PERP,IMX-PERP,INJ-PERP,IOST-PERP,IOTA-PERP,JASMY-PERP,JPY-PERP,KAVA-PERP,KBTT-PERP,KLAY-PERP,KLUNC-PERP,KNC-PERP,KSHIB-PERP,KSM-PERP,KSOS-PERP,LDO-PERP,LEO-PERP,LINA-PERP,LINK-PERP,LOOKS-PERP,LRC-PERP,LTC-PERP,LUNA2-PERP,LUNC-PERP,MANA-PERP,MAPS-PERP,MATIC-PERP,MEDIA-PERP,MID-PERP,MINA-PERP,MKR-PERP,MNGO-PERP,MOB-PERP,MTL-PERP,MVDA10-PERP,MVDA25-PERP,NEAR-PERP,NEO-PERP,OKB-PERP,OMG-PERP,ONE-PERP,ONT-PERP,OP-PERP,OXY-PERP,PAXG-PERP,PEOPLE-PERP,PERP-PERP,POLIS-PERP,PRIV-PERP,PROM-PERP,PUNDIX-PERP,QTUM-PERP,RAY-PERP,REEF-PERP,REN-PERP,RNDR-PERP,RON-PERP,ROSE-PERP,RSR-PERP,RUNE-PERP,RVN-PERP,SAND-PERP,SC-PERP,SCRT-PERP,SECO-PERP,SHIB-PERP,SHIT-PERP,SKL-PERP,SLP-PERP,SNX-PERP,SOL-PERP,SOS-PERP,SPELL-PERP,SRM-PERP,STEP-PERP,STG-PERP,STMX-PERP,STORJ-PERP,STX-PERP,SUSHI-PERP,SXP-PERP,THETA-PERP,TLM-PERP,TOMO-PERP,TONCOIN-PERP,TRU-PERP,TRX-PERP,TRYB-PERP,UNI-PERP,UNISWAP-PERP,USDT-PERP,USTC-PERP,VET-PERP,WAVES-PERP,XAUT-PERP,XEM-PERP,XLM-PERP,XMR-PERP,XRP-PERP,XTZ-PERP,YFI-PERP,YFII-PERP,ZEC-PERP,ZIL-PERP,ZRX-PERP";


}
