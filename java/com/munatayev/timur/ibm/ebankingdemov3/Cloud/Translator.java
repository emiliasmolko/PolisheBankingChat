package com.munatayev.timur.ibm.ebankingdemov3.Cloud;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.translate.Translate;
import com.google.api.services.translate.model.TranslationsListResponse;
import com.google.api.services.translate.model.TranslationsResource;
import com.munatayev.timur.ibm.ebankingdemov3.Utile.APIProperties;

import java.util.Collections;

public class Translator {

    public static String translateMessage(String textToTranslate) {
        String result = null;
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = AndroidJsonFactory.getDefaultInstance();
        Translate.Builder translateBuilder = new Translate.Builder(httpTransport, jsonFactory, null);
        translateBuilder.setApplicationName("eBanking.v3");
        Translate translate = translateBuilder.build();
        try {
            Translate.Translations.List list = translate.translations().list(Collections.singletonList(textToTranslate), "EN");
            list.setKey(APIProperties.getProperty("TranslatorAPI"));
            list.setSource("PL");
            TranslationsListResponse translateResponse = list.execute();
            StringBuilder sb = new StringBuilder();
            for (TranslationsResource tr : translateResponse.getTranslations()) {
                sb.append(tr.getTranslatedText()).append(" ");
            }
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
