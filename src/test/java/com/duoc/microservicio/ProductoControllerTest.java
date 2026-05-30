package com.duoc.microservicio;

import com.duoc.microservicio.controller.ProductoController;
import com.duoc.microservicio.model.Producto;
import com.duoc.microservicio.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void debeListarProductos() throws Exception {
        when(productoService.obtenerTodos()).thenReturn(Arrays.asList(
                new Producto(1L, "Laptop", 599990.0, 10),
                new Producto(2L, "Mouse", 12990.0, 50)
        ));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Laptop"));
    }

    @Test
    void debeObtenerProductoPorId() throws Exception {
        when(productoService.obtenerPorId(1L))
                .thenReturn(Optional.of(new Producto(1L, "Laptop", 599990.0, 10)));

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Laptop"));
    }

    @Test
    void debeRetornar404SiNoExiste() throws Exception {
        when(productoService.obtenerPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/productos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void debeCrearProducto() throws Exception {
        Producto nuevo = new Producto(null, "Monitor", 199990.0, 5);
        Producto creado = new Producto(4L, "Monitor", 199990.0, 5);
        when(productoService.crear(any(Producto.class))).thenReturn(creado);

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.nombre").value("Monitor"));
    }

    @Test
    void debeEliminarProducto() throws Exception {
        when(productoService.eliminar(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());
    }
}
