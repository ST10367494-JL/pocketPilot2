package com.pocketpilot.pocketpilot.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


// BEGIN-CITATIONS room-db
// LINK https://developer.android.com/training/data-storage/room#data-entity
// DESC Entity Definition
// ACCESSED 20260422T121348.456640255+0200
// CSL-REF devel-android-roomdb-setup-entity
// END-CITATIONS
/**
 * @property uid unique id of the category
 * @property colour colour for the category
 */
@Entity
data class Category(
    @PrimaryKey(autoGenerate = true) val categoryId: Long=0,
    val name: String,
    val colour: String
)