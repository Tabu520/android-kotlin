package com.taipt.roomdatabaseexample

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.taipt.roomdatabaseexample.entities.Director
import com.taipt.roomdatabaseexample.entities.School
import com.taipt.roomdatabaseexample.entities.Student
import com.taipt.roomdatabaseexample.entities.Subject
import com.taipt.roomdatabaseexample.entities.relations.*

@Dao
interface SchoolDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertSchool(school: School)

    @Insert(onConflict = REPLACE)
    suspend fun insertDirector(director: Director)

    @Insert(onConflict = REPLACE)
    suspend fun insertStudent(student: Student)

    @Insert(onConflict = REPLACE)
    suspend fun insertSubject(subject: Subject)

    @Insert(onConflict = REPLACE)
    suspend fun insertStudentSubjectCrossRef(crossRef: StudentSubjectCrossRef)

    @Transaction
    @Query("SELECT * FROM school WHERE schoolName = :schoolName")
    fun getSchoolAndDirectorWithSchoolName(schoolName: String): LiveData<List<SchoolAndDirector>>

    @Transaction
    @Query("SELECT * FROM school WHERE schoolName = :schoolName")
    fun getSchoolWithStudents(schoolName: String): LiveData<List<SchoolWithStudents>>

    @Transaction
    @Query("SELECT * FROM subject WHERE subjectName = :subjectName")
    fun getStudentsOfSubject(subjectName: String): LiveData<List<SubjectWithStudents>>

    @Transaction
    @Query("SELECT * FROM student WHERE studentName = :studentName")
    fun getSubjectsOfStudent(studentName: String): LiveData<List<StudentWithSubjects>>
}