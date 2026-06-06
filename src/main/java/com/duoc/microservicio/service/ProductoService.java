package com.duoc.microservicio.service;

import com.duoc.microservicio.model.Producto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final List<Producto> productos = new ArrayList<>();
    private long contadorId = 1;

    public ProductoService() {
        // Datos de ejemplo al iniciar
        productos.add(new Producto(contadorId++, "notebookplus", 599990.0, 10));
        productos.add(new Producto(contadorId++, "Mouse", 12990.0, 50));
        productos.add(new Producto(contadorId++, "Teclado", 29990.0, 30));
    }

    public List<Producto> obtenerTodos() {
        return new ArrayList<>(productos);
    }

    public Optional<Producto> obtenerPorId(Long id) {
        return productos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public Producto crear(Producto producto) {
        producto.setId(contadorId++);
        productos.add(producto);
        return producto;
    }

    public Optional<Producto> actualizar(Long id, Producto productoActualizado) {
        return productos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .map(p -> {
                    p.setNombre(productoActualizado.getNombre());
                    p.setPrecio(productoActualizado.getPrecio());
                    p.setStock(productoActualizado.getStock());
                    return p;
                });
    }

    public boolean eliminar(Long id) {
        return productos.removeIf(p -> p.getId().equals(id));
    }
}
