package com.example.pratapgurung.testnavigation

import android.accounts.AuthenticatorDescription

data class Order (
    var orderId:String? = "",
    var requestedby:String? = "",
    var servicetype: String? = "",
    var address:String? = "",
    var city: String? = "",
    var state:String? = "",
    var zipCode: String? = "",
    var serviceHour: String? = "",
    var description: String? = "",
    var requestedDate:String? = "",
    var completeByDate: String? = "",
    var acceptedby:String? = "",
    var rate:String? = "",
    var status:String? = ""
)