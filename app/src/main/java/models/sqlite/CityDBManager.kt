package models.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


/**
 * Created by Aashish Sharma on 28/5/18
 */
class CityDBManager(context: Context) {

    private val dbName = "CITYDATA"
    private val dbTable = "CityList"
    private val cityId = "cityId"
    private val cityName = "CityName"
    private val cityPopulation = "CityPopulation"
    private val cityState = "State"
    private val dateTime  = "DateTime"
    private val dbVersion = 1

    private val CLITYLISTTABLE = "CREATE TABLE IF NOT EXISTS " + dbTable + " (" + cityId + " INTEGER , " + cityName + " TEXT PRIMARY KEY , " + cityPopulation + " TEXT, " + cityState + " TEXT, "+ dateTime +" DATETIME);"
    private var db: SQLiteDatabase? = null

    fun insert(values: ContentValues): Long = db!!.insert(dbTable, "", values)

    fun queryAll(): Cursor {

        return db!!.rawQuery("select * from $dbTable", null)
    }

    fun delete(selection: String, selectionArgs: Array<String>): Int {
        return db!!.delete(dbTable, selection, selectionArgs)
    }

    inner class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, dbName, null, dbVersion) {

        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(CLITYLISTTABLE)
           // Toast.makeText(this.context, " database is created", Toast.LENGTH_LONG).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("Drop table IF EXISTS $dbTable")
        }
    }

    init {
        val dbHelper = DatabaseHelper(context)
        db = dbHelper.writableDatabase
    }
}