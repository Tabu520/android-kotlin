package taipt4.kotlin.mvvmnoteapp.feature_note.data.data_source

import androidx.room.RoomDatabase

abstract class NoteDatabase: RoomDatabase() {

    abstract val noteDao: NoteDao

    companion object {
        const val DATABASE_NAME = "notes_db"
    }
}