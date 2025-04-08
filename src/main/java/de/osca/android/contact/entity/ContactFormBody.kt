package de.osca.android.contact.entity

import androidx.compose.runtime.mutableStateListOf
import de.osca.android.essentials.utils.constants.EMAIL_PATTERN
import de.osca.android.essentials.utils.extensions.addIfNotContains
import java.util.regex.Pattern

/**
 *
 */
data class ContactFormBody(
    private var _name: String = "",
    private var _contactId: String = "",
    private var _message: String = "",
    private var _email: String = "",
    private var _dataPrivacyChecked: Boolean = false,
    private val _invalidFields: MutableList<Int> = mutableStateListOf()
) {
    val message: String get() = _message
    val name: String get() = _name
    val contactId: String get() = _contactId
    val email: String get() = _email
    val dataPrivacyChecked: Boolean get() = _dataPrivacyChecked
    val invalidFields get() = _invalidFields
    fun isValid(): Boolean = _invalidFields.isEmpty()

    fun verifyAll(){
        verifyMessage()
        verifyName()
        verifyEmail()
        verifyDataPrivacyChecked()
        verifyContactId()
    }

    fun clearFields() {
        _message = ""
        _name = ""
        _contactId = ""
        _email = ""
        _dataPrivacyChecked = false
        _invalidFields.clear()
        verifyAll()
    }

    fun withMessage(valueMessage: String) {
        _message = valueMessage
        verifyMessage()
    }

    fun withName(valueName: String) {
        _name = valueName
        verifyName()
    }

    fun withEmail(valueEmail: String) {
        _email = valueEmail
        verifyEmail()
    }

    fun withContactId(valueContactId: String) {
        _contactId = valueContactId
        verifyContactId()
    }

    fun withDataPrivacyChecked(dataPrivacyChecked: Boolean) {
        _dataPrivacyChecked = dataPrivacyChecked
        verifyDataPrivacyChecked()
    }

    private fun verifyMessage() {
        if (_message.length < 5) {
            _invalidFields.addIfNotContains(FIELD_MESSAGE)
        } else {
            _invalidFields.remove(FIELD_MESSAGE)
        }
    }

    private fun verifyName() {
        if (_name.isBlank()) {
            _invalidFields.addIfNotContains(FIELD_NAME)
        } else {
            _invalidFields.remove(FIELD_NAME)
        }
    }

    private fun verifyEmail() {
        val emailPattern: Pattern = Pattern.compile(
            EMAIL_PATTERN,
            Pattern.CASE_INSENSITIVE or Pattern.MULTILINE or Pattern.DOTALL
        )
        if (_email.isEmpty() || emailPattern.matcher(_email).matches().not()) {
            _invalidFields.addIfNotContains(FIELD_EMAIL)
        } else {
            _invalidFields.remove(FIELD_EMAIL)
        }
    }

    private fun verifyDataPrivacyChecked() {
        if (!_dataPrivacyChecked) {
            _invalidFields.addIfNotContains(FIELD_DATA_PRIVACY)
        } else {
            _invalidFields.remove(FIELD_DATA_PRIVACY)
        }
    }

    private fun verifyContactId() {
        if (_contactId.isBlank()) {
            _invalidFields.addIfNotContains(FIELD_CONTACT_DATA)
        } else {
            _invalidFields.remove(FIELD_CONTACT_DATA)
        }
    }

    companion object {
        const val FIELD_MESSAGE = 0
        const val FIELD_NAME = 1
        const val FIELD_CONTACT_DATA = 2
        const val FIELD_EMAIL = 3
        const val FIELD_DATA_PRIVACY = 4
    }
}
