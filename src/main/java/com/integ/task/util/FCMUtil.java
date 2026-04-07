package com.integ.task.util;

import net.minidev.json.JSONObject;
import org.springframework.util.ObjectUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class FCMUtil {
    public static void send_FCM_Notification(String tokenId, String server_key, String message, String title, String payment_qr) {
        try {
            URL url = new URL(FCMConstraint.FCM_URL);
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(Boolean.FALSE);
            conn.setDoInput(Boolean.TRUE);
            conn.setDoOutput(Boolean.TRUE);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "key=" + server_key);
            conn.setRequestProperty("Content-Type", "application/json");

            JSONObject infoJson = new JSONObject();
            infoJson.put("header", title);
            infoJson.put("message", message);
            infoJson.put("payment_qr", payment_qr);

            JSONObject json = new JSONObject();
            json.put("to", tokenId.trim());
            json.put("data", infoJson);
            json.put("content_available", true);
            json.put("apns-priority", 5);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();

            int status = 0;

            if (!ObjectUtils.isEmpty(conn)) {
                status = conn.getResponseCode();
            }

            if (status != 0) {
                if (status == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    System.out.println(message);
                } else if (status == 301) {
                    System.out.println("Unknown Error occurred :");
                } else if (status == 401) {
                    System.out.println("Notification Response : TokenId : " + tokenId + " Error occurred :");
                } else if (status == 501) {
                    System.out.println("Notification Response : [ errorCode=ServerError ] TokenId : " + tokenId);
                } else if (status == 503) {
                    System.out.println("Notification Response : FCM Service is Unavailable  TokenId : " + tokenId);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("FCMUtil.main()");
        //send_FCM_Notification("eayhV8sHdoA:APA91bGOsbv6ycfIydYp1wF9Rh0hsK2m8ccn5xCE7_Q8rTOCGsP3R4I2bN7WvhSERPpkTZR18afFidUq6CvS65PWsXrvMWoml0IO0-aA92jFqFli70EyhRSkFEIRPMtR7PH2wM0p6By9",FCMConstraint.SERVER_KEY_TECHNICIAN,"Report Done","Hello How are you ?");
        send_FCM_Notification("dgGm5RH8S6er_PY_4Ia9yQ:APA91bFEkJtjcNQWhZHd-pEbQ3vPgU9oDRHtXvZe8Gi99gKY5Nxc5jA3Jou1oKzXeWIFMvYobuo4DtVJvX66nHxF9DQngSUZX6P4iNzwVIzL17YNAb30ssVm0qSxxUmKwpDSt3IsAoV4","AIzaSyA8up3Jz1LQxzKCJQ2ppxRdYUiQv_BqNPk","Report Done - Vijay","Hello How are you ?", "https://s3.ap-south-1.amazonaws.com/easewire/qrcodes/1742-20240905674291@yesbank.png");
        System.out.println("FCMUtil.main() END..");
    }
}
