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

class FormActivity : AppCompatActivity() {
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        val nameEditText: EditText = findViewById(R.id.nameEditText)
        val ageEditText: EditText = findViewById(R.id.ageEditText)
        val calendarView: CalendarView = findViewById(R.id.periodCalendarView)
        val sendButton: Button = findViewById(R.id.sendButton)

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        selectedDate = dateFormat.format(Date(calendarView.date))

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            selectedDate = dateFormat.format(calendar.time)
        }

        sendButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val age = ageEditText.text.toString()

            if (name.isEmpty() || age.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sharedPref = getSharedPreferences("PeriodTrackerPrefs", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("name", name)
                putString("age", age)
                putString("lastPeriodDate", selectedDate)
                apply()
            }

            val intent = Intent(this, ConfirmationActivity::class.java).apply {
                putExtra("NAME", name)
                putExtra("AGE", age)
                putExtra("PERIOD_DATE", selectedDate)
            }
            startActivity(intent)
        }
    }
}
