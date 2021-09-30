package com.example.restaurant.db;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.restaurant.Fundamentals.Composition;
import com.example.restaurant.Fundamentals.Dish;
import com.example.restaurant.Fundamentals.Part;
import com.example.restaurant.Fundamentals.Product;
import com.example.restaurant.Fundamentals.User;

public class DataBaseHandler extends SQLiteOpenHelper {


    //Data base details
    //***
    public static final String DB_NAME = "restaurant.db";

    //tables
    private final static String DISH_TABLE =  "DISH_TABLE";
    private final static String PRODUCT_TABLE =     "PRODUCT_TABLE";
    private final static String PART_TABLE =       "PART_TABLE";
    private final static String USER_TABLE =       "USER_TABLE";
    private final static String COMPOSITION_TABLE =       "COMPOSITION_TABLE";
    //private final String BREWER_TABLE =     "BREWER_TABLE";
    //private final String CHIEF_TABLE =      "CHIEF_TABLE";
    //private final String CUSTOMER_TABLE =   "CUSTOMER_TABLE";
    // private final String TASTER_TABLE =     "TASTER_TABLE";

    //columns
    private static final String HASH_CODE = "HASH_CODE";

    //BREWER_TABLE
    private final static String ID_WAITER = "ID_WAITER";
    //private final String NAME = "NAME";
    //private final String EMAIL = "EMAIL";
    //private final String PHONE = "PHONE";
    //private final String PASSPORT_NUMBER = "PASSPORT_NUMBER";
    //private final String BIRTH_DATE = "BIRTH_DATE";

    //CHIEF_TABLE
    private final static String ID_CHIEF = "ID_CHIEF";
    //private final String NAME = "NAME";
    //private final String EMAIL = "EMAIL";
    //private final String PHONE = "PHONE";
    //private final String BIRTH_DATE = "BIRTH_DATE";

    //CUSTOMER_TABLE
    private final static String ID_TABLE = "ID_TABLE";
    //private final String NAME = "NAME";
    //private final String EMAIL = "EMAIL";
    //private final String PHONE = "PHONE";
    //private final String BIRTH_DATE = "BIRTH_DATE";

    //TASTER_TABLE
    private final static String ID_TASTER = "ID_TASTER";
    //private final String NAME = "NAME";
    //private final String EMAIL = "EMAIL";
    //private final String PHONE = "PHONE";
    //private final String BIRTH_DATE = "BIRTH_DATE";

    //DISHES
    private final static String ID_DISH = "ID_DISH";
    private final static String NAME = "NAME";
    private final static String AUTHOR = "AUTHOR";
    private final static String PRICE = "PRICE";

    //COMPOSITION_TABLE
    private final static String ID_COMPOSITION = "ID_COMPOSITION";
    //private final static String ID_PRODUCT = "ID_PRODUCT";
    //private final static String ID_DISH = "ID_DISH";

    //PART_TABLE
    private final static String ID_PART = "ID_PART";
    //private final static String ID_BEER_SORT = "ID_BEER_SORT";
    //private final static String STATUS = "STATUS";
    //private final static String ID_ORDER = "ID_ORDER";
    //private final static String ID_BREWER = "ID_BREWER";
    //private final static String ID_TASTER = "ID_TASTER";
    //private final static String ID_CUSTOMER = "ID_CUSTOMER";
    private final static String QUANTITY = "QUANTITY";


    //USER_TABLE
    private final static String ID_USER = "ID_USER";
    private final static String ID_PRODUCT = "ID_PRODUCT";
    private final static String LOGIN = "LOGIN";
    private final static String PASSWORD = "PASSWORD";
    //private final static String ID_ROLE = "ID_ROLE";
    private final static String EMAIL = "EMAIL";
    private final static String PHONE = "PHONE";
    private final static String DATE = "DATE";
    private final static String SALARY = "SALARY";


    private final static String STATUS = "STATUS";
    private final static String ROLE = "ROLE";
    private final static String ID_COOK = "ID_COOK";



    //===============================QUERIES=================================



