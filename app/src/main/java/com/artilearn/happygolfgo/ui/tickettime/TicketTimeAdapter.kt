package com.artilearn.happygolfgo.ui.tickettime

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.data.TicketTime
import com.artilearn.happygolfgo.databinding.ItemTicketTimeBinding

class TicketTimeAdapter(
    private val isMultiSelectable:Boolean = false,
    private val itemClick: (TicketTime, Int) -> Unit
) : RecyclerView.Adapter<TicketTimeAdapter.TicketTimeViewHolder>() {

    private val items: ArrayList<TicketTime> = arrayListOf()
    lateinit var binding: ItemTicketTimeBinding

    var onClickCheckBox: ((TicketTime, Int) -> Unit)? = null;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketTimeViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_ticket_time,
            parent,
            false
        )

        return TicketTimeViewHolder(isMultiSelectable,binding).also {
            binding.root.setOnClickListener { position ->
                itemClick(items[it.adapterPosition], it.adapterPosition)
            }
            if (onClickCheckBox != null) {
                binding.timeCheckbox.setOnClickListener { position ->
                    onClickCheckBox?.let { checkBox -> checkBox(items[it.adapterPosition], it.adapterPosition) }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: TicketTimeViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    public  fun getSelectedItemCount() : Int  {
       val selectedItems =    items.filter {  item ->
              item.isChecked
         }
        return selectedItems.size;
    }

    public fun getMutipleTicketTime() : TicketTime? {
        val selectedItems =    items.filter {  item ->
            item.isChecked
        }
        if (selectedItems.size == 0) {
            return null;
        }
        val  ticketTime = selectedItems[0];//.copy();
        var count = 0;
        ticketTime.isMutiple = true;
        ticketTime.firstPlateAvailabilityId = 0;
        ticketTime.firstStartDate = null;
        ticketTime.firstEndDate = null;

        ticketTime.secondPlateAvailabilityId = 0;
        ticketTime.secondStartDate = null;
        ticketTime.secondEndDate = null;

        ticketTime.thirdPlateAvailabilityId = 0;
        ticketTime.thirdStartDate = null;
        ticketTime.thirdEndDate = null;

        selectedItems.forEachIndexed {  index, timeTime ->
            if (timeTime.plateAvailabilityId != null ) {
                when (index) {
                    0 -> {
                        ticketTime.firstPlateAvailabilityId = timeTime.plateAvailabilityId;
                        ticketTime.firstStartDate = timeTime.startDate
                        ticketTime.firstEndDate = timeTime.endDate
                    }
                    1 -> {
                        ticketTime.secondPlateAvailabilityId = timeTime.plateAvailabilityId;
                        ticketTime.secondStartDate = timeTime.startDate
                        ticketTime.secondEndDate = timeTime.endDate
                    }
                    2 -> {
                        ticketTime.thirdPlateAvailabilityId = timeTime.plateAvailabilityId;
                        ticketTime.thirdStartDate = timeTime.startDate
                        ticketTime.thirdEndDate = timeTime.endDate
                    }
                    else -> {
                        count += 1
                    }
                }
            }
        }

        return ticketTime;
    }

    class TicketTimeViewHolder(
        private val isMultiSelectable:Boolean = false,
        private val binding: ItemTicketTimeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TicketTime) {
            binding.item = item
            //feature/2022/0216/multiSelection
            //다중 선택
            if (isMultiSelectable) {
                binding.timeCheckbox.visibility = View.VISIBLE
                binding.timeCheckbox.isChecked =  item.isChecked
            }
            binding.executePendingBindings()
        }
    }

    fun setItems(ticketTime: List<TicketTime>) {
        this.items.addAll(ticketTime)
        notifyDataSetChanged()
    }

    fun clear() {
        this.items.clear()
        notifyDataSetChanged()
    }
}