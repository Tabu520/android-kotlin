package taipt4.kotlin.mvvmnoteapp.feature_note.presentation.notes

import taipt4.kotlin.mvvmnoteapp.feature_note.domain.model.Note
import taipt4.kotlin.mvvmnoteapp.feature_note.domain.util.NoteOrder
import taipt4.kotlin.mvvmnoteapp.feature_note.domain.util.OrderType

data class NoteState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
