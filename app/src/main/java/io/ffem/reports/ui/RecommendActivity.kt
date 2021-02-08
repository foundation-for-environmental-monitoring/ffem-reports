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
package io.ffem.reports.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.SparseArray
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import io.ffem.reports.R
import io.ffem.reports.RecommendationFragment
import io.ffem.reports.common.SensorConstants
import io.ffem.reports.model.RecommendationInfo
import io.ffem.reports.model.TestInfo
import io.ffem.reports.util.AlertUtil.showAlert
import io.ffem.reports.util.AssetsManager.Companion.getInstance
import io.ffem.reports.viewmodel.TestListViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import timber.log.Timber
import java.io.IOException
import java.io.StringReader
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class RecommendActivity : BaseActivity() {
    private val activity: Activity = this
    private val recommendationInfo = RecommendationInfo()

    //    private final WaterTestInfo waterTestInfo = new WaterTestInfo();
    var timeout = true
    private var testInfo: TestInfo? = null
    private var printTemplate: String? = null
    private var date: String? = null
    private var uuid: String? = null
    private var recommendationFragment: RecommendationFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend)
        val fragmentManager = supportFragmentManager
        getTestSelectedByExternalApp(intent)

//        if (testInfo.getSubtype() == API) {
//            sendDummyResultForDebugging();
//            finish();
//        }
        if ("ef9f8703-36b1-407c-9175-4184f6fc974f" == uuid) {
            title = "Fertilizer Recommendation"
            recommendationFragment = RecommendationFragment.newInstance()
            fragmentManager.beginTransaction()
                .add(
                    R.id.fragment_container, recommendationFragment!!,
                    RecommendationFragment::class.java.simpleName
                ).commit()
            printTemplate = getInstance(this)!!
                .loadJsonFromAsset("templates/recommendation_template.html")
            recommendation
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
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        }
    }

    private fun getTestSelectedByExternalApp(intent: Intent) {
        uuid = intent.getStringExtra(SensorConstants.TEST_ID)
        if (uuid != null) {
            val viewModel = ViewModelProvider(this).get(
                TestListViewModel::class.java
            )
            testInfo = viewModel.getTestInfo(uuid)
            if (testInfo != null && intent.extras != null) {
                for (i in intent.extras!!.keySet().indices) {
                    val code = Objects.requireNonNull<Array<Any>>(
                        intent.extras!!.keySet().toTypedArray()
                    )[i].toString()
                    if (code != SensorConstants.TEST_ID && !code.contains("__")) {
                        val pattern = Pattern.compile("_(\\d*?)$")
                        val matcher = pattern.matcher(code)
                        if (matcher.find()) {
                            testInfo!!.resultSuffix = matcher.group(0)
                        } else if (code.contains("_x")) {
                            testInfo!!.resultSuffix = code.substring(code.indexOf("_x"))
                        }
                    }
                }
            }
        }
        if (testInfo == null) {
            setTitle(R.string.notFound)
            alertTestTypeNotSupported()
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
    private fun alertTestTypeNotSupported() {
        var message = getString(R.string.errorTestNotAvailable)
        message = String.format(
            MESSAGE_TWO_LINE_FORMAT,
            message,
            getString(R.string.pleaseContactSupport)
        )
        showAlert(this, R.string.cannotStartTest, message,
            R.string.ok,
            { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                finish()
            }, null,
            { dialogInterface: DialogInterface ->
                dialogInterface.dismiss()
                finish()
            }
        )
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun createWebPrintJob(webView: WebView) {
        val printManager = this
            .getSystemService(PRINT_SERVICE) as PrintManager
        val jobName = "Fertilizer Recommendation - $date"
        val printAdapter = webView.createPrintDocumentAdapter(jobName)

//        PrintAttributes attrib = new PrintAttributes.Builder()
//                .setMediaSize(PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE)
//                . build();
        printManager.print(jobName, printAdapter, PrintAttributes.Builder().build())
    }// Redirect to deprecated method, so you can use it in all SDK versions

    //        recommendationInfo.nitrogenResult = "1";
//        recommendationInfo.phosphorusResult = "1";
//        recommendationInfo.potassiumResult = "1";
    @get:SuppressLint("SetJavaScriptEnabled")
    private val recommendation: Unit
        get() {
            val pd = ProgressDialog(this)
            pd.setMessage(getString(R.string.just_a_moment))
            pd.setCancelable(false)
            pd.show()
            val webView = WebView(this)
            webView.settings.javaScriptEnabled = true
            parseXml(intent.getStringExtra("survey_data"), intent)
            val state = getStringExtra("State")
            val district = getStringExtra("District")
            val cropGroup = getStringExtra("Crop_Group")
            recommendationInfo.farmerName = getStringExtra("Farmer_name")
            recommendationInfo.phoneNumber = getStringExtra("Phone_number")
            recommendationInfo.sampleNumber = getStringExtra("Sample_number")
            recommendationInfo.villageName = getStringExtra("Village_name")
            recommendationInfo.geoLocation = getStringExtra("Geolocation")
            val crop = intent.getStringExtra("Crop")
            val soilType = intent.getStringExtra("Soil_Type")
            val varietyCode = intent.getStringExtra("Variety")
            val seasonCode = intent.getStringExtra("Season")
            if (recommendationInfo.farmerName!!.isEmpty() || recommendationInfo.sampleNumber!!.isEmpty() ||
                state.isEmpty() || district.isEmpty() || cropGroup.isEmpty() || crop!!.isEmpty()
            ) {
                Toast.makeText(
                    this, R.string.error_values_not_filled,
                    Toast.LENGTH_LONG
                ).show()
                pd.dismiss()
                finish()
                return
            }
            recommendationInfo.nitrogenResult = getStringExtra("Available_Nitrogen", "")
            recommendationInfo.phosphorusResult = getStringExtra("Available_Phosphorous", "")
            recommendationInfo.potassiumResult = getStringExtra("Available_Potassium", "")
            recommendationInfo.pH = getStringExtra("pH", "")

//        recommendationInfo.nitrogenResult = "1";
//        recommendationInfo.phosphorusResult = "1";
//        recommendationInfo.potassiumResult = "1";
            if (recommendationInfo.nitrogenResult!!.isEmpty() || recommendationInfo.phosphorusResult!!.isEmpty() ||
                recommendationInfo.potassiumResult!!.isEmpty()
            ) {
                Toast.makeText(
                    this,
                    "All tests have to be completed before requesting a recommendation",
                    Toast.LENGTH_LONG
                ).show()
                pd.dismiss()
                finish()
                return
            }
            val js =
                "javascript:document.getElementById('State_Code').value='" + state + "';StateChange();" +
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
                        "})();"
            val run = Runnable {
                if (timeout) {
                    pd.dismiss()
                    webView.stopLoading()
                    finish()
                    Toast.makeText(activity, "Connection Timed out", Toast.LENGTH_SHORT).show()
                }
            }
            val myHandler = Handler(Looper.myLooper()!!)
            myHandler.postDelayed(run, 20000)
            webView.webViewClient = object : WebViewClient() {

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                }

                override fun onReceivedError(
                    view: WebView,
                    errorCode: Int,
                    description: String,
                    failingUrl: String
                ) {
                    if (description.contains("ERR_INTERNET")) {
                        Toast.makeText(
                            activity,
                            getString(R.string.no_data_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(activity, description, Toast.LENGTH_SHORT).show()
                    }
                    timeout = false
                    pd.dismiss()
                    finish()
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)
                }

//                @TargetApi(Build.VERSION_CODES.M)
//                override fun onReceivedError(view: WebView, req: WebResourceRequest, error: WebResourceError) {
//                    // Redirect to deprecated method, so you can use it in all SDK versions
//                    onReceivedError(view, error.errorCode, error.description.toString(), req.url.toString())
//                }

                override fun onPageFinished(view: WebView, url: String) {
                    view.evaluateJavascript(js) { s: String ->
                        val values = s.replace("\"", "").split(",".toRegex()).toTypedArray()
                        if (values.size > 10) {
                            timeout = false
                            if (s.contains("NaN")) {
                                returnEmptyResult(pd)
                            } else {
                                if (!displayResult(values)) {
                                    returnEmptyResult(pd)
                                }
                            }
                        }
                        GlobalScope.launch {
                            delay(5000)
                            pd.dismiss()
                        }
                    }
                }
            }
            webView.loadUrl(url)
        }

    private fun returnEmptyResult(pd: ProgressDialog) {
        Toast.makeText(
            activity,
            "Could not calculate recommendation. Please check all entries",
            Toast.LENGTH_LONG
        ).show()
        val resultIntent = Intent()
        val results = SparseArray<String>()
        for (i in testInfo!!.results!!.indices) {
            val result = testInfo!!.results!![i]
            resultIntent.putExtra(
                result.name!!.replace(" ", "_")
                        + testInfo!!.resultSuffix, ""
            )
            results.append(result.id, "")
        }
        setResult(RESULT_OK, resultIntent)
        pd.dismiss()
        finish()
    }

    private fun displayResult(values: Array<String>): Boolean {
        val resultIntent = Intent()
        val results = SparseArray<String>()
        recommendationInfo.state = values[0]
        recommendationInfo.district = values[1]
        recommendationInfo.crop = values[2]
        var startIndex = 0
        for (i in 3 until values.size) {
            if (values[i] == "NaN" || isNumeric(values[i])) {
                startIndex = i - 1
                break
            }
        }
        var value = 0.0
        for (i in testInfo!!.results!!.indices) {
            val result = testInfo!!.results!![i]
            resultIntent.putExtra(
                result.name!!.trim { it <= ' ' }.replace(" ", "_")
                        + testInfo!!.resultSuffix, values[i + startIndex]
            )
            results.append(result.id, result.result)
            if (i and 1 != 0) {
                try {
                    value += values[i + startIndex].toDouble()
                } catch (nfe: NumberFormatException) {
                    value = 0.0
                } catch (nfe: NullPointerException) {
                    value = 0.0
                }
            }
            printTemplate = printTemplate!!.replace("#Value$i#", values[i + startIndex])
        }
        if (value == 0.0) {
            return false
        }
        preparePrintDocument()
        recommendationInfo.values = values.copyOfRange(startIndex, values.size)
        recommendationFragment!!.displayResult(recommendationInfo)
        setResult(RESULT_OK, resultIntent)
        return true
    }

    private fun preparePrintDocument() {

        date = SimpleDateFormat(DATE_TIME_FORMAT, Locale.US).format(Calendar.getInstance().time)
        printTemplate = printTemplate!!.replace("#DateTime#", date!!)
        date = SimpleDateFormat(DATE_FORMAT, Locale.US).format(Calendar.getInstance().time)
        printTemplate = printTemplate!!.replace("#Date#", date!!)
        printTemplate = printTemplate!!.replace("#FarmerName#", recommendationInfo.farmerName!!)
        printTemplate = printTemplate!!.replace("#PhoneNumber#", recommendationInfo.phoneNumber!!)
        printTemplate = printTemplate!!.replace("#VillageName#", recommendationInfo.villageName!!)
        printTemplate = printTemplate!!.replace("#State#", recommendationInfo.state!!)
        printTemplate = printTemplate!!.replace("#District#", recommendationInfo.district!!)
        printTemplate = printTemplate!!.replace("#SampleNumber#", recommendationInfo.sampleNumber!!)
        printTemplate = printTemplate!!.replace("#Crop#", recommendationInfo.crop!!)
        if (recommendationInfo.geoLocation != null && recommendationInfo.geoLocation!!.isNotEmpty()) {
            val geoValues = recommendationInfo.geoLocation!!.split(" ".toRegex()).toTypedArray()
            for (i in geoValues.indices) {
                // Also show unit (m) for last two values
                printTemplate = printTemplate!!.replace(
                    "#Geo$i#",
                    if (i > 1) geoValues[i] + "m" else geoValues[i]
                )
            }
        }
        printTemplate = printTemplate!!.replace("#Nitrogen#", recommendationInfo.nitrogenResult!!)
        printTemplate =
            printTemplate!!.replace("#Phosphorus#", recommendationInfo.phosphorusResult!!)
        printTemplate = printTemplate!!.replace("#Potassium#", recommendationInfo.potassiumResult!!)
        printTemplate = printTemplate!!.replace("#pH#", recommendationInfo.pH!!)
        printTemplate = printTemplate!!.replace("#.*?#".toRegex(), "")
    }

    fun onPrintClick(@Suppress("UNUSED_PARAMETER") view: View?) {
        val printWebView = WebView(this)
        printWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return false
            }

            override fun onPageFinished(view: WebView, url: String) {
                createWebPrintJob(view)
            }
        }
        printWebView.loadDataWithBaseURL(
            "file:///android_asset/images/", printTemplate!!,
            "text/HTML", "UTF-8", null
        )
    }

    fun onSaveClick(@Suppress("UNUSED_PARAMETER") view: View?) {
        finish()
    }

    private fun getStringExtra(key: String): String {
        return getStringExtra(key, "")
    }

    private fun getStringExtra(key: String, defaultValue: String): String {
        return intent.getStringExtra(key) ?: return defaultValue
    }

    private fun parseXml(xmlString: String?, intent: Intent) {
        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            xpp.setInput(StringReader(xmlString))
            var eventType = xpp.eventType
            var text = ""
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.TEXT) {
                    text = xpp.text
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (!xpp.name.contains("__") && !xpp.name.contains("instanceID") && text.isNotEmpty()) {
                        intent.putExtra(xpp.name, text)
                        Timber.e("%s : %s", xpp.name, text)
                        text = ""
                    }
                }
                eventType = xpp.next()
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val MESSAGE_TWO_LINE_FORMAT = "%s%n%n%s"
        private const val DATE_TIME_FORMAT = "dd MMM yyyy HH:mm"
        private const val DATE_FORMAT = "dd MMM yyyy"
        private const val url = "https://soilhealth.dac.gov.in/calculator/calculator"
        fun isNumeric(strNum: String): Boolean {
            try {
                strNum.toDouble()
            } catch (nfe: NumberFormatException) {
                return false
            } catch (nfe: NullPointerException) {
                return false
            }
            return true
        }
    }
}