package com.example.i210566

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class SearchResultsCardAdapter(
    private val context: Context,
    private var mentorsList: List<MentorData>
) : RecyclerView.Adapter<SearchResultsCardAdapter.MentorSearchViewHolder>() {

    class MentorSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.tvMentorSearchName)
        val textViewDescription: TextView = itemView.findViewById(R.id.tvMentorSearchTitle)
        val textViewSessionRate: TextView = itemView.findViewById(R.id.tvMentorSearchSessionRate)
        val imageViewProfile: ImageView = itemView.findViewById(R.id.ivMentorSearchProfilePic)

        fun bind(mentor: MentorData, clickListener: (MentorData) -> Unit) {
            textViewName.text = mentor.name
            textViewDescription.text = mentor.description
            textViewSessionRate.text = mentor.price.toString()
            Picasso.get().load(mentor.imageUrl).into(imageViewProfile)

            itemView.setOnClickListener {
                clickListener(mentor)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentorSearchViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.mentor_card_search_layout, parent, false)
        return MentorSearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: MentorSearchViewHolder, position: Int) {
        holder.bind(mentorsList[position]) { clickedMentor ->
            val intent = Intent(context, MentorProfile::class.java).apply {
                putExtra("EXTRA_MENTOR_DATA", clickedMentor)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = mentorsList.size
}