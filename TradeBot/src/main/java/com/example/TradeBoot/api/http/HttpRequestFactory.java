package com.example.TradeBoot.api.http;

import com.example.TradeBoot.api.http.auntification.Auntification;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class HttpRequestFactory {

    private final Auntification auntification;

    private URI apiPartUri = URI.create(UriComponentsBuilder.newInstance().path("/api").toUriString());
    private URI defaultSiteUri = URI.create(CreateSiteUri());

    public HttpRequestFactory(Auntification auntification) {
        this.auntification = auntification;
    }

    public HttpRequest createGetRequest(String uri) {
        return createRequest(uri, "", "GET");
    }

    public HttpRequest createPostRequest(String uri, String body) {
        return createRequest(uri, body, "POST");
    }

    public HttpRequest createDeleteRequest(String uri) {
        return createRequest(uri, "", "DELETE");
    }

    public HttpRequest createDeleteRequest(String uri, String body) {
        return createRequest(uri, body, "DELETE");
    }

    private HttpRequest createRequest(
            String uri,
            String body,
            String httpMethodName) {

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .method(httpMethodName, HttpRequest.BodyPublishers.ofByteArray(body.getBytes()));

        var uriToSignature = URI.create(apiPartUri + uri);

        var fullUri = addPath(defaultSiteUri, uriToSignature);

        requestBuilder = DefaultConfigureRequestBuilder(
                requestBuilder,
                new SignaturePayloadBuilder(httpMethodName, uriToSignature, body),
                fullUri);

        return requestBuilder.build();
    }

    private HttpRequest.Builder DefaultConfigureRequestBuilder(
            HttpRequest.Builder builder,
            SignaturePayloadBuilder signaturePayloadBuilder,
            URI uri) {
        Auntification.AuntificationData auntificationData = auntification.CreateAuthInformation(signaturePayloadBuilder);

        HttpRequest.Builder requestBuilder = builder
                .header("FTX-KEY", auntificationData.getApiKey())
                .header("FTX-SIGN", auntificationData.getSignature())
                .header("FTX-TS", String.valueOf(auntificationData.getTimeStamp()))
                .timeout(Duration.of(10L, ChronoUnit.SECONDS))
                .uri(uri);

        if (this.auntification.getSubAccountName().isPresent())
            requestBuilder = requestBuilder.header("FTX-SUBACCOUNT", this.auntification.getSubAccountName().get());

        return requestBuilder;
    }

    private URI addPath(URI uri, URI path) {
        return addPath(uri, path.toString());
    }
    private URI addPath(URI uri, String path) {
        String newPath;
        if (path.startsWith("/")) newPath = path;
        else if (uri.getPath().endsWith("/")) newPath = uri.getPath() + path;
        else newPath = uri.getPath() + "/" + path;

        return uri.resolve(newPath).normalize();
    }

    private static String CreateSiteUri() {
        String uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("ftx.com")
                .toUriString();

        return uri;
    }

}