    private static final String CREATE_PRODUCT_TABLE = "CREATE TABLE IF NOT EXISTS " + PRODUCT_TABLE + " (" +
            ID_PRODUCT + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            HASH_CODE + " INTEGER, " +
            NAME + " TEXT, " +
            QUANTITY + " INTEGER " +
            ")";


    private static final String CREATE_COMPOSITION_TABLE = "CREATE TABLE IF NOT EXISTS " + COMPOSITION_TABLE + " (" +
            ID_COMPOSITION + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            HASH_CODE + " INTEGER, " +
            ID_PRODUCT + " INTEGER, " +
            ID_DISH + " INTEGER " +
            ")";

    private static final String CREATE_DISH_TABLE = "CREATE TABLE IF NOT EXISTS " + DISH_TABLE + " (" +
            ID_DISH + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            HASH_CODE + " INTEGER, " +
            NAME + " TEXT, " +
            STATUS + " TEXT, " +
            PRICE + " INTEGER " +
            ")";


    private static final String CREATE_PART_TABLE = "CREATE TABLE IF NOT EXISTS " + PART_TABLE + " (" +
            ID_PART + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            HASH_CODE + " INTEGER, " +
            ID_DISH + " INTEGER, " +
            ID_COOK + " INTEGER, " +
            ID_WAITER + " INTEGER, " +
            ID_TABLE + " INTEGER, " +
            QUANTITY + " INTEGER, " +
            STATUS +" TEXT, " +
            DATE + " TEXT " +
            ")";

    private static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + USER_TABLE + " (" +
            ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            HASH_CODE + " INTEGER, " +
            ROLE + " TEXT, " +
            NAME + " TEXT, " +
            PHONE + " TEXT, " +
            EMAIL + " TEXT, " +
            DATE + " TEXT, " +
            LOGIN + " TEXT, " +
            PASSWORD + " TEXT, " +
            SALARY + " INTEGER " +
            ")";

    private static final String CREATE_TRIGGER_1 = "CREATE TRIGGER Change_salary INSERT ON USER_TABLE\n" +
            "BEGIN\n" +
            "SELECT @salary = INSERTED.SALARY FROM INSERTED\n" +
            "IF @salary < 0 \n" +
            "BEGIN\n" +
            "RAISE (FAIL, 'Зарплата не может быть отрицательной')\n" +
            "ROLLBACK TRANSACTION\n" +
            "END";


    private static final String CREATE_TRIGGER_2 = "CREATE TRIGGER Change_price INSERT ON PRODUCT_TABLE\n" +
            "BEGIN\n" +
            "SELECT @quantity = INSERTED.QUANTITY FROM INSERTED\n" +
            "IF @quantity < 0 \n" +
            "BEGIN\n" +
            "RAISE (FAIL, 'Количество не может быть отрицательным)\n" +
            "ROLLBACK TRANSACTION\n" +
            "END";


    //=========================================END_QUERIES=======================================


