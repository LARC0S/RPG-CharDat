# CharDat – Gestor de Personajes de Anima Beyond Fantasy

**CharDat** es una aplicación desarrollada en **Kotlin** para la gestión avanzada de personajes del juego de rol **Anima Beyond Fantasy**.  
Su objetivo es ofrecer una experiencia moderna, ágil y visualmente atractiva que simplifique la creación, edición y almacenamiento de fichas de personaje, manteniendo la fidelidad a las reglas oficiales del sistema.

---

## 🧭 Pantallas principales

### 1. Pantalla de Inicio
<img src="./img/1.png" alt="Pantalla de inicio" width="800">

La pantalla inicial permite acceder rápidamente a la creación o carga de personajes existentes. Su diseño en **Jetpack Compose** proporciona una navegación fluida y una interfaz reactiva.

---

### 2. Ficha del Personaje
<img src="./img/2.png" alt="Ficha del personaje" width="800">

Vista principal donde se gestionan las estadísticas base, características y puntos de desarrollo del personaje.  
Los cálculos y validaciones se realizan automáticamente mediante controladores inyectados con **Hilt**, garantizando separación clara de responsabilidades.

---

### 3. Habilidades y Competencias
<img src="./img/3.png" alt="Pantalla de habilidades" width="800">

Muestra las **habilidades secundarias** organizadas por categorías, actualizándose en tiempo real según los puntos distribuidos.  
La capa de persistencia basada en **Room** mantiene los datos sincronizados localmente, asegurando consistencia incluso sin conexión.

---

### 4. Magia y Poderes
<img src="./img/4.png" alt="Pantalla de magia" width="800">

Sección dedicada a los caminos mágicos, conjuros y poderes sobrenaturales.  
La sincronización con el servicio remoto se realiza a través de un **API REST** protegido con cifrado y autenticación de usuario.  
Los datos se almacenan en **MongoDB**, actuando como backend persistente y fuente de sincronía.

---

### 5. Resumen y Exportación
<img src="./img/5.png" alt="Pantalla de resumen" width="800">

Permite revisar toda la ficha del personaje y exportarla o guardarla localmente.  
Su estructura modular facilita la extensión a nuevos sistemas o juegos en el futuro.

---

## ⚙️ Tecnologías utilizadas

| Tecnología | Función |
|-------------|----------|
| **Kotlin** | Lenguaje principal, orientado a objetos y funcional |
| **Jetpack Compose** | Framework declarativo para la UI |
| **Hilt (Dagger)** | Inyección de dependencias para controladores y repositorios |
| **Room** | Base de datos local y cacheo offline |
| **REST API + MongoDB** | Sincronización remota y almacenamiento centralizado |
| **Encryption Layer** | Cifrado de credenciales y datos sensibles |
| **Coroutines / Flow** | Gestión reactiva y asincronía eficiente |

---

## 🧩 Arquitectura

El proyecto sigue una arquitectura **MVVM (Model–View–ViewModel)** apoyada en los principios de **Clean Architecture**, separando de forma clara la lógica de dominio, presentación y datos.