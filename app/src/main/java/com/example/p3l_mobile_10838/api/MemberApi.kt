package com.example.p3l_mobile_10838.api

class MemberApi {
    companion object {
        val BASE_URL = "https://g0fit.my.id/api/"
//        val BASE_URL = "https://kasihh.my.id/api/"

        val HISTORIMEMBER = BASE_URL + "getHistoryMember/"

        val GET_ALL_GYM_URL = BASE_URL + "indexGym/"
        val BATALGYM = BASE_URL + "batalGym/"
        val STOREDATAGYM = BASE_URL + "AddGym"
        val GET_BY_ID_URL = BASE_URL + "getDataMemberMobile/"

        val STOREDATABOOKINGKELAS = BASE_URL + "storeMobile"
        val DELETEDATABOOKINGKELAS = BASE_URL + "cancelBooking/"
        }
}