package com.amen.studentinternship.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.amen.studentinternship.MainActivity
import com.amen.studentinternship.R
import com.amen.studentinternship.StageModel
import com.amen.studentinternship.stageRepository
import com.amen.studentinternship.stageRepository.singleton.downloadUri
import java.util.*

class AddStageFragment (
    private val context: MainActivity
        ) : Fragment() {
    private var file: Uri? = null
    private var uploadImage:ImageView? = null

        override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View? {
            val view = inflater?.inflate(R.layout.fragment_add_stage, container, false)



            // recuperer uploadImage pour lui associer son composant
            uploadImage = view.findViewById(R.id.preview_image)

            //recuperer le boutton pour charger l'image
            val pickupImageButton = view.findViewById<Button>(R.id.upload_button)

            // lorsqu'on clique ca ouvre les images du telephone
            pickupImageButton.setOnClickListener { pickupImage() }

            // recuperer le bouton confirmer
            val confirmButton = view.findViewById<Button>(R.id.confirm_button)
            confirmButton.setOnClickListener { sendForm(view) }

            return view

        }

    private fun sendForm(view: View) {
        val repo = stageRepository()
        repo.uploadImage(file!!) {
            val stageName = view.findViewById<EditText>(R.id.name_input).text.toString()
            val stageDescription = view.findViewById<EditText>(R.id.description_input).text.toString()
            val duration = view.findViewById<Spinner>(R.id.duration_spinner).selectedItem.toString()
            val nbStagiares = view.findViewById<Spinner>(R.id.nbstagiaires_spinner).selectedItem.toString()
            val downloadImageUrl = downloadUri
            // creer un nouvel objet StageModel
            val stage = StageModel(
                UUID.randomUUID().toString(),
                stageName,
                stageDescription,
                downloadImageUrl.toString(),
                duration,
                nbStagiares
            )
            // envoyer en bd
            repo.insertStage(stage)
        }

    }

    private fun pickupImage() {
        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 47)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 47 && resultCode == Activity.RESULT_OK){

            // verifier si les données sont nulles
            if (data == null || data.data == null) return

            // recuperer l'image selectionnee
            file = data.data

            // maj l'apercu de l'image
            uploadImage?.setImageURI(file)

        }
    }
}