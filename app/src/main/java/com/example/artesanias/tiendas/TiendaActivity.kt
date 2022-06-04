package com.example.artesanias.tiendas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.artesanias.R
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

import kotlinx.android.synthetic.main.activity_tienda.*
import kotlinx.android.synthetic.main.tiendas_content.view.*


class TiendaActivity : AppCompatActivity() {

    private val database = Firebase.database
    private lateinit var messagesListener: ValueEventListener
    private val listTiendas:MutableList<Tienda> = ArrayList()
    val myRef = database.getReference("Tiendas")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tienda)

        newFloatingActionButton.setOnClickListener { v ->
            val intent = Intent(this, AddTiendaActivity::class.java)
            v.context.startActivity(intent)
        }

        listTiendas.clear()
        setupRecyclerView(tiendasRecyclerView)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {

        messagesListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listTiendas.clear()
                dataSnapshot.children.forEach { child ->
                    val tiendas: Tienda? =
                        Tienda(child.child("name").getValue<String>(),
                            child.child("date").getValue<String>(),
                            child.child("description").getValue<String>(),
                            child.child("url").getValue<String>(),
                            child.key)
                    tiendas?.let { listTiendas.add(it) }
                }
                recyclerView.adapter = TiendaViewAdapter(listTiendas)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "messages:onCancelled: ${error.message}")
            }
        }
        myRef.addValueEventListener(messagesListener)

        deleteSwipe(recyclerView)
    }

    class TiendaViewAdapter(private val values: List<Tienda>) :
        RecyclerView.Adapter<TiendaViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.tiendas_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val tiendas = values[position]
            holder.mNameTextView.text = tiendas.name
            holder.mDateTextView.text = tiendas.date
            holder.mPosterImgeView?.let {
                Glide.with(holder.itemView.context)
                    .load(tiendas.url)
                    .into(it)
            }

            holder.itemView.setOnClickListener { v ->
                val intent = Intent(v.context, TiendaDetailActivity::class.java).apply {
                    putExtra("key", tiendas.key)
                }
                v.context.startActivity(intent)
            }

            holder.itemView.setOnLongClickListener{ v ->
                val intent = Intent(v.context, EditTiendaActivity::class.java).apply {
                    putExtra("key",tiendas.key)
                }
                v.context.startActivity(intent)
                true
            }

        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val mNameTextView: TextView = view.nameTiendaTextView
            val mDateTextView: TextView = view.dateTiendaTextView
            val mPosterImgeView: ImageView? = view.posterTiendaImgeView
        }
    }

    private fun deleteSwipe(recyclerView: RecyclerView){
        val touchHelperCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                listTiendas.get(viewHolder.adapterPosition).key?.let { myRef.child(it).setValue(null) }
                listTiendas.removeAt(viewHolder.adapterPosition)
                recyclerView.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
        val itemTouchHelper = ItemTouchHelper(touchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

}