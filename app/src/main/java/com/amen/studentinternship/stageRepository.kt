package com.amen.studentinternship

import android.net.Uri
import com.amen.studentinternship.stageRepository.singleton.databaseRef
import com.amen.studentinternship.stageRepository.singleton.downloadUri
import com.amen.studentinternship.stageRepository.singleton.stageList
import com.amen.studentinternship.stageRepository.singleton.storageReference
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.util.*

class stageRepository {

    object singleton {
        // donner le lien pour acceder au bucket
        private val BUCKET_URL: String = "gs://student-internship-7b708.appspot.com"

        //se connecter a notre espace de stockage
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)
        //se connecter à la reférence "stages"
        val databaseRef = FirebaseDatabase.getInstance().getReference("stages")
        //creer une liste qui va contenir nos stages
        val stageList = arrayListOf<StageModel>()
        //contenire le lien de l'image courante
        var downloadUri: Uri? = null
    }

    fun updateData(callback: () -> Unit) {
        // absorber les donnees depuis databaseRef -> liste des stages
        databaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                // retirer les anciennes
                stageList.clear()
                // recolter la liste
                for (ds in snapshot.children){
                    // construire un objet stage
                    val stage = ds.getValue(StageModel::class.java)

                    // verrifer si le stage n'est pas null
                    if(stage!= null){
                        // ajouter le stage a notre liste
                        stageList.add(stage)
                    }
                }
                // actionner le callback
                callback()
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    // creer une fonction pour envoyer des fichiers sur le storage
    fun uploadImage(file: Uri, callback: () -> Unit){
        // verifier que ce fichier n'est pas null
        if (file != null){
            val fileName = UUID.randomUUID().toString() + ".jpg"
            val ref = storageReference.child(fileName)
            val uploadTask = ref.putFile(file)

            // demarer la tache d'envoi
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>>{ task ->

                //si il y a eu un probleme lors de l'envoi du fichier
                if (!task.isSuccessful){
                    task.exception?.let { throw it }
                }

                return@Continuation ref.downloadUrl

            }).addOnCompleteListener { task ->
                //verifier si tout a bien fonctionne
                if (task.isSuccessful){
                    // recuperer l'image
                    downloadUri = task.result
                    callback()

                }

            }
        }
    }
    // mettre a jour un objet stage en bd
    fun updateStage(stage : StageModel) = databaseRef.child(stage.id).setValue(stage)

    // inserer un nouveau stage au bd
    fun insertStage(stage : StageModel) = databaseRef.child(stage.id).setValue(stage)

    // supp un stage de la bd
    fun deleteStage(stage : StageModel) = databaseRef.child(stage.id).removeValue()




}