package de.osca.android.contact.presentation.args

import de.osca.android.essentials.presentation.component.design.ModuleDesignArgs

/**
 * This is the ContactDesign Interface for Contact
 * It contains design arguments explicit for this module but also
 * generic module arguments which are by default null and can be set to override
 * the masterDesignArgs
 *
 * @property showInfoElement whether or not show the info box at the top
 */
interface ContactDesignArgs : ModuleDesignArgs {
    val showInfoElement: Boolean
}