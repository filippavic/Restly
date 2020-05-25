package com.fer.ppj.restly

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WelcomeSliderAdapter(private val welcomeSlides: List<WelcomeSlide>)
    : RecyclerView.Adapter<WelcomeSliderAdapter.WelcomeSlideViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WelcomeSlideViewHolder {
        return WelcomeSlideViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.slide_item_container,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return welcomeSlides.size
    }

    override fun onBindViewHolder(holder: WelcomeSlideViewHolder, position: Int) {
        holder.bind(welcomeSlides[position])
    }

    inner class WelcomeSlideViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val description = view.findViewById<TextView>(R.id.welcomeDesc)
        private val image = view.findViewById<ImageView>(R.id.welcomeSliderImage)

        fun bind (welcomeSlide: WelcomeSlide) {
            description.text = welcomeSlide.description
            image.setImageResource(welcomeSlide.image)
        }
    }
}