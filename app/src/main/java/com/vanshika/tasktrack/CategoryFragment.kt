package com.vanshika.tasktrack

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.vanshika.tasktrack.databinding.DialogLayoutBinding
import com.vanshika.tasktrack.databinding.FragmentCategoryBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CategoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var binding : FragmentCategoryBinding ?= null
    lateinit var categoryAdapter: ArrayAdapter<CategoryDataClass>
    var categoryArray = arrayListOf<CategoryDataClass>()
    lateinit var tasksDatabase: TasksDatabase
    var categoryDataClass = CategoryDataClass()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tasksDatabase = TasksDatabase.getInstance(requireContext())
        categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, categoryArray)
        binding?.spCategory?.adapter = categoryAdapter
        binding?.fabCategory?.setOnClickListener {
            Dialog(requireContext()).apply {
                var dialogBinding = DialogLayoutBinding.inflate(layoutInflater)
                setContentView(dialogBinding.root)
                window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                show()
                dialogBinding.btnSave.setOnClickListener {
                    if (dialogBinding.etCreateNewCategory.text.toString().isEmpty()){
                        dialogBinding.etCreateNewCategory.error = resources.getString(R.string.enter_category)
                    }else{
                        tasksDatabase.tasksDao().insertCategory(
                            CategoryDataClass(
                                categoryName = dialogBinding.etCreateNewCategory.text.toString()
                            )
                        )
                        getCategoryList()
                        dismiss()
                    }
                }
                dialogBinding.btnCancel.setOnClickListener {
                    dismiss()
                }
            }
        }
        getCategoryList()
        binding?.spCategory?.setOnItemClickListener { parent, view, position, id ->
            Dialog(requireContext()).apply {
                var dialogBinding = DialogLayoutBinding.inflate(layoutInflater)
                setContentView(dialogBinding.root)
                window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                show()
                dialogBinding.tvCategory.setText(R.string.update_category)
                dialogBinding.etCreateNewCategory.setText(categoryArray[position].categoryName)
                dialogBinding.btnSave.setText(resources.getString(R.string.update))
                dialogBinding.btnSave.setOnClickListener {
                    if (dialogBinding.etCreateNewCategory.text.toString().isEmpty()){
                        dialogBinding.etCreateNewCategory.error = resources.getString(R.string.enter_category)
                    }else{
                        tasksDatabase.tasksDao().updateCategory(
                            CategoryDataClass(
                                categoryId = categoryArray[position].categoryId,
                                categoryName = dialogBinding.etCreateNewCategory.text.toString()
                            )
                        )
                        getCategoryList()
                        dismiss()
                    }
                }
                dialogBinding.btnCancel.setOnClickListener {
                    dismiss()
                }
            }
        }
        binding?.spCategory?.setOnItemLongClickListener { parent, view, position, id ->
            var alertDialog = AlertDialog.Builder(requireContext())
            alertDialog.setTitle(resources.getString(R.string.you_want_to_delete_this_list))
            alertDialog.setPositiveButton(resources.getString(R.string.yes)){_,_ ->
                tasksDatabase.tasksDao().deleteCategory(categoryArray[position])
                categoryAdapter.notifyDataSetChanged()
                getCategoryList()
            }
            alertDialog.setNegativeButton(resources.getString(R.string.no)){_,_ ->
            }
            alertDialog.show()
            return@setOnItemLongClickListener true
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CategoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CategoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun getCategoryList(){
        categoryArray.clear()
        categoryArray.addAll(tasksDatabase.tasksDao().getCategory())
        if (categoryArray.size == 0){
            binding?.vsEmptyView?.visibility = View.VISIBLE
        }else {
            binding?.vsEmptyView?.visibility = View.GONE
        }
    }
}