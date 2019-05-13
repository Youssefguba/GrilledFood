package com.example.grilledfood.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.grilledfood.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {

    private static final String DB_NAME = "OrderFoodDB.db";
    private static final int DB_VER = 1 ;

    public Database(Context context) {
        super(context, DB_NAME,null ,DB_VER);
    }


    public boolean checkFoodExists(String categoryId, String userPhone) {
        boolean flag;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        String SQLQuery = String.format("SELECT * FROM OrderDetails WHERE UserPhone='%s' AND ProductId ='%s'", userPhone, categoryId);
        cursor = db.rawQuery(SQLQuery, null);

        if (cursor.getCount() > 0)
            flag = true;

        else
            flag = false;
        cursor.close();
        return flag;
    }


    public List<Order> getCarts(String userPhone) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        String[] sqlSelect = {"UserPhone", "ProductId", "ProductName", "Quantity", "Price", "Discount", "Image", "ExtraOrder", "ExtraPrice", "ExtraQuantity", "HalfOrderName", "HalfOrderPrice", "HalfOrderQuantity", "QuarterOrderName", "QuarterOrderPrice", "QuarterOrderQuantity"};
        String sqlTable = "OrderDetails";

        queryBuilder.setTables(sqlTable);
        Cursor c = queryBuilder.query(db, sqlSelect, "UserPhone=?", new String[]{userPhone}, null, null, null);

            final List<Order> result = new ArrayList<>();
            if (c.moveToFirst()) {
                do {
                    result.add(new Order(
                            c.getString(c.getColumnIndex("UserPhone")),
                            c.getString(c.getColumnIndex("ProductId")),
                            c.getString(c.getColumnIndex("ProductName")),
                            c.getString(c.getColumnIndex("Quantity")),
                            c.getString(c.getColumnIndex("Price")),
                            c.getString(c.getColumnIndex("Discount")),
                            c.getString(c.getColumnIndex("Image")),
                            c.getString(c.getColumnIndex("ExtraOrder")),
                            c.getString(c.getColumnIndex("ExtraPrice")),
                            c.getString(c.getColumnIndex("ExtraQuantity")),
                            c.getString(c.getColumnIndex("HalfOrderName")),
                            c.getString(c.getColumnIndex("HalfOrderPrice")),
                            c.getString(c.getColumnIndex("HalfOrderQuantity")),
                            c.getString(c.getColumnIndex("QuarterOrderName")),
                            c.getString(c.getColumnIndex("QuarterOrderPrice")),
                            c.getString(c.getColumnIndex("QuarterOrderQuantity"))

                    ));
                } while (c.moveToNext());
            }

        return result;
    }

    public void addToCart(Order order){
        SQLiteDatabase db = getReadableDatabase();                  //      1          2           3          4          5       6       7          8          9          10               11           12           13                      14           15                      16          ////         1    2    3    4    5   6    7    8    9    10   11   12   13   14   15   16
        String query = String.format("INSERT OR REPLACE INTO OrderDetails(UserPhone ,ProductId, ProductName, Quantity, Price, Discount, Image, ExtraOrder, ExtraPrice, ExtraQuantity, HalfOrderName, HalfOrderPrice, HalfOrderQuantity, QuarterOrderName, QuarterOrderPrice, QuarterOrderQuantity) VALUES('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s');",
                order.getUserPhone(),
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount(),
                order.getImage(),
                order.getExtraOrder(),
                order.getExtraPrice(),
                order.getExtraQuantity(),
                order.getHalfOrderName(),
                order.getHalfOrderPrice(),
                order.getHalfOrderQuantity(),
                order.getQuarterOrderName(),
                order.getQuarterOrderPrice(),
                order.getQuarterOrderQuantity()
        );

        db.execSQL(query);

    }

    public void cleanCart(String userPhone) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetails WHERE UserPhone='%s'", userPhone);

        db.execSQL(query);

    }

    public int getCountCarts(String userPhone) {
        int count = 0;
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format("SELECT COUNT(*) FROM OrderDetails WHERE UserPhone='%s'", userPhone);

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()) {

            do {
                count = cursor.getInt(0);
            } while (cursor.moveToNext());

        }

        return count;
    }

    public void updateCart(Order order) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format("UPDATE OrderDetails SET Quantity= '%s' WHERE UserPhone = '%s' AND ProductId='%s' AND ExtraQuantity='%s'", order.getQuantity(), order.getUserPhone(), order.getProductId(), order.getExtraQuantity());

        sqLiteDatabase.execSQL(query);
    }

    public void increaseCart(String categoryId, String userPhone) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format("UPDATE OrderDetails SET Quantity= Quantity+1 WHERE UserPhone= '%s' AND ProductId='%s'", userPhone, categoryId);

        sqLiteDatabase.execSQL(query);
    }


    public void removeFromCart(String productId, String userPhone) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetails WHERE UserPhone='%s' AND ProductId='%s'", userPhone, productId);

        db.execSQL(query);
    }
    //Favorites
    public void addToFavorites(String foodId, String userPhone) {

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format("INSERT INTO Favorites(FoodId,UserPhone) VALUES('%s','%s');", foodId, userPhone);
        sqLiteDatabase.execSQL(query);

    }

    public void removeFromFavorites(String foodId, String userPhone) {

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format("DELETE FROM Favorites WHERE FoodId='%s' and UserPhone='%s';", foodId, userPhone);
        sqLiteDatabase.execSQL(query);

    }

    public boolean isFavorites(String foodId, String userPhone) {

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = String.format("SELECT * FROM Favorites WHERE FoodId='%s' and UserPhone='%s';", foodId, userPhone);

        Cursor cursor = sqLiteDatabase.rawQuery(query,null);

        if (cursor.getCount() <= 0 ){
            cursor.close();
            return false;

        }
        cursor.close();
        return true;

    }


}
