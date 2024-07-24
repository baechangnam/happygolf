package com.artilearn.happygolfgo.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.BR

abstract class BaseRecyclerViewAdapter<T : Identifiable>
    : ListAdapter<T, BaseRecyclerViewAdapter.BaseViewHolder<T>>(object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.identifier == newItem.identifier
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }
}) {

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bind(currentList[position])
    }

    override fun onViewRecycled(holder: BaseViewHolder<T>) {
        holder.recycle()
    }

    abstract class BaseViewHolder<T>(
        private val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        open fun bind(item: T) {
            binding.setVariable(BR.item, item)
            binding.executePendingBindings()
        }

        open fun recycle() {}
    }

    abstract class ChildBaseViewHolder<T>(
        private val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        abstract val parentPoss: Int

        open fun bind(item: T) {
            binding.setVariable(BR.item, item)
            binding.executePendingBindings()
        }

        open fun recycle() {}
    }

    abstract class ClickViewHolder<T>(
        private val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        abstract val lectureItemClick: Any
        open fun bind(item: T) {
            binding.setVariable(BR.item, item)
            binding.executePendingBindings()
        }

        open fun recycle() {}
    }
}