package com.example.sixkeeper

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class SavedPasswordFragment : Fragment() {
    private lateinit var attActivity: Activity

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_saved_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewSavedPass()
    }

    @Suppress("DEPRECATION")
    override fun onAttach(activity: Activity) {                                                     // Override on attach
        super.onAttach(activity)
        attActivity = activity                                                                      // Attach activty
    }

    private fun viewSavedPass() {                                                                   // View saved password
        val databaseHandler = DatabaseHandlerClass(attActivity)
        val userSavedPass: List<UserSavedPassModelClass> = databaseHandler.viewSavedPass()

        val userSavedPassId = Array(userSavedPass.size) { "0" }
        val userSavedPassPassword = Array(userSavedPass.size) { "null" }

        for ((index, u) in userSavedPass.withIndex()) {
            userSavedPassId[index] = u.passId.toString()
            userSavedPassPassword[index] = u.generatedPassword
        }

        val savedPasswordListAdapter = SavedPasswordListAdapter(
                attActivity,
                userSavedPassId,
                userSavedPassPassword
        )
        val lvSavedPasswordContainer: ListView =
                (activity as AppCompatActivity).findViewById(R.id.lvSavedPasswordContainer)

        lvSavedPasswordContainer.adapter = savedPasswordListAdapter
    }
}