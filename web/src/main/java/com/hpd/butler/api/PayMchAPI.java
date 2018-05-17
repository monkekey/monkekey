package com.hpd.butler.api;

import com.hpd.butler.bean.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.security.KeyStore;

/**
 * Created by Leo on 2017/8/29.
 */
public class PayMchAPI extends BaseAPI{

    public UnifiedorderResult payUnifiedorder(Unifiedorder unifiedorder){
        String unifiedorderXML = XMLConverUtil.convertToXML(unifiedorder);
        HttpUriRequest httpUriRequest = RequestBuilder.post()
                .setHeader(xmlHeader)
                .setUri(MCH_URI + "/pay/unifiedorder")
                .setEntity(new StringEntity(unifiedorderXML, Charset.forName("utf-8")))
                .build();
        return localHttpClient.execute(httpUriRequest, XmlResponseHandler.createResponseHandler(UnifiedorderResult.class));
    }

    public OrderqueryResult getOrderQuery(Orderquery orderquery){
        String orderqueryXML = XMLConverUtil.convertToXML(orderquery);
        HttpUriRequest httpUriRequest = RequestBuilder.post()
                .setHeader(xmlHeader)
                .setUri(MCH_URI + "/pay/orderquery")
                .setEntity(new StringEntity(orderqueryXML, Charset.forName("utf-8")))
                .build();
        return localHttpClient.execute(httpUriRequest, XmlResponseHandler.createResponseHandler(OrderqueryResult.class));
    }

    public RefundResult payRefund(Refund refund){
        String refundXML = XMLConverUtil.convertToXML(refund);
        System.out.println(refundXML.toString());
        FileInputStream fileInputStream = null;
        try {
            //加载证书
            fileInputStream = new FileInputStream(new File(this.getClass().getClassLoader().getResource("apiclient_cert.p12").getPath()));
            System.out.println(fileInputStream);
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(fileInputStream, refund.getMch_id().toCharArray());

            SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keyStore, refund.getMch_id().toCharArray()).build();

            SSLConnectionSocketFactory sslcsf = new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslcsf)
                    .build();
            HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");
            httpPost.setEntity(new StringEntity(refundXML, Charset.forName("utf-8")));
            RefundResult refundResult = httpclient.execute(httpPost,XmlResponseHandler.createResponseHandler(RefundResult.class));
            return refundResult;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public OrderqueryResult getRefund(Orderquery orderquery){
        String orderqueryXML = XMLConverUtil.convertToXML(orderquery);
        HttpUriRequest httpUriRequest = RequestBuilder.post()
                .setHeader(xmlHeader)
                .setUri(MCH_URI + "/pay/refundquery")
                .setEntity(new StringEntity(orderqueryXML, Charset.forName("utf-8")))
                .build();
        return localHttpClient.execute(httpUriRequest, XmlResponseHandler.createResponseHandler(OrderqueryResult.class));
    }
}
