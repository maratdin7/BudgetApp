package com.example.budget.fragments.menu

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.budget.R
import com.example.budget.adapters.recyclerView.OperationType
import com.example.budget.adapters.recyclerView.UserRecyclerViewAdapter
import com.example.budget.databinding.FragmentCashAccountBinding
import com.example.budget.repository.PersistentRepository
import com.example.budget.repository.view.DialogBuilder
import com.example.budget.viewModel.Event
import com.example.budget.viewModel.ViewModelProviderFactory
import com.example.budget.viewModel.recyclerView.UserViewModel

class UsersFragment : AbstractMenuFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreate(savedInstanceState)
        val binding = FragmentCashAccountBinding.inflate(inflater, container, false)
        findNavController()
        addRecyclerView(binding)

        binding.extendedFab.setOnClickListener {
            DialogBuilder.createDialog()
        }

        return binding.root
    }

    private fun getUserViewModel() =
        ViewModelProvider(this, ViewModelProviderFactory).get(UserViewModel::class.java)

    override fun DialogBuilder.createDialog() {
        val relativeLayout = createRelativeLayout(requireContext())
        val viewModel = getUserViewModel()
        var emailForInvite = ""

        val userNameInputLayout =
            createTextInputLayoutWithEditText(requireContext(), getString(R.string.user_email)).apply {
                layoutParams = createLayoutParamsWithMargin(requireContext()).apply {
                    addRule(RelativeLayout.ALIGN_PARENT_START)
                    addRule(RelativeLayout.ALIGN_PARENT_TOP)
                }
                helperText = context.getString(R.string.helper_text_for_adding_user_to_group)
                editText?.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }

        relativeLayout.addView(userNameInputLayout)

        val positiveButtonClickListener = { viewModel.invitationToJoinGroup(emailForInvite) }

        val builder = createDialogBuilder(requireContext(),
            getString(R.string.add_user_to_group_request), {_, _ -> positiveButtonClickListener()})
        builder.setView(relativeLayout)

        val dialog = builder.createDialog()
        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        userNameInputLayout.editText?.addTextChangedListener(object : DialogBuilder.AbstractTextWatcher() {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                positiveButton.isEnabled = false
                userNameInputLayout.error = when {
                    p0.isNullOrBlank() -> getString(R.string.empty_field)
                    p0.contains('@').not() -> getString(R.string.illegal_email)
                    else -> {
                        emailForInvite = p0.toString()
                        positiveButton.isEnabled = true
                        null
                    }
                }
            }
        })
    }

    @SuppressLint("ShowToast")
    private fun UserViewModel.invitationToJoinGroup(emailForInvite: String) {
        val groupEntity = PersistentRepository.defGroupEntity.value ?: return
        this.invitationToJoinGroup(groupEntity.id, emailForInvite) {
            when (it) {
                is Event.Success -> Toast.makeText(requireContext(),
                    "На почту пользователя отправлено приглашение в группу",
                    Toast.LENGTH_LONG)
                is Event.Error ->
                    Toast.makeText(requireContext(), "Такого пользователя не существует", Toast.LENGTH_LONG)
                Event.Loading -> Unit
            }
        }
    }

    override fun adapterSettings(): RecyclerView.Adapter<RecyclerView.ViewHolder> {
        val viewModel = ViewModelProvider(this, ViewModelProviderFactory).get(UserViewModel::class.java)
        val adapter = UserRecyclerViewAdapter(viewModel)
        return adapter as RecyclerView.Adapter<RecyclerView.ViewHolder>
    }
}

