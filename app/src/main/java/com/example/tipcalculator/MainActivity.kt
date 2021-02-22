package com.example.tipcalculator

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.example.tipcalculator.databinding.ActivityMainBinding

private const val INITIAL_TIP_PERCENT = 15;
private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
   private lateinit var viewBinding: ActivityMainBinding

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      viewBinding = ActivityMainBinding.inflate(layoutInflater)
      val view = viewBinding.root
      setContentView(view)

      viewBinding.apply {
         seekBar.progress = INITIAL_TIP_PERCENT
         tvTipPercentage.text = "$INITIAL_TIP_PERCENT%"

         seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
               tvTipPercentage.text = "$progress%"
               updateTipDescription(progress)
               calculateTipAndTotal()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
         })

         etBase.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
               Log.i(TAG, "After text changed : $s")
               calculateTipAndTotal()
            }
         })
      }
   }

   private fun updateTipDescription(tip: Int) {
      val tipDescription: String = when(tip) {
      in 0..9 -> "Poor"
      in 10..14 -> "Acceptable"
      in 15..19 -> "Good"
      in 20..24 -> "Great"
      else -> "Amazing"
   }
      viewBinding.tvTipDescription.text = tipDescription
     val color =  ArgbEvaluator().evaluate(
         tip.toFloat() / viewBinding.seekBar.max,
         ContextCompat.getColor(this, R.color.tipWorst),
         ContextCompat.getColor(this, R.color.tipGood)
      ) as Int

      viewBinding.tvTipDescription.setTextColor(color)
   }

   private fun calculateTipAndTotal() {
      if(viewBinding.etBase.text.isEmpty()){
         viewBinding.apply {
            tvTipResult.text = ""
            tvTotalResult.text = ""
            return
         }
      }
      val baseAmount = viewBinding.etBase.text.toString()?.toDouble()
      val tipPercent = viewBinding.seekBar.progress
      val tipAmount = baseAmount * tipPercent / 100
      viewBinding.tvTipResult.text = "%.2f".format(tipAmount)
      viewBinding.tvTotalResult.text = "%.2f".format(baseAmount + tipAmount)
   }
}