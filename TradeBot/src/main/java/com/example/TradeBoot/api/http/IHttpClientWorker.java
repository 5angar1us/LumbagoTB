package com.example.TradeBoot.api.http;

import com.example.TradeBoot.api.extentions.RequestExcpetions.Checked.BadRequestByFtxException;

public interface IHttpClientWorker {
    String createGetRequest(String uri);

    String createPostRequest(String uri, String body) throws BadRequestByFtxException;

    boolean createDeleteRequest(String uri) throws BadRequestByFtxException;

    boolean createDeleteRequest(String uri, String body) throws BadRequestByFtxException;
}
