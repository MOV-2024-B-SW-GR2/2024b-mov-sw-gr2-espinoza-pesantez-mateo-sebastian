package com.example.deber

import android.os.Parcel
import android.os.Parcelable

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val categoria: String,
    val marcaId: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
        parcel.writeDouble(precio)
        parcel.writeString(categoria)
        parcel.writeInt(marcaId)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Producto> {
        override fun createFromParcel(parcel: Parcel): Producto = Producto(parcel)
        override fun newArray(size: Int): Array<Producto?> = arrayOfNulls(size)
    }
}
