package com.example.deber

import android.os.Parcel
import android.os.Parcelable

data class Marca(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val fechaFundacion: String,
    val esActiva: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeString(descripcion)
        parcel.writeString(fechaFundacion)
        parcel.writeByte(if (esActiva) 1 else 0)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Marca> {
        override fun createFromParcel(parcel: Parcel): Marca = Marca(parcel)
        override fun newArray(size: Int): Array<Marca?> = arrayOfNulls(size)
    }
}
