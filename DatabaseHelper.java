package com.example.user.reminder;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// Declares a public class named DatabaseHelper that extends SQLiteOpenHelper.
// This class will handle all the low-level database operations.
public class DatabaseHelper extends SQLiteOpenHelper {

    // Defines a constant String TAG for logging, making it easier to filter logs in Logcat.
    private static final String TAG = "DatabaseHelper";
    // Defines a constant String for the database table name.
    private static final String TABLE_NAME = "people_table";
    // Defines a constant String for the first column name, which is "ID".
    private static final String COL1 = "ID";
    // Defines a constant String for the second column name, which is "name".
    private static final String COL2 = "name";

    // Constructor for the DatabaseHelper class.
    public DatabaseHelper(Context context) {
        /* 
        Calls the superclass (SQLiteOpenHelper) constructor.
        context: The application context.
        TABLE_NAME ("people_table"): This is used as the database file name.
        null: A CursorFactory, null for the default.
        1: The version number of the database. If this number increases, onUpgrade will be called.
        */
        super(context, TABLE_NAME, null, 1);
    }

    @Override // Annotation indicating that this method overrides a method from the SQLiteOpenHelper superclass.
    // This method is called when the database is created for the very first time.
    public void onCreate(SQLiteDatabase db) {
        // Defines the SQL string to create the "people_table".
        // It has an "ID" column (integer, primary key, auto-incrementing) and a "name" column (text).
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 +" TEXT)";
        // Executes the SQL statement to create the table.
        db.execSQL(createTable);
    }

    @Override 
    /* 
    Annotation indicating that this method overrides a method from the SQLiteOpenHelper superclass.
    This method is called when the database needs to be upgraded,
    which happens if you increment the database version number in the constructor.
    db: The database.
    i (oldVersion): The old database version.
    i1 (newVersion): The new database version.
    */
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Executes an SQL statement to drop the "people_table" if it already exists.
        // This is a simple upgrade strategy that deletes old data. For production apps, data migration is often needed.
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        // Calls the onCreate method to recreate the table with the new schema.
        onCreate(db);
    }

    // Public method to add a new item (name) to the database.
    // It returns true if the data was inserted successfully, false otherwise.
    public boolean addData(String item) {
        // Gets a writable instance of the database.
        SQLiteDatabase db = this.getWritableDatabase();

        // Creates a new ContentValues object to store column values.
        ContentValues contentValues = new ContentValues();

        // Puts the item (name) into ContentValues, associated with COL2 ("name" column).
        contentValues.put(COL2, item);

        // Logs the action of adding data, including the item and table name.
        Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME);

        // Inserts the ContentValues into the specified table.
        // db.insert() returns the row ID of the newly inserted row, or -1 if an error occurred.
        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns all the data from database
     * @return
     */

    // Public method to retrieve all data (all rows and columns) from the "people_table".
    public Cursor getData(){
        // Gets a writable instance of the database. For read-only operations, getReadableDatabase() is semantically more correct
        // but getWritableDatabase() will also work.
        SQLiteDatabase db = this.getWritableDatabase();
        // Defines the SQL query to select all columns (*) from the "people_table".
        String query = "SELECT * FROM " + TABLE_NAME;

        /*
        Executes the raw SQL query.
        null is for selection arguments, which are not used in this simple query.
        Returns a Cursor object, which points to the result set of the query.
        */
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Returns only the ID that matches the name passed in
     * @param name
     * @return
     */

    // Public method to get the ID for a given name.
    public Cursor getItemID(String name){
        // Gets a writable instance of the database.
        SQLiteDatabase db = this.getWritableDatabase();

        // Defines the SQL query to select the ID (COL1) from "people_table"
        // where the name (COL2) matches the provided 'name'.
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = '" + name + "'";
        // Executes the raw SQL query.
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Updates the name field
     * @param newName
     * @param id
     * @param oldName
     */

    // Public method to update an existing record's name.
    public void updateName(String newName, int id, String oldName){
        // Gets a writable instance of the database.
        SQLiteDatabase db = this.getWritableDatabase();
        // Defines the SQL UPDATE statement.
        // It sets COL2 (name) to 'newName' where COL1 (ID) matches 'id' AND COL2 (name) matches 'oldName'.
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + newName + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + oldName + "'";

        // Logs the constructed SQL query for debugging.
        Log.d(TAG, "updateName: query: " + query);

        // Logs the action of updating the name.
        Log.d(TAG, "updateName: Setting name to " + newName);

        // Executes the SQL UPDATE statement.
        db.execSQL(query);
    }

    /**
     * Delete from database
     * @param id
     * @param name
     */

    // Public method to delete a record from the database.
    public void deleteName(int id, String name){
        // Gets a writable instance of the database.
        SQLiteDatabase db = this.getWritableDatabase();
        // Defines the SQL DELETE statement.
        // It deletes a row from "people_table"
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + name + "'";

        // Logs the constructed SQL query for debugging.
        Log.d(TAG, "deleteName: query: " + query);

        // Logs the action of deleting the record.
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        
        // Executes the SQL DELETE statement.
        db.execSQL(query);
    }

}
