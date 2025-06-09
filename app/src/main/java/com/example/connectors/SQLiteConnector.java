package com.example.connectors;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SQLiteConnector {

    String DATABASE_NAME="SalesDatabase.sqlite";
    private static final String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database=null;
    Activity context;

    public SQLiteConnector() {
    }

    public SQLiteConnector(Activity context) {
        this.context = context;
        copyDatabaseFromAssets(); // Ensure database is copied before opening
        database = context.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
    }

    private void copyDatabaseFromAssets() {
        File dbFile = context.getDatabasePath(DATABASE_NAME);

        if (!dbFile.exists()) {
            try {
                // Use getParentFile() to get the directory where the database file should be.
                File dbDir = dbFile.getParentFile();
                if (dbDir != null && !dbDir.exists()) {
                    // Use mkdirs() to create parent directories if necessary, and check the result.
                    if (!dbDir.mkdirs()) {
                        Log.e("SQLiteConnector", "Failed to create database directory: " + dbDir.getAbsolutePath());
                        // If directory creation fails, throw an IOException to indicate a problem.
                        throw new IOException("Failed to create database directory: " + dbDir.getAbsolutePath());
                    }
                }

                InputStream inputStream = context.getAssets().open(DATABASE_NAME);
                OutputStream outputStream = new FileOutputStream(dbFile);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();
                Log.i("SQLiteConnector", "Database copied from assets.");
            } catch (IOException e) {
                Log.e("SQLiteConnector", "Error copying database from assets", e);
                // Handle the exception, perhaps throw a runtime exception or show an error to the user
                throw new RuntimeException("Error copying database from assets", e);
            }
        } else {
            Log.i("SQLiteConnector", "Database already exists.");
        }
    }

    public Activity getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }

    public SQLiteDatabase openDatabase() {
        // Ensure database is copied before attempting to open, in case openDatabase is called directly
        // or on an instance created with the default constructor and then setContext.
        if (this.context != null && this.database == null) { // Check if context is set and db not already opened
            copyDatabaseFromAssets();
            database = this.context.openOrCreateDatabase(DATABASE_NAME,
                    MODE_PRIVATE, null); // Use MODE_PRIVATE from context
        } else if (this.context != null && this.database != null && !this.database.isOpen()) {
            // If database object exists but is closed, reopen it.
            copyDatabaseFromAssets(); // Ensure it's still there or copied if deleted somehow
            database = this.context.openOrCreateDatabase(DATABASE_NAME,
                    MODE_PRIVATE, null);
        } else if (this.context == null) {
            Log.e("SQLiteConnector", "Context is null, cannot open database.");
            // Optionally throw an exception or handle error appropriately
            return null;
        }
        return database;
    }
}

