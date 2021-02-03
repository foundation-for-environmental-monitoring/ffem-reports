package io.ffem.reports.model

class RecommendationInfo {
    var farmerName: String? = null
    var phoneNumber: String? = null
    var villageName: String? = null
    var sampleNumber: String? = null
    var geoLocation: String? = null
    @JvmField
    var state: String? = null
    @JvmField
    var district: String? = null
    @JvmField
    var crop: String? = null
    @JvmField
    var nitrogenResult: String? = null
    @JvmField
    var phosphorusResult: String? = null
    @JvmField
    var potassiumResult: String? = null
    @JvmField
    var pH: String? = null
    lateinit var values: Array<String>
}