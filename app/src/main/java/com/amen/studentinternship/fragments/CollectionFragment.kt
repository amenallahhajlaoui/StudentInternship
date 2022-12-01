package com.amen.studentinternship.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amen.studentinternship.MainActivity
import com.amen.studentinternship.R
import com.amen.studentinternship.adapter.StageAdapter
import com.amen.studentinternship.adapter.StageItemDecoration
import com.amen.studentinternship.stageRepository.singleton.stageList

class CollectionFragment (
    private val context: MainActivity
        ) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.fragment_collection, container, false)

        // recuperer le recyclerView
        val collectioRecyclerView = view.findViewById<RecyclerView>(R.id.collection_recycler_list)

        collectioRecyclerView.adapter = StageAdapter(context, stageList.filter { it.liked }, R.layout.item_vertical_stage)
        collectioRecyclerView.layoutManager = LinearLayoutManager(context)
        collectioRecyclerView.addItemDecoration(StageItemDecoration())
        return view
    }
}