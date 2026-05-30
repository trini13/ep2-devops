# Microservicio EP2 — Ingeniería DevOps (DOY0101)

**Evaluación Parcial N°2 — Añadiéndole complejidad a nuestro pipeline**
**DUOC UC — 2025**

---

## Descripción del Proyecto

Este proyecto implementa un **microservicio REST** desarrollado con **Spring Boot 3** y Java 17,
que expone una API para gestión de productos. La evaluación consiste en automatizar completamente
su ciclo de vida mediante un **pipeline CI/CD** implementado en **GitHub Actions**.

---

## Arquitectura

```
┌─────────────────────────────────────────────────────┐
│                   PIPELINE CI/CD                     │
│                                                     │
│  Push/PR → Build → Tests → Seguridad → Docker → Deploy│
└─────────────────────────────────────────────────────┘

┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│  Controller  │────▶│   Service    │────▶│    Model     │
│ (REST API)   │     │ (Lógica)     │     │  (Producto)  │
└──────────────┘     └──────────────┘     └──────────────┘
```

---

## Endpoints de la API

| Método | Ruta                  | Descripción             |
|--------|-----------------------|-------------------------|
| GET    | /api/productos        | Lista todos los productos |
| GET    | /api/productos/{id}   | Obtiene un producto por ID |
| POST   | /api/productos        | Crea un nuevo producto  |
| PUT    | /api/productos/{id}   | Actualiza un producto   |
| DELETE | /api/productos/{id}   | Elimina un producto     |
| GET    | /actuator/health      | Estado de salud del servicio |

---

## Estructura del Proyecto

```
ep2-devops/
├── .github/
│   ├── workflows/
│   │   └── ci-cd.yml          # Pipeline GitHub Actions
│   └── dependabot.yml         # Análisis automático de dependencias
├── src/
│   ├── main/java/com/duoc/microservicio/
│   │   ├── MicroservicioApplication.java
│   │   ├── controller/
│   │   │   └── ProductoController.java
│   │   ├── service/
│   │   │   └── ProductoService.java
│   │   └── model/
│   │       └── Producto.java
│   └── test/java/com/duoc/microservicio/
│       ├── ProductoServiceTest.java
│       └── ProductoControllerTest.java
├── Dockerfile                 # Imagen Docker del microservicio
├── docker-compose.yml         # Orquestación de contenedores
├── pom.xml                    # Dependencias Maven
└── README.md                  # Este archivo
```

---

## Pipeline CI/CD — Descripción detallada

El pipeline se activa automáticamente con cada **push** o **pull request** a la rama `main`.
Está compuesto por **4 jobs secuenciales**:

### Job 1: Build y Pruebas Unitarias (IE1, IE2)
1. Descarga el código del repositorio
2. Configura Java 17
3. Compila el proyecto con Maven
4. **Ejecuta las pruebas unitarias con JUnit 5**
5. Publica el reporte de resultados
6. Genera y guarda el JAR como artefacto

### Job 2: Análisis de Seguridad — Snyk (IE3)
- Se ejecuta **solo si el Job 1 pasó**
- Escanea todas las dependencias del proyecto en busca de vulnerabilidades
- **Bloquea el pipeline** si se detectan vulnerabilidades de severidad alta o crítica
- Publica el reporte SARIF en la pestaña "Security" de GitHub

### Job 3: Construcción de Imagen Docker (IE1)
- Se ejecuta **solo si el análisis de seguridad pasó**
- Construye la imagen Docker usando el Dockerfile multi-etapa
- Publica la imagen en Docker Hub con dos etiquetas: `latest` y el SHA del commit

### Job 4: Despliegue Automático (IE4, IE5)
- Se ejecuta **solo en la rama main**
- Levanta el entorno con **Docker Compose** (orquestación IE5)
- Verifica que el microservicio responda correctamente en `/actuator/health`
- Muestra los logs del contenedor para **trazabilidad completa** (IE4)

---

## Garantía de Trazabilidad y Calidad

La trazabilidad se garantiza a través de:

- **Cada commit** activa el pipeline automáticamente
- **Artefactos del pipeline**: el JAR y los reportes de pruebas quedan guardados en GitHub Actions
- **Etiquetas de imagen Docker**: cada imagen lleva el SHA del commit que la generó
- **Logs del contenedor**: visibles en cada ejecución del pipeline
- **Reportes de seguridad**: visibles en la pestaña Security de GitHub
- **Dependabot**: revisa semanalmente si hay actualizaciones de seguridad en las dependencias

---

## Herramientas utilizadas

| Herramienta       | Propósito                              |
|-------------------|----------------------------------------|
| Spring Boot 3     | Framework del microservicio            |
| Java 17           | Lenguaje de programación               |
| Maven             | Gestión de dependencias y build        |
| JUnit 5           | Pruebas unitarias                      |
| Docker            | Contenedorización                      |
| Docker Compose    | Orquestación de contenedores           |
| GitHub Actions    | Pipeline CI/CD                         |
| Snyk              | Análisis de seguridad de dependencias  |
| Dependabot        | Actualizaciones automáticas de seguridad|

---

## Cómo ejecutar localmente

### Con Docker Compose (recomendado)
```bash
docker compose up --build
```
La API estará disponible en: http://localhost:8080/api/productos

### Con Maven directamente
```bash
mvn spring-boot:run
```

---

## Uso de Inteligencia Artificial

Se utilizó **Perplexity Computer (IA)** como apoyo para:
- Generación de la estructura base del proyecto
- Revisión de sintaxis de los archivos de configuración
- Orientación sobre las mejores prácticas del pipeline CI/CD

Todas las ideas, análisis técnicos y justificaciones fueron revisados y validados por la estudiante.
Las conclusiones y reflexiones personales son de autoría propia.

**Referencia de uso de IA:** https://bibliotecas.duoc.cl/ia

---

## Reflexión Personal

*(Escribe aquí tu reflexión personal sin usar IA — mínimo un párrafo explicando qué aprendiste
y cómo contribuiste al proyecto)*

---

*DUOC UC — Ingeniería DevOps DOY0101 — 2025*
