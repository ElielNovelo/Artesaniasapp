package com.example.artesanias

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : AppCompatActivity() {

    private val database = Firebase.database
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        val myRef = database.getReference("Artesanias")

        val name=nameEditText.text
        val date=dateEditText.text
        val description=descriptionEditText.text
        val url=urlEditText.text

        saveButton.setOnClickListener { v ->
            val artesanias = Artesanias(name.toString(), date.toString(), description.toString(), url.toString())
            myRef.child(myRef.push().key.toString()).setValue(artesanias)
            finish()
        }
    }
}