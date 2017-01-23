package com.akisoft.slidingmenu;


import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    // Database Version
	private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "Name of database";
 
    // Labels table name
    private static final String TABLE_NAME = "name of table";
 
    // Labels Table Columns names
    private static final String KEY_UID = "uid";
    private static final String KEY_PASS="pass";
    private static final String KEY_NAME = "userid";
    private static final String KEY_STAT="stat";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    	// Category table create query
    	String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_NAME + "("
        		+ KEY_UID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"+KEY_PASS+" TEXT,"+KEY_STAT+" TEXT)";
    	db.execSQL(CREATE_LOGIN_TABLE);
    	//String CREATE_Startup_Table="CREATE TABLE startup(COUNT INTEGER)";
    	//db.execSQL(CREATE_Startup_Table);
    	
    	
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
 
        // Create tables again
        onCreate(db);
    }
    
    /**
     * Inserting new entries
     * */
    public void insertUser(String uid,String userid,String pass,String stat){
    	SQLiteDatabase db = this.getWritableDatabase();
    	
    	ContentValues values = new ContentValues();
    	values.put(KEY_UID, uid);
    	values.put(KEY_NAME, userid);
    	values.put(KEY_PASS, pass);
    	values.put(KEY_STAT, stat);
    	 
    	// Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }
    
    public void insertCount()
    {
    	SQLiteDatabase db=this.getWritableDatabase();
    	ContentValues values=new ContentValues();
    	values.put("COUNT", 1);
    	db.insert("startup", null, values);
    	db.close();
    }
    
    /**
     * Getting all labels
     * returns list of labels
     * */
    
    //public String checkLogin(String username,String passw)
    public String checkLogin()
    {
    	
    	//String query="SELECT * FROM "+ TABLE_NAME +" WHERE "+KEY_NAME+"="+username+" AND "+KEY_PASS+"="+passw;
    	SQLiteDatabase db = this.getWritableDatabase();
        //Cursor cursor = db.rawQuery(query, null);
       // Cursor cursor=db.query(TABLE_NAME, new String[] {KEY_ID,KEY_NAME,KEY_PASS},KEY_NAME+"=? AND "+KEY_PASS+"=?", new String[]{String.valueOf(username),String.valueOf(passw)},null,null,null,null);
    	Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
    	if(cursor.getCount()>0)
        	return "Success";
        else
        	return "Unsuccessful";
    }
    public String checkActivation()
    {
    	SQLiteDatabase db=this.getWritableDatabase();
    	Cursor cursor=db.rawQuery("SELECT stat FROM "+TABLE_NAME, null);
    	cursor.moveToFirst();
    	if(cursor.getString(0).equals("activated"))
    		return "Success";
    	else
    		return "Unsuccessful";
    }
    public void Activate(String uid)
    {
    	SQLiteDatabase db=this.getWritableDatabase();
    	ContentValues value=new ContentValues();
    	value.put(KEY_STAT, "activated");
    	//db.rawQuery("UPDATE "+TABLE_NAME+" SET stat='activated' WHERE uid="+uid,null);
    	db.update(TABLE_NAME, value, "uid="+uid, null);
    	db.close();
    }
    public List<String> getAllLabels(){
    	List<String> labels = new ArrayList<String>();
    	
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
     
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        
        // closing connection
        cursor.close();
        db.close();
    	
    	// returning lables
    	return labels;
    }
    
   /* public int checkStartup(){
    	// Select All Query
        String selectQuery = "SELECT  * FROM startup";
     
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if(cursor.getCount()>0)
        	return 1;
        else return 0;
        
        // closing connection
            	
    	// returning labels
    }*/
}
