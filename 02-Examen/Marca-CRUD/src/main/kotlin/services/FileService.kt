package services

import entities.Marca
import entities.Producto
import org.w3c.dom.*
import javax.xml.parsers.DocumentBuilderFactory
import java.io.File
import javax.xml.transform.*
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

// Función para leer marcas desde el XML
fun leerMarcasDesdeXML(): List<Marca> {
    val marcas = mutableListOf<Marca>()
    val factory = DocumentBuilderFactory.newInstance()
    val builder = factory.newDocumentBuilder()
    val doc = builder.parse(File("src/main/resources/marcas.xml"))

    val nodeList = doc.getElementsByTagName("marca")
    for (i in 0 until nodeList.length) {
        val node = nodeList.item(i) as Element
        val id = node.getElementsByTagName("id").item(0).textContent.toInt()
        val nombre = node.getElementsByTagName("nombre").item(0).textContent
        val descripcion = node.getElementsByTagName("descripcion").item(0).textContent
        val fechaFundacion = node.getElementsByTagName("fechaFundacion").item(0).textContent
        val esActiva = node.getElementsByTagName("esActiva").item(0).textContent.toBoolean()

        marcas.add(Marca(id, nombre, descripcion, fechaFundacion, esActiva))
    }

    return marcas
}

// Función para leer productos desde el XML
fun leerProductosDesdeXML(): List<Producto> {
    val productos = mutableListOf<Producto>()
    val factory = DocumentBuilderFactory.newInstance()
    val builder = factory.newDocumentBuilder()
    val doc = builder.parse(File("src/main/resources/productos.xml"))

    val nodeList = doc.getElementsByTagName("producto")
    for (i in 0 until nodeList.length) {
        val node = nodeList.item(i) as Element
        val id = node.getElementsByTagName("id").item(0).textContent.toInt()
        val nombre = node.getElementsByTagName("nombre").item(0).textContent
        val precio = node.getElementsByTagName("precio").item(0).textContent.toDouble()
        val categoria = node.getElementsByTagName("categoria").item(0).textContent
        val marcaId = node.getElementsByTagName("marcaId").item(0).textContent.toInt()

        productos.add(Producto(id, nombre, precio, categoria, marcaId))
    }

    return productos
}

// Función para escribir marcas en un archivo XML
fun escribirMarcasA_XML(marcas: List<Marca>) {
    val docFactory = DocumentBuilderFactory.newInstance()
    val docBuilder = docFactory.newDocumentBuilder()
    val doc = docBuilder.newDocument()

    // Elemento raíz
    val rootElement = doc.createElement("marcas")
    doc.appendChild(rootElement)

    // Crear elementos para cada marca
    for (marca in marcas) {
        val marcaElement = doc.createElement("marca")
        rootElement.appendChild(marcaElement)

        val idElement = doc.createElement("id")
        idElement.appendChild(doc.createTextNode(marca.id.toString()))
        marcaElement.appendChild(idElement)

        val nombreElement = doc.createElement("nombre")
        nombreElement.appendChild(doc.createTextNode(marca.nombre))
        marcaElement.appendChild(nombreElement)

        val descripcionElement = doc.createElement("descripcion")
        descripcionElement.appendChild(doc.createTextNode(marca.descripcion))
        marcaElement.appendChild(descripcionElement)

        val fechaElement = doc.createElement("fechaFundacion")
        fechaElement.appendChild(doc.createTextNode(marca.fechaFundacion))
        marcaElement.appendChild(fechaElement)

        val esActivaElement = doc.createElement("esActiva")
        esActivaElement.appendChild(doc.createTextNode(marca.esActiva.toString()))
        marcaElement.appendChild(esActivaElement)
    }

    // Guardar el archivo XML
    val transformerFactory = TransformerFactory.newInstance()
    val transformer = transformerFactory.newTransformer()
    val source = DOMSource(doc)
    val result = StreamResult(File("src/main/resources/marcas.xml"))
    transformer.transform(source, result)
}

// Función para escribir productos en un archivo XML
fun escribirProductosA_XML(productos: List<Producto>) {
    val docFactory = DocumentBuilderFactory.newInstance()
    val docBuilder = docFactory.newDocumentBuilder()
    val doc = docBuilder.newDocument()

    // Elemento raíz
    val rootElement = doc.createElement("productos")
    doc.appendChild(rootElement)

    // Crear elementos para cada producto
    for (producto in productos) {
        val productoElement = doc.createElement("producto")
        rootElement.appendChild(productoElement)

        val idElement = doc.createElement("id")
        idElement.appendChild(doc.createTextNode(producto.id.toString()))
        productoElement.appendChild(idElement)

        val nombreElement = doc.createElement("nombre")
        nombreElement.appendChild(doc.createTextNode(producto.nombre))
        productoElement.appendChild(nombreElement)

        val precioElement = doc.createElement("precio")
        precioElement.appendChild(doc.createTextNode(producto.precio.toString()))
        productoElement.appendChild(precioElement)

        val categoriaElement = doc.createElement("categoria")
        categoriaElement.appendChild(doc.createTextNode(producto.categoria))
        productoElement.appendChild(categoriaElement)

        val marcaIdElement = doc.createElement("marcaId")
        marcaIdElement.appendChild(doc.createTextNode(producto.marcaId.toString()))
        productoElement.appendChild(marcaIdElement)
    }

    // Guardar el archivo XML
    val transformerFactory = TransformerFactory.newInstance()
    val transformer = transformerFactory.newTransformer()
    val source = DOMSource(doc)
    val result = StreamResult(File("src/main/resources/productos.xml"))
    transformer.transform(source, result)
}
