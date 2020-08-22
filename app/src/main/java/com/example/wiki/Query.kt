package com.example.wiki

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso

class Query : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query)
        //Set logo to return to home screen when pressed
        val logo = findViewById<ImageView>(R.id.logo)
        logo.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        //Assign variables to interface objects
        val button = findViewById<Button>(R.id.queryButton)
        val editText = findViewById<EditText>(R.id.textView)
        val inputStr = intent.getStringExtra("INPUT_STR")
        editText.setText(inputStr)
        //If user clicks on button
        button.setOnClickListener {
            // Code to close keyboard after search
            //https://stackoverflow.com/questions/1109022/how-do-you-close-hide-the-android-soft-keyboard-using-java?page=1&tab=votes#tab-top
            val view = this.currentFocus
            view?.let { v ->
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(v.windowToken, 0)
            }
            //Get the user input
            var keyword = editText.text.toString()
            if (!keyword.isBlank()) {

                val def = findViewById<TextView>(R.id.defText)
                val img = findViewById<ImageView>(R.id.image)
                //URL for making a GET request to Wikipedia
                val defurl =
                    "https://simple.wikipedia.org/w/api.php?action=query&format=json&prop=extracts&titles=${keyword}&exsentences=2&exintro=1&explaintext=1&exsectionformat=plain"
                val imgpath =
                    "https://simple.wikipedia.org/w/api.php?action=query&format=json&origin=*&prop=pageimages&titles=${keyword}&piprop=original"
                //Create a volley request queue
                val queue = Volley.newRequestQueue(this)
                //Request image
                val imgRequest = JsonObjectRequest(
                    Request.Method.GET, imgpath, null,
                    Response.Listener { response ->
                        val query = response.getJSONObject("query")
                        val pages = query.getJSONObject("pages")
                        for (key in pages.keys()) {
                            val num = pages.getJSONObject(key)
                            val original = num.getJSONObject("original")
                            val imgurl = original.getString("source")
                            //Loading image using Picasso
                            Picasso.get().load(imgurl).into(img)
                        }

                    },
                    Response.ErrorListener { error ->
                        error.printStackTrace()
                    }
                )
                queue.add(imgRequest)
                //Request definition
                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.GET, defurl, null,
                    Response.Listener { response ->
                        val query = response.getJSONObject("query")
                        val pages = query.getJSONObject("pages")
                        for (key in pages.keys()) {
                            val num = pages.getJSONObject(key)
                            var ans = num.getString("extract")
                            def.text = ans
                        }

                    },
                    Response.ErrorListener { error ->
                        def.text = error.toString()
                    }
                )
                queue.add(jsonObjectRequest)

            }
        }
        //Search the value input on the home screen
        button.performClick()
    }
}