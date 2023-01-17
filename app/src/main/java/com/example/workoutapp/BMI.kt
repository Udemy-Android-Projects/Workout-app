package com.example.workoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.workoutapp.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMI : AppCompatActivity() {

    companion object{
        private const val METRIC_UNITS_VIEW = "METRIC_UNITS_VIEW"
        private const val IMPERIAL_UNITS_VIEW = "IMPERIAL_UNITS_VIEW"
    }


    var binding : ActivityBmiBinding? = null
    private var currentView : String = METRIC_UNITS_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.tbBMI)
        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Calculate BMI"
        }
        binding?.tbBMI?.setNavigationOnClickListener {
            onBackPressed()
        }
        //Default is when metric units are visible
        makeMetricVisible()
        binding?.rgUnits?.setOnCheckedChangeListener{ _,checkedId : Int ->
            if(checkedId == R.id.rbMetric){
                makeMetricVisible()
            }else if(checkedId == R.id.rbImperial){
                makeImperialVisible()
            }
        }
        binding?.btnCalculateBMI?.setOnClickListener{
           calculateBMI()
        }
    }

    private fun validateMetricUnits(): Boolean{
        var isValid = true
        if(binding?.etMetricHeight?.text.toString().isEmpty()){
            isValid = false
        }else if(binding?.etMetricWeight?.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }

    private fun validateImperialUnits(): Boolean{
        var isValid = true
        if(binding?.etImperialHeightFeet?.text.toString().isEmpty()){
            isValid = false
        }
        else if(binding?.etImperialHeightInch?.text.toString().isEmpty()){
            isValid = false
        }
        else if(binding?.etMetricWeight?.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }

    private fun calculateBMI(){
        if(currentView == METRIC_UNITS_VIEW){
            if(validateMetricUnits()){
                val heightValue : Float = binding?.etMetricHeight?.text.toString().toFloat() / 100
                val weightValue : Float = binding?.etMetricWeight?.text.toString().toFloat()
                val BMI : Float = weightValue / (heightValue*heightValue)
                displayBMIResults(BMI)
            }else{
                Toast.makeText(this,"Please enter valid entries",Toast.LENGTH_LONG).show()
            }
        }
        else if(currentView == IMPERIAL_UNITS_VIEW){
            if(validateImperialUnits()){
                val weightValue : Float = binding?.etMetricWeight?.text.toString().toFloat()
                val heightValueFeet : Float = binding?.etImperialHeightFeet?.text.toString().toFloat()
                val heightValueInches : Float = binding?.etImperialHeightInch?.text.toString().toFloat()
                val heightValue : Float = heightValueFeet*12 + heightValueInches
                val BMI : Float = 703 * (weightValue / (heightValue*heightValue))
                displayBMIResults(BMI)
            }else{
                Toast.makeText(this,"Please enter valid entries",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun displayBMIResults(BMI : Float){
        val bmiLabel : String
        val bmiDescription : String
        binding?.llDisplayBmiResult?.visibility = View.VISIBLE
        //compareTo() will be 0 if equal,negative when less,positive when greater
        if(BMI.compareTo(15f) <= 0){
            bmiLabel = "Very severely under weight"
            bmiDescription = "You need to eat more food"
        }else if(BMI.compareTo(15f) > 0 && BMI.compareTo(16f) <= 0){
            bmiLabel = "Severely under weight"
            bmiDescription = "You need to eat more food"
        }else if(BMI.compareTo(16f) > 0 && BMI.compareTo(18.5f) <= 0){
            bmiLabel = "Under weight"
            bmiDescription = "You need to eat more food"
        }else if(BMI.compareTo(18.5f) > 0 && BMI.compareTo(25f) <= 0){
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in good shape"
        }else if(BMI.compareTo(25f) > 0 && BMI.compareTo(30f) <= 0){
            bmiLabel = "Overweight"
            bmiDescription = "You need to start working out before its too late"
        }else if(BMI.compareTo(30f) > 0 && BMI.compareTo(35f) <= 0){
            bmiLabel = "Obese class | (Moderately obese)"
            bmiDescription = "You need to start working out"
        }else if(BMI.compareTo(35f) > 0 && BMI.compareTo(40f) <= 0){
            bmiLabel = "Obese class | (Severely obese)"
            bmiDescription = "You need to start working out and eat vegetables"
        }else{
            bmiLabel = "Obese class | (Very severely obese)"
            bmiDescription = "Never eat food in your life"
        }

        val bmiValue = BigDecimal(BMI.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()
        binding?.tvBMIvalue?.text = bmiValue
        binding?.tvBMItype?.text = bmiLabel
        binding?.tvBMIdescription?.text = bmiDescription

    }

    private fun makeMetricVisible(){
        currentView = METRIC_UNITS_VIEW
        binding?.etMetricWeight?.hint = "WEIGHT (in kg)"
        binding?.etMetricHeight?.hint = "HEIGHT (in cm)"
        binding?.tilMetricHeight?.visibility = View.VISIBLE
        binding?.tilImperialHeightFeet?.visibility = View.GONE
        binding?.tilImperialHeightInch?.visibility = View.GONE

        //Clear values
        binding?.etMetricWeight?.text!!.clear()
        binding?.etMetricHeight?.text!!.clear()
        binding?.llDisplayBmiResult?.visibility = View.INVISIBLE
    }

    private fun makeImperialVisible(){
        currentView = IMPERIAL_UNITS_VIEW
        binding?.etMetricWeight?.hint = "WEIGHT (in pounds)"
        binding?.tilMetricHeight?.visibility = View.INVISIBLE
        binding?.tilImperialHeightFeet?.visibility = View.VISIBLE
        binding?.tilImperialHeightInch?.visibility = View.VISIBLE

        //Clear values
        binding?.etMetricWeight?.text!!.clear()
        binding?.etImperialHeightFeet?.text!!.clear()
        binding?.etImperialHeightInch?.text!!.clear()
        binding?.llDisplayBmiResult?.visibility = View.INVISIBLE
    }
}