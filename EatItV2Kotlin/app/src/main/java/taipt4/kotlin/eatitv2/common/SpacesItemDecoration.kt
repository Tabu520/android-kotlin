package taipt4.kotlin.eatitv2.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacesItemDecoration(private var space: Int): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.right = space
        outRect.left = space
        outRect.top = space
        outRect.bottom = space
    }
}