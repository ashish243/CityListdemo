package sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import javax.xml.datatype.DatatypeConstants.DATETIME

/**
 * Created by Aashish Sharma on 28/5/18
 */
class CityDBManager {

    private val dbName = "CITYDATA"
    private val dbTable = "CityList"
    private val cityId = "cityId"
    private val cityName = "CityName"
    private val cityPopulation = "CityPopulation"
    private val cityState = "State"
    private val DateTime  = "DateTime"
    private val dbVersion = 1

    private val CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " + dbTable + " (" + cityId + " INTEGER ," + cityName + " TEXT PRIMARY KEY, " + cityPopulation + " TEXT, " + cityState + " TEXT, "+ DateTime +" DATETIME);"
    private var db: SQLiteDatabase? = null

    constructor(context: Context) {
        var dbHelper = DatabaseHelper(context)
        db = dbHelper.writableDatabase
    }

    fun insert(values: ContentValues): Long {

        val ID = db!!.insert(dbTable, "", values)
        return ID
    }

    fun queryAll(): Cursor {

        return db!!.rawQuery("select * from " + dbTable, null)
    }

    fun delete(selection: String, selectionArgs: Array<String>): Int {

        val count = db!!.delete(dbTable, selection, selectionArgs)
        return count
    }



    inner class DatabaseHelper : SQLiteOpenHelper {

        var context: Context? = null

        constructor(context: Context) : super(context, dbName, null, dbVersion) {
            this.context = context
        }

        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(CREATE_TABLE_SQL)
           // Toast.makeText(this.context, " database is created", Toast.LENGTH_LONG).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("Drop table IF EXISTS " + dbTable)
        }
    }
}