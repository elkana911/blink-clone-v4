package com.phonegap.plugins.blinkid.recognizers.serialization;

import com.microblink.entities.recognizers.Recognizer;
import com.phonegap.plugins.blinkid.recognizers.RecognizerSerialization;

import org.json.JSONException;
import org.json.JSONObject;

public final class SpainDlFrontRecognizerSerialization implements RecognizerSerialization {

    @Override
    public Recognizer<?, ?> createRecognizer(JSONObject jsonRecognizer) {
        com.microblink.entities.recognizers.blinkid.spain.SpainDlFrontRecognizer recognizer = new com.microblink.entities.recognizers.blinkid.spain.SpainDlFrontRecognizer();
        recognizer.setDetectGlare(jsonRecognizer.optBoolean("detectGlare", true));
        recognizer.setExtractDateOfBirth(jsonRecognizer.optBoolean("extractDateOfBirth", true));
        recognizer.setExtractFirstName(jsonRecognizer.optBoolean("extractFirstName", true));
        recognizer.setExtractIssuingAuthority(jsonRecognizer.optBoolean("extractIssuingAuthority", true));
        recognizer.setExtractLicenceCategories(jsonRecognizer.optBoolean("extractLicenceCategories", true));
        recognizer.setExtractPlaceOfBirth(jsonRecognizer.optBoolean("extractPlaceOfBirth", true));
        recognizer.setExtractSurname(jsonRecognizer.optBoolean("extractSurname", true));
        recognizer.setExtractValidFrom(jsonRecognizer.optBoolean("extractValidFrom", true));
        recognizer.setExtractValidUntil(jsonRecognizer.optBoolean("extractValidUntil", true));
        recognizer.setFaceImageDpi(jsonRecognizer.optInt("faceImageDpi", 250));
        recognizer.setFullDocumentImageDpi(jsonRecognizer.optInt("fullDocumentImageDpi", 250));
        recognizer.setFullDocumentImageExtensionFactors(BlinkIDSerializationUtils.deserializeExtensionFactors(jsonRecognizer.optJSONObject("fullDocumentImageExtensionFactors")));
        recognizer.setReturnFaceImage(jsonRecognizer.optBoolean("returnFaceImage", false));
        recognizer.setReturnFullDocumentImage(jsonRecognizer.optBoolean("returnFullDocumentImage", false));
        recognizer.setReturnSignatureImage(jsonRecognizer.optBoolean("returnSignatureImage", false));
        recognizer.setSignatureImageDpi(jsonRecognizer.optInt("signatureImageDpi", 250));
        return recognizer;
    }

    @Override
    public JSONObject serializeResult(Recognizer<?, ?> recognizer) {
        com.microblink.entities.recognizers.blinkid.spain.SpainDlFrontRecognizer.Result result = ((com.microblink.entities.recognizers.blinkid.spain.SpainDlFrontRecognizer)recognizer).getResult();
        JSONObject jsonResult = new JSONObject();
        try {
            SerializationUtils.addCommonResultData(jsonResult, result);
            jsonResult.put("dateOfBirth", SerializationUtils.serializeDate(result.getDateOfBirth()));
            jsonResult.put("faceImage", SerializationUtils.encodeImageBase64(result.getFaceImage()));
            jsonResult.put("firstName", result.getFirstName());
            jsonResult.put("fullDocumentImage", SerializationUtils.encodeImageBase64(result.getFullDocumentImage()));
            jsonResult.put("issuingAuthority", result.getIssuingAuthority());
            jsonResult.put("licenceCategories", result.getLicenceCategories());
            jsonResult.put("number", result.getNumber());
            jsonResult.put("placeOfBirth", result.getPlaceOfBirth());
            jsonResult.put("signatureImage", SerializationUtils.encodeImageBase64(result.getSignatureImage()));
            jsonResult.put("surname", result.getSurname());
            jsonResult.put("validFrom", SerializationUtils.serializeDate(result.getValidFrom()));
            jsonResult.put("validUntil", SerializationUtils.serializeDate(result.getValidUntil()));
        } catch (JSONException e) {
            // see https://developer.android.com/reference/org/json/JSONException
            throw new RuntimeException(e);
        }
        return jsonResult;
    }

    @Override
    public String getJsonName() {
        return "SpainDlFrontRecognizer";
    }

    @Override
    public Class<?> getRecognizerClass() {
        return com.microblink.entities.recognizers.blinkid.spain.SpainDlFrontRecognizer.class;
    }
}