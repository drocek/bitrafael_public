package com.generalbytes.bitrafael.client;

import jakarta.ws.rs.HeaderParam;
import si.mazi.rescu.ClientConfig;
import si.mazi.rescu.ParamsDigest;

import java.util.function.Supplier;

/**
 * Factory for creating a rescu {@link ClientConfig} with an {@code Authorization: Bearer <key>}
 * header on every HTTP request. The key is resolved via a {@link Supplier} at each call,
 * allowing the key to change without recreating the client.
 */
public class ClientConfigFactory {

    private ClientConfigFactory() {}

    /**
     * Creates a {@link ClientConfig} configured with the {@code Authorization} header.
     * If {@code apiKeySupplier} is {@code null}, returns a plain {@link ClientConfig} without any auth header.
     * The header value is resolved from the supplier on each request.
     * If the supplier returns {@code null} or an empty string, the header is omitted entirely.
     */
    public static ClientConfig create(Supplier<String> apiKeySupplier) {
        ClientConfig config = new ClientConfig();

        if (apiKeySupplier != null) {
            config.addDefaultParam(HeaderParam.class, "Authorization", getBearerTokenDigest(apiKeySupplier));
        }

        return config;
    }

    private static ParamsDigest getBearerTokenDigest(Supplier<String> apiKeySupplier) {
        return invocation -> {
            String apiKey = apiKeySupplier.get();
            return apiKey == null || apiKey.isEmpty() ? null : "Bearer " + apiKey;
        };
    }

}
