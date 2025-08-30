package com.example.myapplication
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.exp

class MainActivity : AppCompatActivity() {

    private lateinit var inputPregnancies: EditText
    private lateinit var inputGlucose: EditText
    private lateinit var inputBloodPressure: EditText
    private lateinit var inputSkinThickness: EditText
    private lateinit var inputInsulin: EditText
    private lateinit var inputBmi: EditText
    private lateinit var inputDpf: EditText
    private lateinit var inputAge: EditText
    private lateinit var btnPredict: Button
    private lateinit var textResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        inputPregnancies = findViewById(R.id.inputPregnancies)
        inputGlucose = findViewById(R.id.inputGlucose)
        inputBloodPressure = findViewById(R.id.inputBloodPressure)
        inputSkinThickness = findViewById(R.id.inputSkinThickness)
        inputInsulin = findViewById(R.id.inputInsulin)
        inputBmi = findViewById(R.id.inputBmi)
        inputDpf = findViewById(R.id.inputDpf)
        inputAge = findViewById(R.id.inputAge)
        btnPredict = findViewById(R.id.btnPredict)
        textResult = findViewById(R.id.textResult)

        btnPredict.setOnClickListener {
            predictDiabetes()
        }
    }

    private fun predictDiabetes() {
        // Get all values with defaults
        val pregnancies = inputPregnancies.text.toString().toDoubleOrNull() ?: 0.0
        val glucose = inputGlucose.text.toString().toDoubleOrNull() ?: 0.0
        val bloodPressure = inputBloodPressure.text.toString().toDoubleOrNull() ?: 0.0
        val skinThickness = inputSkinThickness.text.toString().toDoubleOrNull() ?: 0.0
        val insulin = inputInsulin.text.toString().toDoubleOrNull() ?: 0.0
        val bmi = inputBmi.text.toString().toDoubleOrNull() ?: 0.0
        val dpf = inputDpf.text.toString().toDoubleOrNull() ?: 0.0
        val age = inputAge.text.toString().toDoubleOrNull() ?: 0.0

        // Validate critical inputs
        if (glucose == 0.0) {
            Toast.makeText(this, "Please enter glucose value", Toast.LENGTH_SHORT).show()
            return
        }

        // Dataset-derived formula (simplified from actual logistic regression)
        val probability = calculateProbability(
            pregnancies, glucose, bloodPressure,
            skinThickness, insulin, bmi, dpf, age
        )

        // Binary result
        textResult.text = if (probability >= 0.5) "DIABETIC" else "NON-DIABETIC"
        textResult.setTextColor(
            if (probability >= 0.5) getColor(android.R.color.holo_red_dark)
            else getColor(android.R.color.holo_green_dark)
        )
    }

    // Formula derived from dataset patterns
    private fun calculateProbability(
        pregnancies: Double,
        glucose: Double,
        bloodPressure: Double,
        skinThickness: Double,
        insulin: Double,
        bmi: Double,
        dpf: Double,
        age: Double
    ): Double {
        // Coefficients approximated from dataset analysis
        val z = (-8.0 +
                0.12 * pregnancies +
                0.06 * glucose +
                0.02 * bloodPressure +
                0.01 * skinThickness +
                0.002 * insulin +
                0.07 * bmi +
                1.08 * dpf +
                0.04 * age)

        return 1.0 / (1.0 + exp(-z))
    }
}