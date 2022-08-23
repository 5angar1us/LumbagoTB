package com.example.TradeBoot;

import com.example.TradeBoot.trade.services.FinancialInstrumentService;
import com.example.TradeBoot.configuration.TestServiceInstances;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ShowFinancialInstruments {

    private static FinancialInstrumentService financialInstrumentService;

    @BeforeAll
    static void init() {
        financialInstrumentService = TestServiceInstances.getFinancialInstrumentService();
    }

    @Test
    public void printStableNamesInRow(){
        var names = financialInstrumentService.getStableNames();
        for (String name : names) {
            System.out.print(name+",");
        }
        System.out.println();
    }
    @Test
    public void printStableNamesInColumn(){
        var names = financialInstrumentService.getStableNames();
        for (String name : names) {
            System.out.println(name);
        }
        System.out.println();
    }

    private String allNamesString ="1INCH-PERP,1INCH-0930,1INCH/USD,AAPL-0930,AAPL/USD,AAVE-PERP,AAVE-0930,AAVE/USD,AAVE/USDT,ABNB-0930,ABNB/USD,ACB-0930,ACB/USD,ADA-PERP,ADA-0930,AGLD-PERP,AGLD/USD,AKRO/USD,AKRO/USDT,ALCX-PERP,ALCX/USD,ALEPH/USD,ALGO-PERP,ALGO-0930,ALGO/USD,ALGO/USDT,ALICE-PERP,ALICE/USD,ALPHA-PERP,ALPHA/USD,ALT-PERP,ALT-0930,AMC-0930,AMC/USD,AMD-0930,AMD/USD,AMPL-PERP,AMPL/USD,AMPL/USDT,AMZN-0930,AMZN/USD,ANC-PERP,ANC/USD,APE-PERP,APE-0930,APE/USD,APHA/USD,AR-PERP,ARKK-0930,ARKK/USD,ASD-PERP,ASD/USD,ATLAS-PERP,ATLAS/USD,ATOM-PERP,ATOM-0930,ATOM/USD,ATOM/USDT,AUDIO-PERP,AUDIO/USD,AUDIO/USDT,AURY/USD,AVAX-PERP,AVAX-0930,AVAX/BTC,AVAX/USD,AVAX/USDT,AXS-PERP,AXS-0930,AXS/USD,BABA-0930,BABA/USD,BADGER-PERP,BADGER/USD,BAL-PERP,BAL-0930,BAL/USD,BAL/USDT,BAND-PERP,BAND/USD,BAO/USD,BAR/USD,BAT-PERP,BAT/JPY,BAT/USD,BB-0930,BB/USD,BCH-PERP,BCH-0930,BCH/BTC,BCH/JPY,BCH/USD,BCH/USDT,BICO/USD,BILI-0930,BILI/USD,BIT-PERP,BIT/USD,BITO-0930,BITO/USD,BITW-0930,BITW/USD,BLT/USD,BNB-PERP,BNB-0930,BNB/BTC,BNB/USD,BNB/USDT,BNT-PERP,BNT/USD,BNTX-0930,BNTX/USD,BOBA-PERP,BOBA/USD,BOLSONARO2022,BRZ-PERP,BRZ/USD,BRZ/USDT,BSV-PERP,BSV-0930,BTC-PERP,BTC-MOVE-0817,BTC-MOVE-0818,BTC-MOVE-WK-0819,BTC-MOVE-WK-0826,BTC-MOVE-WK-0902,BTC-MOVE-WK-0909,BTC-0930,BTC-MOVE-2022Q3,BTC-1230,BTC-MOVE-2022Q4,BTC/AUD,BTC/BRZ,BTC/EUR,BTC/JPY,BTC/TRYB,BTC/USD,BTC/USDT,BTT-PERP,BTT/USD,BYND-0930,BYND/USD,C98-PERP,C98/USD,CAD/USD,CAKE-PERP,CEL-PERP,CEL-0930,CEL/BTC,CEL/USD,CELO-PERP,CGC-0930,CGC/USD,CHR-PERP,CHR/USD,CHZ-PERP,CHZ-0930,CHZ/USD,CHZ/USDT,CITY/USD,CLV-PERP,CLV/USD,COIN/USD,COMP-PERP,COMP-0930,COMP/USD,COMP/USDT,CONV/USD,COPE/USD,CQT/USD,CREAM-PERP,CREAM/USD,CREAM/USDT,CRO-PERP,CRO/USD,CRON-0930,CRON/USD,CRV-PERP,CRV/USD,CTX/USD,CTX/USDT,CUSDT/USD,CUSDT/USDT,CVC-PERP,CVC/USD,CVX-PERP,CVX/USD,DAI/USD,DAI/USDT,DASH-PERP,DAWN-PERP,DAWN/USD,DEFI-PERP,DEFI-0930,DENT-PERP,DENT/USD,DFL/USD,DKNG-0930,DKNG/USD,DMG/USD,DMG/USDT,DODO-PERP,DODO/USD,DOGE-PERP,DOGE-0930,DOGE/BTC,DOGE/JPY,DOGE/USD,DOGE/USDT,DOT-PERP,DOT-0930,DOT/BTC,DOT/JPY,DOT/USD,DOT/USDT,DRGN-PERP,DYDX-PERP,DYDX/USD,EDEN-PERP,EDEN/USD,EGLD-PERP,EMB/USD,ENJ-PERP,ENJ/JPY,ENJ/USD,ENS-PERP,ENS/USD,EOS-PERP,EOS-0930,ETC-PERP,ETH-PERP,ETH-0930,ETH-1230,ETH/AUD,ETH/BRZ,ETH/BTC,ETH/EUR,ETH/JPY,ETH/USD,ETH/USDT,ETHE-0930,ETHE/USD,EUR/USD,EURT/EUR,EURT/USD,EURT/USDT,EXCH-PERP,EXCH-0930,FB-0930,FB/USD,FIDA-PERP,FIDA/USD,FIDA/USDT,FIL-PERP,FIL-0930,FLM-PERP,FLOW-PERP,FRONT/USD,FRONT/USDT,FTM-PERP,FTM-0930,FTM/USD,FTT-PERP,FTT/BTC,FTT/JPY,FTT/USD,FTT/USDT,FXS-PERP,FXS/USD,GAL-PERP,GAL/USD,GALA-PERP,GALA/USD,GALFAN/USD,GARI/USD,GBP/USD,GBTC-0930,GBTC/USD,GDX-0930,GDX/USD,GDXJ-0930,GDXJ/USD,GENE/USD,GLD-0930,GLD/USD,GLMR-PERP,GLXY/USD,GME-0930,GME/USD,GMT-PERP,GMT-0930,GMT/USD,GODS/USD,GOG/USD,GOOGL-0930,GOOGL/USD,GRT-PERP,GRT-0930,GRT/USD,GST-PERP,GST-0930,GST/USD,GST/USDT,GT/USD,HBAR-PERP,HGET/USD,HGET/USDT,HMT/USD,HNT-PERP,HNT/USD,HNT/USDT,HOLY-PERP,HOLY/USD,HOOD/USD,HOT-PERP,HT-PERP,HT/USD,HUM/USD,HXRO/USD,HXRO/USDT,ICP-PERP,ICX-PERP,IMX-PERP,IMX/USD,INDI/USD,INTER/USD,IOST-PERP,IOTA-PERP,IP3/USD,JASMY-PERP,JET/USD,JOE/USD,JST/USD,KAVA-PERP,KBTT-PERP,KBTT/USD,KIN/USD,KNC-PERP,KNC/USD,KNC/USDT,KSHIB-PERP,KSHIB/USD,KSM-PERP,KSOS-PERP,KSOS/USD,LDO-PERP,LDO/USD,LEO-PERP,LEO/USD,LINA-PERP,LINA/USD,LINK-PERP,LINK-0930,LINK/BTC,LINK/USD,LINK/USDT,LOOKS-PERP,LOOKS/USD,LRC-PERP,LRC/USD,LTC-PERP,LTC-0930,LTC/BTC,LTC/JPY,LTC/USD,LTC/USDT,LUA/USD,LUA/USDT,MANA-PERP,MANA/USD,MAPS-PERP,MAPS/USD,MAPS/USDT,MATH/USD,MATH/USDT,MATIC-PERP,MATIC/BTC,MATIC/USD,MBS/USD,MCB/USD,MEDIA-PERP,MEDIA/USD,MER/USD,MID-PERP,MID-0930,MINA-PERP,MKR-PERP,MKR/USD,MKR/USDT,MNGO-PERP,MNGO/USD,MOB-PERP,MOB/USD,MOB/USDT,MRNA-0930,MRNA/USD,MSOL/USD,MSTR-0930,MSTR/USD,MTA/USD,MTA/USDT,MTL-PERP,MTL/USD,MVDA10-PERP,MVDA25-PERP,NEAR-PERP,NEAR/USD,NEAR/USDT,NEO-PERP,NEXO/USD,NFLX-0930,NFLX/USD,NIO-0930,NIO/USD,NOK-0930,NOK/USD,NVDA-0930,NVDA/USD,OKB-PERP,OKB-0930,OKB/USD,OMG-PERP,OMG-0930,OMG/JPY,OMG/USD,ONE-PERP,ONT-PERP,OP-PERP,OP-0930,ORBS/USD,OXY-PERP,OXY/USD,OXY/USDT,PAXG-PERP,PAXG/USD,PAXG/USDT,PENN-0930,PENN/USD,PEOPLE-PERP,PEOPLE/USD,PERP-PERP,PERP/USD,PFE-0930,PFE/USD,POLIS-PERP,POLIS/USD,PORT/USD,TRUMP2024,PRISM/USD,PRIV-PERP,PRIV-0930,PROM-PERP,PROM/USD,PSG/USD,PSY/USD,PTU/USD,PUNDIX-PERP,PUNDIX/USD,PYPL-0930,PYPL/USD,QI/USD,QTUM-PERP,RAY-PERP,RAY/USD,REAL/USD,REEF-PERP,REEF/USD,REN-PERP,REN/USD,RNDR-PERP,RNDR/USD,RON-PERP,ROOK/USD,ROOK/USDT,ROSE-PERP,RSR-PERP,RSR/USD,RUNE-PERP,SAND-PERP,SAND/USD,SC-PERP,SCRT-PERP,SECO-PERP,SECO/USD,SHIB-PERP,SHIB/USD,SHIT-PERP,SHIT-0930,SKL-PERP,SKL/USD,SLND/USD,SLP-PERP,SLP/USD,SLRS/USD,SLV-0930,SLV/USD,SNX-PERP,SNX/USD,SNY/USD,SOL-PERP,SOL-0930,SOL/BTC,SOL/JPY,SOL/USD,SOL/USDT,SOS-PERP,SOS/USD,SPA/USD,SPELL-PERP,SPELL/USD,SPY-0930,SPY/USD,SQ-0930,SQ/USD,SRM-PERP,SRM/USD,SRM/USDT,STARS/USD,STEP-PERP,STEP/USD,STETH/USD,STG/USD,STMX-PERP,STMX/USD,STORJ-PERP,STORJ/USD,STSOL/USD,STX-PERP,SUN/USD,SUSHI-PERP,SUSHI-0930,SUSHI/BTC,SUSHI/USD,SUSHI/USDT,SXP-PERP,SXP-0930,SXP/BTC,SXP/USD,SXP/USDT,THETA-PERP,TLM-PERP,TLM/USD,TLRY-0930,TLRY/USD,TOMO-PERP,TOMO/USD,TOMO/USDT,TONCOIN-PERP,TONCOIN/USD,TRU-PERP,TRU/USD,TRU/USDT,TRX-PERP,TRX-0930,TRX/BTC,TRX/USD,TRX/USDT,TRYB-PERP,TRYB/USD,TSLA-0930,TSLA/BTC,TSLA/DOGE,TSLA/USD,TSM-0930,TSM/USD,TULIP/USD,TWTR-0930,TWTR/USD,UBER-0930,UBER/USD,UBXT/USD,UBXT/USDT,UMEE/USD,UNI-PERP,UNI-0930,UNI/BTC,UNI/USD,UNI/USDT,UNISWAP-PERP,UNISWAP-0930,USD/JPY,USDT-PERP,USDT-0930,USDT/USD,USO-0930,USO/USD,VET-PERP,VGX/USD,WAVES-PERP,WAVES-0930,WAVES/USD,WBTC/BTC,WBTC/USD,WFLOW/USD,WNDR/USD,WRX/USD,WRX/USDT,WSB-0930,XAUT-PERP,XAUT/USD,XAUT/USDT,XEM-PERP,XLM-PERP,XMR-PERP,XRP-PERP,XRP-0930,XRP/BTC,XRP/JPY,XRP/USD,XRP/USDT,XTZ-PERP,XTZ-0930,YFI-PERP,YFI-0930,YFI/BTC,YFI/USD,YFI/USDT,YFII-PERP,YFII/USD,YGG/USD,ZEC-PERP,ZIL-PERP,ZM-0930,ZM/USD,ZRX-PERP,ZRX/USD,ADABEAR/USD,ADABULL/USD,ADAHALF/USD,ADAHEDGE/USD,ALGOBEAR/USD,ALGOBULL/USD,ALGOHALF/USD,ALGOHEDGE/USD,ALTBEAR/USD,ALTBULL/USD,ALTHALF/USD,ALTHEDGE/USD,ASDBEAR/USD,ASDBEAR/USDT,ASDBULL/USD,ASDBULL/USDT,ASDHALF/USD,ASDHEDGE/USD,ATOMBEAR/USD,ATOMBULL/USD,ATOMHALF/USD,ATOMHEDGE/USD,BALBEAR/USD,BALBEAR/USDT,BALBULL/USD,BALBULL/USDT,BALHALF/USD,BALHEDGE/USD,BCHBEAR/USD,BCHBEAR/USDT,BCHBULL/USD,BCHBULL/USDT,BCHHALF/USD,BCHHEDGE/USD,BEAR/USD,BEAR/USDT,BEARSHIT/USD,BNBBEAR/USD,BNBBEAR/USDT,BNBBULL/USD,BNBBULL/USDT,BNBHALF/USD,BNBHEDGE/USD,BSVBEAR/USD,BSVBEAR/USDT,BSVBULL/USD,BSVBULL/USDT,BSVHALF/USD,BSVHEDGE/USD,BULL/USD,BULL/USDT,BULLSHIT/USD,BVOL/USD,COMPBEAR/USD,COMPBEAR/USDT,COMPBULL/USD,COMPBULL/USDT,COMPHALF/USD,COMPHEDGE/USD,DEFIBEAR/USD,DEFIBEAR/USDT,DEFIBULL/USD,DEFIBULL/USDT,DEFIHALF/USD,DEFIHEDGE/USD,DOGEBEAR2021/USD,DOGEBULL/USD,DOGEHALF/USD,DOGEHEDGE/USD,DRGNBEAR/USD,DRGNBULL/USD,DRGNHALF/USD,DRGNHEDGE/USD,EOSBEAR/USD,EOSBEAR/USDT,EOSBULL/USD,EOSBULL/USDT,EOSHALF/USD,EOSHEDGE/USD,ETCBEAR/USD,ETCBULL/USD,ETCHALF/USD,ETCHEDGE/USD,ETHBEAR/USD,ETHBEAR/USDT,ETHBULL/USD,ETHBULL/USDT,ETHHALF/USD,ETHHEDGE/USD,EXCHBEAR/USD,EXCHBULL/USD,EXCHHALF/USD,EXCHHEDGE/USD,GRTBEAR/USD,GRTBULL/USD,HALF/USD,HALFSHIT/USD,HEDGE/USD,HEDGESHIT/USD,HTBEAR/USD,HTBULL/USD,HTHALF/USD,HTHEDGE/USD,IBVOL/USD,KNCBEAR/USD,KNCBEAR/USDT,KNCBULL/USD,KNCBULL/USDT,KNCHALF/USD,KNCHEDGE/USD,LEOBEAR/USD,LEOBULL/USD,LEOHALF/USD,LEOHEDGE/USD,LINKBEAR/USD,LINKBEAR/USDT,LINKBULL/USD,LINKBULL/USDT,LINKHALF/USD,LINKHEDGE/USD,LTCBEAR/USD,LTCBEAR/USDT,LTCBULL/USD,LTCBULL/USDT,LTCHALF/USD,LTCHEDGE/USD,MATICBEAR2021/USD,MATICBULL/USD,MATICHALF/USD,MATICHEDGE/USD,MIDBEAR/USD,MIDBULL/USD,MIDHALF/USD,MIDHEDGE/USD,MKRBEAR/USD,MKRBULL/USD,OKBBEAR/USD,OKBBULL/USD,OKBHALF/USD,OKBHEDGE/USD,PAXGBEAR/USD,PAXGBULL/USD,PAXGHALF/USD,PAXGHEDGE/USD,PRIVBEAR/USD,PRIVBULL/USD,PRIVHALF/USD,PRIVHEDGE/USD,SUSHIBEAR/USD,SUSHIBULL/USD,SXPBEAR/USD,SXPBULL/USD,SXPHALF/USD,SXPHALF/USDT,SXPHEDGE/USD,THETABEAR/USD,THETABULL/USD,THETAHALF/USD,THETAHEDGE/USD,TOMOBEAR2021/USD,TOMOBULL/USD,TOMOHALF/USD,TOMOHEDGE/USD,TRXBEAR/USD,TRXBULL/USD,TRXHALF/USD,TRXHEDGE/USD,TRYBBEAR/USD,TRYBBULL/USD,TRYBHALF/USD,TRYBHEDGE/USD,UNISWAPBEAR/USD,UNISWAPBULL/USD,USDTBEAR/USD,USDTBULL/USD,USDTHALF/USD,USDTHEDGE/USD,VETBEAR/USD,VETBEAR/USDT,VETBULL/USD,VETBULL/USDT,VETHEDGE/USD,XAUTBEAR/USD,XAUTBULL/USD,XAUTHALF/USD,XAUTHEDGE/USD,XLMBEAR/USD,XLMBULL/USD,XRPBEAR/USD,XRPBEAR/USDT,XRPBULL/USD,XRPBULL/USDT,XRPHALF/USD,XRPHEDGE/USD,XTZBEAR/USD,XTZBEAR/USDT,XTZBULL/USD,XTZBULL/USDT,XTZHALF/USD,XTZHEDGE/USD,ZECBEAR/USD,ZECBULL/USD,1INCH-PERP,AAVE-PERP,ADA-PERP,AGLD-PERP,ALCX-PERP,ALGO-PERP,ALICE-PERP,ALPHA-PERP,ALT-PERP,AMPL-PERP,ANC-PERP,APE-PERP,AR-PERP,ASD-PERP,ATLAS-PERP,ATOM-PERP,AUDIO-PERP,AVAX-PERP,AXS-PERP,BADGER-PERP,BAL-PERP,BAND-PERP,BAT-PERP,BCH-PERP,BIT-PERP,BNB-PERP,BNT-PERP,BOBA-PERP,BRZ-PERP,BSV-PERP,BTC-PERP,BTT-PERP,C98-PERP,CAKE-PERP,CEL-PERP,CELO-PERP,CHR-PERP,CHZ-PERP,CLV-PERP,COMP-PERP,CREAM-PERP,CRO-PERP,CRV-PERP,CVC-PERP,CVX-PERP,DASH-PERP,DAWN-PERP,DEFI-PERP,DENT-PERP,DMG-PERP,DODO-PERP,DOGE-PERP,DOT-PERP,DRGN-PERP,DYDX-PERP,EDEN-PERP,EGLD-PERP,ENJ-PERP,ENS-PERP,EOS-PERP,ETC-PERP,ETH-PERP,EXCH-PERP,FIDA-PERP,FIL-PERP,FLM-PERP,FLOW-PERP,FTM-PERP,FTT-PERP,FXS-PERP,GAL-PERP,GALA-PERP,GLMR-PERP,GMT-PERP,GRT-PERP,GST-PERP,HBAR-PERP,HNT-PERP,HOLY-PERP,HOT-PERP,HT-PERP,ICP-PERP,ICX-PERP,IMX-PERP,IOST-PERP,IOTA-PERP,JASMY-PERP,KAVA-PERP,KBTT-PERP,KNC-PERP,KSHIB-PERP,KSM-PERP,KSOS-PERP,LDO-PERP,LEO-PERP,LINA-PERP,LINK-PERP,LOOKS-PERP,LRC-PERP,LTC-PERP,MANA-PERP,MAPS-PERP,MATIC-PERP,MEDIA-PERP,MID-PERP,MINA-PERP,MKR-PERP,MNGO-PERP,MOB-PERP,MTL-PERP,MVDA10-PERP,MVDA25-PERP,NEAR-PERP,NEO-PERP,OKB-PERP,OMG-PERP,ONE-PERP,ONT-PERP,OP-PERP,OXY-PERP,PAXG-PERP,PEOPLE-PERP,PERP-PERP,POLIS-PERP,PRIV-PERP,PROM-PERP,PUNDIX-PERP,QTUM-PERP,RAMP-PERP,RAY-PERP,REEF-PERP,REN-PERP,RNDR-PERP,RON-PERP,ROSE-PERP,RSR-PERP,RUNE-PERP,SAND-PERP,SC-PERP,SCRT-PERP,SECO-PERP,SHIB-PERP,SHIT-PERP,SKL-PERP,SLP-PERP,SNX-PERP,SOL-PERP,SOS-PERP,SPELL-PERP,SRM-PERP,SRN-PERP,STEP-PERP,STMX-PERP,STORJ-PERP,STX-PERP,SUSHI-PERP,SXP-PERP,THETA-PERP,TLM-PERP,TOMO-PERP,TONCOIN-PERP,TRU-PERP,TRX-PERP,TRYB-PERP,UNI-PERP,UNISWAP-PERP,USDT-PERP,USTC-PERP,VET-PERP,WAVES-PERP,XAUT-PERP,XEM-PERP,XLM-PERP,XMR-PERP,XRP-PERP,XTZ-PERP,YFI-PERP,YFII-PERP,ZEC-PERP,ZIL-PERP,ZRX-PERP";


    public void canSeparateString(){
        var futuresString = allNamesString.split(",");
        var allNames = financialInstrumentService.getStableNames();


        assertTrue(futuresString.length == allNames.size());
    }
    @Test
    public void printChanges(){

        var futuresString = allNamesString.split(",");
        var allNames = financialInstrumentService.getStableNames();

        System.out.println(futuresString.length);
        System.out.println(allNames.size());

        var others = Stream.of(futuresString)
                .filter(i -> !allNames.contains(i))
                .collect(Collectors.toList());

        others.stream().forEach(other -> System.out.println(other));
    }
}
