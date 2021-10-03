package com.example.budget.repository.view

import android.content.Context
import android.content.DialogInterface
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import com.example.budget.R
import com.example.budget.repository.FormatterRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.RangeSlider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.NumberFormat
import java.util.*

object DialogBuilder {

    private fun generateId(): Int = ViewCompat.generateViewId()

    abstract class AbstractTextWatcher : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {}
    }

    fun TextInputLayout.addTextChangedListener(callback: (p0: String?, p1: Int, p2: Int, p3: Int) -> Unit) {
        this.editText!!.addTextChangedListener(object : AbstractTextWatcher() {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                callback(p0?.toString(), p1, p2, p3)
            }
        })
    }

    fun createDialogBuilder(
        context: Context,
        title: String = "",
        positiveButtonListener: (dialog: DialogInterface, which: Int) -> Unit = { _, _ -> },
        neutralButtonEnable: Boolean = true,
    ): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(context).apply {
            setTitle(title)
            setPositiveButton(context.getString(R.string.ok)) { d, w ->
                positiveButtonListener(d, w)
            }
            if (neutralButtonEnable)
                setNeutralButton(context.getString(R.string.cancel), null)
            setCancelable(false)
        }
    }

    fun MaterialAlertDialogBuilder.createDialog(): AlertDialog {
        return create().apply {
            show()
            getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
        }
    }

    fun createRadioGroup(context: Context, names: List<String>, isChecked: Int = 0): RadioGroup {
        val radioGroup = RadioGroup(context).apply {
            id = generateId()
        }

        val radioButtons = names.map {
            RadioButton(context).apply {
                id = generateId()
                text = it
            }
        }

        radioButtons[isChecked].isChecked = true

        radioButtons.forEach { radioGroup.addView(it) }
        return radioGroup
    }

    fun createIntSlider(context: Context, sumRange: IntRange): RangeSlider {
        return RangeSlider(context).apply {
            id = generateId()
            valueFrom = sumRange.first.toFloat()
            valueTo = sumRange.last.toFloat()
            stepSize = 100f
            values = listOf(sumRange.first.toFloat(), sumRange.last.toFloat())
            setLabelFormatter {
                FormatterRepository.priceFormatter.apply {
                    maximumFractionDigits = 0
                }
                    .format(it.toDouble())
            }

        }
    }

    fun createTextInputLayoutWithEditText(
        context: Context,
        hint: String,
    ): TextInputLayout =
        TextInputLayout(context).apply {
            boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_OUTLINE
            this.hint = hint
            id = generateId()
            layoutParams = createLayoutParamsWithMargin(context)
            addView(TextInputEditText(context))
        }

    fun createRelativeLayout(context: Context): RelativeLayout {
        return RelativeLayout(context).apply {
            layoutParams = createLayoutParams()
            this.id = ViewCompat.generateViewId()
        }
    }

    fun createLayoutParams(): RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.MATCH_PARENT,
        RelativeLayout.LayoutParams.WRAP_CONTENT
    )

    fun createLayoutParamsWithMargin(context: Context): RelativeLayout.LayoutParams {
        val layoutParams = createLayoutParams()
        layoutParams.setMargins(
            20.toDp(context),
            8.toDp(context),
            20.toDp(context),
            8.toDp(context)
        )
        return layoutParams
    }

    fun hideKeyboardOnFocusChanged(context: Context, view: View, hasFocus: Boolean) {
        if (!hasFocus) {
            val aa: InputMethodManager? = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            aa!!.hideSoftInputFromWindow(view.applicationWindowToken, 0)
        }
    }

    fun Int.toDp(context: Context): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
    ).toInt()
}