package ru.netology.nMediaApp.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

var POST_TEXT: String = "Post text"

class EditPostResultContract : ActivityResultContract<String, String?>() {


    override fun createIntent(context: Context, input: String): Intent {
       val intetnt = Intent(context, EditPostActivity::class.java)
        intetnt.putExtra(POST_TEXT, input)
        return intetnt
    }


    override fun parseResult(resultCode: Int, intent: Intent?): String? =
        if (resultCode == Activity.RESULT_OK) {
            intent?.getStringExtra(Intent.EXTRA_TEXT)
        } else {
            null
        }
}
