package ru.otus.dbservice;

import ru.otus.dao.Id;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class SqlScripture {
  private String tableName;
  private String fldNamesStr ="";
  private List<Field> fldsList = new ArrayList<>();
  private String parmsDummy="";
  private Field idFld;
  private String insertQry;
  private String selectQry;
  private String updateQry;

    public  <T> SqlScripture(Class  clazz) {
       tableName = clazz.getSimpleName();
       idFld =checkId(clazz);

       String updSQL ="update "+tableName+" set ";

       for (Field f : clazz.getDeclaredFields()) {
          f.setAccessible(true);
          this.fldNamesStr += ("," + f.getName());
          this.parmsDummy+=",?";
          this.fldsList.add(f);
          updSQL = updSQL.concat(f.getName() + "= ?,");
          f.setAccessible(false);
      }
       fldNamesStr = fldNamesStr.substring(1);
       parmsDummy = parmsDummy.substring(1);

        selectQry = "select "+ fldNamesStr +" from "+ tableName+" where "+idFld.getName()+"  = ?";
        insertQry =  "insert into "+ tableName+"("+ fldNamesStr +") values ("+parmsDummy+")";
        updateQry = updSQL.substring(0,updSQL.length()-1)+" where "+idFld.getName()+" = ?" ;
  }

   String getInsertQuery(){ return  this.insertQry;        }
  String getSelectQuery(){ return this.selectQry; }
  String getUpdateQuery(){ return updateQry;}
  String getCountQuery(){
      return "select count(*) from "+ tableName +"  where "+idFld.getName()+"  = ?";
  }

    public Field getIdFld() {
        return idFld;
    }

    public List<Field> getFldsList() {
        return fldsList;
    }

    private Field checkId(Class clazz) {

        for (Field field: clazz.getDeclaredFields()){
            field.setAccessible(true);
            if(field.isAnnotationPresent(Id.class)) {
                return  field;
            }
            field.setAccessible(false);
        }
        return  null;
    }
}
