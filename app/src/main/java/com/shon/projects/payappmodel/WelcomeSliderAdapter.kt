package com.shon.projects.payappmodel

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.PictureDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target


class WelcomeSliderAdapter(
    val list: MutableList<String>,
    val context: Context
) :
    RecyclerView.Adapter<SliderViewHolder>() {

    private var requestBuilder: RequestBuilder<PictureDrawable>? = null
    var stringArray: Array<String> = context.resources.getStringArray(R.array.welcome_text)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {

        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_welcome_viewpager_layout, parent, false)
        return SliderViewHolder(view)
    }

    override fun getItemCount() = list.size


    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {

        val typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf")
        holder?.welcome_desc_txt.typeface = typeface

        Glide.with(holder.itemView)  //2
            .load(list[position]) //3
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.loader.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.loader.visibility = View.GONE
                    return false
                }
            }

            )//TODO: interviewer review - for caching
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_launcher_foreground)
            .centerCrop() //4
            .error(R.drawable.ic_error_not_found) //6
            .into(holder.sliderImage)

        holder?.welcome_desc_txt.text = stringArray[position]
    }

}

class SliderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val sliderImage: ImageView = itemView.findViewById(R.id.sliderImage)
    val loader: ProgressBar = itemView.findViewById(R.id.progress_circular)
    val welcome_desc_txt: AppCompatTextView = itemView.findViewById(R.id.welcome_desc_txt)
}
