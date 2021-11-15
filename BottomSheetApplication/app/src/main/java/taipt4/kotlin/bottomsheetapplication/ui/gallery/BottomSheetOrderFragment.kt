package taipt4.kotlin.bottomsheetapplication.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import taipt4.kotlin.bottomsheetapplication.R

class BottomSheetOrderFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = LayoutInflater.from(context)
            .inflate(R.layout.fragment_order_filter, container, false)
        initViews(root)

        return root
    }

    private fun initViews(view: View) {
//        Log.d("TaiPT4", view.toString() + "jahfkjahsfkjhakjfa")

    }

    companion object {
        private var instance: BottomSheetOrderFragment? = null

        fun getInstance(): BottomSheetOrderFragment {
            if (instance == null) {
                instance = BottomSheetOrderFragment()
            }
            return instance!!
        }
    }
}

