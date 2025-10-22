# Period Tracker - Aplicación de Seguimiento Menstrual

## Descripción General
Aplicación Android desarrollada en Kotlin que permite a las usuarias registrar y hacer seguimiento de su ciclo menstrual. La app incluye registro de información personal y un calendario para marcar la fecha del último periodo.


## Estructura del Proyecto
- **MainActivity.kt** - Pantalla de bienvenida
- **FormActivity.kt** - Pantalla de formulario
- **ConfirmationActivity.kt** - Pantalla de confirmación

####  Ventana 1 - Presentación (MainActivity)
**Archivo:** `MainActivity.kt` y `activity_main.xml`

**Componentes obligatorios:**
-  TextView que muestra el nombre de la app
-  Button "Iniciar" que abre la segunda ventana
-  Intent para navegar a FormActivity

**Layout usado:** RelativeLayout

---

####  Ventana 2 - Formulario (FormActivity)
**Archivo:** `FormActivity.kt` y `activity_form.xml`

**Componentes básicos 
1.  EditText para nombre
2.  EditText para edad
3.  CalendarView para fecha del periodo 
4.  Button para enviar datos

**Funcionalidad:**
- Los datos NO se guardan en base de datos
- Se pasan a la siguiente ventana con `Intent.putExtra()`
- Se usa SharedPreferences solo para persistencia local

**Layout usado:** LinearLayout

---

####  Ventana 3 - Confirmación (ConfirmationActivity)
**Archivo:** `ConfirmationActivity.kt` y `activity_confirmation.xml`

**Componentes obligatorios:**
-  TextViews que muestran los datos ingresados
-  Button "Salir" que cierra la app con `finishAffinity()`

**Layout usado:** LinearLayout

---

## Explicación Detallada del Código

### 1. MainActivity.kt
**Propósito:** Pantalla de bienvenida de la aplicación

```kotlin
// LÍNEAS 1-7: Declaración del paquete e importaciones
package com.example.periodtracker
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

// LÍNEAS 8-19: Clase principal de la ventana de presentación
class MainActivity : AppCompatActivity() {
    // LÍNEA 9-11: Método onCreate - Se ejecuta cuando se crea la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // LÍNEA 11: Establece el layout XML de esta actividad
        setContentView(R.layout.activity_main)
        
        // LÍNEA 13: Obtiene referencia al botón "Iniciar" del layout
        val startButton: Button = findViewById(R.id.startButton)
        
        // LÍNEAS 14-17: Define qué hacer cuando se presiona el botón
        startButton.setOnClickListener {
            // LÍNEA 15: Crea un Intent para abrir FormActivity
            val intent = Intent(this, FormActivity::class.java)
            // LÍNEA 16: Inicia la nueva actividad
            startActivity(intent)
        }
    }
}
```

**Resumen MainActivity.kt:**
- **Líneas 1-7:** Importaciones necesarias
- **Líneas 8-19:** Clase que maneja la pantalla inicial
- **Líneas 13-17:** Lógica del botón para navegar a la siguiente pantalla

---

### 2. activity_main.xml
**Propósito:** Diseño visual de la pantalla de presentación

```xml
<!-- LÍNEAS 1-8: Declaración del layout principal (RelativeLayout) -->
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="16dp">

    <!-- LÍNEAS 10-17: TextView que muestra el nombre de la app -->
    <TextView
        android:id="@+id/appNameTextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/app_name"          <!-- Texto: "Period Tracker" -->
        android:textSize="34sp"                    <!-- Tamaño grande -->
        android:layout_centerHorizontal="true"     <!-- Centrado horizontalmente -->
        android:layout_marginTop="100dp"/>         <!-- Margen superior -->

    <!-- LÍNEAS 19-26: Botón "Iniciar" -->
    <Button
        android:id="@+id/startButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/start_button"        <!-- Texto: "Iniciar" -->
        android:layout_below="@id/appNameTextView" <!-- Debajo del título -->
        android:layout_centerHorizontal="true"     <!-- Centrado -->
        android:layout_marginTop="50dp"/>          <!-- Separación -->
</RelativeLayout>
```

