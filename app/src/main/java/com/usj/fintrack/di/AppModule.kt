package com.usj.fintrack.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Application-level singleton dependencies.
 *
 * Phase 1 scope (offline-first, no Firebase/ML Kit).
 * Future additions:
 *   - FirebaseAuth instance (Phase 6)
 *   - ML Kit data sources (Phase 4)
 *   - DataStore / UserPreferencesDataSource (Phase 2.5)
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule
