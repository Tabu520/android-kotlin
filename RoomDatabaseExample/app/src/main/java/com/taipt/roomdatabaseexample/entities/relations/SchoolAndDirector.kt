package com.taipt.roomdatabaseexample.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.taipt.roomdatabaseexample.entities.Director
import com.taipt.roomdatabaseexample.entities.School

data class SchoolAndDirector(
    @Embedded val school: School,
    @Relation(
        parentColumn = "schoolName",
        entityColumn = "schoolName"
    )
    val director: Director
)
