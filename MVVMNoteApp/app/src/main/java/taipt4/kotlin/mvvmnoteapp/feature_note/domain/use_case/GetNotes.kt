package taipt4.kotlin.mvvmnoteapp.feature_note.domain.use_case

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import taipt4.kotlin.mvvmnoteapp.feature_note.domain.model.Note
import taipt4.kotlin.mvvmnoteapp.feature_note.domain.repository.NoteRepository
import taipt4.kotlin.mvvmnoteapp.feature_note.domain.util.NoteOrder
import taipt4.kotlin.mvvmnoteapp.feature_note.domain.util.OrderType
import java.util.*

class GetNotes(private val repository: NoteRepository) {

    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)
    ): Flow<List<Note>> {
        return repository.getNotes().map { notes ->
            when (noteOrder.orderType) {
                is OrderType.Ascending -> {
                    when (noteOrder) {
                        is NoteOrder.Title -> notes.sortedBy { it.title.lowercase(Locale.getDefault()) }
                        is NoteOrder.Date -> notes.sortedBy { it.timeStamp }
                        is NoteOrder.Color -> notes.sortedBy { it.color }
                    }
                }
                is OrderType.Descending -> {
                    when (noteOrder) {
                        is NoteOrder.Title -> notes.sortedByDescending { it.title.lowercase(Locale.getDefault()) }
                        is NoteOrder.Date -> notes.sortedByDescending { it.timeStamp }
                        is NoteOrder.Color -> notes.sortedByDescending { it.color }
                    }
                }
            }
        }
    }
}