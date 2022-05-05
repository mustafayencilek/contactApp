package com.example.contactsfree

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ContactAdapter(
    private val contacts: MutableList<Contact>,
    private var database: SQLiteDatabase,
    private var context: Context
) :
    RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {


    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val contactName: TextView = view.findViewById(R.id.layoutNameTv)
        val contactNumber: TextView = view.findViewById(R.id.layoutPhoneTv)
        val contactDeleteIV: ImageView = view.findViewById(R.id.layoutContactDeleteIV)

        override fun onClick(p0: View?) {

        }
    }

    fun addContact(contact: Contact) {
        contacts.add(contact)
        notifyItemInserted(contacts.size - 1)
    }

    fun deleteContact(position: Int) {
        var contactName = contacts[position].contactName
        var contactNumber = contacts[position].contacNumber
        database.delete("todotable", "number=?", arrayOf((contactNumber).toString()))
        contacts.removeAt(position)
        notifyDataSetChanged()
    }

    fun callNumber(position: Int) {
        var contactNumber = contacts[position].contacNumber
        context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:0${contactNumber}")))
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.contact_layout, parent, false)
        return ContactViewHolder(v)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val curcont = contacts[position]
        holder.contactName.text = curcont.contactName
        holder.contactNumber.text = curcont.contacNumber
        holder.contactDeleteIV.setImageResource(R.drawable.ic_baseline_delete_forever_24)
        holder.contactDeleteIV.setOnClickListener {
            deleteContact(position)
        }
        holder.contactName.setOnClickListener {
            callNumber(position)
        }
        holder.contactNumber.setOnClickListener {
            callNumber(position)
        }
    }

    override fun getItemCount(): Int {
        return contacts.size
    }
}