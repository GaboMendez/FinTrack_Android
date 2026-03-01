package com.usj.fintrack.data.database

/**
 * Room type converters for [com.usj.fintrack.data.database.FinTrackDatabase].
 *
 * All entity fields currently use types that Room handles natively
 * (Long, Double, Boolean, String), so no converters are needed at this time.
 *
 * Add converters here when introducing columns that store non-primitive types
 * (e.g. serialised lists, custom value objects, etc.).
 */
class Converters {

    // Example template — uncomment and adapt when a converter is required:
    //
    // @TypeConverter
    // fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }
    //
    // @TypeConverter
    // fun dateToTimestamp(date: Date?): Long? = date?.time
}