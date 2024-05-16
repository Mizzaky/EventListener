package com.example.eventlistener

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.app.Activity

class BMI_Activity : AppCompatActivity() {

    lateinit var beratEditText: EditText
    lateinit var tinggiEditText: EditText
    lateinit var hitungButton: Button
    lateinit var resultTextView: TextView
    lateinit var ageGroupSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bmi)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        beratEditText = findViewById(R.id.berat_edit_text)
        tinggiEditText = findViewById(R.id.tinggi_edit_text)
        hitungButton = findViewById(R.id.hitung_button)
        resultTextView = findViewById(R.id.result_text_view)

        hitungButton.setOnClickListener {
            val berat = beratEditText.text.toString().toDouble()
            val tinggi = tinggiEditText.text.toString().toDouble()
            val bmi = berat / (tinggi / 100 * tinggi / 100)

            var status = ""
            if (bmi < 18.5) {
                status = "Underweight"
            } else if (bmi >= 18.5 && bmi <= 24.9) {
                status = "Healthy"
            } else if (bmi >= 25 && bmi <= 29.99) {
                status = "Overweight"
            } else {
                status = "Obese"
            }

            resultTextView.text = "Status: $status"

            // Create an Intent to start the ResultActivity
            val intent = Intent(this, ResultActivity::class.java)

            // Add the result as an extra to the Intent
            intent.putExtra("result", resultTextView.text.toString())

            // Start the ResultActivity
            startActivityForResult(intent, REQUEST_CODE)
        }

        // Initialize the Spinner
        ageGroupSpinner = findViewById(R.id.age_group_spinner)

        // Set the adapter for the Spinner
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.age_group,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        ageGroupSpinner.adapter = adapter

        // Set an OnItemSelectedListener for the Spinner
        ageGroupSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val ageGroup = parent.getItemAtPosition(position).toString()
                if (ageGroup == "Dewasa") {
                    // Set the interpretation for adults
                    interpretBMIFornAdults(beratEditText.text.toString().toDouble(), tinggiEditText.text.toString().toDouble())
                } else if (ageGroup == "Anak-anak") {
                    // Set the interpretation for children
                    interpretBMIFornChildren(beratEditText.text.toString().toDouble(), tinggiEditText.text.toString().toDouble())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    // Add these functions to interpret BMI for adults and children
    fun interpretBMIFornAdults(berat: Double, tinggi: Double) {
        val bmi = berat / (tinggi / 100 * tinggi / 100)

        var status = ""
        if (bmi < 18.5) {
            status = "Underweight"
        } else if (bmi >= 18.5 && bmi <= 24.9) {
            status = "Healthy"
        } else if (bmi >= 25 && bmi <= 29.99) {
            status = "Overweight"
        } else {
            status = "Obese"
        }

        resultTextView.text = "Status: $status"
    }

    fun interpretBMIFornChildren(berat: Double, tinggi: Double) {
        // Implement the interpretation logic for children's BMI here
    }

    companion object {
        const val REQUEST_CODE = 1
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringExtra("result")
            // Do something with the result
            resultTextView.text = "Result: $result"
        }
    }
}
