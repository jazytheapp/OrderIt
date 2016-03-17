/** History: Created by Hieu Thien
 * Description: Database access class
 */

package orderit.mainapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import orderit.mainapp.model.BillOrderItem;
import orderit.mainapp.model.MenuItem;
import orderit.mainapp.model.MenuGroup;
import orderit.mainapp.model.OrderItem;
import orderit.mainapp.model.OrderManager;
import orderit.mainapp.model.User;
import orderit.mainapp.model.TableItem;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    /** "users" table */
    private static final String USERS_TABLE = "users";
    private static final String USERS_COLUMN_ID = "id";
    private static final String USERS_COLUMN_USERNAME = "username";
    private static final String USERS_COLUMN_PASSWORD = "password";
    private static final String USERS_COLUMN_BUSINESSID = "business_id";
    private static final String USERS_COLUMN_ROLEID = "role_id";
    private static final String USERS_COLUMN_CREATEDATE = "created";
    private static final String USERS_COLUMN_MODIFIEDDATE = "modified";

    /** "tables" table */
    private static final String TABLES_TABLE = "tables";
    private static final String TABLES_COLUMN_ID = "id";
    private static final String TABLES_COLUMN_BUSINESSID = "business_id";
    private static final String TABLES_COLUMN_NAME = "name";
    private static final String TABLES_COLUMN_STATUS = "status";
    private static final String TABLES_COLUMN_CREATEDATE = "created";
    private static final String TABLES_COLUMN_MODIFIEDDATE = "modified";

    /** "orders" table */
    private static final String ORDERS_TABLE = "orders";
    private static final String ORDERS_COLUMN_ID = "id";
    private static final String ORDERS_COLUMN_USERID = "user_id";
    private static final String ORDERS_COLUMN_TABLEID = "table_id";
    private static final String ORDERS_COLUMN_STATUS = "status";
    private static final String ORDERS_COLUMN_CREATEDATE = "created";
    private static final String ORDERS_COLUMN_MODIFIEDDATE = "modified";

    /** "order_details" table */
    private static final String ORDERDETAILS_TABLE = "order_details";
    private static final String ORDERDETAILS_COLUMN_ORDERID = "order_id";
    private static final String ORDERDETAILS_COLUMN_ITEMID = "item_id";
    private static final String ORDERDETAILS_COLUMN_ITEMQUANTITY = "item_quantity";
    private static final String ORDERDETAILS_COLUMN_STATUS = "status";
    private static final String ORDERDETAILS_COLUMN_CREATEDATE = "created";
    private static final String ORDERDETAILS_COLUMN_MODIFIEDDATE = "modified";

    /** Private constructor to avoid object creation from outside classes */

    private static final String USER_COLUMN_USERNAME = "username";
    private static final String USER_COLUMN_PASSWORD = "password";
    private static final String USER_COLUMN_BUSINESSID = "business_id";
    private static final String USER_COLUMN_ROLEID = "role_id";
    private static final String USER_COLUMN_CREATEDATE = "created";
    private static final String USER_COLUMN_MODIFIEDDATE = "modified";

    private static final String TABLE_USERS = "users";


    private static final int ITEM_CATEGORY_1_ID = 1;
    private static final int ITEM_CATEGORY_2_ID = 2;
    private static final int ITEM_CATEGORY_3_ID = 3;
    private static final int ITEM_CATEGORY_4_ID = 4;
    private static final int ITEM_CATEGORY_5_ID = 5;
    private static final int ITEM_CATEGORY_6_ID = 6;
    private static final int ITEM_CATEGORY_7_ID = 7;
    private static final int ITEM_CATEGORY_8_ID = 8;

    /**;
     * Private constructor to avoid object creation from outside classes.
     *
     * @param context
     */

    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /** Return a singleton instance of DatabaseAccess */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /** Open the database connection */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /** Close the database connection */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read table name from the database.
     *
     * @return a list of table name
     */
    public List<TableItem> InitTableItems() {
        List<TableItem> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM tables", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TableItem tableItem = new TableItem();

            tableItem.setId(cursor.getInt(0));
            tableItem.setBusinessId(cursor.getInt(1));
            tableItem.setName(cursor.getString(2));
            tableItem.setStatus(cursor.getInt(3));

            list.add(tableItem);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    /** Search password */
    public String SearchPassword(String userName)
    {
        String query = "Select " + USERS_COLUMN_USERNAME + ", " + USERS_COLUMN_PASSWORD + " from " + USERS_TABLE;
        Cursor cursor = database.rawQuery(query, null);
        String _userName, _password = "12345677890-qrwereytryuuiuiyuisdfsdfggfhgjhkljklkl;czxcxzcvcbcvnvbmnbm,,<><><:";
        if(cursor.moveToFirst())
        {
            do {
                _userName = cursor.getString(0);
                if(userName.equalsIgnoreCase(_userName))
                {
                    _password = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        return _password;
    }

    /** Insert user to local database */
    public void InsertUser(User u)
    {
        ContentValues values = new ContentValues();
        values.put(USERS_COLUMN_ID, u.getId());
        values.put(USERS_COLUMN_USERNAME, u.getUserName());
        values.put(USERS_COLUMN_PASSWORD, u.getPassword());
        values.put(USERS_COLUMN_BUSINESSID, u.getBusinessId());
        values.put(USERS_COLUMN_ROLEID, u.getRoleId());
        this.database.insert(USERS_TABLE, null, values);
    }

    /** Get list of bill order details */
    public List<BillOrderItem> getBillOrderItemListByOrderID(int orderID) {
        List<BillOrderItem> list = new ArrayList<>();
        String query = "select items.name,order_details.item_quantity,items.price,items.price_unit from order_details" +
                " left join items on items.id = order_details.item_id and order_details.order_id = " + orderID;
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            BillOrderItem billOrderItem = new BillOrderItem();
            billOrderItem.setOrderName(cursor.getString(0));
            billOrderItem.setQuantity(cursor.getString(1));
            billOrderItem.setSinglePrice(cursor.getString(2));
            list.add(billOrderItem);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    /**
     * Read Menu from the database.
     *
     * @return a hash map of Menu
     */
    public Map<Integer, MenuItem> InitMenu() {
        Map<Integer, MenuItem> menuManager = new LinkedHashMap<Integer, MenuItem>();

        Cursor cursor = database.rawQuery("SELECT * FROM items", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // Create new item
            MenuItem menuItem = new MenuItem();

            menuItem.setId(cursor.getInt(0));
            menuItem.setName(cursor.getString(1));
            menuItem.setPrice(cursor.getInt(2));
            menuItem.setPriceUnit(cursor.getString(3));
            menuItem.setAveMakeTime(cursor.getInt(4));
            menuItem.setAveMakeTimeUnit(cursor.getString(5));
            menuItem.setCategoryId(cursor.getInt(8));

            menuManager.put(menuItem.getId(), menuItem);
            cursor.moveToNext();
        }
        cursor.close();

        return menuManager;
    }

    public OrderManager QueryOrderInfoByTableID(int tableID) {
        OrderManager orderManager = new OrderManager();

        Cursor cursor = database.rawQuery("SELECT * FROM orders where table_id="+tableID+"", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            orderManager.setId(cursor.getInt(0));
            orderManager.setUserId(cursor.getInt(1));
            orderManager.setTableId(cursor.getInt(2));
            orderManager.setStatus(cursor.getInt(3));

            cursor.moveToNext();
        }
        cursor.close();

        return orderManager;
    }

    public Map<Integer, OrderItem> QueryOrderItemByOrderID(int orderID) {
        Map<Integer, OrderItem> orderItemMap = new LinkedHashMap<Integer, OrderItem>();

        Cursor cursor = database.rawQuery("SELECT * FROM order_details where order_id="+orderID+"", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            OrderItem orderItem = new OrderItem();

            orderItem.setId(cursor.getInt(0));
            orderItem.setOrderId(cursor.getInt(1));
            orderItem.setMenuItemId(cursor.getInt(2));
            orderItem.setMenuItemQuantity(cursor.getInt(3));

            orderItemMap.put(orderItem.getMenuItemId(), orderItem);
            cursor.moveToNext();
        }
        cursor.close();

        return orderItemMap;
    }

    public List<MenuGroup> QueryGroupMenu() {
        List<MenuGroup> list = new ArrayList<MenuGroup>();

        Cursor cursor = database.rawQuery("SELECT * FROM categories", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MenuGroup menuGroup = new MenuGroup();

            menuGroup.setId(cursor.getInt(0));
            menuGroup.setName(cursor.getString(1));

            list.add(menuGroup);
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    public Map<Integer, List<MenuItem>> makeMenu(List<MenuGroup> menuGroups, Map<Integer, OrderItem> orderItemMap ) {
        Map<Integer, List<MenuItem>> menuMap = new LinkedHashMap<Integer, List<MenuItem>>();

        for (MenuGroup menuGroup : menuGroups) {

            List<MenuItem> menuItemList = new ArrayList<MenuItem>();

            int totalOrderQuantity = 0;

            Cursor cursor = database.rawQuery("SELECT * FROM items where category_id="+menuGroup.getId()+"", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                // Create new item
                MenuItem menuItem = new MenuItem();

                menuItem.setId(cursor.getInt(0));
                menuItem.setName(cursor.getString(1));
                menuItem.setPrice(cursor.getInt(2));
                menuItem.setPriceUnit(cursor.getString(3));
                menuItem.setAveMakeTime(cursor.getInt(4));
                menuItem.setAveMakeTimeUnit(cursor.getString(5));
                menuItem.setCategoryId(cursor.getInt(8));

                if(orderItemMap.get(menuItem.getId()) != null) {
                    totalOrderQuantity += orderItemMap.get(menuItem.getId()).getMenuItemQuantity();
                }

                menuItemList.add(menuItem);
                cursor.moveToNext();
            }
            cursor.close();

            menuGroup.setOrderQuantity(totalOrderQuantity);
            menuMap.put(menuGroup.getId(), menuItemList);
        }

        return menuMap;
    }

    public int QueryTableIdByOrderId(int OrderId) {
        int tableId = 0;

        Cursor cursor = database.rawQuery("SELECT table_id FROM orders where id="+OrderId+"", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            tableId = cursor.getInt(0);
            cursor.moveToNext();
        }
        cursor.close();

        return tableId;
    }

    public int QueryTableStatusByTableId(int TableId) {
        int tableStatus = 0;

        Cursor cursor = database.rawQuery("SELECT status FROM tables where id="+TableId+"", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            tableStatus = cursor.getInt(0);
            cursor.moveToNext();
        }
        cursor.close();

        return tableStatus;
    }

    public void UpdateOrderQuantity(int orderId, int itemId, int newQuantity) {
        ContentValues cv = new ContentValues();
        cv.put("item_quantity", newQuantity);
        database.update("order_details", cv, "order_id="+orderId+" and item_id="+itemId, null);
    }
}
