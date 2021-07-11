package com.example.dailynews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class newsadapter(var listener:onitemcclick) :RecyclerView.Adapter<Newsviewholder>() {

    private var itemsthing: ArrayList<News> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Newsviewholder {


        var view =LayoutInflater.from(parent.context).inflate(R.layout.item_news,parent,false)
        var viewholder=Newsviewholder(view)
        

        view.setOnClickListener{

            listener.onitemclicked(itemsthing[viewholder.adapterPosition])
        }
        return viewholder

    }

    override fun onBindViewHolder(holder: Newsviewholder, position: Int) {
        var currentItem =itemsthing[position]
        holder.titleview.text=currentItem.title
        holder.author.text=currentItem.author
        Glide.with(holder.itemView.context).load(currentItem.imageurl).into(holder.image)

    }

    override fun getItemCount(): Int {
        return itemsthing.size
    }

    fun updatenews(updatednews:ArrayList<News>){
        itemsthing.clear()
        itemsthing.addAll(updatednews)

        notifyDataSetChanged()
    }

}

class Newsviewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var titleview:TextView=itemView.findViewById(R.id.textView)
    var image:ImageView=itemView.findViewById(R.id.image)
    var author:TextView=itemView.findViewById(R.id.author)

}
interface  onitemcclick{
    fun onitemclicked(item:News)



}