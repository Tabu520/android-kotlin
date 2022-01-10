package com.avenue.baseframework.core.adapters

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.avenue.baseframework.R
import com.avenue.baseframework.core.models.MenuModel
import java.lang.Exception

class MenuExpandableListviewAdapter(
    private var context: Context,
    private var itemHeaderList: MutableList<MenuModel>?,
    private var itemChildList: SparseArray<MutableList<MenuModel>>?
) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        itemHeaderList?.let {
            return it.size
        }
        return 0
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        val listChildByGroup =
            itemHeaderList?.get(groupPosition)?.let { itemChildList?.get(it.menuId) }
        listChildByGroup?.let {
            if (it.isNotEmpty()) {
                return it.size
            }
        }
        return 0
    }

    override fun getGroup(groupPosition: Int): Any {
        return itemHeaderList?.get(groupPosition)!!
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return itemChildList!![itemHeaderList!![groupPosition].menuId][childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val imageViewIconMenu: ImageView
        val textViewNameMenu: TextView
        val imageViewIconIndicator: ImageView
        var view: View? = convertView
        try {
            if (view == null) {
                val infalInflater =
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                view = infalInflater.inflate(R.layout.menu_item_parentgroup, parent, false)
            }
            imageViewIconMenu =
                view!!.findViewById(R.id.menu_main_drawer_item_parentgroup_iconmenu)
            textViewNameMenu =
                view.findViewById(R.id.menu_main_drawer_item_parentgroup_namemenu)
            imageViewIconIndicator =
                view.findViewById(R.id.menu_main_drawer_item_parentgroup_iconindicator)
            val parentItem = getGroup(groupPosition) as MenuModel
            textViewNameMenu.text = parentItem.webscreenName
            imageViewIconMenu.setImageResource(parentItem.iconMenu)
            if (getChildrenCount(groupPosition) > 0) {
                imageViewIconIndicator.visibility = View.VISIBLE
                if (isExpanded) imageViewIconIndicator.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24) else imageViewIconIndicator.setImageResource(
                    R.drawable.ic_baseline_keyboard_arrow_up_24
                )
            } else {
                imageViewIconIndicator.visibility = View.GONE
            }
        } catch (ex: Exception) {
            Toast.makeText(
                context,
                "Lỗi khi khởi tạo giao diện menu cha: " + ex.message,
                Toast.LENGTH_SHORT
            ).show()
        }
        return view!!
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val imageViewIconMenu: ImageView
        var view: View? = convertView
        if (view == null) {
            val infalInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = infalInflater.inflate(R.layout.menu_item_childgroup, parent, false)
        }
        imageViewIconMenu = view!!.findViewById(R.id.menu_main_drawer_item_childgroup_iconmenu)
        val textViewNameMenu: TextView = view.findViewById(R.id.menu_main_drawer_item_childgroup_namemenu)
        val childMenu = getChild(groupPosition, childPosition) as MenuModel
        textViewNameMenu.text = childMenu.webscreenName
        //imageViewIconMenu.setImageResource(childMenu.getIconMenu());
        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}