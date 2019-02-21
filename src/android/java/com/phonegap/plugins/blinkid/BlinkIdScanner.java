/**
 * PhoneGap is available under *either* the terms of the modified BSD license *or* the
 * MIT License (2008). See http://opensource.org/licenses/alphabetical for full text.
 *
 * Copyright (c) Matt Kane 2010
 * Copyright (c) 2011, IBM Corporation
 * Copyright (c) 2013, Maciej Nux Jaros
 */
package com.phonegap.plugins.blinkid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.microblink.MicroblinkSDK;
import com.microblink.intent.IntentDataTransferMode;
import com.microblink.entities.recognizers.RecognizerBundle;
import com.microblink.uisettings.UISettings;
import com.phonegap.plugins.blinkid.overlays.OverlaySettingsSerializers;
import com.phonegap.plugins.blinkid.recognizers.RecognizerSerializers;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BlinkIdScanner extends CordovaPlugin {

    private static final int REQUEST_CODE = 1337;

    private static final String SCAN_WITH_CAMERA = "scanWithCamera";
    private static final String CANCELLED = "cancelled";

    private static final String RESULT_LIST = "resultList";

    private CallbackContext mCallbackContext;
    private RecognizerBundle mRecognizerBundle;

    /**
     * Constructor.
     */
    public BlinkIdScanner() {
    }

    /**
     * Executes the request.
     *
     * This method is called from the WebView thread. To do a non-trivial amount
     * of work, use: cordova.getThreadPool().execute(runnable);
     *
     * To run on the UI thread, use:
     * cordova.getActivity().runOnUiThread(runnable);
     *
     * @param action
     *            The action to execute.
     * @param args
     *            The exec() arguments.
     * @param callbackContext
     *            The callback context used when calling back into JavaScript.
     * @return Whether the action was valid.
     *
     * @sa
     *     https://github.com/apache/cordova-android/blob/master/framework/src/org
     *     /apache/cordova/CordovaPlugin.java
     */
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
		final CordovaPlugin that = this;
		
        mCallbackContext = callbackContext;

        try {
            if (action.equals(SCAN_WITH_CAMERA)) {
                JSONObject jsonOverlaySettings = args.getJSONObject(0);
                JSONObject jsonRecognizerCollection = args.getJSONObject(1);
                JSONObject jsonLicenses = args.getJSONObject(2);

                setLicense(jsonLicenses);
                mRecognizerBundle = RecognizerSerializers.INSTANCE.deserializeRecognizerCollection(jsonRecognizerCollection);
                UISettings overlaySettings = OverlaySettingsSerializers.INSTANCE.getOverlaySettings(jsonOverlaySettings, mRecognizerBundle);

                // unable to use ActivityRunner because we need to use cordova's activity launcher
                Intent intent = new Intent(that.cordova.getActivity().getBaseContext(), overlaySettings.getTargetActivity());
                overlaySettings.saveToIntent(intent);
                this.cordova.startActivityForResult(that, intent, REQUEST_CODE);
            } else {
                return false;
            }
            return true;
        } catch (JSONException e) {
            mCallbackContext.error("JSON error: " + e.getMessage());
            return false;
        }
    }

    private void setLicense( JSONObject jsonLicense ) throws JSONException {
		final CordovaPlugin that = this;
		
        MicroblinkSDK.setShowTimeLimitedLicenseWarning(
                jsonLicense.optBoolean("showTimeLimitedLicenseKeyWarning", true)
        );
        String androidLicense = jsonLicense.getString("android");
        String licensee = jsonLicense.optString("licensee", null);
        //Context context = cordova.getContext();
		Context context = that.cordova.getActivity().getBaseContext();
		
        if (licensee == null) {
            MicroblinkSDK.setLicenseKey(androidLicense, context);
        } else {
            MicroblinkSDK.setLicenseKey(androidLicense, licensee, context);
        }
        MicroblinkSDK.setIntentDataTransferMode(IntentDataTransferMode.PERSISTED_OPTIMISED);
    }

    /**
     * Called when the scanner intent completes.
     *
     * @param requestCode
     *            The request code originally supplied to
     *            startActivityForResult(), allowing you to identify who this
     *            result came from.
     * @param resultCode
     *            The integer result code returned by the child activity through
     *            its setResult().
     * @param data
     *            An Intent, which can return result data to the caller (various
     *            data can be attached to Intent "extras").
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                // load bundle
                mRecognizerBundle.loadFromIntent(data);

                JSONObject result = new JSONObject();
                try {
                    result.put(CANCELLED, false);

                    JSONArray resultList = RecognizerSerializers.INSTANCE.serializeRecognizerResults(mRecognizerBundle.getRecognizers());
                    result.put(RESULT_LIST, resultList);
                } catch(JSONException e) {
                    throw new RuntimeException(e);
                }

                mCallbackContext.success(result);

            } else if (resultCode == Activity.RESULT_CANCELED) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put(CANCELLED, true);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                mCallbackContext.success(obj);

            } else {
                mCallbackContext.error("Unexpected error");
            }
        }
    }
}
