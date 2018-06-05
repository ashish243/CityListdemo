package controller.citylistInterface

import android.support.v4.app.DialogFragment

/**
* Created by Aashish Shrama on 29/5/18.
*/
interface CityDataInterface {
    fun onDialogPositiveClick(dialog: DialogFragment)
    fun onDialogNegativeClick(dialog: DialogFragment)
}