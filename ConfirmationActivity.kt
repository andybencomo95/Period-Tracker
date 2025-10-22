package com.example.periodtracker

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ConfirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        val nameTextView: TextView = findViewById(R.id.nameTextView)
        val ageTextView: TextView = findViewById(R.id.ageTextView)
        val periodDateTextView: TextView = findViewById(R.id.periodDateTextView)
        val daysAgoTextView: TextView = findViewById(R.id.daysAgoTextView)
        val exitButton: Button = findViewById(R.id.exitButton)

        val name = intent.getStringExtra("NAME")
        val age = intent.getStringExtra("AGE")
        val periodDate = intent.getStringExtra("PERIOD_DATE")

        nameTextView.text = "Nombre: $name"
        ageTextView.text = "Edad: $age"
        periodDateTextView.text = "Última menstruación: $periodDate"

        if (periodDate != null) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            try {
                val lastPeriodDate = dateFormat.parse(periodDate)
                val currentDate = Date()
                
                if (lastPeriodDate != null) {
                    val diffInMillis = currentDate.time - lastPeriodDate.time
                    val daysAgo = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS)
                    
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

        exitButton.setOnClickListener { finishAffinity() }
    }
}
