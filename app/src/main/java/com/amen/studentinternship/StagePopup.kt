package com.amen.studentinternship

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.amen.studentinternship.adapter.StageAdapter
import com.bumptech.glide.Glide

class StagePopup (private val adapter: StageAdapter , private val currentStage: StageModel) : Dialog(adapter.context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup_stages_details)
        setupComponents()
        setupCloseButton()
        setupDeleteButton()
        setupStarButton()
    }

    private fun updateStar(button: ImageView) {
        if (currentStage.liked){
            button.setImageResource(R.drawable.ic_star)
        }
        else{
            button.setImageResource(R.drawable.ic_unstar)
        }
    }

    private fun setupStarButton() {
        // recuperer
        val starButton = findViewById<ImageView>(R.id.star_button)
        updateStar(starButton)
        // interaction
        starButton.setOnClickListener{
            currentStage.liked = !currentStage.liked
            val repo = stageRepository()
            repo.updateStage(currentStage)
            updateStar(starButton)
        }
    }

    private fun setupDeleteButton() {
        findViewById<ImageView>(R.id.delete_button).setOnClickListener{
            // supp le stage de la bd
            val repo = stageRepository()
            repo.deleteStage(currentStage)
            dismiss()
        }
    }

    private fun setupCloseButton() {
        findViewById<ImageView>(R.id.colse_button).setOnClickListener{
            // fermer la fenetre popup
            dismiss()
        }
    }

    private fun setupComponents() {
        // actualiser l'image du stage
        val stageImage = findViewById<ImageView>(R.id.image_item)
        Glide.with(adapter.context).load(Uri.parse(currentStage.imageUrl)).into(stageImage)

        // actualiser le nom du stage
        findViewById<TextView>(R.id.popup_stage_name).text = currentStage.name

        // actualiser le description du stage
        findViewById<TextView>(R.id.popop_stage_description_subtitle).text = currentStage.description

        // actualiser la durée du stage
        findViewById<TextView>(R.id.popop_stage_duration_subtitle).text = currentStage.duration

        // actualiser le nbstagiaires du stage
        findViewById<TextView>(R.id.popop_stage_nbstagiaires_subtitle).text = currentStage.nbStagiares
    }

}