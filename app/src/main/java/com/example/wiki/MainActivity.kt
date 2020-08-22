package com.example.wiki

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Assign vals to interface objects
        val editText = findViewById<EditText>(R.id.textView)
        val button = findViewById<Button>(R.id.searchButton)
        //Save the query and jump to next screen
        button.setOnClickListener {
            val inputStr = editText.text.toString()
            if (!inputStr.isBlank()) {
                val intent = Intent(this, Query::class.java)
                intent.putExtra("INPUT_STR", inputStr);
                startActivity(intent)
            }
        }
    }
    
}