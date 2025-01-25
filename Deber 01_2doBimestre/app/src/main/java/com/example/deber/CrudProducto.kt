package com.example.deber

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class CrudProducto : AppCompatActivity() {
    // Mostrar mensajes de error o éxito
    private fun mostrarSnackbar(texto: String) {
        Snackbar.make(findViewById(R.id.cl_crud_producto), texto, Snackbar.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_producto)

        // Variables del layout
        val btnGuardar = findViewById<Button>(R.id.btn_guardar_producto)
        val inputNombre = findViewById<EditText>(R.id.input_nombre_producto)
        val inputPrecio = findViewById<EditText>(R.id.input_precio_producto)
        val inputCategoria = findViewById<EditText>(R.id.input_categoria_producto)

        // Obtener datos del Intent
        val modo = intent.getStringExtra("modo") ?: "crear"
        val producto: Producto? = intent.getParcelableExtra("producto")
        val marcaId = intent.getIntExtra("marcaId", -1)

        if (marcaId == -1) {
            mostrarSnackbar("Error: No se proporcionó un ID válido de marca.")
            finish()
            return
        }

        // Configurar vista para modo de edición
        if (modo == "editar" && producto != null) {
            inputNombre.setText(producto.nombre)
            inputPrecio.setText(producto.precio.toString())
            inputCategoria.setText(producto.categoria)
            btnGuardar.text = getString(R.string.actualizar)
        } else {
            btnGuardar.text = getString(R.string.crear)
        }

        // Acción del botón guardar
        btnGuardar.setOnClickListener {
            val nombre = inputNombre.text.toString().trim()
            val precioString = inputPrecio.text.toString().trim()
            val categoria = inputCategoria.text.toString().trim()

            // Validar entradas
            if (nombre.isEmpty() || precioString.isEmpty() || categoria.isEmpty()) {
                mostrarSnackbar("Por favor, completa todos los campos.")
                return@setOnClickListener
            }

            val precio = precioString.toDoubleOrNull()
            if (precio == null || precio <= 0) {
                mostrarSnackbar("Por favor, ingresa un precio válido.")
                return@setOnClickListener
            }

            if (modo == "crear") {
                // Crear nuevo producto
                val respuesta = BaseDeDatos.tablaMarcaProducto?.crearProducto(
                    nombre,
                    precio,
                    categoria,
                    marcaId
                )
                if (respuesta == true) {
                    mostrarSnackbar("Producto creado correctamente.")
                    setResult(RESULT_OK)
                    finish()
                } else {
                    mostrarSnackbar("Error al crear el producto.")
                }
            } else if (modo == "editar" && producto != null) {
                // Actualizar producto existente (incluyendo ID)
                val respuesta = BaseDeDatos.tablaMarcaProducto?.actualizarProducto(
                    nombre,
                    precio,
                    categoria,
                    producto.id
                )
                if (respuesta == true) {
                    mostrarSnackbar("Producto actualizado correctamente.")
                    setResult(RESULT_OK)
                    finish()
                } else {
                    mostrarSnackbar("Error al actualizar el producto.")
                }
            }
        }
    }
}