**Resumen activity_main.xml:**
- **Líneas 1-8:** Contenedor RelativeLayout
- **Líneas 10-17:** TextView con el nombre de la app
- **Líneas 19-26:** Button para iniciar el registro

---

### 3. FormActivity.kt
**Propósito:** Formulario de registro con calendario para el periodo

```kotlin
// LÍNEAS 1-12: Paquete e importaciones
package com.example.periodtracker
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

// LÍNEAS 14-60: Clase del formulario
class FormActivity : AppCompatActivity() {
    // LÍNEA 15: Variable para almacenar la fecha seleccionada
    private var selectedDate: String = ""

    // LÍNEAS 17-60: Método onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        // LÍNEAS 22-25: Obtener referencias a los componentes del formulario
        val nameEditText: EditText = findViewById(R.id.nameEditText)
        val ageEditText: EditText = findViewById(R.id.ageEditText)
        val calendarView: CalendarView = findViewById(R.id.periodCalendarView)
        val sendButton: Button = findViewById(R.id.sendButton)

        // LÍNEAS 27-28: Configurar formato de fecha y establecer fecha inicial
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        selectedDate = dateFormat.format(Date(calendarView.date))

        // LÍNEAS 30-34: Listener para cuando cambia la fecha en el calendario
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            selectedDate = dateFormat.format(calendar.time)
        }

        // LÍNEAS 36-58: Listener del botón "Guardar"
        sendButton.setOnClickListener {
            // LÍNEAS 37-38: Obtener valores de los campos
            val name = nameEditText.text.toString()
            val age = ageEditText.text.toString()

            // LÍNEAS 40-43: Validar que los campos no estén vacíos
            if (name.isEmpty() || age.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // LÍNEAS 45-51: Guardar datos en SharedPreferences (persistencia local)
            val sharedPref = getSharedPreferences("PeriodTrackerPrefs", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("name", name)
                putString("age", age)
                putString("lastPeriodDate", selectedDate)
                apply()
            }

            // LÍNEAS 53-57: Crear Intent y pasar datos a ConfirmationActivity
            val intent = Intent(this, ConfirmationActivity::class.java).apply {
                putExtra("NAME", name)
                putExtra("AGE", age)
                putExtra("PERIOD_DATE", selectedDate)
            }
            startActivity(intent)
        }
    }
}
```

**Resumen FormActivity.kt:**
- **Líneas 1-12:** Importaciones necesarias para el formulario
- **Líneas 15:** Variable para almacenar fecha seleccionada
- **Líneas 22-25:** Referencias a los 4 componentes del formulario (EditText x2, CalendarView, Button)
- **Líneas 30-34:** Manejo del cambio de fecha en el calendario
- **Líneas 40-43:** Validación de campos
- **Líneas 45-51:** Guardado opcional en SharedPreferences (NO base de datos)
- **Líneas 53-57:** Envío de datos con Intent.putExtra() a la siguiente pantalla

---

### 4. activity_form.xml
**Propósito:** Diseño del formulario con calendario

