package flynn.pro.flatears;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;



class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "records.db";
    private static final int DATABASE_VERSION = 3;
    //TABLE RECS
    public static final String RECORDINGS_TABLE = "RECS";
    public static final String KEY_ID = "_id";
    public static final String KEY_LINTIME = "linuxtime";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_CALLTYPE = "calltype";
    public static final String KEY_ANUM = "calling";
    public static final String KEY_BNUM = "called";
    public static final String KEY_SOURCE = "source";
    public static final String KEY_FORMAT = "format";
    public static final String KEY_DEVID = "deviceID";
    public static final String KEY_ANDROIDID = "androidID";
    public static final String KEY_LINK = "link";
    public static final String KEY_STATUS = "status";
    public static final String KEY_RESERVED = "reserved";


    //TABLE LOG
    private static final String LOG_TABLE = "LOGS";
    public static final String KEY_LOG_ID = "_id";
    public static final String KEY_LOG_TIME = "time";
    public static final String KEY_LOG_TAG = "tag";
    public static final String KEY_LOG_LEVEL = "level";
    public static final String KEY_LOG_MESSAGE = "message";
    public static final String KEY_LOG_RESERVERD = "reserved";

    private static DatabaseHelper sInstance;

    public static final String TAG = "DBHELPER";

    public static synchronized DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        try {
            if (sInstance == null) {
                sInstance = new DatabaseHelper(context.getApplicationContext());
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        return sInstance;
    }


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    String CREATE_TABLE_RECS =
            "CREATE TABLE " + RECORDINGS_TABLE + " ("
                    + KEY_ID + " integer primary key autoincrement, "
                    + KEY_LINTIME + " TEXT, "
                    + KEY_DATE + " TEXT, "
                    + KEY_TIME + " TEXT, "
                    + KEY_CALLTYPE + " TEXT, "
                    + KEY_ANUM + " TEXT, "
                    + KEY_BNUM + " TEXT, "
                    + KEY_DURATION + " TEXT, "
                    + KEY_SOURCE + " TEXT, "
                    + KEY_FORMAT + " TEXT, "
                    + KEY_LINK + " TEXT, "
                    + KEY_DEVID + " TEXT, "
                    + KEY_ANDROIDID + " TEXT, "
                    + KEY_STATUS + " TEXT, "
                    + KEY_RESERVED + " TEXT); ";

    String CREATE_TABLE_LOGS =
        "CREATE TABLE " + LOG_TABLE + " ("
                + KEY_LOG_ID + " integer primary key autoincrement, "
                + KEY_LOG_TIME + " TEXT, "
                + KEY_LOG_LEVEL + " TEXT, "
                + KEY_LOG_TAG + " TEXT, "
                + KEY_LOG_MESSAGE + " TEXT, "
                + KEY_LOG_RESERVERD + " TEXT); ";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RECS);
        db.execSQL(CREATE_TABLE_LOGS);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + RECORDINGS_TABLE + ";");//", " + LOG_TABLE + ";");
            onCreate(db);
        }
    }


    public void log (String time, String TAG, String message) {
        SQLiteDatabase db = getWritableDatabase();


        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(KEY_LOG_TIME, time);
            cv.put(KEY_LOG_TAG, TAG);
            cv.put(KEY_LOG_MESSAGE, message);

            db.insertOrThrow(LOG_TABLE, null, cv);
            db.setTransactionSuccessful();

        } catch (Exception e) {
            Log.d(TAG, "Ошибка записи лога в БД");
        } finally {
            db.endTransaction();
        }
    }


    public void addInitial(ContentValues contentValues) {

        SQLiteDatabase db = getWritableDatabase();


        db.beginTransaction();
        try {

            db.insertOrThrow(RECORDINGS_TABLE, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            L.w(TAG, "Ошибка при добавлении начальных значений");
        } finally {
            db.endTransaction();
            L.v(TAG, "Запись начальных данных в БД прошла успешно");
        }
    }





//    public long addOrUpdateUser(User user) {
//        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
//        SQLiteDatabase db = getWritableDatabase();
//        long userId = -1;
//
//        db.beginTransaction();
//        try {
//            ContentValues values = new ContentValues();
//            values.put(KEY_USER_NAME, user.userName);http://ru.aliexpress.com/item/Original-Meizu-M3-Mini-4G-LTE-Cell-Phone-MTK-6750-Octa-Core-5-0-Screen-2GB/32658321992.html?aff_platform=aaf&sk=eub6yrrBy%3A&cpt=1472506977019&af=113569&cv=1314379&cn=3ocoxnkpybril41cwud2sp59fopfbaqd&dp=v5_3ocoxnkpybril41cwud2sp59fopfbaqd&afref=http%3A%2F%2Fvk.com%2Faway.ph&aff_trace_key=d3dd7d81b53c4c29b446b8c490412c95-1472506977019-03880-eub6yrrBy&aff_platform=aaf&sk=eub6yrrBy%3A&cpt=1472666934185&af=103425&cv=1566075&cn=2ocsd2tu996nhlns4pp6vvmn6wm4omuu&dp=v5_2ocsd2tu996nhlns4pp6vvmn6wm4omuu&afref=http%3A%2F%2Fvk.com%2Faway.ph&aff_trace_key=46fce300fd21402094dce652add07cb2-1472666934185-05024-eub6yrrBy
//            values.put(KEY_USER_PROFILE_PICTURE_URL, user.profilePictureUrl);
//
//            // First try to update the user in case the user already exists in the database
//            // This assumes userNames are unique
//            int rows = db.update(TABLE_USERS, values, KEY_USER_NAME + "= ?", new String[]{user.userName});
//
//            // Check if update succeeded
//            if (rows == 1) {
//                // Get the primary key of the user we just updated
//                String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
//                        KEY_USER_ID, TABLE_USERS, KEY_USER_NAME);
//                Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(user.userName)});
//                try {
//                    if (cursor.moveToFirst()) {
//                        userId = cursor.getInt(0);
//                        db.setTransactionSuccessful();
//                    }
//                } finally {
//                    if (cursor != null && !cursor.isClosed()) {
//                        cursor.close();
//                    }
//                }
//            } else {
//                // user with this userName did not already exist, so insert new user
//                userId = db.insertOrThrow(TABLE_USERS, null, values);
//                db.setTransactionSuccessful();
//            }
//        } catch (Exception e) {
//            L.d(TAG, "Error while trying to add or update user");
//        } finally {
//            db.endTransaction();
//        }
//        return userId;
//    }


    public void updateLast(ContentValues cv) {

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // :: GOTO LAST RAW
            Cursor cursor = db.rawQuery("SELECT _ID FROM " + RECORDINGS_TABLE, null);
            cursor.moveToLast();
            String index = cursor.getString(cursor.getColumnIndex(KEY_ID));
            int rows = db.update(RECORDINGS_TABLE, cv, KEY_ID + "=" + index, null); //new String[]{index}'
            if (rows == 1) db.setTransactionSuccessful();
            cursor.close();
        } catch (CursorIndexOutOfBoundsException e) {
            L.i(TAG, "Запись о файле отсутствует в таблице");
        } catch (Exception e) {
            L.e(TAG, "ОШИБКА ОБНОВЛЕНИЯ ДАННЫХ "+e);
        } finally {
            db.endTransaction();
        }
    }

    public void setState (String link, String state) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_STATUS, state);
        db.beginTransaction();
        try {
            String whereClause = "link = \""+link+"\"";
            Cursor cursor = db.query(RECORDINGS_TABLE, null, whereClause, null, null, null, null);
            cursor.moveToLast();
            String index = cursor.getString(cursor.getColumnIndex(KEY_ID));
            int rows = db.update(RECORDINGS_TABLE, cv, KEY_ID + "=" + index, null);
            if (rows == 1) db.setTransactionSuccessful();
            //L.i(TAG, "Файлу "+link+" установлен статус "+state);
            cursor.close();
        } catch (Exception e) {
            L.d(TAG, "ОШИБКА ОБНОВЛЕНИЯ ДАННЫХ "+e);
        } finally {
            db.endTransaction();
        }
    }

    public Map <String, String> getStates() {
        Map< String, String > map = new HashMap<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+KEY_LINK+", "+KEY_STATUS+" FROM "+RECORDINGS_TABLE, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    map.put(cursor.getString(cursor.getColumnIndex(KEY_LINK)), cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            L.d(TAG, "ОШИБКА при попытке получения всех статусов из таблицы");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return map;
    }

    public ContentValues getValues (String link) {

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+RECORDINGS_TABLE+" WHERE link=\""+link+"\"",  null);

        //ArrayList<ContentValues> retVal = new ArrayList<ContentValues>();
        ContentValues cv = new ContentValues();

        try {
            if(cursor.moveToFirst()) {
                do {
                    cv = new ContentValues();
                    DatabaseUtils.cursorRowToContentValues(cursor, cv);
                    //retVal.add(cv);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            L.d(TAG, "ОШИБКА при попытке получения всех статусов из таблицы");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }


        return cv;
    }


    // Get all posts in the database
//    public List<Post> getAllPosts() {
//        List<Post> posts = new ArrayList<>();
//
//        // SELECT * FROM POSTS
//        // LEFT OUTER JOIN USERS
//        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
//        String POSTS_SELECT_QUERY =
//                String.format("SELECT * FROM %s LEFT OUTER JOIN %s ON %s.%s = %s.%s",
//                        TABLE_POSTS,
//                        TABLE_USERS,
//                        TABLE_POSTS, KEY_POST_USER_ID_FK,
//                        TABLE_USERS, KEY_USER_ID);
//
//        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
//        // disk space scenarios)
//        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
//        try {
//            if (cursor.moveToFirst()) {
//                do {
//                    User newUser = new User();
//                    newUser.userName = cursor.getString(cursor.getColumnIndex(KEY_USER_NAME));
//                    newUser.profilePictureUrl = cursor.getString(cursor.getColumnIndex(KEY_USER_PROFILE_PICTURE_URL));
//
//                    Post newPost = new Post();
//                    newPost.text = cursor.getString(cursor.getColumnIndex(KEY_POST_TEXT));
//                    newPost.user = newUser;
//                    posts.add(newPost);
//                } while(cursor.moveToNext());
//            }
//        } catch (Exception e) {
//            L.d(TAG, "Error while trying to get posts from database");
//        } finally {
//            if (cursor != null && !cursor.isClosed()) {
//                cursor.close();
//            }
//        }
//        return posts;
//    }

    // Update the user's profile picture url
//    public int updateUserProfilePicture(User user) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_USER_PROFILE_PICTURE_URL, user.profilePictureUrl);
//
//        // Updating profile picture url for user with that userName
//        return db.update(TABLE_USERS, values, KEY_USER_NAME + " = ?",
//                new String[] { String.valueOf(user.userName) });
//    }

    // Delete all posts and users in the database
    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(RECORDINGS_TABLE, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            L.d(TAG, "Ошибка удаления таблицы");
        } finally {
            db.endTransaction();
        }
    }

}