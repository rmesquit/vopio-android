package info.vopio.android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import timber.log.Timber

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import info.vopio.android.Utilities.Constants
import info.vopio.android.Utilities.MessageUploader

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HostFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HostFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var localUsername: String? = null
    private var localUserEmail: String? = null


    lateinit var fragmentContext: Context
    lateinit var fragmentContainer: View
    lateinit var thisFirebaseDatabaseReference : DatabaseReference
    lateinit var newSessionID : String
    lateinit var dataSnapshotList : DataSnapshot


    private fun hostNewSession(){

        var allowedToHost = false

        // Loop through Real Time Database -- look for matching user emails (whitelisted) for professors.
        for (snapshot in dataSnapshotList.child("host_list").children) {

            val hostEmail = snapshot.child("email").value

            if (hostEmail == localUserEmail){
                allowedToHost = true
                break
            }
        }

        if (allowedToHost) {
            Timber.i("-->>SpeechX: onDataChange HOST!")

            this.newSessionID = thisFirebaseDatabaseReference.push().key.toString()

            val intent = Intent(fragmentContext, HostSessionActivity::class.java)
            intent.putExtra(Constants.SESSION_USERNAME, localUsername)
            intent.putExtra(Constants.SESSION_USER_EMAIL, localUserEmail)
            intent.putExtra(Constants.HOST_TAG, allowedToHost)
            intent.putExtra(Constants.SESSION_KEY, this.newSessionID)
            startActivity(intent)
        } else {

            val alertDialogBuilder = AlertDialog.Builder(fragmentContext)
            alertDialogBuilder
                .setTitle("Oops!")
                .setMessage("Hosting is not available with your account.")
                .setCancelable(true)
                .setPositiveButton("Dismiss") { dialog, which ->

                    dialog.cancel()
                }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            localUsername = it.getString(ARG_PARAM1)
            localUserEmail = it.getString(ARG_PARAM2)
        }

        thisFirebaseDatabaseReference = FirebaseDatabase.getInstance().reference
        thisFirebaseDatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                // check email list against local user email -- when creating new session must have host credential
                dataSnapshotList = dataSnapshot
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Timber.i("-->>SpeechX: onDataChange Failed to read value:${error.toException()}")
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        fragmentContainer = inflater.inflate(R.layout.fragment_host, container, false)
        fragmentContext = fragmentContainer.context

        val hostBtn : Button = fragmentContainer.findViewById(R.id.hostSessionBtn)
        hostBtn.setOnClickListener {

            hostNewSession()
//            MessageUploader().saveWord(thisFirebaseDatabaseReference, "new word",
//                localUserEmail.toString()
//            )

        }

        return fragmentContainer
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HostFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HostFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}