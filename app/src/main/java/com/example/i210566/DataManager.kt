package com.example.i210566

import java.io.Serializable


object DataManager {
    var currentUser: UserData? = null
    var currentMentor: MentorData? = null
}

data class MentorData(
    var Documentid: String? = null,
    var description: String? = null,
    var imageUrl: String? = null,
    var name: String? = null,
    var price: Long? = null,
    var status: String? = null
) : Serializable


data class UserData(
    var userId: String? = null,
    var city: String? = null,
    var contact: String? = null,
    var country: String? = null,
    var email: String? = null,
    var name: String? = null,
    var profileCover: String? = null,
    var profilePhoto: String? = null,
    var heartedMentors: List<String>? = null
)

data class ReviewData(
    var userId: String? = null,
    var mentorName: String? = null,
    var rating: Double? = null,
    var experience: String? = null
)
