package de.osca.android.contact.navigation

import androidx.navigation.navDeepLink
import de.osca.android.contact.R
import de.osca.android.contact.navigation.ContactNavItems.ContactFormNavItem.icon
import de.osca.android.contact.navigation.ContactNavItems.ContactFormNavItem.route
import de.osca.android.contact.navigation.ContactNavItems.ContactFormNavItem.title
import de.osca.android.essentials.domain.entity.navigation.NavigationItem

/**
 * Navigation Routes for Contact
 */
sealed class ContactNavItems {
    /**
     * Route for the default/main screen
     * @property title title of the route (a name to display)
     * @property route route for this navItem (name is irrelevant)
     * @property icon the icon to display
     */
    object ContactFormNavItem : NavigationItem(
        title = R.string.contact_title,
        route = "contact_form",
        icon = R.drawable.ic_main_contact,
        deepLinks = listOf(navDeepLink { uriPattern = "solingen://contact" }),
    )
}
