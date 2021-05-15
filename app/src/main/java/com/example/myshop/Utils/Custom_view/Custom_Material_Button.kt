package com.example.myshop.Utils.Custom_view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import com.google.android.material.button.MaterialButton

class Custom_Material_Button(context: Context, attributeSet: AttributeSet):MaterialButton(context,attributeSet) {
    init {
       applybtn()
    }

    fun applybtn(){
        var ir_sans_type_face =Typeface.createFromAsset(context.assets,"fonts/"+"ir_sans_font.ttf")
         typeface=ir_sans_type_face
      }
}