package com.example.deber

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class CrudMarca : AppCompatActivity() {
    // Mostrar mensajes de error o éxito
    private fun mostrarSnackbar(texto: String) {
        Snackbar.make(findViewById(R.id.cl_crud_marca), texto, Snackbar.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_marca)

        // Variables del layout
        val btnGuardar = findViewById<Button>(R.id.btn_guardar_marca)
        val inputNombre = findViewById<EditText>(R.id.input_nombre_marca)
        val inputDescripcion = findViewById<EditText>(R.id.input_descripcion_marca)
        val inputFechaFundacion = findViewById<EditText>(R.id.input_fecha_fundacion_marca)
        val switchActiva = findViewById<Switch>(R.id.switch_activa_marca)

        // Obtener modo y marca desde el Intent
        val modo = intent.getStringExtra("modo") ?: "crear"
        val marca: Marca? = intent.getParcelableExtra("marca")

        // Configurar vista para modo de edición
        if (modo == "editar" && marca != null) {
            inputNombre.setText(marca.nombre)
            inputDescripcion.setText(marca.descripcion)
            inputFechaFundacion.setText(marca.fechaFundacion)
            switchActiva.isChecked = marca.esActiva
            btnGuardar.text = getString(R.string.actualizar) // Usar recursos de texto
        } else {
            btnGuardar.text = getString(R.string.crear)
        }

        // Acción del botón guardar
        btnGuardar.setOnClickListener {
            val nombre = inputNombre.text.toString().trim()
            val descripcion = inputDescripcion.text.toString().trim()
            val fechaFundacion = inputFechaFundacion.text.toString().trim()
            val esActiva = switchActiva.isChecked

            // Validar entradas
            if (nombre.isEmpty() || descripcion.isEmpty() || fechaFundacion.isEmpty()) {
                mostrarSnackbar("Por favor, completa todos los campos.")
                return@setOnClickListener
            }

            if (modo == "crear") {
                // Crear nueva marca
                val respuesta = BaseDeDatos.tablaMarcaProducto?.crearMarca(
                    nombre,
                    descripcion,
                    fechaFundacion,
                    esActiva
                )
                if (respuesta == true) {
                    mostrarSnackbar("Marca creada correctamente.")
                    setResult(RESULT_OK)
                    finish()
                } else {
                    mostrarSnackbar("Error al crear la marca.")
                }
            } else if (modo == "editar" && marca != null) {
                // Actualizar marca existente
                val respuesta = BaseDeDatos.tablaMarcaProducto?.actualizarMarca(
                    nombre,
                    descripcion,
                    fechaFundacion,
                    esActiva,
                    marca.id
                )
                if (respuesta == true) {
                    mostrarSnackbar("Marca actualizada correctamente.")
                    setResult(RESULT_OK)
                    finish()
                } else {
                    mostrarSnackbar("Error al actualizar la marca.")
                }
            }
        }
    }
}
