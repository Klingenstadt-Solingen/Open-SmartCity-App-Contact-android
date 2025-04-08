package de.osca.android.contact.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import de.osca.android.contact.R
import de.osca.android.contact.entity.ContactFormBody
import de.osca.android.contact.entity.ContactFormBody.Companion.FIELD_DATA_PRIVACY
import de.osca.android.contact.presentation.args.ContactDesignArgs
import de.osca.android.essentials.presentation.component.design.*
import de.osca.android.essentials.presentation.component.screen_wrapper.ScreenWrapper
import de.osca.android.essentials.presentation.component.topbar.ScreenTopBar
import de.osca.android.essentials.presentation.nav_items.EssentialsNavItems
import de.osca.android.essentials.utils.extensions.SetSystemStatusBar

/**
 * @param navController from the navigationGraph
 * @param contactViewModel screen creates the corresponding viewModel
 * @param masterDesignArgs main design arguments for the overall design
 * @param
 */
@Composable
fun ContactScreen(
    navController: NavController,
    contactViewModel: ContactViewModel = hiltViewModel(),
    masterDesignArgs: MasterDesignArgs = contactViewModel.defaultDesignArgs,
    contactDesignArgs: ContactDesignArgs = contactViewModel.contactDesignArgs
) {
    LaunchedEffect(Unit) {
        contactViewModel.initializeContacts()
    }

    val townHallContactList = remember { contactViewModel.townHallContacts }
    val invalidFields = remember { contactViewModel.formBody.value.invalidFields }
    val sendAttempt = remember { contactViewModel.sendAttempt }

    SetSystemStatusBar(
        !(contactDesignArgs.mIsStatusBarWhite ?: masterDesignArgs.mIsStatusBarWhite), Color.Transparent
    )

    ScreenWrapper(
        topBar = {
            ScreenTopBar(
                title = stringResource(id = contactDesignArgs.vModuleTitle),
                navController = navController,
                overrideTextColor = contactDesignArgs.mTopBarTextColor,
                overrideBackgroundColor = contactDesignArgs.mTopBarBackColor,
                masterDesignArgs = masterDesignArgs
            )
        },
        viewModel = contactViewModel,
        retryAction = {
            contactViewModel.initializeContacts()
        },
        masterDesignArgs = masterDesignArgs,
        moduleDesignArgs = contactDesignArgs
    ) {
        RootContainer(
            masterDesignArgs = masterDesignArgs,
            moduleDesignArgs = contactDesignArgs
        ) {
            item {
                SimpleSpacedList(
                    masterDesignArgs = masterDesignArgs
                ) {
                    if (contactDesignArgs.showInfoElement) {
                        BaseCardContainer(
                            masterDesignArgs = masterDesignArgs,
                            moduleDesignArgs = contactDesignArgs
                        ) {
                            Text(
                                text = stringResource(id = R.string.contact_explain_text),
                                style = masterDesignArgs.normalTextStyle,
                                color = contactDesignArgs.mCardTextColor ?: masterDesignArgs.mCardTextColor
                            )
                        }
                    }

                    BaseCardContainer(
                        masterDesignArgs = masterDesignArgs,
                        moduleDesignArgs = contactDesignArgs
                    ) {
                        SimpleSpacedList(
                            masterDesignArgs = masterDesignArgs,
                            overrideSpace = 8.dp
                        ) {
                            BaseTextField(
                                textFieldTitle = stringResource(id = R.string.global_contact_name),
                                textValue = remember { mutableStateOf(contactViewModel.formBody.value.name) },
                                isError = sendAttempt.value && invalidFields.contains(
                                    ContactFormBody.FIELD_NAME
                                ),
                                onTextChange = {
                                    contactViewModel.formBody.value.withName(it)
                                },
                                lineCount = 1,
                                masterDesignArgs = masterDesignArgs,
                                moduleDesignArgs = contactDesignArgs
                            )
                            if (sendAttempt.value && invalidFields.contains(ContactFormBody.FIELD_NAME)) {
                                ErrorLabel(
                                    text = stringResource(id = R.string.global_name_missing_hint),
                                    masterDesignArgs = masterDesignArgs
                                )
                            }

                            BaseTextField(
                                textFieldTitle = stringResource(id = R.string.global_contact_mail),
                                textValue = remember { mutableStateOf(contactViewModel.formBody.value.email) },
                                isError = sendAttempt.value && invalidFields.contains(
                                    ContactFormBody.FIELD_EMAIL
                                ),
                                onTextChange = {
                                    contactViewModel.formBody.value.withEmail(it)
                                },
                                lineCount = 1,
                                masterDesignArgs = masterDesignArgs,
                                moduleDesignArgs = contactDesignArgs
                            )
                            if (sendAttempt.value && invalidFields.contains(ContactFormBody.FIELD_EMAIL)) {
                                ErrorLabel(
                                    text = stringResource(id = R.string.global_email_missing_hint),
                                    masterDesignArgs = masterDesignArgs
                                )
                            }

                            BaseDropDown(
                                displayTexts = townHallContactList.map { it.name ?: "" },
                                onSelectedItemChanged = {
                                    contactViewModel.formBody.value.withContactId(
                                        townHallContactList[it].objectId ?: ""
                                    )
                                },
                                masterDesignArgs = masterDesignArgs,
                                moduleDesignArgs = contactDesignArgs
                            )

                            BaseTextField(
                                textFieldTitle = stringResource(id = R.string.global_contact_message),
                                textValue = remember { mutableStateOf(contactViewModel.formBody.value.message) },
                                isError = sendAttempt.value && invalidFields.contains(
                                    ContactFormBody.FIELD_MESSAGE
                                ),
                                fieldHeight = 250.dp,
                                onTextChange = {
                                    contactViewModel.formBody.value.withMessage(it)
                                },
                                masterDesignArgs = masterDesignArgs,
                                moduleDesignArgs = contactDesignArgs
                            )
                            if (sendAttempt.value && invalidFields.contains(ContactFormBody.FIELD_MESSAGE)) {
                                ErrorLabel(
                                    text = stringResource(id = R.string.global_message_missing_hint),
                                    masterDesignArgs = masterDesignArgs
                                )
                            }

                            BaseDataPrivacyElement(
                                initialState = contactViewModel.formBody.value.dataPrivacyChecked,
                                onSwitch = {
                                    contactViewModel.formBody.value.withDataPrivacyChecked(it)
                                },
                                onClickDataPrivacy = {
                                    navController.navigate(EssentialsNavItems.DataPrivacyNavItem.route)
                                },
                                masterDesignArgs = masterDesignArgs,
                                moduleDesignArgs = contactDesignArgs
                            )
                            if (sendAttempt.value && invalidFields.contains(FIELD_DATA_PRIVACY)) {
                                ErrorLabel(
                                    text = stringResource(id = R.string.global_dataprivacy_missing_hint),
                                    masterDesignArgs = masterDesignArgs
                                )
                            }

                            MainButton(
                                buttonText = stringResource(id = R.string.global_send_button),
                                onClick = {
                                    sendAttempt.value = true
                                    contactViewModel.sendFormData(navController)
                                },
                                masterDesignArgs = masterDesignArgs,
                                moduleDesignArgs = contactDesignArgs
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorLabel(
    masterDesignArgs: MasterDesignArgs,
    text: String
) {
    Text(
        text = text,
        color = masterDesignArgs.errorTextColor,
        style = masterDesignArgs.normalTextStyle,
        modifier = Modifier
            .padding(start = 8.dp)
    )
}