package com.duoc.microservicio;

import com.duoc.microservicio.model.Producto;
import com.duoc.microservicio.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductoServiceTest {

    private ProductoService productoService;

    @BeforeEach
    void setUp() {
        productoService = new ProductoService();
    }

    @Test
    void debeRetornarTodosLosProductos() {
        List<Producto> productos = productoService.obtenerTodos();
        assertFalse(productos.isEmpty(), "La lista de productos no debe estar vacía");
        assertEquals(3, productos.size(), "Debe haber 3 productos iniciales");
    }

    @Test
    void debeObtenerProductoPorId() {
        Optional<Producto> producto = productoService.obtenerPorId(1L);
        assertTrue(producto.isPresent(), "El producto con ID 1 debe existir");
        assertEquals("notebookplus", producto.get().getNombre());
    }

    @Test
    void debeRetornarVacioSiIdNoExiste() {
        Optional<Producto> producto = productoService.obtenerPorId(999L);
        assertFalse(producto.isPresent(), "No debe existir producto con ID 999");
    }

    @Test
    void debeCrearProductoNuevo() {
        Producto nuevo = new Producto(null, "Monitor", 199990.0, 5);
        Producto creado = productoService.crear(nuevo);
        assertNotNull(creado.getId(), "El nuevo producto debe tener un ID asignado");
        assertEquals("Monitor", creado.getNombre());
        assertEquals(4, productoService.obtenerTodos().size());
    }

    @Test
    void debeActualizarProductoExistente() {
        Producto actualizado = new Producto(null, "Laptop Pro", 799990.0, 8);
        Optional<Producto> resultado = productoService.actualizar(1L, actualizado);
        assertTrue(resultado.isPresent(), "Debe poder actualizar el producto con ID 1");
        assertEquals("Laptop Pro", resultado.get().getNombre());
        assertEquals(799990.0, resultado.get().getPrecio());
    }

    @Test
    void debeEliminarProductoExistente() {
        boolean eliminado = productoService.eliminar(1L);
        assertTrue(eliminado, "Debe eliminar el producto con ID 1");
        assertEquals(2, productoService.obtenerTodos().size());
    }

    @Test
    void noDebeEliminarSiIdNoExiste() {
        boolean eliminado = productoService.eliminar(999L);
        assertFalse(eliminado, "No debe eliminar si el ID no existe");
    }
}
