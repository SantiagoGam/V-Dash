# V-Dash :)

V-Dash es una colección de widgets para Android enfocada en el aprendizaje pasivo del inglés. No es una aplicación tradicional: no tiene menús ni pantalla principal. Es una herramienta de inmersión que vive en tu pantalla de inicio.

Está pensada para aprovechar la exposición repetida: cada vez que desbloqueas el celular, ves un verbo irregular o un *phrasal verb* sin tener que abrir nada.

<!-- Agrega aquí una captura de los dos widgets: ![V-Dash](capturas/widgets.png) -->

## ✨ Características

- **Dos widgets independientes:**
  - **Verbos irregulares:** infinitivo, pasado y participio con fonética, traducción y un ejemplo con su traducción.
  - **Phrasal verbs:** fonética, significado y dos ejemplos con su traducción.
- **Cambio al toque:** toca el fondo de cualquier tarjeta para pasar a otro verbo al azar.
- **Pronunciación sin internet:** el widget de phrasal verbs tiene un botón de altavoz (🔊) que usa el motor de voz del celular, en inglés de EE. UU. y al 85 % de velocidad.
- **Datos locales:** 131 verbos irregulares y 202 phrasal verbs, guardados en archivos JSON dentro de la app. No usa internet ni recopila datos.
- **Diseño oscuro y minimalista**, pensado para leerse rápido sobre cualquier fondo.
- **Sin pantalla principal:** si abres la app desde el cajón de aplicaciones, solo aparece un aviso breve que se cierra solo.

## 📋 Requisitos

Necesitas:

