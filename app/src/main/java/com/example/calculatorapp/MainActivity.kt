package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.lang.NumberFormatException

private const val savedOperand1 = "savedOperand1"
private const val savedOperand2 = "savedOperand2"
private const val savedOperation = "savedOperation"

class MainActivity : AppCompatActivity() {
    private lateinit var result: EditText
    private lateinit var newNumber: EditText
    private val displayOperation by lazy(LazyThreadSafetyMode.NONE) { findViewById<TextView>(R.id.operation) }



    //Variables to hold the operands and the type of calculation
    private var operand1: Double? = null
    private var operand2: Double = 0.0
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        result = findViewById(R.id.result)
        newNumber = findViewById(R.id.newNumber)


        //Data input buttons
        val button0: Button = findViewById(R.id.button0)
        val button1: Button = findViewById(R.id.button1)
        val button2: Button = findViewById(R.id.button2)
        val button3: Button = findViewById(R.id.button3)
        val button4: Button = findViewById(R.id.button4)
        val button5: Button = findViewById(R.id.button5)
        val button6: Button = findViewById(R.id.button6)
        val button7: Button = findViewById(R.id.button7)
        val button8: Button = findViewById(R.id.button8)
        val button9: Button = findViewById(R.id.button9)
        val buttonDot: Button = findViewById(R.id.dot)
        //Clear Screan button
        val clearScreen: Button = findViewById(R.id.clear)

        clearScreen.setOnClickListener{ v ->
            operand1 = null
            operand2 = 0.0
            result.setText("")
            newNumber.setText("")
        }



        //Operation buttons

        val buttonEqual: Button = findViewById(R.id.buttonEqual)
        val buttonDivide: Button = findViewById(R.id.divide)
        val buttonMultiply: Button = findViewById(R.id.multiply)
        val buttonSubtract: Button = findViewById(R.id.subtract)
        val buttonAdd: Button = findViewById(R.id.add)


        //adding an onclick listener

        val listener = View.OnClickListener { v ->
            //casting the view as button
            val b = v as Button
            newNumber.append(b.text)
        }

        //adding the listener to each button
        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        //Listen for an operation ( plus, minus, divide, subtract )

        val opListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            try {
                val value = newNumber.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException){
                newNumber.setText("")
            }
            pendingOperation = op
            displayOperation.text = pendingOperation
        }

        //Add the opListener to all the operation buttons
        buttonEqual.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)
        buttonAdd.setOnClickListener(opListener)
        buttonMultiply.setOnClickListener(opListener)
        buttonSubtract.setOnClickListener(opListener)
    }


    private fun performOperation(value: Double, operation: String){

        if(operand1 == null){
            operand1 = value
        }else {
            operand2 = value

            if(pendingOperation == "="){
                pendingOperation = operation
            }

            //handling for different operations
            when(pendingOperation){
                "=" -> operand1 = operand2
                "/" -> if(operand2 == 0.0){
                          operand1 = Double.NaN
                       } else {
                         operand1 = operand1!! / operand2
                       }
                "x" ->  operand1 = operand1!! * operand2
                "-" -> operand1  = operand1!! - operand2
                "+" -> operand1  = operand1!! + operand2
            }
        }

        result.setText(operand1.toString())
        newNumber.setText("")
//        displayOperation.text = operation
    }



    //Saving State
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(operand1 != null){
        outState.putDouble(savedOperand1, operand1!!)
        }

        outState.putDouble(savedOperand2, operand2)

        outState.putString(savedOperation, pendingOperation)
    }


    //Restoring State

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        operand1 = savedInstanceState.getDouble(savedOperand1)
        operand2 = savedInstanceState.getDouble(savedOperand2)
        pendingOperation = savedInstanceState.getString(savedOperation, "")
        displayOperation.text = pendingOperation
    }
}
