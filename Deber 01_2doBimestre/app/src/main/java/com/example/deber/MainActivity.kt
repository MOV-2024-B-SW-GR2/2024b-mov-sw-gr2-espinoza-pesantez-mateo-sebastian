package com.example.deber

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar Base de Datos
        BaseDeDatos.tablaMarcaProducto = SqliteHelper(this)

        val btnVerMarcas = findViewById<Button>(R.id.btn_ver_marcas)
        btnVerMarcas.setOnClickListener {
            irActividad(MarcaListView::class.java)
        }

        val btnVerProductos = findViewById<Button>(R.id.btn_ver_productos)
        btnVerProductos.setOnClickListener {
            irActividad(ProductoListView::class.java)
        }
    }

    private fun irActividad(clase: Class<*>) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }
}
