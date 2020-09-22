package com.troy.tersive.ui.read

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.troy.tersive.R
import com.troy.tersive.model.data.WebDoc
import com.troy.tersive.ui.ext.inflate

class ReadListAdapter(private val context: Context, private val viewModel: ReadListViewModel) :
    ListAdapter<WebDoc, ReadListAdapter.Holder>(Differ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(parent)

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position)
//        holder.itemView.titleText.text = item.title
//        holder.itemView.authorText.text = context.getString(R.string.author_name, item.author)
        holder.itemView.setOnClickListener {
            viewModel.onClick(item)
        }
    }

    class Holder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_book))

    object Differ : DiffUtil.ItemCallback<WebDoc>() {
        override fun areItemsTheSame(oldItem: WebDoc, newItem: WebDoc) = oldItem.url == newItem.url
        override fun areContentsTheSame(oldItem: WebDoc, newItem: WebDoc) = oldItem == newItem
    }
}
