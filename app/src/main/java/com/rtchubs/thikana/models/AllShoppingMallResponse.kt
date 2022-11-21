package com.rtchubs.thikana.models

import java.io.Serializable

data class AllShoppingMallResponse(val code: Int?, val status: String?, val message: String?, val data: List<ShoppingMall>?)

data class ShoppingMall(val id: Int?, val name: String?, val email: String?, val address: String?, val phone: String?,
                        val level: Int?, val thumbnail: String?, val offday: String?, val created_at: String?,
                        val updated_at: String?, val lat: Double?, val long: Double?, val username: String?, val password: String?,
                        val mall_type: String?, val levels: List<ShoppingMallLevel>?) : Serializable

data class ShoppingMallLevel(val id: Int?, val name: String?, val block_number: String?,
                             val shopping_mall_id: Int?, val created_at: String?, val updated_at: String?) : Serializable

data class ShoppingMallGroup(val title: String, val type: String, val malls: ArrayList<ShoppingMall>)