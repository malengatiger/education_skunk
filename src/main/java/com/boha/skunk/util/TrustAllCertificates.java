package com.boha.skunk.util;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class TrustAllCertificates implements X509TrustManager {
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        // Accept all client certificates
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        // Accept all server certificates
    }

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}

