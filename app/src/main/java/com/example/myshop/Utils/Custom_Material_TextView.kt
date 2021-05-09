package com.example.myshop.Utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import com.google.android.material.textview.MaterialTextView


class Custom_Material_TextView(context: Context, attributeSet: AttributeSet):MaterialTextView(
    context,
    attributeSet
) {
    init {
        apply_EDT()
    }
    fun apply_EDT(){
        setPadding(1,1,1,1)
        var ir_sans_type_face = Typeface.createFromAsset(
            context.assets,
            "Myfont/" + "ir_sans_font.ttf"
        )
        typeface=ir_sans_type_face

    }

}