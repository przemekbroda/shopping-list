package com.brodeon.shoppinglist

import android.util.Log
import android.view.*
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brodeon.shoppinglist.data.ShoppingListItem
import kotlinx.android.synthetic.main.shopping_list_item.view.*

class ShoppingItemsRVAdapter(private var listener: OnItemClicked)
    : RecyclerView.Adapter<ShoppingItemsRVAdapter.ItemsViewHolder>(){

    private var itemsList: List<ShoppingListItem>? = null
    var onLongShoppingListItem: ShoppingListItem? = null


    interface OnItemClicked {
        fun onItemLongClicked(position: Int, shoppingListItem: ShoppingListItem)
        fun onCheckboxChanged(position: Int, shoppingListItem: ShoppingListItem, isChecked: Boolean)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ItemsViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.shopping_list_item, viewGroup, false)
        return ItemsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemsList?.size ?: 0
    }

    fun updateList(itemList: List<ShoppingListItem>) {
        this.itemsList = itemList
        notifyDataSetChanged()
    }

    fun updateListElement(position: Int) {
        notifyItemChanged(position)
    }

    override fun onBindViewHolder(viewHolder: ItemsViewHolder, position: Int) {
        itemsList?.let {
            val item = it[position]

            viewHolder.isBoughtCheckBox.isChecked = item.isBought

            viewHolder.isBoughtCheckBox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                Log.d("itemsAdapter", "isChecked = $isChecked")
                listener.onCheckboxChanged(position, item, isChecked)
            })

            viewHolder.itemDescription.text = item.itemText

            viewHolder.view.setOnLongClickListener {
                onLongShoppingListItem = item
                false
            }
        }
    }

    inner class ItemsViewHolder : androidx.recyclerview.widget.RecyclerView.ViewHolder, View.OnCreateContextMenuListener {
        var view: View
        var itemDescription: TextView
        var isBoughtCheckBox: CheckBox

        constructor(itemView: View) : super(itemView) {
            view = itemView
            itemDescription = itemView.item_description_tv!!
            isBoughtCheckBox = itemView.is_bought_cb!!

            view.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            menu?.add(Menu.NONE, R.id.edit_item_cvi, Menu.NONE, "Edit item")
            menu?.add(Menu.NONE, R.id.delete_item_cvi, Menu.NONE, "Delete item")
        }
    }
}