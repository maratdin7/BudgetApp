package com.example.budget

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.budget.databinding.ActivityMainBinding
import com.example.budget.dto.GroupEntity
import com.example.budget.repository.view.DialogBuilder
import com.example.budget.viewModel.Event
import com.example.budget.viewModel.GroupViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputLayout


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var navController: NavController

    private lateinit var groupViewModel: GroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        drawerLayout = binding.drawerLayout
        toolbar = binding.appBar.toolbar

        navigationView = binding.navigationView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment

        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(navigationView, navController)
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)

        setSupportActionBar(toolbar)
        drawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close)

        groupViewModel = ViewModelProvider(this).get(GroupViewModel::class.java)
    }

    private fun getAllUserGroups() {
        groupViewModel.getAllUserGroups {
            when (it) {
                is Event.Success -> {
                    val groups = it.data
                    if (groups.isNullOrEmpty()) {
                        createGroup()
                    }
                    groupViewModel.setGroupsList(groups!!)
                }
                is Event.Error -> {
                    Toast.makeText(applicationContext, getString(R.string.group_loading_error),
                        Toast.LENGTH_LONG).show()
                }
                Event.Loading -> {
                }
            }
        }
    }

    private fun createGroup() {
        createGroupDialog()

        val observer = observerWithRemove<GroupEntity> { o, t ->
            if (t != null) {
                groupViewModel.setGroupsList(listOf(t))
                groupViewModel.curGroupEntity.removeObserver(o)
            }
        }
        groupViewModel.curGroupEntity.observe(this, observer)
    }

    private fun <T> observerWithRemove(callback: (observer: Observer<T>, t: T?) -> Unit) = object : Observer<T> {
        override fun onChanged(t: T?) {
            callback(this, t)
        }
    }

    private fun createGroupDialog() {
        DialogBuilder.run {
            val relativeLayout = createRelativeLayout(this@MainActivity)

            val groupNameInputLayout = createTextInputLayoutWithEditText(this@MainActivity,
                getString(R.string.name_of_group_field)).apply {
                layoutParams = createLayoutParamsWithMargin(this@MainActivity)
            }

            val builder = createDialogBuilder(this@MainActivity, getString(R.string.add_group_title),
                { d, _ -> createGroupEntity(d, groupNameInputLayout) }, false)

            relativeLayout.addView(groupNameInputLayout)
            builder.setView(relativeLayout)
            val dialog = builder.createDialog()
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

            with(groupNameInputLayout) {
                this.addTextChangedListener { p0, _, _, _ ->
                    error = getString(R.string.group_name_error).takeIf { p0.isNullOrBlank() }
                    positiveButton.isEnabled = error == null
                }
            }
        }
    }

    private fun createGroupEntity(
        d: DialogInterface,
        groupNameInputLayout: TextInputLayout,
    ) {
        val groupName = groupNameInputLayout.editText!!.text.toString()
        groupViewModel.createGroup(groupName) {
            when (it) {
                is Event.Success -> {
                    groupViewModel.saveGroupEntity(this, it.data!!)
                    d.dismiss()
                }
                is Event.Error ->
                    groupNameInputLayout.error = getString(R.string.create_group_error)
                Event.Loading -> {
                }
            }
        }
    }

    fun GroupViewModel.saveGroupEntity(groupEntity: GroupEntity) {
        saveGroupEntity(applicationContext, groupEntity)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        val menuItem = menu!!.findItem(R.id.spinner)
        val spinner = menuItem.actionView as AppCompatSpinner
        spinner.apply {
            adapter = createSpinnerAdapter()
            loadPrevSaveGroup()

            onItemSelectedListener = onSpinnerItemSelectedListener()

            setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) getAllUserGroups()

                false
            }
        }
        return true
    }

    private fun AppCompatSpinner.loadPrevSaveGroup() {
        val observer = observerWithRemove<List<GroupEntity>> { o, t ->
            if (t.isNullOrEmpty())
                return@observerWithRemove

            groupViewModel.loadGroupEntity(applicationContext)

            var curGroupEntity = groupViewModel.curGroupEntity.value
            if (curGroupEntity == null) {
                curGroupEntity = t.first()
            }
            val curGroupId = t.indexOf(curGroupEntity)
            if (curGroupId == -1) return@observerWithRemove

            setSelection(curGroupId)
            groupViewModel.groupsList.removeObserver(o)
        }

        groupViewModel.groupsList.observe(this@MainActivity, observer)
    }

    private fun onSpinnerItemSelectedListener() = object : OnItemSelectedListener {
        override fun onItemSelected(
            adapter: AdapterView<*>, v: View?,
            position: Int, id: Long,
        ) {
            val item = adapter.getItemAtPosition(position).toString()
            groupViewModel.saveGroupEntity(groupViewModel.groupsList.value!![position])
            Toast.makeText(applicationContext, "Группа: $item",
                Toast.LENGTH_LONG).show()
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }

    private fun createSpinnerAdapter(): ArrayAdapter<String> {
        val spinnerAdapter =
            ArrayAdapter<String>(this, R.layout.item_spinner, mutableListOf())
        groupViewModel.groupsList.observe(this) {
            println(groupViewModel.groupsList.value)
            spinnerAdapter.clear()
            spinnerAdapter.addAll(groupViewModel.groupsList.value!!.map { it.name })
        }

        getAllUserGroups()
        return spinnerAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (drawerToggle.onOptionsItemSelected(item))
            true
        else
            super.onOptionsItemSelected(item)
    }

}

@BindingAdapter("errorText")
fun setErrorMessage(view: TextInputLayout, errorMessage: String?) {
    view.error = errorMessage
}

@BindingAdapter("onCloseIconListener")
fun setOnCloseIconClickListener(chip: Chip, callback: (c: Chip) -> Unit) {
    chip.setOnCloseIconClickListener { callback(it as Chip) }
}