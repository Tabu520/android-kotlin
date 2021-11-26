package com.taipt.roomdatabaseexample.entities.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.taipt.roomdatabaseexample.entities.Student
import com.taipt.roomdatabaseexample.entities.Subject

data class StudentWithSubjects(
    @Embedded val student: Student,
    @Relation(
        parentColumn = "studentName",
        entityColumn = "subjectName",
        associateBy = Junction(StudentSubjectCrossRef::class)
    )
    val subjects: List<Subject>
)
