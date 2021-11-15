package taipt4.kotlin.bottomsheetapplication.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import taipt4.kotlin.bottomsheetapplication.R

class CommentFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = LayoutInflater.from(context)
            .inflate(R.layout.fragment_bottom_sheet_comment, container, false)
        initViews(root)

        return root

    }

    private fun initViews(root: View) {
        Log.d("TaiPT4", root.toString())
    }

    companion object {
        private var instance: CommentFragment? = null

        fun getInstance(): CommentFragment {
            if (instance == null) {
                instance = CommentFragment()
            }
            return instance!!
        }
    }
}