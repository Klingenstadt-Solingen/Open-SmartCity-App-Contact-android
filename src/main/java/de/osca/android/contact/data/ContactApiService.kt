package de.osca.android.contact.data

import de.osca.android.contact.entity.ContactFormContact
import de.osca.android.contact.entity.ContactRequest
import de.osca.android.contact.navigation.ContactNavItems.ContactFormNavItem.icon
import de.osca.android.contact.navigation.ContactNavItems.ContactFormNavItem.route
import de.osca.android.contact.navigation.ContactNavItems.ContactFormNavItem.title
import de.osca.android.essentials.domain.entity.ObjectCreationResponse
import de.osca.android.essentials.utils.annotations.UnwrappedResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ContactApiService {

    /**
     * Getting all Contacts
     * Endpoint: "classes/ContactFormContact"
     */
    @GET("classes/ContactFormContact")
    suspend fun getContacts(): Response<List<ContactFormContact>>

    /**
     * Sending the form data
     * Endpoint: "classes/ContactFormData"
     */
    @UnwrappedResponse
    @POST("classes/ContactFormData")
    suspend fun postContactForm(@Body contactRequest: ContactRequest) : Response<ObjectCreationResponse>
}