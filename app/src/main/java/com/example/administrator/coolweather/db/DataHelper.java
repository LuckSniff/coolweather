package com.example.administrator.coolweather.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.coolweather.MyApplication;
import com.example.administrator.coolweather.util.DataUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用该类对数据库进行操作
 */
/**
 * 本想自己使用反射来映射，所需知识点不是很多，但细节太多，故还是使用Litepal，有空借鉴其源码，在写一遍
 */

public class DataHelper {

    private static  String dataName;
    private static Context context;
    private static int version;
    private static DataUtil dataUtil;
    private static SQLiteDatabase db;

    private Object object;

//    static{
//        dataName = "Loation";
//        context = MyApplication.getContext();
//        version = 1;
//        dataUtil = new DataUtil(context,dataName,null,version);   //提供不同的DataUtil就好
//        db = dataUtil.getWritableDatabase();
//    }

    public DataHelper(){

    }

    public DataHelper(String dataName,Context context,int version){
        this.dataName = dataName;
        this.context = context;
        this.version = version;
    }



    /**
     * 获得数据库
     * @return
     */
    public static SQLiteDatabase getDatabase(){
//        dataUtil = new DataUtil(context,dataName,null,version);   //提供不同的DataUtil就好
//        db = dataUtil.getWritableDatabase();
        return db;
    }

    /**
     * 将数据存到数据库中
     * @param dataClass
     */
    public static void  save(Object dataClass)  {
        try {
            String data = getDataByReflect(dataClass);

            //获得Table名，这里的TableName就是类名
            int index = dataClass.getClass().getName().lastIndexOf(".");
            String tableName = dataClass.getClass().getName().substring(index+1);

            //获得Column的名称和对应的values
            String[] allData = getDataByReflect(dataClass).split(";");
            String column = "";
            String values = "";
            for(String datas:allData){
                column += datas.split(":")[0] + ",";
                values += datas.split(":")[1]+ ",";
            }
            //去掉最后的，号
            column = column.substring(0,column.lastIndexOf(","));
            values = values.substring(0,values.lastIndexOf(","));

            //执行SQL插入
            db.execSQL("insert into "+tableName+"("+ column+")"+" values "+"("+ values+")");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *查询数据库
     */
    public static List findAll(Class<? extends Object> dataClass){


        //获得Table名，这里的TableName就是类名
        int index = dataClass.getClass().getName().lastIndexOf(".");
        String tableName = dataClass.getClass().getName().substring(index+1);

        List<Object> list = new ArrayList<Object>();

        Cursor cursor =  db.rawQuery("select * from "+ tableName,null);

        if(cursor.moveToFirst()){
            do{


            }while(cursor.moveToNext());

        }
        return null;

    }


    /**
     *  通过反射获得JavaBean的数据
     *  返回的数据格式：fieldName1:数据；fieldName1：数据；...
     * @param dataClass
     */
    public static String getDataByReflect(Object dataClass)throws Exception{
        Field[] field = dataClass.getClass().getDeclaredFields(); //获得实体类所有的field属性
        String content = "";
        for(int i=0;i<field.length;i++){
            String nativeName = field[i].getName();
            String name = nativeName.substring(0,1).toUpperCase() + nativeName.substring(1); //获得field的名称，并将其首字母大写
            String type = field[i].getGenericType().toString();   //获得field的类型


            if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class
                // "，后面跟类名
                Method m = dataClass.getClass().getMethod("get"+name);
                String value = (String) m.invoke(dataClass); // 调用getter方法获取属性值
                if (value != null) {
                    content += nativeName +":"+ value +";";
                }
            }
            if (type.equals("int")) {
                Method m = dataClass.getClass().getMethod("get" + name);
                Integer value = (Integer) m.invoke(dataClass);
                if (value != null) {
                    content +=nativeName+":"+ value +";";
                }
            }
        }
        return content;
    }
}