```xml
<!-- LÍNEAS 1-9: LinearLayout vertical con fondo rosa claro -->
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp"
    android:background="@color/pink_light">

    <!-- LÍNEAS 10-18: Título de la app -->
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/app_name"
        android:textSize="28sp"
        android:textColor="@color/pink_dark"
        android:textStyle="bold"
        android:layout_gravity="center_horizontal"/>

    <!-- LÍNEAS 20-24: Campo de texto para nombre (EditText 1) -->
    <EditText
        android:id="@+id/nameEditText"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="@string/name_hint"/>

    <!-- LÍNEAS 26-31: Campo de texto para edad (EditText 2) -->
    <EditText
        android:id="@+id/ageEditText"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="@string/age_hint"
        android:inputType="number"/>

    <!-- LÍNEAS 33-38: Etiqueta para el calendario -->
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/period_date_label"
        android:textSize="16sp"
        android:textStyle="bold"/>

    <!-- LÍNEAS 40-44: CalendarView para seleccionar fecha (Componente 3) -->
    <CalendarView
        android:id="@+id/periodCalendarView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"/>

    <!-- LÍNEAS 46-52: Botón para guardar (Componente 4) -->
    <Button
        android:id="@+id/sendButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/send_button"
        android:layout_gravity="center_horizontal"
        android:backgroundTint="@color/pink_dark"/>
</LinearLayout>
```

**Resumen activity_form.xml:**
- **Líneas 1-9:** LinearLayout vertical como contenedor
- **Líneas 20-24:** EditText #1 - Nombre
- **Líneas 26-31:** EditText #2 - Edad
- **Líneas 40-44:** CalendarView - Selección de fecha
- **Líneas 46-52:** Button - Enviar datos
- **Total:** 4+ componentes básicos (cumple requisito)

---

### 5. ConfirmationActivity.kt
**Propósito:** Muestra los datos ingresados y calcula días desde el último periodo

```kotlin
// LÍNEAS 1-8: Paquete e importaciones
package com.example.periodtracker
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

// LÍNEAS 10-56: Clase de confirmación
class ConfirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        // LÍNEAS 16-20: Referencias a los TextViews que mostrarán los datos
        val nameTextView: TextView = findViewById(R.id.nameTextView)
        val ageTextView: TextView = findViewById(R.id.ageTextView)
        val periodDateTextView: TextView = findViewById(R.id.periodDateTextView)
        val daysAgoTextView: TextView = findViewById(R.id.daysAgoTextView)
        val exitButton: Button = findViewById(R.id.exitButton)

        // LÍNEAS 22-25: Recibir datos enviados desde FormActivity
        val name = intent.getStringExtra("NAME")
        val age = intent.getStringExtra("AGE")
        val periodDate = intent.getStringExtra("PERIOD_DATE")

        // LÍNEAS 27-29: Mostrar datos en los TextViews
        nameTextView.text = "Nombre: $name"
        ageTextView.text = "Edad: $age"
        periodDateTextView.text = "Última menstruación: $periodDate"

        // LÍNEAS 31-52: Calcular cuántos días han pasado desde el último periodo
        if (periodDate != null) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            try {
                // LÍNEAS 34-35: Parsear la fecha y obtener la fecha actual
                val lastPeriodDate = dateFormat.parse(periodDate)
                val currentDate = Date()
                
                if (lastPeriodDate != null) {
                    // LÍNEAS 38-39: Calcular diferencia en milisegundos y convertir a días
                    val diffInMillis = currentDate.time - lastPeriodDate.time
                    val daysAgo = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS)
                    
                    // LÍNEAS 41-48: Mostrar mensaje según los días transcurridos
                    if (daysAgo == 0L) {
                        daysAgoTextView.text = "Registrado hoy"
                    } else if (daysAgo == 1L) {
                        daysAgoTextView.text = "Hace 1 día"
                    } else {
                        daysAgoTextView.text = "Hace $daysAgo días"
                    }
                }
            } catch (e: Exception) {
                daysAgoTextView.text = ""
            }
        }

        // LÍNEA 54: Botón salir - cierra todas las actividades
        exitButton.setOnClickListener { finishAffinity() }
    }
}
```

**Resumen ConfirmationActivity.kt:**
- **Líneas 1-8:** Importaciones
- **Líneas 16-20:** Referencias a TextViews para mostrar datos
- **Líneas 22-25:** Recepción de datos con Intent.getStringExtra()
- **Líneas 27-29:** Mostrar datos recibidos
- **Líneas 31-52:** Cálculo de días transcurridos desde el último periodo
- **Línea 54:** Botón que cierra la app con finishAffinity()

