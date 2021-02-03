/*
 * Copyright (C) ffem (Foundation for Environmental Monitoring)
 *
 * This file is part of ffem reports
 *
 * ffem Reports is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * ffem Reports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ffem Reports. If not, see <http://www.gnu.org/licenses/>.
 */

package io.ffem.reports.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.SparseArray;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.ffem.reports.R;
import io.ffem.reports.RecommendationFragment;
import io.ffem.reports.WaterReportFragment;
import io.ffem.reports.common.SensorConstants;
import io.ffem.reports.model.RecommendationInfo;
import io.ffem.reports.model.Result;
import io.ffem.reports.model.TestInfo;
import io.ffem.reports.util.AlertUtil;
import io.ffem.reports.util.AssetsManager;
import io.ffem.reports.viewmodel.TestListViewModel;

public class RecommendActivity extends BaseActivity {

    private static final String MESSAGE_TWO_LINE_FORMAT = "%s%n%n%s";

    private static final String DATE_TIME_FORMAT = "dd MMM yyyy HH:mm";
    private static final String DATE_FORMAT = "dd MMM yyyy";
    private static final String url = "https://soilhealth.dac.gov.in/calculator/calculator";

    private final Activity activity = this;
    private final RecommendationInfo recommendationInfo = new RecommendationInfo();

    //    private final WaterTestInfo waterTestInfo = new WaterTestInfo();
    boolean timeout = true;
    private TestInfo testInfo;
    private String printTemplate;
    private String date;
    private String uuid;
    private RecommendationFragment recommendationFragment;

    public static boolean isNumeric(String strNum) {
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);


        FragmentManager fragmentManager = getSupportFragmentManager();

        getTestSelectedByExternalApp(getIntent());

//        if (testInfo.getSubtype() == API) {
//            sendDummyResultForDebugging();
//            finish();
//        }

        if ("ff51c68c-faec-49e9-87b4-0880684be446".equals(uuid)) {
            setTitle("Water Test Report");
            WaterReportFragment waterReportFragment = WaterReportFragment.newInstance("", "");
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, waterReportFragment,
                            WaterReportFragment.class.getSimpleName()).commit();
            printTemplate = AssetsManager.getInstance(this)
                    .loadJsonFromAsset("templates/water_test_template.html");
        } else {
            setTitle("Fertilizer Recommendation");
            recommendationFragment = RecommendationFragment.newInstance("", "");
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, recommendationFragment,
                            RecommendationFragment.class.getSimpleName()).commit();
            printTemplate = AssetsManager.getInstance(this)
                    .loadJsonFromAsset("templates/recommendation_template.html");
            getRecommendation();
        }
    }

