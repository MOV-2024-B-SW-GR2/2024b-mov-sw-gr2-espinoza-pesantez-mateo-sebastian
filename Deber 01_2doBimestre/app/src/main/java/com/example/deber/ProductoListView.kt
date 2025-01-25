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
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ProductoListView : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val listaProductos = mutableListOf<Producto>()
    private var marcaId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productolist_view)

        listView = findViewById(R.id.lv_lista_productos)
        val txtProductos = findViewById<TextView>(R.id.txt_view_producto)
        val btnAgregarProducto = findViewById<Button>(R.id.btn_agregar_producto)

        // Obtener el ID de la marca
        val marca = intent.getParcelableExtra<Marca>("marca")
        marcaId = marca?.id ?: -1
        txtProductos.text = "Productos de ${marca?.nombre}"

        // Inicializar adaptador
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaProductos.map { it.nombre })
        listView.adapter = adapter

        // Registrar menú contextual
        registerForContextMenu(listView)

        // Cargar datos iniciales
        cargarDatosDesdeBaseDeDatos()

        // Acción del botón para agregar un nuevo producto
        btnAgregarProducto.setOnClickListener {
            irActividad(CrudProducto::class.java)
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
                val productoSeleccionado = listaProductos[posicionItemSeleccionado]
                irActividad(CrudProducto::class.java, productoSeleccionado)
                true
            }
            R.id.mi_eliminar -> {
                abrirDialogoEliminar()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun abrirDialogoEliminar() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Desea eliminar el producto?")
        builder.setPositiveButton("Aceptar") { _, _ ->
            val productoSeleccionado = listaProductos[posicionItemSeleccionado]
            val id = productoSeleccionado.id
            val eliminado = BaseDeDatos.tablaMarcaProducto?.eliminarProducto(id)
            if (eliminado == true) {
                cargarDatosDesdeBaseDeDatos()
            }
        }
        builder.setNegativeButton("Cancelar", null)
        builder.create().show()
    }

    private fun cargarDatosDesdeBaseDeDatos() {
        val productos = BaseDeDatos.tablaMarcaProducto?.obtenerProductosPorMarca(marcaId)
        listaProductos.clear()
        if (productos != null) {
            listaProductos.addAll(productos)
        }
        adapter.clear()
        adapter.addAll(listaProductos.map { it.nombre })
        adapter.notifyDataSetChanged()
    }

    private fun irActividad(clase: Class<*>, producto: Producto? = null) {
        val intent = Intent(this, clase)
        intent.putExtra("marcaId", marcaId)
        if (producto != null) {
            intent.putExtra("modo", "editar")
            intent.putExtra("producto", producto)
        } else {
            intent.putExtra("modo", "crear")
        }
        startActivityForResult(intent, 1)
    }
}
