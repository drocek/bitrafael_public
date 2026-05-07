package com.generalbytes.bitrafael.client;

import jakarta.ws.rs.HeaderParam;
import org.junit.Test;
import si.mazi.rescu.ClientConfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ClientConfigFactoryTest {

    @Test
    public void testCreate_nullSupplier() {
        ClientConfig config = ClientConfigFactory.create(null);

        assertNull(config.getDefaultParamsMap().get(HeaderParam.class));
    }

    @Test
    public void testCreate_supplierWithKey() {
        ClientConfig config = ClientConfigFactory.create(() -> "my-api-key");

        assertEquals("Bearer my-api-key", getAuthorizationValue(config));
    }

    @Test
    public void testCreate_supplierReturningNull() {
        ClientConfig config = ClientConfigFactory.create(() -> null);

        assertEquals("", getAuthorizationValue(config));
    }

    @Test
    public void testCreate_supplierIsDynamic() {
        String[] key = new String[1];
        ClientConfig config = ClientConfigFactory.create(() -> key[0]);

        key[0] = "first-key";
        assertEquals("Bearer first-key", getAuthorizationValue(config));

        key[0] = "second-key";
        assertEquals("Bearer second-key", getAuthorizationValue(config));
    }

    private String getAuthorizationValue(ClientConfig config) {
        return config.getDefaultParamsMap().get(HeaderParam.class).getParamValue("Authorization").toString();
    }

}
