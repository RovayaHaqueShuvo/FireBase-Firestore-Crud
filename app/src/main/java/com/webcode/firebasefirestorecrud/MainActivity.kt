package com.webcode.firebasefirestorecrud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.webcode.firebasefirestorecrud.databinding.ActivityMainBinding
import java.security.Timestamp

class MainActivity : AppCompatActivity(), DataAdapter.ItemClickListener {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val db = FirebaseFirestore.getInstance()
    private val dataCollection = db.collection("data")
    private val data = mutableListOf<Data>()
    private lateinit var adapter: DataAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapter = DataAdapter(data, this)
        binding.RecyclerView.adapter = adapter
        binding.RecyclerView.layoutManager = LinearLayoutManager(this)

        binding.button.setOnClickListener {
            val title = binding.IDEdTXT.text.toString()
            val description = binding.StudentnameEDTXT.text.toString()

            if (title.isNotEmpty() && description.isNotEmpty()) {
                addData(title, description)
            }
        }
        fetchData()

    }

    private fun fetchData() {
        dataCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                data.clear()
                for(document in it){
                    val item = document.toObject(Data::class.java)
                    item.id = document.id
                    data.add(item)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Data fetched failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addData(title: String, description: String) {
        val newData = Data(title = title, description = description, timestamp = Timestamp.now())
        dataCollection.add(newData)
            .addOnSuccessListener {
                newData.id = it.id
                data.add(newData)
                adapter.notifyDataSetChanged()
                binding.IDEdTXT.text?.clear()
                binding.StudentnameEDTXT.text?.clear()
                fetchData()
                Toast.makeText(this, "Data added successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Data added failed", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onEditItemClick(data: Data) {
        binding.IDEdTXT.setText(data.title)
        binding.StudentnameEDTXT.setText(data.description)
        binding.button.text = "Update"

        binding.button.setOnClickListener {
            val updateTitle = binding.IDEdTXT.text.toString()
            val updateDescription = binding.StudentnameEDTXT.text.toString()

            if(updateTitle.isNotEmpty() && updateDescription.isNotEmpty()){
                val updateData = Data(data.id, updateTitle, updateDescription,Timestamp.now())

                dataCollection.document(data.id!!)
                    .set(updateData)
                    .addOnSuccessListener {
                        binding.IDEdTXT.text?.clear()
                        binding.StudentnameEDTXT.text?.clear()
                        Toast.makeText(this, "Data Updated", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Data updated failed", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    override fun onDeleteItemClick(data: Data) {
        dataCollection.document(data.id!!)
            .delete()
            .addOnSuccessListener {
                adapter.notifyDataSetChanged()
                fetchData()
                Toast.makeText(this, "Data deleted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Data deletion failed", Toast.LENGTH_SHORT).show()
            }
    }
}