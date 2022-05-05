package com.example.contactsfree

import android.annotation.SuppressLint
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerview: RecyclerView
    private lateinit var contactName: EditText
    private lateinit var contactNumber: EditText
    private lateinit var addContactBt: Button
    private lateinit var contactadapter: ContactAdapter
    lateinit var database: SQLiteDatabase

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerview = findViewById(R.id.recyclerview)
        contactName = findViewById(R.id.contactName)
        contactNumber = findViewById(R.id.contactNumber)
        addContactBt = findViewById(R.id.addContactBt)
        database = this.openOrCreateDatabase("contactDB", MODE_PRIVATE, null)
        database.execSQL("CREATE TABLE IF NOT EXISTS todotable(id INTEGER PRIMARY KEY,name VARCHAR,number VARCHAR)")
        contactadapter = ContactAdapter(mutableListOf(), database, this@MainActivity)
        recyclerview.adapter = contactadapter
        recyclerview.layoutManager = LinearLayoutManager(this@MainActivity)
        database = this.openOrCreateDatabase("contactDB", MODE_PRIVATE, null)
        database.execSQL("CREATE TABLE IF NOT EXISTS todotable(id INTEGER PRIMARY KEY,name VARCHAR,number VARCHAR)")
        val cursor: Cursor = database.rawQuery("select name, number from todotable ", null)


        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val name: String = cursor.getString(cursor.getColumnIndex("name"))
                val number = cursor.getString(cursor.getColumnIndex("number"))
                val contact: Contact = Contact(name, number)
                contactadapter.addContact(contact)
                cursor.moveToNext()
            }
        }





        addContactBt.setOnClickListener {
            val name = contactName.text.toString()
            val number = contactNumber.text.toString()
            try {
                val sqlString = "INSERT INTO todotable (name,number) VALUES(?,?)"
                val statement = database.compileStatement(sqlString)
                statement.bindString(1, name)
                statement.bindString(2, number)
                statement.execute()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (name.isNotEmpty() && number.isNotEmpty() && number.length == 10) {
                val contact: Contact = Contact(name, number)
                contactadapter.addContact(contact)
                contactName.text.clear()
                contactNumber.text.clear()

            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Lütfen başında 0 olmadan 10 haneli numaranızı giriniz!",
                    Toast.LENGTH_LONG
                ).show()
            }


        }

    }
}