//    @Override
//    public void onAttachFragment(Fragment fragment) {
//        super.onAttachFragment(fragment);
//
//        switch (uuid) {
//            case "ff51c68c-faec-49e9-87b4-0880684be446":
//                if (getWaterReport()) {
//                    prepareWaterTestPrintDocument();
//                }
//                break;
//            default:
//                getRecommendation();
//        }
//    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    private void getTestSelectedByExternalApp(Intent intent) {

        uuid = intent.getStringExtra(SensorConstants.TEST_ID);
        if (uuid != null) {
            final TestListViewModel viewModel =
                    ViewModelProviders.of(this).get(TestListViewModel.class);
            testInfo = viewModel.getTestInfo(uuid);

            if (testInfo != null && intent.getExtras() != null) {
                for (int i = 0; i < intent.getExtras().keySet().size(); i++) {
                    String code = Objects.requireNonNull(intent.getExtras().keySet().toArray())[i].toString();
                    if (!code.equals(SensorConstants.TEST_ID) && !code.contains("__")) {
                        Pattern pattern = Pattern.compile("_(\\d*?)$");
                        Matcher matcher = pattern.matcher(code);
                        if (matcher.find()) {
                            testInfo.setResultSuffix(matcher.group(0));
                        } else if (code.contains("_x")) {
                            testInfo.setResultSuffix(code.substring(code.indexOf("_x")));
                        }
                    }
                }
            }
        }

        if (testInfo == null) {
            setTitle(R.string.notFound);
            alertTestTypeNotSupported();
        }
    }

    /*
    private boolean getWaterReport() {
        waterTestInfo.testerName = getStringExtra("Tester name");
        waterTestInfo.phoneNumber = getStringExtra("Phone number");
        waterTestInfo.lake = getStringExtra("Lake");
        waterTestInfo.location = getStringExtra("Location");
        waterTestInfo.date = getStringExtra("Date and time");
        waterTestInfo.time = getStringExtra("Time");

        if (waterTestInfo.testerName.isEmpty() || waterTestInfo.lake.isEmpty()) {
            Toast.makeText(this,
                    "All details should be filled for generating a test report.",
                    Toast.LENGTH_LONG).show();
            finish();
            return false;
        }

        waterTestInfo.nitrateResult = getStringExtra("Nitrate", "");
        waterTestInfo.nitrateUnit = testInfo.getResults().get(0).getUnit();
        waterTestInfo.phosphateResult = getStringExtra("Phosphate", "");
        waterTestInfo.phosphateUnit = testInfo.getResults().get(1).getUnit();
        waterTestInfo.pHResult = getStringExtra("pH", "");
        waterTestInfo.pHUnit = testInfo.getResults().get(2).getUnit();
        waterTestInfo.dissolvedOxygenResult = getStringExtra("Dissolved Oxygen", "");
        waterTestInfo.dissolvedOxygenUnit = testInfo.getResults().get(3).getUnit();

        if (waterTestInfo.nitrateResult.isEmpty() || waterTestInfo.phosphateResult.isEmpty() ||
                waterTestInfo.pHResult.isEmpty() || waterTestInfo.dissolvedOxygenResult.isEmpty()) {
            Toast.makeText(this,
                    "All tests have to be completed for generating the test report",
                    Toast.LENGTH_LONG).show();
            finish();
        }

        waterReportFragment.displayResult(waterTestInfo);

        return true;
    }

    private void prepareWaterTestPrintDocument() {
        date = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.US).format(Calendar.getInstance().getTime());
        printTemplate = printTemplate.replace("#DateTime#", date);
        date = new SimpleDateFormat(DATE_FORMAT, Locale.US).format(Calendar.getInstance().getTime());
//        printTemplate = printTemplate.replace("#Date#", date);

        printTemplate = printTemplate.replace("#TesterName#", waterTestInfo.testerName);
        printTemplate = printTemplate.replace("#PhoneNumber#", waterTestInfo.phoneNumber);
        printTemplate = printTemplate.replace("#Lake#", waterTestInfo.lake);
        printTemplate = printTemplate.replace("#Location#", waterTestInfo.location);
        printTemplate = printTemplate.replace("#Date#", waterTestInfo.date);
        printTemplate = printTemplate.replace("#Time#", waterTestInfo.time);

        if (waterTestInfo.geoLocation != null && !waterTestInfo.geoLocation.isEmpty()) {
            String[] geoValues = waterTestInfo.geoLocation.split(" ");
            for (int i = 0; i < geoValues.length; i++) {
                // Also show unit (m) for last two values
                printTemplate = printTemplate.replace("#Geo" + i + "#",
                        i > 1 ? geoValues[i] + "m" : geoValues[i]);
            }
        }

        printTemplate = printTemplate.replace("#Nitrate#", waterTestInfo.nitrateResult);
        printTemplate = printTemplate.replace("#Nitrate_Unit#", waterTestInfo.nitrateUnit);
        printTemplate = printTemplate.replace("#Nitrate_A#", "High");

        printTemplate = printTemplate.replace("#Phosphate#", waterTestInfo.phosphateResult);
        printTemplate = printTemplate.replace("#Phosphate_Unit#", waterTestInfo.phosphateUnit);
        printTemplate = printTemplate.replace("#Phosphate_A#", "Normal");

        printTemplate = printTemplate.replace("#pH#", waterTestInfo.pHResult);
        printTemplate = printTemplate.replace("#pH_Unit#", waterTestInfo.pHUnit);
        printTemplate = printTemplate.replace("#pH_A#", "Normal");

        printTemplate = printTemplate.replace("#DissolvedOxygen#", waterTestInfo.dissolvedOxygenResult);
        printTemplate = printTemplate.replace("#DissolvedOxygen_Unit#", waterTestInfo.dissolvedOxygenUnit);
        printTemplate = printTemplate.replace("#DissolvedOxygen_A#", "Low");

        printTemplate = printTemplate.replaceAll("#.*?#", "");
    }
    */

    /**
     * Alert displayed when an unsupported contaminant test type was requested.
     */
    private void alertTestTypeNotSupported() {

        String message = getString(R.string.errorTestNotAvailable);
        message = String.format(MESSAGE_TWO_LINE_FORMAT, message, getString(R.string.pleaseContactSupport));

        AlertUtil.showAlert(this, R.string.cannotStartTest, message,
                R.string.ok,
                (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    finish();
                }, null,
                dialogInterface -> {
                    dialogInterface.dismiss();
                    finish();
                }
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createWebPrintJob(WebView webView) {

        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);

        String jobName = "Fertilizer Recommendation - " + date;

        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(jobName);

//        PrintAttributes attrib = new PrintAttributes.Builder()
//                .setMediaSize(PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE)
//                . build();

        printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void getRecommendation() {

        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.just_a_moment));
        pd.setCancelable(false);
        pd.show();

        WebView webView = new WebView(this);

        webView.getSettings().setJavaScriptEnabled(true);

        String state = getStringExtra("State");
        String district = getStringExtra("District");
        String cropGroup = getStringExtra("Crop_Group");
        recommendationInfo.farmerName = getStringExtra("Farmer_name");
        recommendationInfo.phoneNumber = getStringExtra("Phone_number");
        recommendationInfo.sampleNumber = getStringExtra("Sample_number");
        recommendationInfo.villageName = getStringExtra("Village_name");
        recommendationInfo.geoLocation = getStringExtra("Geolocation");

        String crop = getIntent().getStringExtra("Crop");
        String soilType = getIntent().getStringExtra("Soil_Type");
        String varietyCode = getIntent().getStringExtra("Variety");
        String seasonCode = getIntent().getStringExtra("Season");

        if (recommendationInfo.farmerName.isEmpty() || recommendationInfo.sampleNumber.isEmpty() ||
                state.isEmpty() || district.isEmpty() || cropGroup.isEmpty() || crop.isEmpty()) {
            Toast.makeText(this, R.string.error_values_not_filled,
                    Toast.LENGTH_LONG).show();
            pd.dismiss();
            finish();
            return;
        }

        recommendationInfo.nitrogenResult = getStringExtra("Available_Nitrogen", "");
        recommendationInfo.phosphorusResult = getStringExtra("Available_Phosphorous", "");
        recommendationInfo.potassiumResult = getStringExtra("Available_Potassium", "");
        recommendationInfo.pH = getStringExtra("pH", "");

//        recommendationInfo.nitrogenResult = "1";
//        recommendationInfo.phosphorusResult = "1";
//        recommendationInfo.potassiumResult = "1";

        if (recommendationInfo.nitrogenResult.isEmpty() || recommendationInfo.phosphorusResult.isEmpty() ||
                recommendationInfo.potassiumResult.isEmpty()) {
            Toast.makeText(this,
                    "All tests have to be completed before requesting a recommendation",
                    Toast.LENGTH_LONG).show();
            pd.dismiss();
            finish();
            return;
        }

        final String js = "javascript:document.getElementById('State_Code').value='" + state + "';StateChange();" +
                "javascript:document.getElementById('District_CodeDDL').value='" + district + "';DistrictChange('" + district + "');" +
                "javascript:document.getElementById('N').value='" + recommendationInfo.nitrogenResult + "';" +
                "javascript:document.getElementById('P').value='" + recommendationInfo.phosphorusResult + "';" +
                "javascript:document.getElementById('K').value='" + recommendationInfo.potassiumResult + "';" +
                "document.getElementsByClassName('myButton')[0].click();" +
                "javascript:document.getElementById('Group_Code').value='" + cropGroup + "';Crop(" + cropGroup + ");" +
                "javascript:document.getElementById('Crop_Code').value='" + crop + "';Variety(" + crop + ");" +
                "javascript:document.getElementById('Soil_type_code').value='" + soilType + "';GetDistinctValues(" + soilType + ");" +
                "javascript:document.getElementById('Variety_Code').value='" + varietyCode + "';GetDistinctValues(" + varietyCode + ");" +
                "javascript:document.getElementById('Season_Code').value='" + seasonCode + "';GetDistinctValues(" + seasonCode + ");" +
                "javascript:document.getElementById('AddCrop').click();" +
                "(function() { " +
                "return " +
                "document.getElementById('State_Code').options[document.getElementById('State_Code').selectedIndex].text + ',' +" +
                "document.getElementById('District_CodeDDL').options[document.getElementById('District_CodeDDL').selectedIndex].text + ',' +" +
                "document.getElementById('Crop_Code').options[document.getElementById('Crop_Code').selectedIndex].text + ',' +" +
                "document.getElementById('Soil_type_code').options[document.getElementById('Soil_type_code').selectedIndex].text + ',' +" +
                "document.getElementById('Variety_Code').options[document.getElementById('Variety_Code').selectedIndex].text + ',' +" +
                "document.getElementById('Season_Code').options[document.getElementById('Season_Code').selectedIndex].text + ',' +" +
                "document.getElementById('C1F1').options[document.getElementById('C1F1').selectedIndex].text + ',' +" +
                "document.getElementById('Comb1_Fert1_Rec_dose1').value + ',' +" +
                "document.getElementById('C1F2').options[document.getElementById('C1F2').selectedIndex].text + ',' +" +
                "document.getElementById('Comb1_Fert2_Rec_dose1').value + ',' +" +
                "document.getElementById('C1F3').options[document.getElementById('C1F3').selectedIndex].text + ',' +" +
                "document.getElementById('Comb1_Fert3_Rec_dose1').value + ',' +" +
                "document.getElementById('C2F1').options[document.getElementById('C2F1').selectedIndex].text + ',' +" +
                "document.getElementById('Comb2_Fert1_Rec_dose1').value + ',' +" +
                "document.getElementById('C2F2').options[document.getElementById('C2F2').selectedIndex].text + ',' +" +
                "document.getElementById('Comb2_Fert2_Rec_dose1').value + ',' +" +
                "document.getElementById('C2F3').options[document.getElementById('C2F3').selectedIndex].text + ',' +" +
                "document.getElementById('Comb2_Fert3_Rec_dose1').value;" +
                "})();";

        Runnable run = () -> {
            if (timeout) {
                pd.dismiss();
                webView.stopLoading();
                finish();
                Toast.makeText(activity, "Connection Timed out", Toast.LENGTH_SHORT).show();
            }
        };
        Handler myHandler = new Handler(Looper.myLooper());
        myHandler.postDelayed(run, 20000);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (description.contains("ERR_INTERNET")) {
                    Toast.makeText(activity,
                            getString(R.string.no_data_connection),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
                }
                timeout = false;
                pd.dismiss();
                finish();
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError error) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, error.getErrorCode(), error.getDescription().toString(), req.getUrl().toString());
            }

            public void onPageFinished(WebView view, String url) {

                view.evaluateJavascript(js, s -> {

                    String[] values = s.replace("\"", "").split(",");
                    if (values.length > 10) {
                        timeout = false;

                        if (s.contains("NaN")) {
                            returnEmptyResult(pd);

                        } else {
                            if (!displayResult(values)) {
                                returnEmptyResult(pd);
                            }
                        }
                    }

                    (new Handler()).postDelayed(pd::dismiss, 5000);
                });
            }
        });

        webView.loadUrl(url);
    }

    private void returnEmptyResult(ProgressDialog pd) {
        Toast.makeText(activity, "Could not calculate recommendation. Please check all entries", Toast.LENGTH_LONG).show();
        Intent resultIntent = new Intent();
        SparseArray<String> results = new SparseArray<>();
        for (int i = 0; i < testInfo.getResults().size(); i++) {
            Result result = testInfo.getResults().get(i);
            resultIntent.putExtra(result.getName().replace(" ", "_")
                    + testInfo.getResultSuffix(), "");
            results.append(result.getId(), "");
        }
        setResult(Activity.RESULT_OK, resultIntent);
        pd.dismiss();
        finish();
    }

    private boolean displayResult(String[] values) {

        Intent resultIntent = new Intent();

        SparseArray<String> results = new SparseArray<>();
        recommendationInfo.state = values[0];
        recommendationInfo.district = values[1];
        recommendationInfo.crop = values[2];

        int startIndex = 0;
        for (int i = 3; i < values.length; i++) {
            if (values[i].equals("NaN") || isNumeric(values[i])) {
                startIndex = i - 1;
                break;
            }
        }

        double value = 0;

        for (int i = 0; i < testInfo.getResults().size(); i++) {
            Result result = testInfo.getResults().get(i);
            resultIntent.putExtra(result.getName().trim().replace(" ", "_")
                    + testInfo.getResultSuffix(), values[i + startIndex]);

            results.append(result.getId(), result.getResult());

            if ((i & 1) != 0) {
                try {
                    value += Double.parseDouble(values[i + startIndex]);
                } catch (NumberFormatException | NullPointerException nfe) {
                    value = 0;
                }
            }

            printTemplate = printTemplate.replace("#Value" + i + "#", values[i + startIndex]);
        }

        if (value == 0) {
            return false;
        }

        preparePrintDocument();

        recommendationInfo.values = Arrays.copyOfRange(values, startIndex, values.length);

        recommendationFragment.displayResult(recommendationInfo);

        setResult(Activity.RESULT_OK, resultIntent);

        return true;
    }

    private void preparePrintDocument() {
        date = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.US).format(Calendar.getInstance().getTime());
        printTemplate = printTemplate.replace("#DateTime#", date);
        date = new SimpleDateFormat(DATE_FORMAT, Locale.US).format(Calendar.getInstance().getTime());
        printTemplate = printTemplate.replace("#Date#", date);
        printTemplate = printTemplate.replace("#FarmerName#", recommendationInfo.farmerName);
        printTemplate = printTemplate.replace("#PhoneNumber#", recommendationInfo.phoneNumber);
        printTemplate = printTemplate.replace("#VillageName#", recommendationInfo.villageName);
        printTemplate = printTemplate.replace("#State#", recommendationInfo.state);
        printTemplate = printTemplate.replace("#District#", recommendationInfo.district);
        printTemplate = printTemplate.replace("#SampleNumber#", recommendationInfo.sampleNumber);
        printTemplate = printTemplate.replace("#Crop#", recommendationInfo.crop);

        if (recommendationInfo.geoLocation != null && !recommendationInfo.geoLocation.isEmpty()) {
            String[] geoValues = recommendationInfo.geoLocation.split(" ");
            for (int i = 0; i < geoValues.length; i++) {
                // Also show unit (m) for last two values
                printTemplate = printTemplate.replace("#Geo" + i + "#",
                        i > 1 ? geoValues[i] + "m" : geoValues[i]);
            }
        }

        printTemplate = printTemplate.replace("#Nitrogen#", recommendationInfo.nitrogenResult);
        printTemplate = printTemplate.replace("#Phosphorus#", recommendationInfo.phosphorusResult);
        printTemplate = printTemplate.replace("#Potassium#", recommendationInfo.potassiumResult);
        printTemplate = printTemplate.replace("#pH#", recommendationInfo.pH);

        printTemplate = printTemplate.replaceAll("#.*?#", "");
    }

    public void onPrintClick(View view) {
        WebView printWebView = new WebView(this);
        printWebView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                createWebPrintJob(view);
            }
        });

        printWebView.loadDataWithBaseURL("file:///android_asset/images/", printTemplate,
                "text/HTML", "UTF-8", null);
    }

    public void onSaveClick(View view) {
        finish();
    }

    private String getStringExtra(String key) {
        return getStringExtra(key, "");
    }

    @SuppressWarnings("SameParameterValue")
    private String getStringExtra(String key, String defaultValue) {
        String value = getIntent().getStringExtra(key);
        if (value == null) {
            return defaultValue;
        }

        return value;
    }
}
