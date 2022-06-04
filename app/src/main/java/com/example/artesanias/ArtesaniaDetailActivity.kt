package com.example.artesanias

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add.descriptionTextView
import kotlinx.android.synthetic.main.activity_add.nameTextView
import kotlinx.android.synthetic.main.activity_artesania_detail.*


class ArtesaniaDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artesania_detail)

        val key = intent.getStringExtra("key")
        val database = Firebase.database
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val myRef = database.getReference("Artesanias").child(key.toString())

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val artesanias:Artesanias? = dataSnapshot.getValue(Artesanias::class.java)
                if (artesanias != null) {
                    nameTextView.text = artesanias.name.toString()
                    descriptionTextView.text = artesanias.description.toString()
                    images(artesanias.url.toString())
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
            .into(posterImgeView)

        Glide.with(this)
            .load(url)
            .into(backgroundImageView)
    }


}