---

### 6. activity_confirmation.xml
**Propósito:** Diseño de la pantalla de confirmación

```xml
<!-- LÍNEAS 1-10: LinearLayout vertical con fondo rosa -->
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp"
    android:background="@color/pink_light">

    <!-- LÍNEAS 11-19: Título "Información Guardada" -->
    <TextView
        android:id="@+id/confirmationTitleTextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/confirmation_title"
        android:textSize="28sp"
        android:textColor="@color/pink_dark"
        android:textStyle="bold"/>

    <!-- LÍNEAS 21-25: TextView para mostrar el nombre -->
    <TextView
        android:id="@+id/nameTextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="18sp"/>

    <!-- LÍNEAS 27-31: TextView para mostrar la edad -->
    <TextView
        android:id="@+id/ageTextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="18sp"/>

    <!-- LÍNEAS 33-39: TextView para mostrar la fecha del periodo -->
    <TextView
        android:id="@+id/periodDateTextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="18sp"
        android:textStyle="bold"
        android:textColor="@color/pink_dark"/>

    <!-- LÍNEAS 41-45: TextView para mostrar días transcurridos -->
    <TextView
        android:id="@+id/daysAgoTextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="16sp"/>

    <!-- LÍNEAS 47-53: Botón "Salir" -->
    <Button
        android:id="@+id/exitButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/exit_button"
        android:layout_gravity="center_horizontal"
        android:backgroundTint="@color/pink_dark"/>
</LinearLayout>
```

**Resumen activity_confirmation.xml:**
- **Líneas 1-10:** LinearLayout como contenedor
- **Líneas 11-19:** Título de la pantalla
- **Líneas 21-31:** TextViews para nombre y edad
- **Líneas 33-39:** TextView para fecha del periodo
- **Líneas 41-45:** TextView para días transcurridos
- **Líneas 47-53:** Botón para salir de la app

---

## Colores Personalizados

**Archivo:** `values/colors.xml`

```xml
<resources>
    <!-- Colores temáticos de la app (tema menstrual - rosa) -->
    <color name="pink_primary">#FFC0CB</color>      <!-- Rosa suave -->
    <color name="pink_dark">#FF69B4</color>         <!-- Rosa oscuro -->
    <color name="pink_light">#FFE6E6</color>        <!-- Rosa muy claro (fondo) -->
    <color name="ic_launcher_background">#FF69B4</color>  <!-- Ícono -->
    <color name="ic_launcher_foreground">#FFFFFF</color>  <!-- Ícono -->
</resources>
```

---

## Strings (Textos de la App)

**Archivo:** `values/strings.xml`

```xml
<resources>
    <string name="app_name">Period Tracker</string>
    <string name="start_button">Iniciar</string>
    <string name="name_hint">Nombre</string>
    <string name="age_hint">Edad</string>
    <string name="period_date_label">Selecciona la fecha de tu última menstruación:</string>
    <string name="send_button">Guardar</string>
    <string name="exit_button">Salir</string>
    <string name="confirmation_title">Información Guardada</string>
</resources>
```

---

## Navegación entre Pantallas

### Flujo de la aplicación:
```
MainActivity (Presentación)
    ↓ (Intent al presionar "Iniciar")
FormActivity (Formulario)
    ↓ (Intent con putExtra() al presionar "Guardar")
ConfirmationActivity (Confirmación)
    ↓ (finishAffinity() al presionar "Salir")
App cerrada
```

### Paso de datos:
```kotlin
// En FormActivity.kt - Envío de datos
intent.putExtra("NAME", name)
intent.putExtra("AGE", age)
intent.putExtra("PERIOD_DATE", selectedDate)

// En ConfirmationActivity.kt - Recepción de datos
val name = intent.getStringExtra("NAME")
val age = intent.getStringExtra("AGE")
val periodDate = intent.getStringExtra("PERIOD_DATE")
```

---

