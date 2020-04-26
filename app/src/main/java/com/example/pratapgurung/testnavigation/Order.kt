package com.example.pratapgurung.testnavigation

import android.accounts.AuthenticatorDescription

data class Order (
    var orderId:String? = "",
    var custId:String? = "",
    var serviceId: String? = "",
    var address:String? = "",
    var city: String? = "",
    var state:String? = "",
    var zipCode: String? = "",
    var serviceHour: String? = "",
    var description: String? = "",
    var requestDate:String? = "",
    var completDate: String? = "",
    var agentId:String? = "",
    var rate:String? = ""
)