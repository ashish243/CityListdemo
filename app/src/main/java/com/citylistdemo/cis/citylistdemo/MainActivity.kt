package com.citylistdemo.cis.citylistdemo

import BeanClasses.CityListBean
import adapter.CityListAdapter
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
import android.widget.Toast
import citydataInterface.CityDataInterface
import cityfragment.AddCityDialog
import com.baoyz.actionsheet.ActionSheet
import kotlinx.android.synthetic.main.activity_main.*
import sqlite.CityDBManager
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : FragmentActivity(), View.OnClickListener, CityDataInterface, ActionSheet.ActionSheetListener {

    var mAdapter: CityListAdapter? = null
    var listNotes = ArrayList<CityListBean>()
    var deletedList = ArrayList<String>()
    var dbManager: CityDBManager? = null
    var sortingFlag = "ByDate"
    lateinit var snackbar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dbManager = CityDBManager(this)
        Initialize()
        loadQueryAll()

    }

    fun Initialize() {
        iv_add_city.setOnClickListener(this)
        iv_sortlist.setOnClickListener(this)
    }

   // Function for inhitialize RecyclerView
    private fun setupRecyclerView() {
        var swipeController: SwipeController? = null

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = mAdapter

       // List swipe action handle here
        swipeController = SwipeController(object : SwipeControllerActions() {
            override
            fun onRightClicked(position: Int) {


                if(deletedList.size==0){
                    deletedList.add(listNotes[position].CityName.toString())
                  }else{
                    val selectionArgs = arrayOf(deletedList[0])
                    dbManager?.delete("CityName=?", selectionArgs)
                    deletedList.clear()
                    deletedList.add(listNotes[position].CityName.toString())
                }

               /*val selectionArgs = arrayOf(listNotes[position].CityName.toString())
                dbManager?.delete("CityName=?", selectionArgs)*/


                listNotes?.removeAt(position)
                mAdapter?.notifyItemRemoved(position)
                mAdapter?.notifyItemRangeChanged(position, mAdapter!!.getItemCount())


                if (listNotes.size == 0) {
                    tv_norecord_found.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                    iv_sortlist.visibility = View.GONE
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
                swipeController?.onDraw(c)
            }
        })
    }

    // Function to get city list from database
    fun loadQueryAll() {

        val cursor = dbManager?.queryAll()

        listNotes.clear()
        if (cursor!!.moveToFirst()) {

            do {
                val CityName = cursor.getString(cursor.getColumnIndex("CityName"))
                val CityPopulation = cursor.getString(cursor.getColumnIndex("CityPopulation"))
                val Satate = cursor.getString(cursor.getColumnIndex("State"))
                val DateTime = cursor.getString(cursor.getColumnIndex("DateTime"))
                listNotes.add(CityListBean(CityName, CityPopulation.toInt(), Satate, DateTime))

            } while (cursor.moveToNext())
        }

        //Log.v("position", "listNotes.size" + listNotes.size)

        if (listNotes.size != 0) {

            tv_norecord_found.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            iv_sortlist.visibility = View.VISIBLE
            mAdapter = CityListAdapter(this, listNotes)

            CheckSorting()

            setupRecyclerView()
        }
    }


    // Comparator for Ascending Order By Name
    var StringAscComparator: Comparator<CityListBean> = object : Comparator<CityListBean> {
        override
        fun compare(app1: CityListBean, app2: CityListBean): Int {

            return app1.let { it.CityName.compareTo(app2.let { it.CityName },true) }
        }
    }

    // Comparator for Date Desending Order By Date
    var DateAscComparator: Comparator<CityListBean> = object : Comparator<CityListBean> {
        override
        fun compare(app2: CityListBean, app1: CityListBean): Int {

            return app1.let { it.Date.compareTo(app2.let { it.Date },true) }
        }
    }

    override fun onClick(v: View) {


        when (v) {
            iv_add_city -> {

                if(deletedList.size!=0){
                    val selectionArgs = arrayOf(deletedList[0])
                    dbManager?.delete("CityName=?", selectionArgs)
                    deletedList.clear()
                }

                val fm = supportFragmentManager
                val addCityDialog = AddCityDialog()
                addCityDialog.setCancelable(false)
                addCityDialog.show(fm, "fragment_add_city")

            }
            iv_sortlist ->{
                openActionSheet()
            }
            else -> { // Note the block
                //print("")
            }
        }
    }

    fun openActionSheet(){
        ActionSheet.createBuilder(this, getSupportFragmentManager())
                .setCancelButtonTitle(getString(R.string.app_cancel))
                .setOtherButtonTitles(getString(R.string.app_sort_with_name), getString(R.string.app_sort_with_population), getString(R.string.app_sort_with_date))
                .setCancelableOnTouchOutside(true)
                .setListener(this).show()
    }


    fun CheckSorting(){
        if(sortingFlag.equals("ByDate")){
            listNotes.sortWith(DateAscComparator)
        }else if(sortingFlag.equals("ByName")){
            listNotes.sortWith(StringAscComparator)
        }else{
            // public fun <T> Array<out T>.sortWith(comparator: Comparator<in T>): Unit
            // -> Sorts the array in-place according to the order specified by the given [comparator].
            listNotes.sortWith(object: Comparator<CityListBean>{
                override fun compare(p1: CityListBean, p2: CityListBean): Int = when {
                    p1.CityPopulation > p2.CityPopulation -> 1
                    p1.CityPopulation == p2.CityPopulation -> 0
                    else -> -1
                }
            })
        }
    }


    ///////////////////////////////// Action Sheet For Sorting List Click listener ///////////////////////////////////
    override fun onOtherButtonClick(actionSheet: ActionSheet, index: Int) {

       // Toast.makeText(this,""+index,Toast.LENGTH_LONG).show()
        if(index==0){
            sortingFlag= "ByName"
            CheckSorting()
            recyclerView.adapter = mAdapter
            mAdapter?.notifyDataSetChanged()
        }else if(index==1){
            sortingFlag = "ByPopulation"
            CheckSorting()

            recyclerView.adapter = mAdapter
            mAdapter?.notifyDataSetChanged()
        }else if(index==2){
            sortingFlag= "ByDate"
            CheckSorting()
            recyclerView.adapter = mAdapter
            mAdapter?.notifyDataSetChanged()
        }else{

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

    fun removeDeletedItem(){
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
        Log.v("Stop","Stop");
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.v("Destroy","Destroy");
   }

}
