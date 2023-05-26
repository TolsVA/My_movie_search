package com.example.my_movie_search.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.my_movie_search.model.Persons

@Entity(
    tableName = "persons",
    indices = [
        Index("id", unique = true)
    ]
)
data class PersonsDbEntity(
    @ColumnInfo(name = "id_row") @PrimaryKey(autoGenerate = true) var idRow: Long,
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "photo") val photo: String?,
    @ColumnInfo(name = "name", collate = ColumnInfo.NOCASE) val name: String,
    @ColumnInfo(name = "profession") val profession: String?
) {
    fun toPersons(): Persons = Persons(
        idRow = idRow,
        id = id,
        photo = photo,
        name = name,
        enName = null,
        profession = profession,
        enProfession = null
    )

    companion object {
        fun fromPersonsData(persons: Persons) = PersonsDbEntity(
            idRow = 0, // SQLite автоматически генерирует идентификатор, если idRow = 0
            id = persons.id!!,
            photo = persons.photo,
            name = persons.name!!,
            profession = persons.profession
        )
    }
}