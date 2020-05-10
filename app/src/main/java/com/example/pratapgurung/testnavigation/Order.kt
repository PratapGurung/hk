package com.example.pratapgurung.testnavigation

import android.accounts.AuthenticatorDescription
import java.sql.Timestamp

data class Order (
    var orderId:String? = "",
    var requestedby:String? = "",
    var serviceType: String? = "",
    var address:String? = "",
    var city: String? = "",
    var state:String? = "",
    var zipCode: String? = "",
    var serviceHour: String? = "",
    var description: String? = "",
    var requestDate:String? = "",
    var completeByDate: String? = "",
    var acceptedby:String? = "",
    var rate:String? = "",
    var status:String? = "",
    var timestamp: Long
)