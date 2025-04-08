package de.osca.android.contact.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import de.osca.android.contact.R
import de.osca.android.contact.data.ContactApiService
import de.osca.android.contact.entity.ContactFormBody
import de.osca.android.contact.entity.ContactRequest
import de.osca.android.contact.entity.ContactFormContact
import de.osca.android.contact.presentation.args.ContactDesignArgs
import de.osca.android.essentials.presentation.base.BaseViewModel
import de.osca.android.essentials.utils.extensions.displayContent
import de.osca.android.essentials.utils.extensions.loading
import de.osca.android.essentials.utils.extensions.resetWith
import de.osca.android.essentials.utils.strings.EssentialsStrings
import de.osca.android.networkservice.utils.RequestHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Contact
 * @param contactDesignArgs the design arguments of the module
 * @param contactApiService the api-endpoints for Parse
 * @param requestHandler handling the request and response for parse
 * @param strings handling the strings from essentials-module
 *
 * @property formBody all the data that is relevant for sending
 * @property townHallContacts contacts for the contact to a specific area
 * @property sendAttempt has the user (tried) to send the form
 */
@HiltViewModel
class ContactViewModel @Inject constructor(
    val contactDesignArgs: ContactDesignArgs,
    private val contactApiService: ContactApiService,
    private val requestHandler: RequestHandler,
    private val strings: EssentialsStrings
) : BaseViewModel() {
    var formBody = mutableStateOf(ContactFormBody())
    val townHallContacts = mutableStateListOf<ContactFormContact>()
    val sendAttempt = mutableStateOf(false)

    /**
     * call this function to initialize all contacts for the town hall.
     * it sets the screen to loading, fetches the data from parse and when
     * it finished successful then displays the content and when an error
     * occurred it displays an message screen
     */
    fun initializeContacts() {
        formBody.value.verifyAll()

        viewModelScope.launch {
            wrapperState.loading()
            fetchContacts()
        }
    }

    /**
     * fetches all contacts from parse and when successfully loaded then
     * displays the content
     */
    fun fetchContacts(): Job = launchDataLoad {
        val result = requestHandler.makeRequest(contactApiService::getContacts) ?: emptyList()
        townHallContacts.resetWith(result)
        townHallContacts.sortBy { it.position }

        wrapperState.displayContent()
    }

    /**
     * performs the sending of the form data.
     * it shows a toast for success and for error
     */
    fun sendFormData(navController: NavController): Job = launchDataLoad {
        if (formBody.value.isValid()) {
            val contactRequestData = ContactRequest.fromContactFormBody(formBody.value)

            val result = requestHandler.makeRequest {
                contactApiService.postContactForm(contactRequestData)
            }
            if (result?.isSuccessful == true) {
                formBody.value.clearFields()

                shortToast(strings.getString(R.string.contact_success_message))

                navController.navigateUp()
            } else {
                shortToast(strings.getString(R.string.contact_failure_message))
            }

            sendAttempt.value = false
        }
    }
}