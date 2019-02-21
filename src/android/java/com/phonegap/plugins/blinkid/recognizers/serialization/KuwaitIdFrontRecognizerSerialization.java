package com.phonegap.plugins.blinkid.recognizers.serialization;

import com.microblink.entities.recognizers.Recognizer;
import com.phonegap.plugins.blinkid.recognizers.RecognizerSerialization;

import org.json.JSONException;
import org.json.JSONObject;

public final class KuwaitIdFrontRecognizerSerialization implements RecognizerSerialization {

    @Override
    public Recognizer<?, ?> createRecognizer(JSONObject jsonRecognizer) {
        com.microblink.entities.recognizers.blinkid.kuwait.KuwaitIdFrontRecognizer recognizer = new com.microblink.entities.recognizers.blinkid.kuwait.KuwaitIdFrontRecognizer();
        recognizer.setDetectGlare(jsonRecognizer.optBoolean("detectGlare", true));
        recognizer.setExtractBirthDate(jsonRecognizer.optBoolean("extractBirthDate", true));
        recognizer.setExtractName(jsonRecognizer.optBoolean("extractName", true));
        recognizer.setExtractNationality(jsonRecognizer.optBoolean("extractNationality", true));
        recognizer.setExtractSex(jsonRecognizer.optBoolean("extractSex", true));
        recognizer.setFaceImageDpi(jsonRecognizer.optInt("faceImageDpi", 250));
        recognizer.setFullDocumentImageDpi(jsonRecognizer.optInt("fullDocumentImageDpi", 250));
        recognizer.setFullDocumentImageExtensionFactors(BlinkIDSerializationUtils.deserializeExtensionFactors(jsonRecognizer.optJSONObject("fullDocumentImageExtensionFactors")));
        recognizer.setReturnFaceImage(jsonRecognizer.optBoolean("returnFaceImage", false));
        recognizer.setReturnFullDocumentImage(jsonRecognizer.optBoolean("returnFullDocumentImage", false));
        return recognizer;
    }

    @Override
    public JSONObject serializeResult(Recognizer<?, ?> recognizer) {
        com.microblink.entities.recognizers.blinkid.kuwait.KuwaitIdFrontRecognizer.Result result = ((com.microblink.entities.recognizers.blinkid.kuwait.KuwaitIdFrontRecognizer)recognizer).getResult();
        JSONObject jsonResult = new JSONObject();
        try {
            SerializationUtils.addCommonResultData(jsonResult, result);
            jsonResult.put("birthDate", SerializationUtils.serializeDate(result.getBirthDate()));
            jsonResult.put("civilIdNumber", result.getCivilIdNumber());
            jsonResult.put("expiryDate", SerializationUtils.serializeDate(result.getExpiryDate()));
            jsonResult.put("faceImage", SerializationUtils.encodeImageBase64(result.getFaceImage()));
            jsonResult.put("fullDocumentImage", SerializationUtils.encodeImageBase64(result.getFullDocumentImage()));
            jsonResult.put("name", result.getName());
            jsonResult.put("nationality", result.getNationality());
            jsonResult.put("sex", result.getSex());
        } catch (JSONException e) {
            // see https://developer.android.com/reference/org/json/JSONException
            throw new RuntimeException(e);
        }
        return jsonResult;
    }

    @Override
    public String getJsonName() {
        return "KuwaitIdFrontRecognizer";
    }

    @Override
    public Class<?> getRecognizerClass() {
        return com.microblink.entities.recognizers.blinkid.kuwait.KuwaitIdFrontRecognizer.class;
    }
}