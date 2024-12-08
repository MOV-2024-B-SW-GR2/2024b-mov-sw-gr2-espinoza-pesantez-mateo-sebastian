package services

import entities.Marca

// Funciones CRUD para marcas
fun agregarMarca(marcas: MutableList<Marca>, marca: Marca) {
    marcas.add(marca)
}

fun eliminarMarca(marcas: MutableList<Marca>, id: Int) {
    val marca = marcas.find { it.id == id }
    marca?.let { marcas.remove(it) }
}

fun actualizarMarca(marcas: MutableList<Marca>, id: Int, marcaActualizada: Marca) {
    val marcaIndex = marcas.indexOfFirst { it.id == id }
    if (marcaIndex != -1) {
        marcas[marcaIndex] = marcaActualizada
    }
}
