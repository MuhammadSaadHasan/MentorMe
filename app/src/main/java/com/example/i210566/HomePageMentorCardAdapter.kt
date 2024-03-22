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
        fun bind(mentor: MentorData) {
            val tvMentorName = itemView.findViewById<TextView>(R.id.tvMentorName)
            val tvMentorSessionRate = itemView.findViewById<TextView>(R.id.tvMentorSessionRate)
            val tvMentorTitle = itemView.findViewById<TextView>(R.id.tvMentorTitle)
            val ivMentorImage = itemView.findViewById<ImageView>(R.id.Mentor1) // Assuming you have an ImageView for the mentor's image

            tvMentorName.text = mentor.name
            tvMentorSessionRate.text = mentor.price?.toString() ?: "N/A"
            tvMentorTitle.text = mentor.description

            // Example of loading an image using Glide (you need to add the library to your build.gradle)
            mentor.imageUrl?.let {
                Glide.with(itemView.context)
                    .load(it)
                    .into(ivMentorImage)
            }


            itemView.setOnClickListener {
                val context = itemView.context
                // Check if context is an instance of Activity and if it is not finishing
                if (context is Activity && !context.isFinishing) {
                    try {
                        val intent = Intent(context, MentorProfile::class.java) // Ensure this class exists and is declared in the manifest
                        intent.putExtra("EXTRA_MENTOR_NAME", mentor.name)
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Failed to start activity: ${e.message}", Toast.LENGTH_LONG).show()
                        e.printStackTrace() // This will print the stack trace to the log
                    }
                } else {
                    Toast.makeText(context, "Context is not valid or the Activity is finishing", Toast.LENGTH_LONG).show()
                }
            }


        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mentor_card_layout, parent, false)
        return MentorViewHolder(view)
    }

    override fun onBindViewHolder(holder: MentorViewHolder, position: Int) {
        holder.bind(mentorsList[position])
    }

    override fun getItemCount() = mentorsList.size
}
