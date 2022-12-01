package com.amen.studentinternship.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amen.studentinternship.*
import com.bumptech.glide.Glide

class StageAdapter(
    val context: MainActivity,
    private val stageList: List<StageModel>,
    private val layoutId: Int
    ) : RecyclerView.Adapter<StageAdapter.ViewHolder>(){

    // boite pour ranger tout les commposants a controler
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val stageImage =view.findViewById<ImageView>(R.id.image_item)
        val stageName : TextView? =view.findViewById(R.id.name_item)
        val stageDescription : TextView? =view.findViewById(R.id.description_item)
        val starIcon = view.findViewById<ImageView>(R.id.star_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(layoutId,parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // recuperer les info du stage
        val currentStage = stageList[position]

        // recuperer le repository
        val repo = stageRepository()

        // utiliser glide pour recuperer l'image a partir de son lien -> composant
        Glide.with(context).load(Uri.parse(currentStage.imageUrl)).into(holder.stageImage)

        // mise a jour du nom du stage
        holder.stageName?.text = currentStage.name

        // mise a jour du description du stage
        holder.stageDescription?.text = currentStage.description

        // verifier si le stage a été liked
        if (currentStage.liked){
            holder.starIcon.setImageResource(R.drawable.ic_star)
        }
        else {
            holder.starIcon.setImageResource(R.drawable.ic_unstar)
        }

        // rajouter une interaction sur cette etoile
        holder.starIcon.setOnClickListener{
            // inverse si le bouton liked ou non
            currentStage.liked = !currentStage.liked
            // mettre a jour l'objet stage
            repo.updateStage(currentStage)
        }
        // interaction lors du clic sur un stage
        holder.itemView.setOnClickListener{
            // afficher le popup
            StagePopup(this, currentStage).show()
        }
    }

    override fun getItemCount(): Int = stageList.size

}