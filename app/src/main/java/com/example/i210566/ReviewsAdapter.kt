package com.example.i210566

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReviewsAdapter(private val reviewsList: List<ReviewData>) : RecyclerView.Adapter<ReviewsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mentorNameTextView: TextView = view.findViewById(R.id.tvMentorName)
        val ratingBar: RatingBar = view.findViewById(R.id.userRatingBar)
        val reviewTextView: TextView = view.findViewById(R.id.MyExperienceWidget) // Assuming you want to show the review text here
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_reviews_card_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = reviewsList[position]
        holder.mentorNameTextView.text = review.mentorName
        holder.ratingBar.rating = review.rating?.toFloat() ?: 0f
        holder.reviewTextView.text = review.experience
    }

    override fun getItemCount() = reviewsList.size
}
