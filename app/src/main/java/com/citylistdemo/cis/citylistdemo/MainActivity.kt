package com.citylistdemo.cis.citylistdemo

import models.CityListBean

import android.graphics.Canvas
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.citylistdemo.cis.citylistdemo.cityfragment.AddCityDialog
import com.baoyz.actionsheet.ActionSheet
import controller.CityListAdapter
import controller.SwipeControllerActions
import controller.citylistInterface.CityDataInterface
import kotlinx.android.synthetic.main.activity_main.*
import models.sqlite.CityDBManager
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : FragmentActivity(), View.OnClickListener, CityDataInterface, ActionSheet.ActionSheetListener {

    var mAdapter: CityListAdapter? = null
    var listNotes = ArrayList<CityListBean>()
    var deletedList = ArrayList<String>()
    var dbManager: CityDBManager? = null
    private var sortingFlag = "ByDate"
    lateinit var snackbar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dbManager = CityDBManager(this)
        initialize()
        loadQueryAll()

    }

   private fun initialize() {
        iv_add_city.setOnClickListener(this)
        iv_sortlist.setOnClickListener(this)
    }

   // Function for initialize RecyclerView
    private fun setupRecyclerView() {
        val swipeController: SwipeController?

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = mAdapter

       // List swipe action handle here
        swipeController = SwipeController(object : SwipeControllerActions() {
            override
            fun onRightClicked(position: Int) {

                if (deletedList.isEmpty()) {
                    deletedList.add(listNotes[position].CityName.toUpperCase())
                } else {
                    val selectionArgs = arrayOf(deletedList[0])
                    dbManager?.delete("CityName=?", selectionArgs)
                    deletedList.clear()
                    deletedList.add(listNotes[position].CityName.toUpperCase())
                }

                /*val selectionArgs = arrayOf(listNotes[position].CityName.toString())
                 dbManager?.delete("CityName=?", selectionArgs)*/


                listNotes.removeAt(position)
                mAdapter?.notifyItemRemoved(position)
                mAdapter?.notifyItemRangeChanged(position, mAdapter!!.itemCount)


                if (listNotes.size == 0) {
                    tv_norecord_found.visibility = VISIBLE
                    recyclerView.visibility = GONE
                    iv_sortlist.visibility = GONE
                }


                snackbar = Snackbar
                        .make(root_view, "You can undo your last item", 3000)
                        .setAction("Undo") {
                            // Do something when undo action button clicked
                            loadQueryAll()
                            deletedList.clear()
                        }

                snackbar.show()

            }
        })

        val itemTouchhelper = ItemTouchHelper(swipeController)
        itemTouchhelper.attachToRecyclerView(recyclerView)

        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
                swipeController.onDraw(c)
            }
        })
    }

    // Function to get city list from database
    fun loadQueryAll() {

        val cursor = dbManager?.queryAll()

        listNotes.clear()
        if (cursor!!.moveToFirst()) {

            do {
                val cityName = cursor.getString(cursor.getColumnIndex("CityName"))
                val cityPopulation = cursor.getString(cursor.getColumnIndex("CityPopulation"))
                val state = cursor.getString(cursor.getColumnIndex("State"))
                val dateTime = cursor.getString(cursor.getColumnIndex("DateTime"))
                listNotes.add(CityListBean(cityName, cityPopulation.toInt(), state, dateTime))

            } while (cursor.moveToNext())
        }

        //Log.v("position", "listNotes.size" + listNotes.size)

        if (listNotes.size != 0) {

            tv_norecord_found.visibility = View.GONE
            recyclerView.visibility = VISIBLE
            iv_sortlist.visibility = VISIBLE
            mAdapter = CityListAdapter(listNotes)

            this.checkSorting()

            setupRecyclerView()
        }
    }


    // Comparator for Ascending Order By Name
    private var stringAscComparator: Comparator<CityListBean> = object : Comparator<CityListBean> {
        override
        fun compare(app1: CityListBean, app2: CityListBean): Int {

            return app1.CityName.compareTo(app2.CityName,true)
        }
    }

    // Comparator for Date Desending Order By Date
    private var dateAscComparator: Comparator<CityListBean> = object : Comparator<CityListBean> {
        override
        fun compare(app2: CityListBean, app1: CityListBean): Int {

            return app1.Date.compareTo(app2.Date,true)
        }
    }

    override fun onClick(v: View) = when (v) {
        iv_add_city -> {

            if(deletedList.size!=0){
                val selectionArgs = arrayOf(deletedList[0])
                dbManager?.delete("CityName=?", selectionArgs)
                deletedList.clear()
            }

            val fm = supportFragmentManager
            val addCityDialog = AddCityDialog()
            addCityDialog.isCancelable = false
            addCityDialog.show(fm, "fragment_add_city")

        }
        iv_sortlist ->{
            openActionSheet()
        }
        else -> { // Note the block
            //print("")
        }
    }

    private fun openActionSheet(){
        ActionSheet.createBuilder(this, supportFragmentManager)
                .setCancelButtonTitle(getString(R.string.app_cancel))
                .setOtherButtonTitles(getString(R.string.app_sort_with_name), getString(R.string.app_sort_with_population), getString(R.string.app_sort_with_date))
                .setCancelableOnTouchOutside(true)
                .setListener(this).show()
    }


    private fun checkSorting() = when {
        sortingFlag == "ByDate" -> listNotes.sortWith(dateAscComparator)
        sortingFlag == "ByName" -> listNotes.sortWith(stringAscComparator)
        else -> // public fun <T> Array<out T>.sortWith(comparator: Comparator<in T>): Unit
            // -> Sorts the array in-place according to the order specified by the given [comparator].
            listNotes.sortWith(object: Comparator<CityListBean>{
                override fun compare(p1: CityListBean, p2: CityListBean): Int = when {
                    p1.CityPopulation > p2.CityPopulation -> 1
                    p1.CityPopulation == p2.CityPopulation -> 0
                    else -> -1
                }
            })
    }


    ///////////////////////////////// Action Sheet For Sorting List Click listener ///////////////////////////////////
    override fun onOtherButtonClick(actionSheet: ActionSheet, index: Int) {

       // Toast.makeText(this,""+index,Toast.LENGTH_LONG).show()
        when (index) {
            0 -> {
                sortingFlag= "ByName"
                checkSorting()
                recyclerView.adapter = mAdapter
                mAdapter?.notifyDataSetChanged()
            }
            1 -> {
                sortingFlag = "ByPopulation"
                checkSorting()

                recyclerView.adapter = mAdapter
                mAdapter?.notifyDataSetChanged()
            }
            2 -> {
                sortingFlag= "ByDate"
                checkSorting()
                recyclerView.adapter = mAdapter
                mAdapter?.notifyDataSetChanged()
            }
            else -> {

            }
        }
    }

    ///////////////////////////////// Action Sheet For Dismiss Action Sheet  ///////////////////////////////////
    override fun onDismiss(actionSheet: ActionSheet, isCancle: Boolean) {
        //Toast.makeText(getApplicationContext(), "dismissed isCancle = " + isCancle, Toast.LENGTH_SHORT).show();
    }


    ///////////////////////////////// Interface events trrigger from Fragment Dialog  ///////////////////////////////////
    override fun onDialogPositiveClick(dialog: DialogFragment) {
        //Toast.makeText(this, "click Positive", Toast.LENGTH_LONG).show()


        loadQueryAll()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        Toast.makeText(this, "click Cancel", Toast.LENGTH_LONG).show()
    }

    private fun removeDeletedItem(){
        if(deletedList.size!=0){
            val selectionArgs = arrayOf(deletedList[0])
            dbManager?.delete("CityName=?", selectionArgs)
            deletedList.clear()

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        removeDeletedItem()
    }

    override fun onStop() {
        super.onStop()
        removeDeletedItem()
        Log.v("Stop","Stop")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.v("Destroy","Destroy")
   }

}
