package com.vanshika.tasktrack

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.vanshika.tasktrack.databinding.DateTimeFormatViewBinding
import com.vanshika.tasktrack.databinding.FragmentProfileBinding
import com.vanshika.tasktrack.databinding.TimeViewBinding

import androidx.appcompat.app.AppCompatDelegate
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var binding: FragmentProfileBinding? = null
    var array = arrayListOf<String>()
    var timeArray = arrayListOf<String>()
    lateinit var sharedPreferences : SharedPreferences
    lateinit var editor : SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences(
            resources.getString(R.string.app_name),
            MODE_PRIVATE
        )
        editor = sharedPreferences.edit()
        array.addAll(resources.getStringArray(R.array.date_time_format))
        timeArray.addAll(resources.getStringArray(R.array.time_format))
        binding?.tvCategory?.setOnClickListener {
            findNavController().navigate(R.id.categoryFragment)
        }
        binding?.tvFeedback?.setOnClickListener {
            try {
                var intent = Intent(Intent.ACTION_SEND)
                intent.setType("text/email")
                intent.putExtra(Intent.EXTRA_EMAIL, "abc@gmail.com")
                startActivity(intent)
            } catch (exception: Exception) {
                Toast.makeText(
                    requireContext(), R.string.unable_to_send_your_email, Toast.LENGTH_LONG
                ).show()
            }
        }
        binding?.lvDate?.setOnClickListener {
            Dialog(requireContext()).apply {
                var dialog = DateTimeFormatViewBinding.inflate(layoutInflater)
                setContentView(dialog.root)
                show()
                window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                dialog.listView.setOnItemClickListener { parent, view, position, id ->
                    Log.e("TAG", " dialog.listView.selectedItem ${dialog.listView.selectedItem}")
                    saveDateTimeFormat(array[position])
                    Log.e("TAG", " dialog.listView.selectedItem ${array[position]}")
                    dismiss()
                }
            }
        }
        binding?.lvTheme?.setOnClickListener {
            AlertDialog.Builder(requireContext()).apply {
                setTitle(resources.getString(R.string.change_theme))
                setMessage(resources.getString(R.string.change_theme_msg))
                setPositiveButton(resources.getString(R.string.dark)){_,_,->
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    editor.putBoolean("dark", true)
                    editor.commit()
                    editor.apply()
                    requireActivity().recreate()
                }
                setNegativeButton(resources.getString(R.string.light)){_,_,->
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    editor.putBoolean("dark", false)
                    editor.commit()
                    editor.apply()
                    requireActivity().recreate()
                }
                    .show()
            }
        }
        binding?.lvTime?.setOnClickListener {
            Dialog(requireContext()).apply {
                var dialog = TimeViewBinding.inflate(layoutInflater)
                setContentView(dialog.root)
                show()
                window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                dialog.listView.setOnItemClickListener { parent, view, position, id ->
                    saveTime(timeArray[position])
                    dismiss()
                }
            }
        }
        binding?.lvReward?.setOnClickListener {
            findNavController().navigate(R.id.rewardsFragment)
        }
    }

    private fun saveDateTimeFormat(dateTimeFormat: String) {
        editor.putString("dateTimeFormat", dateTimeFormat)
        editor.apply()
    }
    private fun saveTime(timeFormat: String){
        val sharedPreferences : SharedPreferences = requireContext().getSharedPreferences(
            resources.getString(R.string.app_name),
            MODE_PRIVATE
        )
        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("timeFormat",timeFormat)
        editor.apply()
    }
}