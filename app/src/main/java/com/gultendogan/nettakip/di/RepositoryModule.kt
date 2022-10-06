package com.gultendogan.nettakip.di

import com.gultendogan.nettakip.data.local.NetDao
import com.gultendogan.nettakip.data.repository.NetRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @Provides
    fun provideNetRepository(
        dbDao: NetDao
    ): NetRepository {
        return NetRepository(dbDao)
    }
}