- Un celular con **Android 7.0 (API 24) o superior**.
- Un computador con [Android Studio](https://developer.android.com/studio) instalado.
- Un **cable USB que transmita datos** (algunos cables solo cargan).
- Unos **10 GB libres** en el disco, que es lo que ocupa Android Studio con sus herramientas.
- Para la pronunciación: la voz en **inglés (EE. UU.)** instalada en el motor de texto a voz del celular.

## 📱 Instalación

Sigue estos pasos para compilar el proyecto e instalar los widgets en tu celular directamente desde el código fuente. No hace falta saber programar. Tiempo estimado: 20 a 40 minutos, casi todo esperando descargas y la primera compilación.

### Paso 1: Preparar el celular

1. Ve a **Ajustes > Acerca del teléfono** y toca siete veces **Número de compilación**. Aparecerá un aviso de que ya eres desarrollador.
2. Entra a **Opciones de desarrollador** y activa **Depuración por USB**.
3. En algunas marcas (por ejemplo Realme, Oppo o Xiaomi) también debes activar **Instalar vía USB**.
4. Conecta el celular al computador con el cable y acepta el aviso **Permitir depuración USB** que aparece en la pantalla.

> La ruta exacta de los menús cambia un poco según la marca. Si no encuentras una opción, usa el buscador de Ajustes.

### Paso 2: Descargar el proyecto

Abre una terminal y ejecuta:

```bash
git clone https://github.com/TU_USUARIO/VDash.git
```

También puedes descargar el repositorio como ZIP desde GitHub (botón **Code > Download ZIP**) y descomprimirlo.

### Paso 3: Abrir el proyecto en Android Studio

1. Abre Android Studio y elige **Open**.
2. Selecciona la carpeta `VDash`.
3. Espera a que termine la barra de progreso de abajo. La primera sincronización de Gradle puede tardar **varios minutos**, y es normal que parezca quieta.

### Paso 4: Instalar en tu celular

1. En la barra superior, elige tu celular en la lista de dispositivos.
2. Pulsa el botón verde **Run** (o `Shift + F10`).
3. Espera a que compile. Cuando termine, la app se instalará y se abrirá un aviso breve que se cierra solo. Es lo esperado: V-Dash no tiene pantalla principal.

### Paso 5: Añadir los widgets a la pantalla de inicio

1. Mantén pulsado un espacio vacío de la pantalla de inicio.
2. Elige **Widgets**.
3. Busca **V-Dash**. Verás dos widgets: **verbos irregulares** y **phrasal verbs**.
4. Arrástralos a tu pantalla.
5. Estíralos hasta **4 filas de alto** para que los ejemplos se vean completos.

Listo: toca cualquier tarjeta para pasar a otro verbo.

### Opcional: versión release

La versión que instala **Run** es la de depuración, que es la más lenta. Para probar una más ligera:

1. Abre `app/build.gradle.kts`.
2. Dentro de `buildTypes > release`, agrega esta línea:

   ```kotlin
   signingConfig = signingConfigs.getByName("debug")
   ```

3. Pulsa **Sync Now** en el aviso azul de arriba.
4. Abre **Build Variants** (`View > Tool Windows > Build Variants`) y cambia `app` de `debug` a `release`.
5. Pulsa **Run**.

Se instala sobre la versión anterior. Si Android rechaza la instalación con `INSTALL_FAILED_UPDATE_INCOMPATIBLE`, desinstala V-Dash del celular y vuelve a pulsar **Run**. Para volver a la versión de depuración, cambia `release` a `debug` en el mismo panel.

## 🔧 Problemas comunes

| Problema | Qué hacer |
|---|---|
| El widget dice "Can't show content" | Comprueba que `irregular_verbs.json` y `phrasal_verbs.json` estén en `app/src/main/assets/`, vuelve a pulsar **Run** y quita y añade el widget. |
| La instalación falla con `INSTALL_FAILED_USER_RESTRICTED` | Activa **Instalar vía USB** en Opciones de desarrollador. |
| El celular no aparece en Android Studio | Prueba otro cable, revisa que la depuración USB esté activa y acepta el aviso en la pantalla del celular. |
| En la terminal, `adb devices` dice `unauthorized` | Desbloquea el celular y acepta el aviso de depuración USB. |
| En la terminal, `adb devices` dice `no permissions` (Linux) | Faltan permisos USB; instala las reglas udev con `sudo apt install android-sdk-platform-tools-common` y reconecta el celular. |
| El altavoz no suena | Revisa que la voz en inglés (EE. UU.) esté instalada y que el volumen multimedia no esté en silencio. |
| Un texto del widget aparece cortado | Estira el widget para darle más altura. |
| Los widgets tardan en responder al tocar | Es una limitación de cómo Android redibuja los widgets. Prueba la versión release y revisa que el ahorro de batería no esté restringiendo la app. |
| Gradle da error al sincronizar | Copia el mensaje completo de la pestaña **Build** o **Sync** y búscalo; casi siempre es una dependencia que no se pudo descargar por falta de internet. |

## ⚠️ Limitaciones conocidas

- **La fonética no está verificada** contra un diccionario y puede tener errores. Si encuentras alguno, corrígelo en los archivos JSON.
- El cambio de verbo es **solo manual** (al tocar). Todavía no rota automáticamente.
- Los widgets de **pantalla de bloqueo** dependen de la marca y la versión de Android; no todos los celulares los permiten.
- La respuesta al tocar tiene un pequeño retraso propio de los widgets de Android.
- Probado únicamente en un Realme.

## 🗂️ Agregar tus propios verbos

Los datos están en `app/src/main/assets/`. Cada archivo es una lista de objetos.

**`irregular_verbs.json`**

```json
{"b":"write","bp":"/raɪt/","p":"wrote","pp":"/roʊt/","pt":"written","ptp":"/ˈrɪtn/","es":"escribir","ex":"He wrote me a long letter.","exEs":"Él me escribió una carta larga."}
```

**`phrasal_verbs.json`**

```json
{"v":"give up","ph":"/ɡɪv ʌp/","es":"rendirse / dejar de","ex":"Never give up on your dreams.","exEs":"Nunca renuncies a tus sueños.","ex2":"I gave up smoking last year.","exEs2":"Dejé de fumar el año pasado."}
```

Después de editar, vuelve a pulsar **Run**.

## 🛠️ Tecnologías

- **Kotlin**
- **Jetpack Glance** para los widgets
- **Text-to-Speech** de Android

## 📝 Nota

Proyecto personal, sin fines de lucro.

