package com.taipt.roomdatabaseexample.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.taipt.roomdatabaseexample.entities.School
import com.taipt.roomdatabaseexample.entities.Student

data class SchoolWithStudents(
    @Embedded val school: School,
    @Relation(
        parentColumn = "schoolName",
        entityColumn = "schoolName"
    )
    val students: List<Student>
)
