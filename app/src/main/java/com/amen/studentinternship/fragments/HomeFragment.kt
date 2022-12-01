package com.amen.studentinternship.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.amen.studentinternship.MainActivity
import com.amen.studentinternship.R
import com.amen.studentinternship.StageModel
import com.amen.studentinternship.adapter.StageAdapter
import com.amen.studentinternship.adapter.StageItemDecoration
import com.amen.studentinternship.stageRepository.singleton.stageList

class HomeFragment(
    private val context: MainActivity
) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_home, container, false)

        // recuperer le recyclerview
        val horizontalRecyclerView = view.findViewById<RecyclerView>(R.id.horizontal_recycler_view)
        horizontalRecyclerView.adapter = StageAdapter(context, stageList.filter { !it.liked }, R.layout.item_horizontal_stage)

        // recuperer le second reyclerview
        val verticalRecyclerView = view.findViewById<RecyclerView>(R.id.vertical_recycler_view)
        verticalRecyclerView.adapter = StageAdapter(context, stageList, R.layout.item_vertical_stage)
        verticalRecyclerView.addItemDecoration(StageItemDecoration())


        return view
    }
}