import entities.Marca
import entities.Producto
import services.*

fun main() {
    val marcas = leerMarcasDesdeXML().toMutableList()
    val productos = leerProductosDesdeXML().toMutableList()

    while (true) { // Bucle infinito hasta que se seleccione la opción 9 (Salir)
        println("Bienvenido al sistema de gestión de marcas y productos")
        println("1. Listar marcas")
        println("2. Listar productos por marca")
        println("3. Agregar marca")
        println("4. Agregar producto")
        println("5. Eliminar marca")
        println("6. Eliminar producto")
        println("7. Actualizar marca")
        println("8. Actualizar producto")
        println("9. Salir")
        print("Seleccione una opción: ")

        when (readLine()?.toIntOrNull()) {
            1 -> {
                // Listar marcas formateadas
                marcas.forEach {
                    println("Marca: ${it.nombre}")
                    println("Descripción: ${it.descripcion}")
                    println("Fecha de Fundación: ${it.fechaFundacion}")
                    println()
                }
            }
            2 -> {
                // Listar productos por marca
                println("Elija la marca de los productos que desea ver:")
                marcas.forEachIndexed { index, marca ->
                    println("${index + 1}. ${marca.nombre}")
                }
                print("Seleccione una marca por número: ")
                val marcaSeleccionada = readLine()?.toIntOrNull()?.minus(1)
                if (marcaSeleccionada != null && marcaSeleccionada in marcas.indices) {
                    val marca = marcas[marcaSeleccionada]
                    println("Productos de la marca ${marca.nombre}:")
                    productos.filter { it.marcaId == marca.id }.forEach {
                        println("Producto: ${it.nombre}")
                        println("Precio: ${it.precio}")
                        println("Categoría: ${it.categoria}")
                        println()
                    }
                } else {
                    println("Opción inválida.")
                }
            }
            3 -> {
                // Agregar marca
                print("Ingrese el nombre de la nueva marca: ")
                val nombre = readLine() ?: ""
                print("Ingrese la descripción de la marca: ")
                val descripcion = readLine() ?: ""
                print("Ingrese la fecha de fundación de la marca: ")
                val fechaFundacion = readLine() ?: ""
                print("¿Está activa la marca? (true/false): ")
                val esActiva = readLine()?.toBoolean() ?: false
                val nuevaMarca = Marca(marcas.size + 1, nombre, descripcion, fechaFundacion, esActiva)
                agregarMarca(marcas, nuevaMarca)
            }
            4 -> {
                // Agregar producto
                print("Ingrese el nombre del nuevo producto: ")
                val nombre = readLine() ?: ""
                print("Ingrese el precio del producto: ")
                val precio = readLine()?.toDoubleOrNull() ?: 0.0
                print("Ingrese la categoría del producto: ")
                val categoria = readLine() ?: ""
                println("Seleccione la marca del producto:")
                marcas.forEachIndexed { index, marca ->
                    println("${index + 1}. ${marca.nombre}")
                }
                print("Seleccione una marca por número: ")
                val marcaIdSeleccionada = readLine()?.toIntOrNull()?.minus(1)
                if (marcaIdSeleccionada != null && marcaIdSeleccionada in marcas.indices) {
                    val marcaId = marcas[marcaIdSeleccionada].id
                    val nuevoProducto = Producto(productos.size + 1, nombre, precio, categoria, marcaId)
                    agregarProducto(productos, nuevoProducto)
                } else {
                    println("Opción inválida.")
                }
            }
            5 -> {
                // Eliminar marca por nombre
                print("Ingrese el nombre de la marca a eliminar: ")
                val nombreMarca = readLine() ?: ""
                val marcaAEliminar = marcas.find { it.nombre.equals(nombreMarca, ignoreCase = true) }
                if (marcaAEliminar != null) {
                    eliminarMarca(marcas, marcaAEliminar.id)
                } else {
                    println("Marca no encontrada.")
                }
            }
            6 -> {
                // Eliminar producto por nombre
                print("Ingrese el nombre del producto a eliminar: ")
                val nombreProducto = readLine() ?: ""
                val productoAEliminar = productos.find { it.nombre.equals(nombreProducto, ignoreCase = true) }
                if (productoAEliminar != null) {
                    eliminarProducto(productos, productoAEliminar.id)
                } else {
                    println("Producto no encontrado.")
                }
            }
            7 -> {
                // Actualizar marca por nombre
                print("Ingrese el nombre de la marca a actualizar: ")
                val nombreMarca = readLine() ?: ""
                val marcaExistente = marcas.find { it.nombre.equals(nombreMarca, ignoreCase = true) }
                marcaExistente?.let {
                    print("Nuevo nombre: ")
                    val nombre = readLine() ?: it.nombre
                    print("Nueva descripción: ")
                    val descripcion = readLine() ?: it.descripcion
                    print("Nueva fecha de fundación: ")
                    val fechaFundacion = readLine() ?: it.fechaFundacion
                    print("¿Está activa la marca? (true/false): ")
                    val esActiva = readLine()?.toBoolean() ?: it.esActiva
                    val marcaActualizada = Marca(it.id, nombre, descripcion, fechaFundacion, esActiva)
                    actualizarMarca(marcas, it.id, marcaActualizada)
                } ?: println("Marca no encontrada.")
            }
            8 -> {
                // Actualizar producto por nombre
                print("Ingrese el nombre del producto a actualizar: ")
                val nombreProducto = readLine() ?: ""
                val productoExistente = productos.find { it.nombre.equals(nombreProducto, ignoreCase = true) }
                productoExistente?.let {
                    print("Nuevo nombre: ")
                    val nombre = readLine() ?: it.nombre
                    print("Nuevo precio: ")
                    val precio = readLine()?.toDoubleOrNull() ?: it.precio
                    print("Nueva categoría: ")
                    val categoria = readLine() ?: it.categoria
                    print("Nuevo nombre de marca: ")
                    marcas.forEachIndexed { index, marca ->
                        println("${index + 1}. ${marca.nombre}")
                    }
                    val marcaId = readLine()?.toIntOrNull()?.minus(1)?.let { marcas[it].id } ?: it.marcaId
                    val productoActualizado = Producto(it.id, nombre, precio, categoria, marcaId)
                    actualizarProducto(productos, it.id, productoActualizado)
                } ?: println("Producto no encontrado.")
            }
            9 -> {
                println("Saliendo...")
                break // Salir del bucle y terminar el programa
            }
            else -> println("Opción no válida. Por favor, intente nuevamente.")
        }

        // Guardar cambios en archivos XML
        escribirMarcasA_XML(marcas)
        escribirProductosA_XML(productos)
    }
}
