package com.example.i210566

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MentorAdapter(private val mentorsList: List<MentorData>) : RecyclerView.Adapter<MentorAdapter.MentorViewHolder>() {

    class MentorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.tvMentorName)
        val textViewDescription: TextView = itemView.findViewById(R.id.tvMentorTitle)
        val textViewSessionRate: TextView = itemView.findViewById(R.id.tvMentorSessionRate)
        val availabilityIndicator: View = itemView.findViewById(R.id.availabilityIndicator)
        val ivHeartIcon: ImageView = itemView.findViewById(R.id.ivHeartIcon)
        // Add more views here if needed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mentor_card_layout, parent, false)
        return MentorViewHolder(view)
    }

    override fun onBindViewHolder(holder: MentorViewHolder, position: Int) {
        val mentor = mentorsList[position]
        with(holder) {
            textViewName.text = mentor.name
            textViewDescription.text = mentor.description
            textViewSessionRate.text = mentor.price.toString()

            // Example using Glide to load images:
            // Glide.with(itemView.context).load(mentor.imageUrl).into(ivHeartIcon)
        }
    }

    override fun getItemCount(): Int = mentorsList.size
}
