package com.example.i210566

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

// import your Glide or Picasso library here if you're planning to use it for image loading

class HomePageMentorCardAdapter(private val mentorsList: List<MentorData>) : RecyclerView.Adapter<HomePageMentorCardAdapter.MentorViewHolder>() {

    class MentorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMentorName: TextView = itemView.findViewById(R.id.tvMentorName)
        private val tvMentorSessionRate: TextView = itemView.findViewById(R.id.tvMentorSessionRate)
        private val tvMentorTitle: TextView = itemView.findViewById(R.id.tvMentorTitle)
        //private val ivMentorImage: ImageView = itemView.findViewById(R.id.ivMentorImage)

        fun bind(mentor: MentorData, onClick: (MentorData) -> Unit) {
            tvMentorName.text = mentor.name
            tvMentorSessionRate.text = mentor.price?.toString() ?: "N/A"
            tvMentorTitle.text = mentor.description
            //mentor.imageUrl?.let { imageUrl -> Glide.with(itemView.context).load(imageUrl).into(ivMentorImage) }

            itemView.setOnClickListener {
                onClick(mentor)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mentor_card_layout, parent, false)
        return MentorViewHolder(view)
    }

    override fun onBindViewHolder(holder: MentorViewHolder, position: Int) {
        holder.bind(mentorsList[position]) {
            val intent = Intent(holder.itemView.context, MentorProfile::class.java).apply {
                putExtra("EXTRA_MENTOR_DATA", it)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = mentorsList.size
}
