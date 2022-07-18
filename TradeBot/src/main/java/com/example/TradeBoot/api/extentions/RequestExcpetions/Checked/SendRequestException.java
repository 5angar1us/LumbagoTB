package com.example.TradeBoot.api.extentions.RequestExcpetions.Checked;

import com.example.TradeBoot.api.extentions.RequestExcpetions.Checked.BadRequestByFtxException;

public class SendRequestException extends BadRequestByFtxException {

    public SendRequestException(String s, Exception e) {
        super(s, e);
    }
    public SendRequestException(String s) {
        super(s);
    }
}
