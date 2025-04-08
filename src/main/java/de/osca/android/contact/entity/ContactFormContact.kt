package de.osca.android.contact.entity

import com.google.gson.annotations.SerializedName

/**
 * The Json-Response-Data-Structure
 */
data class ContactFormContact(
    @SerializedName("objectId")
    var objectId: String? = "",
    @SerializedName("createdAt")
    var createdAt: String? = "",
    @SerializedName("updatedAt")
    var updatedAt: String? = "",
    @SerializedName("category")
    var category: String? = "",
    @SerializedName("email")
    var email: String? = "",
    @SerializedName("emailSubject")
    var emailSubject: String? = "",
    @SerializedName("title", alternate = ["name"])
    var name: String? = "",
    @SerializedName("position")
    var position: Int = 0
)