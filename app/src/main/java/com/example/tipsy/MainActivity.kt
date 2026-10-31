package com.example.tipsy

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15
private const val INITIAL_PEOPLE_PERCENT = 5

class MainActivity : AppCompatActivity() {

    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvPercetageLabel: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView

    private lateinit var seekBarPerHead: SeekBar

    private lateinit var tvPersonAmount: TextView
    private lateinit var tvPerHead: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etBaseAmount = findViewById(R.id.tdBaseAmount)
        seekBarTip = findViewById(R.id.sbPercentage)
        tvPercetageLabel = findViewById(R.id.tvPercetageLabel)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        //hold seek bar vale
        seekBarPerHead = findViewById(R.id.sbPerHead)

        tvPersonAmount = findViewById(R.id.tvPersonAmount)
        tvPerHead = findViewById(R.id.tvPerHead)

        // Set the initial tip percentage
        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvPercetageLabel.text = "$INITIAL_TIP_PERCENT%"

        seekBarPerHead.progress = INITIAL_PEOPLE_PERCENT
        tvPerHead.text = "/$INITIAL_PEOPLE_PERCENT"


        // Listen for SeekBar changes
        seekBarTip.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {

                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    Log.i(TAG, "onProgressChanged: $progress")

                    // Update percentage label
                    tvPercetageLabel.text = "$progress%"

                    // Recalculate tip and total immediately
                    computeTipAndTotal()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            }
        )

        seekBarPerHead.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {

                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    Log.i(TAG, "onProgressChanged: $progress")

                    // Update percentage label
                    tvPerHead.text = "/$progress"

                    // Recalculate tip and total immediately
                    computeTipAndTotal()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            }
        )

        // Listen for changes to the bill amount
        etBaseAmount.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged: $s")
                computeTipAndTotal()
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {}
        })
    }

    // Calculate the tip and total bill
    private fun computeTipAndTotal() {

        val baseAmount = etBaseAmount.text.toString().toDoubleOrNull()

        if (baseAmount == null) {
            tvTipAmount.text = ""
            tvTotalAmount.text = ""
            tvPersonAmount.text = ""

            return
        }

        val tipPercent = seekBarTip.progress
        val howMany = seekBarPerHead.progress

        val tipAmount = baseAmount * tipPercent / 100.0
        val totalAmount = baseAmount + tipAmount

        val personAmount = totalAmount/howMany



        // Display results with two decimal places
        tvTipAmount.text = String.format(Locale.US, "%.2f", tipAmount)
        tvTotalAmount.text = String.format(Locale.US, "%.2f", totalAmount)

        //display per person head
        tvPersonAmount.text =String.format(Locale.US, "%.2f", personAmount)
    }
}