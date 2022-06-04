package com.example.artesanias.tiendas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.artesanias.Artesanias
import com.example.artesanias.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_tienda_detail.*



class TiendaDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tienda_detail)

        val key = intent.getStringExtra("key")
        val database = Firebase.database
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val myRef = database.getReference("Tiendas").child(key.toString())

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val tiendas: Tienda? = dataSnapshot.getValue(Tienda::class.java)
                if (tiendas != null) {
                    nameTiendaTextView.text = tiendas.name.toString()
                    descriptionTiendaTextView.text = tiendas.description.toString()
                    images(tiendas.url.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })

    }

    private  fun images(url: String){
        Glide.with(this)
            .load(url)
            .into(posterTiendaImgeView)

        Glide.with(this)
            .load(url)
            .into(backgroundTiendaImageView)
    }

}