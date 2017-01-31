package com.spp.banu.aluradmi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.spp.banu.aluradmi.dbSchema.AlurDbSchema;
import com.spp.banu.aluradmi.dbSchema.BerkasDbSchema;
import com.spp.banu.aluradmi.dbSchema.GedungDbSchema;
import com.spp.banu.aluradmi.dbSchema.JurusanDbSchema;
import com.spp.banu.aluradmi.dbSchema.KategoriDbSchema;
import com.spp.banu.aluradmi.dbSchema.KeteranganDbSchema;
import com.spp.banu.aluradmi.dbSchema.LantaiDbSchema;
import com.spp.banu.aluradmi.dbSchema.LokasiDbSchema;
import com.spp.banu.aluradmi.dbSchema.RuangDbSchema;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by banu on 16/11/16.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "aluradmi.sqlite";
    private static final String TAG = "com.spp.aluradmi.databaseHelper";
    private static DatabaseHelper helper;
    private static final String TABLE_JURUSAN = "CREATE TABLE "+ JurusanDbSchema.JurusanTable.TABLE_NAME +
            " (" + JurusanDbSchema.JurusanTable.Kolom.ID_JURUSAN + " INTEGER PRIMARY KEY NOT NULL, " +
            JurusanDbSchema.JurusanTable.Kolom.NAMA + " TEXT NOT NULL, " +
             JurusanDbSchema.JurusanTable.Kolom.STATUS+" INTEGER(1) NOT NULL DEFAULT 0, " +
             JurusanDbSchema.JurusanTable.Kolom.TIMESTAMP + " TEXT NOT NULL)";

    private static final String TABLE_KATEGORI = "CREATE TABLE " + KategoriDbSchema.KategoriTable.TABLE_NAME +" (" +
            "" +KategoriDbSchema.KategoriTable.Kolom.ID_KATEGORI+" INTEGER PRIMARY KEY NOT NULL, " +
            " " + KategoriDbSchema.KategoriTable.Kolom.NAMA+" TEXT NOT NULL, " + KategoriDbSchema.KategoriTable.Kolom.TIMESTAMP+
            " TEXT NOT NULL )";
    private static final String TABLE_LOKASI = "CREATE TABLE "+ LokasiDbSchema.LokasiTable.TABLE_NAME+" (" +
            ""+LokasiDbSchema.LokasiTable.Kolom.ID_LOKASI+" INTEGER PRIMARY KEY NOT NULL, " +
            ""+LokasiDbSchema.LokasiTable.Kolom.NAMA+" TEXT NOT NULL, " +
            ""+LokasiDbSchema.LokasiTable.Kolom.TIMESTAMP+" TEXT NOT NULL , " +
            ""+LokasiDbSchema.LokasiTable.Kolom.LATTITUDE+" REAL, " +
            ""+LokasiDbSchema.LokasiTable.Kolom.LONGITUDE+" REAL)";
    private static final String TABLE_ALUR = "CREATE TABLE "+ AlurDbSchema.AlurTable.TABLE_NAME+" (" +
            ""+AlurDbSchema.AlurTable.Kolom.ID_ALUR+" INTEGER NOT NULL PRIMARY KEY, " +
            ""+AlurDbSchema.AlurTable.Kolom.NAMA+" TEXT NOT NULL, " +
            ""+AlurDbSchema.AlurTable.Kolom.URUT+" INTEGER(3) NOT NULL, " +
            ""+ AlurDbSchema.AlurTable.Kolom.TIMESTAMP+" TEXT NOT NULL , " +
            ""+ AlurDbSchema.AlurTable.Kolom.ID_JURUSAN+" INTEGER NOT NULL, "+
            ""+AlurDbSchema.AlurTable.Kolom.ID_KATEGORI +" INTEGER NOT NULL, FOREIGN KEY (id_kategori) REFERENCES " + KategoriDbSchema.KategoriTable.TABLE_NAME+
            " (id_kategori), FOREIGN KEY (id_jurusan) REFERENCES "+JurusanDbSchema.JurusanTable.TABLE_NAME+ " (id_jurusan))";
    private static final String TABLE_KETERANGAN = "CREATE TABLE "+ KeteranganDbSchema.KeteranganTable.TABLE_NAME+" (" +
            ""+ KeteranganDbSchema.KeteranganTable.Kolom.ID_KETERANGAN+" INTEGER PRIMARY KEY NOT NULL," +
            ""+ KeteranganDbSchema.KeteranganTable.Kolom.NAMA+" TEXT NOT NULL, " +
            ""+ KeteranganDbSchema.KeteranganTable.Kolom.KETERANGAN+" TEXT, " +
            ""+ KeteranganDbSchema.KeteranganTable.Kolom.STATUS+" INTEGER(1) NOT NULL DEFAULT 0, " +
            ""+ KeteranganDbSchema.KeteranganTable.Kolom.TIMESTAMP+" TEXT NOT NULL ," +
            ""+ KeteranganDbSchema.KeteranganTable.Kolom.ID_ALUR+" INTEGER NOT NULL, " +
            ""+ KeteranganDbSchema.KeteranganTable.Kolom.ID_RUANG+" INTEGER NOT NULL," +
            ""+ KeteranganDbSchema.KeteranganTable.Kolom.URUT + " INTEGER(3) NOT NULL," +
            "FOREIGN KEY ("+ KeteranganDbSchema.KeteranganTable.Kolom.ID_ALUR+") REFERENCES "+ AlurDbSchema.AlurTable.TABLE_NAME +
            " (id_alur), FOREIGN KEY (id_ruang) REFERENCES " + RuangDbSchema.RuangTable.TABLE_NAME+
            " (id_ruang))";
    private static final String TABLE_BERKAS = "CREATE TABLE "+ BerkasDbSchema.BerkasTable.TABLE_NAME+" (" +
            ""+ BerkasDbSchema.BerkasTable.Kolom.ID_BERKAS+" INTEGER NOT NULL PRIMARY KEY," +
            " "+ BerkasDbSchema.BerkasTable.Kolom.NAMA+" TEXT NOT NULL, " +
            ""+ BerkasDbSchema.BerkasTable.Kolom.TIMESTAMP+" TEXT NOT NULL , " +
            ""+ BerkasDbSchema.BerkasTable.Kolom.ID_KETERANGAN+" INTEGER NOT NULL, FOREIGN KEY (id_keterangan) REFERENCES "+ KeteranganDbSchema.KeteranganTable.TABLE_NAME+" (id_keterangan))";
    private static final String TABLE_GEDUNG = "CREATE TABLE " + GedungDbSchema.GedungTable.TABLE_NAME +
            "( " + GedungDbSchema.GedungTable.Kolom.ID_GEDUNG + " INTEGER NOT NULL PRIMARY KEY ," +
            GedungDbSchema.GedungTable.Kolom.NAMA + " TEXT NOT NULL , " +
            GedungDbSchema.GedungTable.Kolom.LATITUDE + " REAL, " +
            GedungDbSchema.GedungTable.Kolom.LONGITUDE + " REAL, " +
            GedungDbSchema.GedungTable.Kolom.TIMESTAMP + " TEXT NOT NULL )";
    private static final String TABLE_LANTAI = "CREATE TABLE " + LantaiDbSchema.LantaiTable.TABLE_NAME +
            "( " + LantaiDbSchema.LantaiTable.Kolom.ID_LANTAI + " INTEGER NOT NULL PRIMARY KEY ," +
            LantaiDbSchema.LantaiTable.Kolom.NAMA + " TEXT NOT NULL ," +
            LantaiDbSchema.LantaiTable.Kolom.LINK + " TEXT , " +
            LantaiDbSchema.LantaiTable.Kolom.THUMBNAIL + " TEXT , " +
            LantaiDbSchema.LantaiTable.Kolom.TIMESTAMP + " TEXT NOT NULL, " +
            LantaiDbSchema.LantaiTable.Kolom.ID_GEDUNG + " INTEGER NOT NULL, FOREIGN KEY (" +
            LantaiDbSchema.LantaiTable.Kolom.ID_GEDUNG + ") REFERENCES " +
            GedungDbSchema.GedungTable.TABLE_NAME + "(" + GedungDbSchema.GedungTable.Kolom.ID_GEDUNG + "))";

    private static final String TABLE_RUANG = "CREATE TABLE " + RuangDbSchema.RuangTable.TABLE_NAME +
            "( " + RuangDbSchema.RuangTable.Kolom.ID_RUANG + " INTEGER NOT NULL PRIMARY KEY , " +
            RuangDbSchema.RuangTable.Kolom.NAMA + " TEXT NOT NULL, " +
            RuangDbSchema.RuangTable.Kolom.TIMESTAMP + " TEXT NOT NULL, " +
            RuangDbSchema.RuangTable.Kolom.ID_LANTAI + " INTEGER NOT NULL , FOREIGN KEY (" +
            RuangDbSchema.RuangTable.Kolom.ID_LANTAI + ") REFERENCES " +
            LantaiDbSchema.LantaiTable.TABLE_NAME + "(" + LantaiDbSchema.LantaiTable.Kolom.ID_LANTAI +"))";
    private boolean walModeEnabled;
    private DatabaseHelper(Context context, boolean gwalMode) {
        super(context, DATABASE_NAME, null , 1);
        this.walModeEnabled = gwalMode;

        if (walModeEnabled){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                setWriteAheadLoggingEnabled(true);
                Log.w(TAG, "WAL is supported");
            }
        }
    }
    public static synchronized DatabaseHelper getInstance(Context context, boolean gwalMode){
        if (helper == null){
            helper = new DatabaseHelper(context, gwalMode);
        }
        return helper;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_JURUSAN);
        sqLiteDatabase.execSQL(TABLE_KATEGORI);
        //sqLiteDatabase.execSQL(TABLE_LOKASI);
        sqLiteDatabase.execSQL(TABLE_GEDUNG);
        sqLiteDatabase.execSQL(TABLE_LANTAI);
        sqLiteDatabase.execSQL(TABLE_RUANG);
        sqLiteDatabase.execSQL(TABLE_ALUR);
        sqLiteDatabase.execSQL(TABLE_KETERANGAN);
        sqLiteDatabase.execSQL(TABLE_BERKAS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS jurusan");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS kategori");
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS lokasi");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS gedung");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS lantai");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS ruang");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS alur");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS keterangan");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS berkas");
        this.onCreate(sqLiteDatabase);
    }

    private ContentValues contentValues(JSONObject jsonObject){
        ContentValues values = new ContentValues();
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()){
            try {
                String theKey = (String)keys.next();
                String theValue = jsonObject.getString(theKey);
                Log.d("key TAG", "KEYS: " + theKey);
                Log.d("value TAG", "VALUE: " + theValue);
                values.put(theKey,theValue);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return values;
    }
    public void insertData(String table, JSONArray jsonArray){
        SQLiteDatabase db = getWritableDatabase();
        if (jsonArray.length() != 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.getString("id_"+table);
                    boolean exist = this.isDataExist(table,id);
                    if (!exist){
                        ContentValues values = contentValues(jsonObject);
                        db.insert(table, null, values);
                    }
                    else{
                        Log.i(TAG, "insertData: data " + table + " " + id + " sudah ada");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateData(String table, String field, String field_id, JSONObject jsonObject){
        JSONObject object = null;
        try {
            object = jsonObject.getJSONObject(table);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ContentValues values = contentValues(object);
        SQLiteDatabase db = getWritableDatabase();
        db.update(table, values, field + " = ?", new String[] {field_id});
    }

    public void deleteData(String table, String field, String field_id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(table,field + "= ?", new String[]{field_id});
        Log.i(TAG, "deleteData: " + table + " " + field + " " + field_id);
    }
    private boolean isDataExist(String table, String id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + table + " WHERE id_" +table + " = ?",new String[]{id} );
        if (cursor.getCount() > 0){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        // backward compatibility hack to support WAL on pre-jelly-bean devices
        if(walModeEnabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB &&
                    Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                db.enableWriteAheadLogging();
            } else {
                Log.w(TAG, "WAL is not supported on API levels below 11.");
            }
        }
    }

}
