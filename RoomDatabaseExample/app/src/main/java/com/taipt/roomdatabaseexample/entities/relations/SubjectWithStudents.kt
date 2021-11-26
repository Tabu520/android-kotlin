package com.taipt.roomdatabaseexample.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.taipt.roomdatabaseexample.entities.Student
import com.taipt.roomdatabaseexample.entities.Subject

data class SubjectWithStudents(
    @Embedded val subject: Subject,
    @Relation(
        parentColumn = "subjectName",
        entityColumn = "studentName",
        associateBy = Junction(StudentSubjectCrossRef::class)
    )
    val students: List<Student>
)
