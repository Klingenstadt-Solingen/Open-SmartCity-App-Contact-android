package de.osca.android.contact.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.osca.android.contact.data.ContactApiService
import de.osca.android.essentials.data.client.OSCAHttpClient
import javax.inject.Singleton

/**
 * The dependency injection
 */
@Module
@InstallIn(SingletonComponent::class)
class ContactModule {

    @Singleton
    @Provides
    fun contactApiService(oscaHttpClient: OSCAHttpClient): ContactApiService =
        oscaHttpClient.create(ContactApiService::class.java)
}