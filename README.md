# Microservicio EP2 — Ingeniería DevOps (DOY0101)

**Evaluación Parcial N°3 — Añadiéndole complejidad a nuestro pipeline**
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

## Reflexión Personal Cristina Silva

Durante el desarrollo de esta evaluación, el mayor desafío que enfrenté fue comprender el funcionamiento del pipeline CI/CD. Al principio, no tenía claridad sobre cómo los jobs se conectaban entre sí ni por qué el orden importaba. Ver que un job dependía del anterior para ejecutarse me ayudó a entender que el pipeline no es solo una secuencia de comandos, sino una cadena donde cada etapa garantiza que la siguiente tenga sentido.

También aprendí que configurar herramientas como Snyk y Docker Hub requiere más que solo instalarlas hay que conectarlas correctamente con el repositorio mediante secrets, lo cual me enseñó la importancia de la seguridad en los entornos de desarrollo. Cometer errores en esa configuración y tener que corregirlos me permitió entender mucho mejor cómo funciona cada pieza.

En general, esta evaluación me demostró que DevOps no se trata solo de automatizar tareas, sino de construir un flujo confiable que acompañe el código desde que se escribe hasta que se despliega.

En cuanto a nuestra contribución al proyecto, el trabajo fue dividido entre ambos integrantes. Sebastián Antipan se encargó del desarrollo del microservicio con Spring Boot, incluyendo la estructura del controlador, servicio y modelo. Por mi parte, me encargué de la configuración del Dockerfile, Docker Compose, las pruebas unitarias con JUnit y el armado completo del pipeline CI/CD en GitHub Actions, incluyendo la integración con Snyk y Docker Hub.

### Reflexión y Contribución de Sebastián Antipan

Mi principal aprendizaje se centró en cómo la estructura del microservicio impacta en la automatización y gobernanza. Afrontar las alertas de seguridad y entender que un pipeline "rojo" (bloqueado por Snyk) es en realidad un éxito de protección me enseñó el verdadero valor de DevSecOps. Además, aplicar límites estrictos de CPU y memoria en Docker Compose me dio una visión clara de cómo escalar infraestructura en la nube de forma segura.

**Mi contribución:** Desarrollé el microservicio base con Spring Boot 3 (API REST, controladores, servicios y modelos) y colaboré en la configuración de la orquestación, estableciendo los límites de hardware (escalabilidad) en el `docker-compose.yml`.

---

*DUOC UC — Ingeniería DevOps DOY0101 — 2025*

---
---

# Extensión EP3 — Observabilidad y Entornos Reales en DevOps

**Evaluación Parcial N°3**
**DUOC UC — 2025**

---

## Descripción EP3

Esta evaluación extiende el pipeline CI/CD del EP2 incorporando herramientas de **observabilidad,
monitoreo y cumplimiento normativo**. El microservicio ahora se despliega automáticamente en una
instancia **EC2 de AWS Academy** con monitoreo en tiempo real mediante **Prometheus y Grafana**.

---

## Nueva Arquitectura EP3

```
┌─────────────────────────────────────────────────────────────────┐
│                        PIPELINE CI/CD                            │
│                                                                 │
│  Push/PR → Build → Tests → Seguridad → Docker → Deploy (EC2)   │
└─────────────────────────────────────────────────────────────────┘

┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│  Spring Boot │────▶│  Prometheus  │────▶│   Grafana    │
│  Actuator    │     │  (métricas)  │     │  (dashboard) │
└──────────────┘     └──────────────┘     └──────────────┘
```

---

## Nuevos Endpoints EP3

| Método | Ruta                    | Descripción                        |
|--------|-------------------------|------------------------------------|
| GET    | /actuator/prometheus    | Métricas expuestas para Prometheus |
| GET    | /actuator/metrics       | Métricas internas del microservicio|

---

## Nuevos Archivos EP3

```
ep2-devops/
├── prometheus.yml             # Configuración de scraping de métricas
└── (docker-compose.yml actualizado con Prometheus y Grafana)
```

---

## Implementación EP3 — Indicadores de Evaluación

### IE1: Monitoreo con Prometheus
Se agregó la dependencia **Micrometer Registry Prometheus** al `pom.xml`, lo que permite
que Spring Boot exponga automáticamente métricas en el endpoint `/actuator/prometheus`.
Prometheus recolecta estas métricas cada 15 segundos según la configuración en `prometheus.yml`.

### IE2: Despliegue en entorno orquestado (AWS Academy EC2)
El microservicio se despliega automáticamente en una instancia **EC2 t3.micro con Amazon Linux 2023**
mediante el pipeline. Docker Compose orquesta tres servicios: el microservicio, Prometheus y Grafana,
todos en la misma red `ep2-network`.

### IE3: Dashboard de métricas en Grafana
Se configuró un dashboard llamado **"EP3 DevOps - Métricas Microservicio"** en Grafana con 4 paneles:

