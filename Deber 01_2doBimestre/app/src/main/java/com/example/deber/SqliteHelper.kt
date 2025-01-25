package com.example.deber

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqliteHelper(context: Context?) : SQLiteOpenHelper(context, "moviles", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("PRAGMA foreign_keys = ON;")
        val scriptCrearTablaMarca = """
            CREATE TABLE Marca(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT,
                descripcion TEXT,
                fechaFundacion TEXT,
                esActiva BOOLEAN
            )
        """
        db?.execSQL(scriptCrearTablaMarca)

        val scriptCrearTablaProducto = """
            CREATE TABLE Producto(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT,
                precio REAL,
                categoria TEXT,
                marcaId INTEGER,
                FOREIGN KEY(marcaId) REFERENCES Marca(id) ON DELETE CASCADE
            )
        """
        db?.execSQL(scriptCrearTablaProducto)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun crearMarca(nombre: String, descripcion: String, fechaFundacion: String, esActiva: Boolean): Boolean {
        val db = writableDatabase
        val valores = ContentValues()
        valores.put("nombre", nombre)
        valores.put("descripcion", descripcion)
        valores.put("fechaFundacion", fechaFundacion)
        valores.put("esActiva", esActiva)
        val resultado = db.insert("Marca", null, valores)
        db.close()
        return resultado != -1L
    }

    fun obtenerTodasLasMarcas(): List<Marca> {
        val lista = mutableListOf<Marca>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Marca", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val nombre = cursor.getString(1)
                val descripcion = cursor.getString(2)
                val fechaFundacion = cursor.getString(3)
                val esActiva = cursor.getInt(4) == 1
                lista.add(Marca(id, nombre, descripcion, fechaFundacion, esActiva))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun actualizarMarca(nombre: String, descripcion: String, fechaFundacion: String, esActiva: Boolean, id: Int): Boolean {
        val db = writableDatabase
        val valores = ContentValues()
        valores.put("nombre", nombre)
        valores.put("descripcion", descripcion)
        valores.put("fechaFundacion", fechaFundacion)
        valores.put("esActiva", esActiva)
        val resultado = db.update("Marca", valores, "id=?", arrayOf(id.toString()))
        db.close()
        return resultado > 0
    }

    fun eliminarMarca(id: Int): Boolean {
        val db = writableDatabase
        val resultado = db.delete("Marca", "id=?", arrayOf(id.toString()))
        db.close()
        return resultado > 0
    }

    fun crearProducto(nombre: String, precio: Double, categoria: String, marcaId: Int): Boolean {
        val db = writableDatabase
        val valores = ContentValues()
        valores.put("nombre", nombre)
        valores.put("precio", precio)
        valores.put("categoria", categoria)
        valores.put("marcaId", marcaId)
        val resultado = db.insert("Producto", null, valores)
        db.close()
        return resultado != -1L
    }

    fun obtenerProductosPorMarca(marcaId: Int): List<Producto> {
        val lista = mutableListOf<Producto>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Producto WHERE marcaId = ?", arrayOf(marcaId.toString()))
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val nombre = cursor.getString(1)
                val precio = cursor.getDouble(2)
                val categoria = cursor.getString(3)
                lista.add(Producto(id, nombre, precio, categoria, marcaId))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun actualizarProducto(nombre: String, precio: Double, categoria: String, id: Int): Boolean {
        val db = writableDatabase
        val valores = ContentValues()
        valores.put("nombre", nombre)
        valores.put("precio", precio)
        valores.put("categoria", categoria)
        val resultado = db.update("Producto", valores, "id=?", arrayOf(id.toString()))
        db.close()
        return resultado > 0
    }

    fun eliminarProducto(id: Int): Boolean {
        val db = writableDatabase
        val resultado = db.delete("Producto", "id=?", arrayOf(id.toString()))
        db.close()
        return resultado > 0
    }
}
