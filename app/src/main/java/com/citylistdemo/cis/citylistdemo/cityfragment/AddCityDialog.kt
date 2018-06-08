package com.citylistdemo.cis.citylistdemo.cityfragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.citylistdemo.cis.citylistdemo.R
import controller.citylistInterface.CityDataInterface
import android.app.Activity
import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import controller.CheckValidation
import kotlinx.android.synthetic.main.add_city_data.*
import kotlinx.android.synthetic.main.add_city_data.view.*
import models.sqlite.CityDBManager
import utility.Helper
import java.text.SimpleDateFormat
import java.util.*


/**
* Created by Aashish Sharma on 28/5/18.
*/
class AddCityDialog : DialogFragment() {

    private var cityDataInterface: CityDataInterface? = null
    private var helper: Helper? = null
    private var validation: CheckValidation? = null

    private var cityName = ""
    private var cityPopulation=""
    private var cityState = ""

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
         val rootView = inflater?.inflate(R.layout.add_city_data, container,
                false)
        helper = Helper(activity)

        cityName       = rootView?.et_enter_city_name?.text.toString()

        rootView?.btn_cancel?.setOnClickListener({
            dialog.dismiss()
        })

        rootView?.btn_add_data?.setOnClickListener({

        if(checkValidation ())               {
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val strDate = sdf.format(Date())

                Log.v("position", "listNotes.size$strDate")

                val dbManager = CityDBManager(activity)

                val values = ContentValues()
                values.put("CityName", cityName.toUpperCase())
                values.put("CityPopulation", cityPopulation)
                values.put("State", cityState)
                values.put("DateTime",strDate)

                val mID = dbManager.insert(values)

                if (mID > 0) {
                   // Toast.makeText(activity, "City Added successfully!", Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                    cityDataInterface?.onDialogPositiveClick(this)
                } else {
                    Toast.makeText(activity, "City already exit!", Toast.LENGTH_LONG).show()
                }
            }
        })
        // Do something else
        return rootView
    }

    private fun checkValidation (): Boolean
    {
        cityName       = et_enter_city_name.text.toString().trim()
        cityPopulation = et_enter_city_population.text.toString().trim()
        cityState      = et_enter_city_state.text.toString().trim()

        validation = CheckValidation()

        if(!validation!!.isCityNameValid(cityName)){
            helper?.showToast(activity,""+getString(R.string.app_enter_city_name))
            false
        }else if(!validation!!.isCityPopulationValid(cityPopulation)){
            helper?.showToast(activity,""+getString(R.string.app_enter_population))
            false
        }else if(cityPopulation.startsWith("0",false)){
            helper?.showToast(activity,""+getString(R.string.app_enter_validpopulation))
            false
        }else if(!validation!!.isCityStateValid(cityState)){
            helper?.showToast(activity,""+getString(R.string.app_enter_state))
            false
        }else{
          return true
        }
        return false

    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        cityDataInterface = activity as CityDataInterface?
    }
}