    public DataBaseHandler(Activity activity) {
        super(activity.getApplicationContext(), DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DISH_TABLE);
        db.execSQL(CREATE_PART_TABLE);
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_PRODUCT_TABLE);
        db.execSQL(CREATE_COMPOSITION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DISH_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PART_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PRODUCT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + COMPOSITION_TABLE);
        onCreate(db);
    }


    //=======================================INSERT_BLOCK=======================================
    public void insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(ROLE, user.getRole());
        contentValues.put(HASH_CODE, user.getHashCode());
        contentValues.put(NAME, user.getName());
        contentValues.put(PHONE, user.getPhone());
        contentValues.put(EMAIL, user.getEmail());
        contentValues.put(SALARY, user.getSalary());
        contentValues.put(DATE, user.getBirthDate());
        contentValues.put(LOGIN, user.getLogin());
        contentValues.put(PASSWORD, user.getPassword());

        db.insert(USER_TABLE, null, contentValues);
    }

    public void insertDish(Dish dish) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(HASH_CODE, dish.getHashCode());
        contentValues.put(NAME, dish.getName());
        contentValues.put(PRICE, dish.getPrice());

        db.insert(DISH_TABLE, null, contentValues);
    }

    public void insertProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(HASH_CODE, product.getHashCode());
        contentValues.put(NAME, product.getName());
        contentValues.put(QUANTITY, product.getQuantity());

        db.insert(PRODUCT_TABLE, null, contentValues);
    }


    public void insertPart(Part item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(HASH_CODE, item.getHashCode());
        contentValues.put(ID_DISH, item.getDishId());
        contentValues.put(ID_WAITER, item.getIdWaiter());
        contentValues.put(ID_TABLE, item.getIdTable());
        contentValues.put(ID_COOK, item.getIdCook());
        contentValues.put(STATUS, item.getStatus());
        contentValues.put(QUANTITY, item.getQuantity());
        contentValues.put(DATE, item.getDate());

        db.insert(PART_TABLE, null, contentValues);
    }

    public void insertComp(Composition composition) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(HASH_CODE, composition.getHashCode());
        contentValues.put(ID_PRODUCT, composition.getIdProduct());
        contentValues.put(ID_DISH, composition.getIdDish());

        db.insert(COMPOSITION_TABLE, null, contentValues);
    }

    //=======================================UPDATE_BLOCK=======================================

    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(ROLE, user.getRole());
        contentValues.put(NAME, user.getName());
        contentValues.put(PHONE, user.getPhone());
        contentValues.put(EMAIL, user.getEmail());
        contentValues.put(SALARY, user.getSalary());
        contentValues.put(DATE, user.getBirthDate());
        contentValues.put(LOGIN, user.getLogin());
        contentValues.put(PASSWORD, user.getPassword());

        String whereClause = HASH_CODE + "=?";
        // now define what those ? should be
        String[] whereArgs = new String[]{String.valueOf(user.getHashCode())};

        db.update(USER_TABLE, contentValues, whereClause, whereArgs);
    }

    public void updateDish(Dish item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(NAME, item.getName());
        contentValues.put(PRICE, item.getPrice());
        contentValues.put(STATUS, item.getStatus());

        String whereClause = HASH_CODE + "=?";
        // now define what those ? should be
        String[] whereArgs = new String[]{String.valueOf(item.getHashCode())};

        db.update(DISH_TABLE, contentValues, whereClause, whereArgs);
    }


    public void updatePart(Part item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(ID_DISH, item.getDishId());
        contentValues.put(ID_WAITER, item.getIdWaiter());
        contentValues.put(ID_TABLE, item.getIdTable());
        contentValues.put(ID_COOK, item.getIdCook());
        contentValues.put(STATUS, item.getStatus());
        contentValues.put(QUANTITY, item.getQuantity());
        contentValues.put(DATE, item.getDate());

        String whereClause = HASH_CODE + "=?";
        // now define what those ? should be
        String[] whereArgs = new String[]{String.valueOf(item.getHashCode())};

        db.update(PART_TABLE, contentValues, whereClause, whereArgs);
    }

    public void updateComp(Composition item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(ID_PRODUCT, item.getIdProduct());
        contentValues.put(ID_DISH, item.getIdDish());

        String whereClause = HASH_CODE + "=?";
        // now define what those ? should be
        String[] whereArgs = new String[]{String.valueOf(item.getHashCode())};

        db.update(COMPOSITION_TABLE, contentValues, whereClause, whereArgs);
    }

    public void updateProduct(Product item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(ID_PRODUCT, item.getIdProduct());
        contentValues.put(NAME, item.getName());
        contentValues.put(QUANTITY, item.getQuantity());

        String whereClause = HASH_CODE + "=?";
        // now define what those ? should be
        String[] whereArgs = new String[]{String.valueOf(item.getHashCode())};

        db.update(PRODUCT_TABLE, contentValues, whereClause, whereArgs);
    }



    //=======================================SELECT_BLOCK=======================================

    public User findUserLoginPass(String login, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = LOGIN + "=? AND "+ PASSWORD + "=?";
        String[] selectionArgs = new String[]{String.valueOf(login), String.valueOf(password)};
        Cursor cursor = db.query(USER_TABLE, null, selection, selectionArgs, null, null, null);


        User user = null;

        if (cursor.moveToFirst()) {
            user = new User();
            // достаем данные из курсора
            user.setIdUser(cursor.getInt(cursor.getColumnIndex(ID_USER)));
            user.setHashCode(cursor.getInt(cursor.getColumnIndex(HASH_CODE)));
            user.setRole(cursor.getString(cursor.getColumnIndex(ROLE)));
            user.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            user.setPhone(cursor.getString(cursor.getColumnIndex(PHONE)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(EMAIL)));
            user.setBirthDate(cursor.getString(cursor.getColumnIndex(DATE)));
            user.setLogin(cursor.getString(cursor.getColumnIndex(LOGIN)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(PASSWORD)));
            user.setSalary(cursor.getInt(cursor.getColumnIndex(SALARY)));
        }
        cursor.close();
        return user;
    }

    public String isAlreadyUser(String login, String phone, String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection2 = PHONE + "=?" ;
        String[] selectionArgs2 = new String[]{String.valueOf(phone)};
        Cursor cursor2 = db.query(USER_TABLE, null, selection2, selectionArgs2, null, null, null);

        String selection3 = EMAIL + "=?" ;
        String[] selectionArgs3 = new String[]{String.valueOf(email)};
        Cursor cursor3 = db.query(USER_TABLE, null, selection3, selectionArgs3, null, null, null);

        String selection4 = LOGIN + "=?" ;
        String[] selectionArgs4 = new String[]{String.valueOf(login)};
        Cursor cursor4 = db.query(USER_TABLE, null, selection4, selectionArgs4, null, null, null);

        User user = null;

        String test = "OK";

        if (cursor2.moveToFirst()) {
            user = new User();
            // достаем данные из курсора
            user.setIdUser(cursor2.getInt(cursor2.getColumnIndex(ID_USER)));
            test = "Пользователь уже существует.";
        }
        cursor2.close();

        if (cursor3.moveToFirst()) {
            user = new User();
            // достаем данные из курсора
            user.setIdUser(cursor3.getInt(cursor3.getColumnIndex(ID_USER)));
            test = "Пользователь уже существует.";
        }
        cursor3.close();

        if (cursor4.moveToFirst()) {
            user = new User();
            // достаем данные из курсора
            user.setIdUser(cursor4.getInt(cursor4.getColumnIndex(ID_USER)));
            test = "Пользователь уже существует.";
        }
        cursor4.close();

        return test;
    }


    public String isAlreadyHaveDish(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = NAME + "=?" ;
        String[] selectionArgs = new String[]{String.valueOf(name)};
        Cursor cursor = db.query(DISH_TABLE, null, selection, selectionArgs, null, null, null);
        Dish dish = null;

        String test = "OK";
        if (cursor.moveToFirst()) {
            dish = new Dish();
            // достаем данные из курсора
            dish.setIdDish(cursor.getInt(cursor.getColumnIndex(ID_DISH)));
            test = "Такое блюдо уже существует.";
        }
        cursor.close();
        return test;
    }

    public String isAlreadyHaveProduct(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = NAME + "=?" ;
        String[] selectionArgs = new String[]{String.valueOf(name)};
        Cursor cursor = db.query(PRODUCT_TABLE, null, selection, selectionArgs, null, null, null);
        Product item = null;

        String test = "OK";
        if (cursor.moveToFirst()) {
            item = new Product();
            // достаем данные из курсора
            item.setIdProduct(cursor.getInt(cursor.getColumnIndex(ID_PRODUCT)));
            test = "Такой продукт уже существует.";
        }
        cursor.close();
        return test;
    }

    public String isAlreadyHaveComposition(Composition comp) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = ID_PRODUCT + "=? AND "+ ID_DISH + "=?";
        String[] selectionArgs = new String[]{String.valueOf(comp.getIdProduct()),
                String.valueOf(comp.getIdDish())};
        Cursor cursor = db.query(COMPOSITION_TABLE, null, selection, selectionArgs, null, null, null);
        String test = "OK";
        if (cursor.moveToFirst()) {
            test = "Такой продукт уже существует.";
        }
        cursor.close();
        return test;
    }


    public Cursor selectFromUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + USER_TABLE;
        return db.rawQuery(query, null);
    }

    public Cursor selectFromDish() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + DISH_TABLE;
        return db.rawQuery(query, null);
    }

    public Cursor selectFromPart() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + PART_TABLE;
        return db.rawQuery(query, null);
    }

    public Cursor selectFromProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + PRODUCT_TABLE;
        return db.rawQuery(query, null);
    }

    /* (6) Вывод заказа закрепленного за конкрентным официантом */
    public Cursor selectFromPartByWaiter(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + PART_TABLE + " WHERE " + ID_WAITER + "=" + id;
        return db.rawQuery(query, null);
    }

    public Cursor selectFromPartByDish(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + PART_TABLE + " WHERE " + ID_DISH + "=" + id;
        return db.rawQuery(query, null);
    }

    /* (2) Вывод заказа по конкретному столу */
    public Cursor selectFromPartByTable(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + PART_TABLE + " WHERE " + ID_TABLE + "=" + id;
        return db.rawQuery(query, null);
    }

    public Cursor selectIngredientsByDish(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + COMPOSITION_TABLE + " WHERE " + ID_DISH + "=" + id;
        return db.rawQuery(query, null);
    }

    public Cursor selectIngredientsByProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + COMPOSITION_TABLE + " WHERE " + ID_PRODUCT + "=" + id;
        return db.rawQuery(query, null);
    }


    /* (3) Вывод средней стоимости заказа */
    public double getAveragePrice() {
        int sum = 0;
        int quantity = 0;
        double averagePrice = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + PART_TABLE + " WHERE " + STATUS + "=" + "'Готов'";

        Cursor cursor = db.rawQuery(query, null);
        Part item = null;

        while (cursor.moveToNext()) {
            item = getPartByID(cursor.getColumnIndex(ID_PART));
            int price = getDishById(item.getDishId()).getPrice();
            sum += price * item.getQuantity();
            quantity += 1;
        }
        averagePrice = sum / quantity;

        cursor.close();

        return averagePrice;
    }

    /* (1) Вывод выручки ресторана за все время */
    public int getAllMoney() {
        int sum = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + PART_TABLE + " WHERE " + STATUS + "=" + "Готов";

        Cursor cursor = db.rawQuery(query, null);
        Part item = null;

        while (cursor.moveToNext()) {
            item = getPartByID(cursor.getColumnIndex(ID_PART));
            int price = getDishById(item.getDishId()).getPrice();
            sum += price * item.getQuantity();
        }

        cursor.close();

        return sum;
    }


    public User getUserByHashCode(int hashCode) {
        User user = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = HASH_CODE + "=?";
        String[] selectionArgs = new String[]{String.valueOf(hashCode)};
        Cursor cursor = db.query(USER_TABLE, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            user = new User();
            // достаем данные из курсора
            user.setIdUser(cursor.getInt(cursor.getColumnIndex(ID_USER)));
            user.setHashCode(cursor.getInt(cursor.getColumnIndex(HASH_CODE)));
            user.setRole(cursor.getString(cursor.getColumnIndex(ROLE)));
            user.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            user.setPhone(cursor.getString(cursor.getColumnIndex(PHONE)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(EMAIL)));
            user.setSalary(cursor.getInt(cursor.getColumnIndex(SALARY)));
            user.setBirthDate(cursor.getString(cursor.getColumnIndex(DATE)));
            user.setLogin(cursor.getString(cursor.getColumnIndex(LOGIN)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(PASSWORD)));
        }
        cursor.close();
        return user;
    }

    public User getUserByID(int id) {
        User user = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = ID_USER + "=?";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(USER_TABLE, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            user = new User();
            // достаем данные из курсора
            user.setIdUser(cursor.getInt(cursor.getColumnIndex(ID_USER)));
            user.setHashCode(cursor.getInt(cursor.getColumnIndex(HASH_CODE)));
            user.setRole(cursor.getString(cursor.getColumnIndex(ROLE)));
            user.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            user.setPhone(cursor.getString(cursor.getColumnIndex(PHONE)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(EMAIL)));
            user.setSalary(cursor.getInt(cursor.getColumnIndex(SALARY)));
            user.setBirthDate(cursor.getString(cursor.getColumnIndex(DATE)));
            user.setLogin(cursor.getString(cursor.getColumnIndex(LOGIN)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(PASSWORD)));
        }
        cursor.close();
        return user;
    }

    /* (10) Кто является администратором */
    public User getAdmin() {
        User user = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = ROLE + "=?";
        String[] selectionArgs = new String[]{"Admin"};
        Cursor cursor = db.query(USER_TABLE, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            user = new User();
            // достаем данные из курсора
            user.setIdUser(cursor.getInt(cursor.getColumnIndex(ID_USER)));
            user.setHashCode(cursor.getInt(cursor.getColumnIndex(HASH_CODE)));
            user.setRole(cursor.getString(cursor.getColumnIndex(ROLE)));
            user.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            user.setPhone(cursor.getString(cursor.getColumnIndex(PHONE)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(EMAIL)));
            user.setSalary(cursor.getInt(cursor.getColumnIndex(SALARY)));
            user.setBirthDate(cursor.getString(cursor.getColumnIndex(DATE)));
            user.setLogin(cursor.getString(cursor.getColumnIndex(LOGIN)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(PASSWORD)));
        }
        cursor.close();
        return user;
    }




    public Part getPartByHashCode(int hashCode) {
        Part part = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = HASH_CODE + "=?";
        String[] selectionArgs = new String[]{String.valueOf(hashCode)};
        Cursor cursor = db.query(PART_TABLE, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            part = new Part();
            // достаем данные из курсора

            part.setIdPart(cursor.getInt(cursor.getColumnIndex(ID_PART)));
            part.setHashCode(cursor.getInt(cursor.getColumnIndex(HASH_CODE)));
            part.setIdDish(cursor.getInt(cursor.getColumnIndex(ID_DISH)));
            part.setIdWaiter(cursor.getInt(cursor.getColumnIndex(ID_WAITER)));
            part.setIdTable(cursor.getInt(cursor.getColumnIndex(ID_TABLE)));
            part.setIdCook(cursor.getInt(cursor.getColumnIndex(ID_COOK)));
            part.setQuantity(cursor.getInt(cursor.getColumnIndex(QUANTITY)));
            part.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
            part.setDate(cursor.getString(cursor.getColumnIndex(DATE)));

            Dish dish = getDishById(part.getDishId());
            part.setDishName(dish.getName());
            part.setPrice(dish.getPrice() * part.getQuantity());
        }
        cursor.close();
        return part;
    }

    public Part getPartByID(int id) {
        Part part = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = ID_PART + "=?";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(PART_TABLE, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            part = new Part();
            // достаем данные из курсора

            part.setIdPart(cursor.getInt(cursor.getColumnIndex(ID_PART)));
            part.setHashCode(cursor.getInt(cursor.getColumnIndex(HASH_CODE)));
            part.setIdDish(cursor.getInt(cursor.getColumnIndex(ID_DISH)));
            part.setIdWaiter(cursor.getInt(cursor.getColumnIndex(ID_WAITER)));
            part.setIdTable(cursor.getInt(cursor.getColumnIndex(ID_TABLE)));
            part.setIdCook(cursor.getInt(cursor.getColumnIndex(ID_COOK)));
            part.setQuantity(cursor.getInt(cursor.getColumnIndex(QUANTITY)));
            part.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
            part.setDate(cursor.getString(cursor.getColumnIndex(DATE)));

            Dish dish = getDishById(part.getDishId());
            part.setDishName(dish.getName());
            part.setPrice(dish.getPrice() * part.getQuantity());
        }
        cursor.close();
        return part;
    }

    public Dish getDishById(int id) {
        Dish dish = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = ID_DISH + "=?";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(DISH_TABLE, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            dish = new Dish();
            // достаем данные из курсора
            dish.setIdDish(cursor.getInt(cursor.getColumnIndex(ID_DISH)));
            dish.setHashCode(cursor.getInt(cursor.getColumnIndex(HASH_CODE)));
            dish.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            dish.setPrice(cursor.getInt(cursor.getColumnIndex(PRICE)));
            dish.setStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
        }
        cursor.close();
        return dish;
    }

    public Product getProductById(int id) {
        Product item = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = ID_PRODUCT + "=?";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(PRODUCT_TABLE, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            item = new Product();
            // достаем данные из курсора
            item.setIdProduct(cursor.getInt(cursor.getColumnIndex(ID_PRODUCT)));
            item.setHashCode(cursor.getInt(cursor.getColumnIndex(HASH_CODE)));
            item.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            item.setQuantity(cursor.getInt(cursor.getColumnIndex(QUANTITY)));
        }
        cursor.close();
        return item;
    }

    public Composition getCompByProductAndDish(int idProduct, int idDish) {
        Composition item = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = ID_PRODUCT + "=? AND "+ ID_DISH + "=?";
        String[] selectionArgs = new String[]{String.valueOf(idProduct), String.valueOf(idDish)};
        Cursor cursor = db.query(COMPOSITION_TABLE, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            item = new Composition();
            // достаем данные из курсора
            item.setIdComp(cursor.getInt(cursor.getColumnIndex(ID_COMPOSITION)));
            item.setHashCode(cursor.getInt(cursor.getColumnIndex(HASH_CODE)));
            item.setIdProduct(cursor.getInt(cursor.getColumnIndex(ID_PRODUCT)));
            item.setIdDish(cursor.getInt(cursor.getColumnIndex(ID_DISH)));
        }
        cursor.close();
        return item;
    }



    /*public List<Product> selectIngredientsByDish(int id) {

        int hash;
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + COMPOSITION_TABLE + " WHERE " + ID_DISH + "=" + id;

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            hash = cursor.getInt(cursor.getColumnIndex("HASH_CODE"));
            Product item = getProductByHash(hash);
            products.add(item);
        }

        cursor.close();
        return products;
    }*/




    //=======================================DROP_BLOCK=========================================


    public void deleteUser(User item) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + USER_TABLE + " WHERE " + HASH_CODE + " = " + item.getHashCode();
        db.execSQL(query);
    }

    public void deleteUser(int hash) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + USER_TABLE + " WHERE " + ID_USER + " = " + hash;
        db.execSQL(query);
    }


    public void deleteDish(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + DISH_TABLE + " WHERE " + ID_DISH + " = " + id;
        db.execSQL(query);
    }


    public void deleteDish(Dish item) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + DISH_TABLE + " WHERE " + HASH_CODE + " = " + item.getHashCode();
        db.execSQL(query);
    }

    public void deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + PRODUCT_TABLE + " WHERE " + ID_PRODUCT + " = " + id;
        db.execSQL(query);
    }

    public void deletePart(Part item) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + PART_TABLE + " WHERE " + HASH_CODE + " = " + item.getHashCode();
        db.execSQL(query);
    }

    public void deletePart(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + PART_TABLE + " WHERE " + ID_PART + " = " + id;
        db.execSQL(query);
    }

    public void deleteComp(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + COMPOSITION_TABLE + " WHERE " + ID_COMPOSITION + " = " + id;
        db.execSQL(query);
    }


    public void clearDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE FROM " + PART_TABLE);
        db.execSQL("DELETE FROM " + DISH_TABLE);
        db.execSQL("DELETE FROM " + USER_TABLE);
        db.execSQL("DELETE FROM " + PRODUCT_TABLE);
        db.execSQL("DELETE FROM " + COMPOSITION_TABLE);
    }
}