| Panel | Query | Descripción |
|-------|-------|-------------|
| Uso de Memoria JVM | `jvm_memory_used_bytes` | Memoria heap y non-heap en tiempo real |
| Uso de CPU | `process_cpu_usage` | Porcentaje de CPU consumido |
| Solicitudes HTTP | `http_server_requests_seconds_count` | Total de requests recibidos |
| Disponibilidad | `up` | Estado del microservicio (1=activo, 0=caído) |

### IE4: Integración documentada en el pipeline
Cada herramienta cumple un rol específico en la toma de decisiones técnicas:
- **Prometheus** permite detectar picos de memoria o CPU que indiquen cuellos de botella
- **Grafana** visualiza tendencias para decidir si escalar la infraestructura
- **Actuator** expone el estado interno del servicio para validación automática en el pipeline
- **Branch Protection Rules** garantizan que ningún cambio llegue a producción sin pasar los checks

### IE5: Políticas de cumplimiento — Branch Protection Rules
Se configuraron reglas de protección en la rama `main` de GitHub que exigen:
- Aprobación mediante Pull Request antes de hacer merge
- Que todos los status checks del pipeline pasen exitosamente
- Bloqueo de force pushes directos a main

### IE6: Pipeline se detiene ante fallas críticas
Gracias a las Branch Protection Rules, ningún código puede llegar a la rama `main`
sin que los jobs de Build, Seguridad y Docker hayan pasado exitosamente. Esto garantiza
que el entorno productivo en EC2 nunca recibe código con errores o vulnerabilidades críticas.

---

## Herramientas agregadas en EP3

| Herramienta          | Propósito                                      |
|----------------------|------------------------------------------------|
| Micrometer           | Exposición de métricas para Prometheus         |
| Prometheus           | Recolección de métricas del microservicio      |
| Grafana              | Visualización de métricas en dashboards        |
| AWS Academy EC2      | Entorno de despliegue en la nube               |
| Branch Protection    | Políticas de cumplimiento y control de calidad |

---

## Cómo acceder al entorno EP3

Una vez desplegado en EC2:
- Microservicio: http://IP_EC2:8080/api/productos
- Prometheus: http://IP_EC2:9090
- Grafana: http://IP_EC2:3000 (usuario: admin / contraseña: admin123)

---

## Uso de Inteligencia Artificial EP3

Se utilizó **Claude (Anthropic)** como apoyo para:
- Configuración de la integración entre Spring Boot Actuator y Prometheus
- Estructura del archivo `prometheus.yml`
- Configuración del job de despliegue en EC2 via SSH en el pipeline
- Revisión de sintaxis de archivos de configuración

Todas las ideas, análisis técnicos y justificaciones fueron revisados y validados por ambos integrantes.
Las conclusiones y reflexiones personales son de autoría propia.

**Referencia de uso de IA:** https://bibliotecas.duoc.cl/ia

---

## Reflexión Personal EP3 — Cristina Silva

Durante el desarrollo de la EP3, el aprendizaje más significativo que obtuve fue entender cómo llevar un proyecto desde el entorno local hasta una infraestructura real en la nube. Trabajar con AWS Academy y desplegar en una instancia EC2 me permitió comprender que DevOps va mucho más allá de automatizar un pipeline: implica pensar en la disponibilidad, el monitoreo y la seguridad del sistema en un entorno real.

Configurar Prometheus y Grafana me enseñó que observar el comportamiento de una aplicación en producción es tan importante como desarrollarla. Ver las métricas de memoria y CPU en tiempo real en el dashboard me dio una perspectiva completamente nueva sobre cómo se toman decisiones técnicas en equipos de desarrollo reales. Esta evaluación consolidó mi comprensión del ciclo de vida completo del software, desde el código hasta la producción monitoreada.

---

## Reflexión Personal EP3 — Sebastián Antipan

El mayor aprendizaje de la EP3 fue comprender el proceso completo de llevar un proyecto a una instancia real en la nube. Configurar EC2 en AWS Academy, instalar Docker, conectar el pipeline mediante SSH y ver cómo el deploy se ejecutaba automáticamente ante cada push fue una experiencia que consolidó todo lo aprendido durante el curso de manera práctica y concreta.

Lo que más me impactó fue entender cómo Prometheus y Grafana trabajan juntos para dar visibilidad al sistema. Antes veía el monitoreo como algo abstracto; ahora comprendo que es una herramienta de toma de decisiones: si la memoria sube sostenidamente o la CPU se dispara, el dashboard te lo muestra antes de que el sistema colapse. Eso es el verdadero valor de la observabilidad en DevOps.

---
cambios prueba

*DUOC UC — Ingeniería DevOps DOY0101 — 2025*

Despliegue de evidencia EP3 realizado por Cristina Silva en cuenta propia AWS Academy - 11/07/2026
Feature: se documenta el uso del dashboard de Grafana para el equipo.
Hotfix: corregido enlace roto en la documentacion del proyecto.