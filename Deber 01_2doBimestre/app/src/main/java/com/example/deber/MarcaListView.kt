package com.example.deber

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MarcaListView : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val listaMarcas = mutableListOf<Marca>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marcalist_view)

        listView = findViewById(R.id.lv_lista_marcas)
        val btnCrearMarca = findViewById<Button>(R.id.btn_agregar_marca)

        // Inicializar adaptador
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaMarcas.map { it.nombre })
        listView.adapter = adapter

        // Registrar menú contextual
        registerForContextMenu(listView)

        // Cargar datos iniciales
        cargarDatosDesdeBaseDeDatos()

        // Acción del botón para crear una nueva marca
        btnCrearMarca.setOnClickListener {
            irActividad(CrudMarca::class.java)
        }
    }

    private var posicionItemSeleccionado = -1

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_contextual, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        posicionItemSeleccionado = info.position
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar -> {
                val marcaSeleccionada = listaMarcas[posicionItemSeleccionado]
                irActividad(CrudMarca::class.java, marcaSeleccionada)
                true
            }
            R.id.mi_eliminar -> {
                abrirDialogoEliminar()
                true
            }
            R.id.mi_productos -> {
                val marcaSeleccionada = listaMarcas[posicionItemSeleccionado]
                irActividad(ProductoListView::class.java, marcaSeleccionada)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun abrirDialogoEliminar() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Desea eliminar la marca?")
        builder.setPositiveButton("Aceptar") { _, _ ->
            val marcaSeleccionada = listaMarcas[posicionItemSeleccionado]
            val id = marcaSeleccionada.id
            val eliminado = BaseDeDatos.tablaMarcaProducto?.eliminarMarca(id)
            if (eliminado == true) {
                cargarDatosDesdeBaseDeDatos()
            }
        }
        builder.setNegativeButton("Cancelar", null)
        builder.create().show()
    }

    private fun cargarDatosDesdeBaseDeDatos() {
        val marcas = BaseDeDatos.tablaMarcaProducto?.obtenerTodasLasMarcas()
        listaMarcas.clear()
        if (marcas != null) {
            listaMarcas.addAll(marcas)
        }
        adapter.clear()
        adapter.addAll(listaMarcas.map { it.nombre })
        adapter.notifyDataSetChanged()
    }

    private fun irActividad(clase: Class<*>, marca: Marca? = null) {
        val intent = Intent(this, clase)
        if (marca != null) {
            intent.putExtra("modo", "editar")
            intent.putExtra("marca", marca)
        } else {
            intent.putExtra("modo", "crear")
        }
        startActivityForResult(intent, 1)
    }
}
