package com.KAC.vikranthpublications.model

data class AttendanceDataClass(val login_date : String? = null,
                               val login_time : String? = null,
                               val login_location:String? = null,
                               val logout_time:String? = null,
                               val total_hours:String? = null,
                               val logout_location:String? = null)
