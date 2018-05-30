package cityfragment

import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.citylistdemo.cis.citylistdemo.R
import citydataInterface.CityDataInterface
import android.app.Activity
import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.add_city_data.*
import kotlinx.android.synthetic.main.add_city_data.view.*
import sqlite.CityDBManager
import utility.Helper
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Aashish Sharma on 28/5/18.
 */
class AddCityDialog : DialogFragment() {

    private var cityDataInterface: CityDataInterface? = null
    private var helper: Helper? = null

    var cityName = ""; var cityPopulation="";var cityState = "";

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
         val rootView = inflater!!.inflate(R.layout.add_city_data, container,
                false)
        helper = Helper(activity)

        cityName       = rootView.et_enter_city_name.text.toString();

        rootView.btn_cancel.setOnClickListener({
            dialog.dismiss()
        })

        rootView.btn_add_data.setOnClickListener({
            if(checlValidation ())               {

                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val strDate = sdf.format(Date())

                Log.v("position", "listNotes.size" + strDate)

                var dbManager = CityDBManager(activity)

                var values = ContentValues()
                values.put("CityName", cityName)
                values.put("CityPopulation", cityPopulation)
                values.put("State", cityState)
                values.put("DateTime",strDate)

                val mID = dbManager.insert(values)

                if (mID > 0) {
                   // Toast.makeText(activity, "City Added successfully!", Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                    cityDataInterface?.onDialogPositiveClick(this)
                } else {
                    Toast.makeText(activity, "City alreday exit!", Toast.LENGTH_LONG).show()
                }

            }
        })
        // Do something else
        return rootView
    }

    fun checlValidation (): Boolean
    {
        cityName       = et_enter_city_name.text.toString();
        cityPopulation = et_enter_city_population.text.toString();
        cityState      = et_enter_city_state.text.toString();

        if(cityName.isNullOrEmpty())               {
            helper?.showToast(activity,""+getString(R.string.app_enter_city_name))
            return false
        }else if(cityPopulation.isNullOrEmpty())       {
            helper?.showToast(activity,""+getString(R.string.app_enter_population))
            return false
        }else if(cityState.isNullOrEmpty())      {
            helper?.showToast(activity,""+getString(R.string.app_enter_state))
            return false
        }else{
            return true
        }

    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        cityDataInterface = activity as CityDataInterface?
    }
}