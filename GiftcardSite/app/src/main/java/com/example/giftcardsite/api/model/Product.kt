package com.example.giftcardsite.api.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Product(
        @SerializedName("product_id")
        val productId: Int,
        @SerializedName("product_name")
        val productName: String?,
        @SerializedName("product_image_path")
        val productImageLink: String?,
        @SerializedName("recommended_price")
        val recommendedPrice: Int,
        @SerializedName("description")
        val description: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(productId)
        parcel.writeString(productName)
        parcel.writeString(productImageLink)
        parcel.writeInt(recommendedPrice)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}