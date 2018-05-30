package adapter

import BeanClasses.CityListBean
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.citylistdemo.cis.citylistdemo.R
import kotlinx.android.synthetic.main.city_list_row.view.*

/**
 * Created by cis on 29/5/18.
 */
class CityListAdapter(var context: Context, var cityList: ArrayList<CityListBean> ): RecyclerView.Adapter<CityListAdapter.MyCityViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyCityViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.city_list_row, parent, false)
        return CityListAdapter.MyCityViewHolder(view)
    }



    override fun getItemCount(): Int = cityList.size

    override fun onBindViewHolder(holder: MyCityViewHolder?, position: Int) {

        var mVale = cityList[position]
        holder?.tvCityName?.text  = "Name: "+mVale.CityName
        holder?.tvCityState?.text = "StateName: "+mVale.State
        holder?.tvState?.text     = "Population: "+mVale.CityPopulation

    }

    class MyCityViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvCityName  = view.tv_cityname
        val tvCityState = view.tv_state
        val tvState     = view.tv_city_population

    }
}