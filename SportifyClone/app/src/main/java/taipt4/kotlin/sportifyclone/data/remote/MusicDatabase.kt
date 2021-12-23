package taipt4.kotlin.sportifyclone.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import taipt4.kotlin.sportifyclone.data.entities.Song
import taipt4.kotlin.sportifyclone.other.Constants.SONG_COLLECTION
import java.lang.Exception

class MusicDatabase {

    private val firestore = FirebaseFirestore.getInstance()
    private val songCollection = firestore.collection(SONG_COLLECTION)

    suspend fun getAllSongs() : List<Song> {
        return try {
            songCollection.get().await().toObjects(Song::class.java)
        } catch (exception: Exception) {
            emptyList()
        }
    }
}