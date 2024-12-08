package services

import entities.Producto

// Funciones CRUD para productos
fun agregarProducto(productos: MutableList<Producto>, producto: Producto) {
    productos.add(producto)
}

fun eliminarProducto(productos: MutableList<Producto>, id: Int) {
    val producto = productos.find { it.id == id }
    producto?.let { productos.remove(it) }
}

fun actualizarProducto(productos: MutableList<Producto>, id: Int, productoActualizado: Producto) {
    val productoIndex = productos.indexOfFirst { it.id == id }
    if (productoIndex != -1) {
        productos[productoIndex] = productoActualizado
    }
}
