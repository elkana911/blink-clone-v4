package com.phonegap.plugins.blinkid.overlays;

import com.microblink.entities.recognizers.RecognizerBundle;
import com.microblink.uisettings.UISettings;

import org.json.JSONObject;

public interface OverlaySettingsSerialization {

    UISettings createUISettings(JSONObject jsonUISettings, RecognizerBundle recognizerBundle);

    String getJsonName();
}
