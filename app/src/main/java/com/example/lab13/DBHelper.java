package com.example.lab13;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DBHelper {
    private static final String DATABASE_NAME = "StudentesDB.sqlite";
    private static final String DB_PATH_SUFFIX = "/databases/";
    private SQLiteDatabase db = null;
    private Context context;

    public DBHelper(Context context) {
        this.context = context;
        processSQLite();
    }

    private void processSQLite() {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                // Check if file exists in assets
                boolean fileExistsInAssets = false;
                try {
                    String[] assets = context.getAssets().list("");
                    if (assets != null) {
                        for (String asset : assets) {
                            if (DATABASE_NAME.equals(asset)) {
                                fileExistsInAssets = true;
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (fileExistsInAssets) {
                    CopyDatabaseFromAsset();
                } else {
                    // Create db and populate with demo data
                    db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
                    db.execSQL("CREATE TABLE IF NOT EXISTS Student (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, address TEXT, gender INTEGER)");
                    
                    // Insert default data
                    db.execSQL("INSERT INTO Student(name, address, gender) VALUES('Hà Dư Tuấn', 'TDTU', 1)");
                    db.execSQL("INSERT INTO Student(name, address, gender) VALUES('Tom', 'Hutech', 1)");
                    db.execSQL("INSERT INTO Student(name, address, gender) VALUES('Jenny', 'SuPham', 0)");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void CopyDatabaseFromAsset() {
        try {
            InputStream myInput = context.getAssets().open(DATABASE_NAME);
            String outFileName = getPathDatabaseSystem();
            File f = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists()) {
                f.mkdir();
            }
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getPathDatabaseSystem() {
        return context.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }

    public ArrayList<Student> getAllStudent() {
        ArrayList<Student> list = new ArrayList<>();
        db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM Student", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String address = cursor.getString(2);
                int gender = cursor.getInt(3);
                list.add(new Student(id, name, address, gender));
            }
            cursor.close();
        }
        db.close();
        return list;
    }

    public void insertStudent(Student student) {
        db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("name", student.getName());
        values.put("address", student.getAddress());
        values.put("gender", student.getGender());
        long newId = db.insert("Student", null, values);
        student.setId((int) newId);
        db.close();
    }

    public void updateStudent(Student student) {
        db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("name", student.getName());
        values.put("address", student.getAddress());
        values.put("gender", student.getGender());
        db.update("Student", values, "id = ?", new String[]{String.valueOf(student.getId())});
        db.close();
    }

    public void deleteStudent(Student student) {
        db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        db.delete("Student", "id = ?", new String[]{String.valueOf(student.getId())});
        db.close();
    }
}
