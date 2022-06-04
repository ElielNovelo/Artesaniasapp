package com.example.artesanias.tiendas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.artesanias.Artesanias
import com.example.artesanias.R
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

import kotlinx.android.synthetic.main.activity_add_tienda.*

class AddTiendaActivity : AppCompatActivity() {

    private val database = Firebase.database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tienda)


        val myRef = database.getReference("Tiendas")

        val name=nameTiendaEditText.text
        val date=dateTiendaEditText.text
        val description=descriptionTiendaEditText.text
        val url=urlTiendaEditText.text

        saveTiendaButton.setOnClickListener { v ->
            val tienda = Tienda(name.toString(), date.toString(), description.toString(), url.toString())
            myRef.child(myRef.push().key.toString()).setValue(tienda)
            finish()
        }


    }
}