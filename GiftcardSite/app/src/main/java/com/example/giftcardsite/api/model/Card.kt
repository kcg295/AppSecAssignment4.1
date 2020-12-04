package com.example.giftcardsite.api.model

import android.os.Parcel
import android.os.Parcelable

data class Card(val data: String?,
                val product: Product?,
                var amount: Double = 0.0,
                var used: Boolean = false,
                var id : Int) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readParcelable(Product::class.java.classLoader),
            parcel.readDouble(),
            parcel.readByte() != 0.toByte(),
            parcel.readInt()) {
    }

    fun setAmount(amount: Double): Int {
        if (amount >= 0) {
            this.amount = amount;
            return 0;
        }
        return -1;
    }

    fun setUsed() {
        this.used = true;
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(data)
        parcel.writeParcelable(product, flags)
        parcel.writeDouble(amount)
        parcel.writeByte(if (used) 1 else 0)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Card> {
        override fun createFromParcel(parcel: Parcel): Card {
            return Card(parcel)
        }

        override fun newArray(size: Int): Array<Card?> {
            return arrayOfNulls(size)
        }
    }

}