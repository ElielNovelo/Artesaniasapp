package com.example.artesanias.tiendas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import com.example.artesanias.Artesanias
import com.example.artesanias.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_edit_tienda.*


class EditTiendaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_tienda)

        val key = intent.getStringExtra("key")
        val database = Firebase.database
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val myRef =
            database.getReference("Tiendas").child(key!!)

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val tiendas: Tienda? = dataSnapshot.getValue(Tienda::class.java)
                if (tiendas != null) {
                    nameTiendaEditText.text = Editable.Factory.getInstance().newEditable(tiendas.name)
                    dateTiendaEditText.text = Editable.Factory.getInstance().newEditable(tiendas.date)
                    descriptionTiendaEditText.text =
                        Editable.Factory.getInstance().newEditable(tiendas.description)
                    urlTiendaEditText.text = Editable.Factory.getInstance().newEditable(tiendas.url)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })

        saveTiendaButton.setOnClickListener { v ->

            val name: String = nameTiendaEditText.text.toString()
            val date: String = dateTiendaEditText.text.toString()
            val description: String = descriptionTiendaEditText.text.toString()
            val url: String = urlTiendaEditText.text.toString()

            myRef.child("name").setValue(name)
            myRef.child("date").setValue(date)
            myRef.child("description").setValue(description)
            myRef.child("url").setValue(url)

            finish()
        }

    }
}