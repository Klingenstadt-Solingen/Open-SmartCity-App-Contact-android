package de.osca.android.contact.entity

import com.google.gson.annotations.SerializedName

/**
 * The Json-Request-Body-Structure
 */
data class ContactRequest(
    @SerializedName("name")
    var name: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("message")
    var message: String = "",
    @SerializedName("contactId")
    var contactId: String = ""
) {
    companion object {
        fun fromContactFormBody(form: ContactFormBody): ContactRequest {
            return ContactRequest(
                name = form.name,
                message = form.message,
                contactId = form.contactId,
                email = form.email
            )
        }
    }
}