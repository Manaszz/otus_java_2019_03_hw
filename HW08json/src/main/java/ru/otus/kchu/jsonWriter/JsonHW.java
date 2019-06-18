package ru.otus.kchu.jsonWriter;

import com.google.gson.Gson;

import java.util.*;


public class JsonHW {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        Gson gson = new Gson();
//        BagOfPrimitives obj = new BagOfPrimitives(22, "test", 10);
        String[] strArr= {"Winter", "Spring", "Summer", "Autumn"};
        int[] intArr= {50,60};
        List <Integer>tmpList = new ArrayList( Arrays.asList(1,2,3));
        Person[] objArr= {new Person("john",12), new Person("smith",90)};
        BagOfPrimitives obj = new BagOfPrimitives(22, "test", 10,intArr, strArr,objArr,tmpList);

        String json = gson.toJson(obj);
        String myJson = MyJsonWriter.toJson(obj);
        System.out.println("------------------------------------------");
        System.out.println("1.Obj:"+obj);
        System.out.println("2.Gson:"+json);


        BagOfPrimitives obj2 = gson.fromJson(myJson, BagOfPrimitives.class);
        System.out.println("3.ObjfromJson:"+obj2);
        System.out.println("equals:" + obj.equals(obj2));
    }

    static class BagOfPrimitives {
        private final int value1;
        private final String value2;
        private final int value3;
        public static boolean isSmth;
        int [] val4IntArr;
        String [] val5StrArr;
        Person[] val6ObjArr;
        List <Integer> val7List = new ArrayList<>();
        Map <String,Integer> val8Map = new HashMap();



        BagOfPrimitives(int value1, String value2, int value3, int[]v4Arr, String[] v5Arr, Person[] val6ObjArr, List valList ) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
            this.val4IntArr = v4Arr;
            this.val5StrArr = v5Arr;
            this.val6ObjArr = val6ObjArr;
            this.val7List =   valList;
            this.val8Map.put("zzz",11);
            this.val8Map.put("yyy",22);
            isSmth = false;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BagOfPrimitives that = (BagOfPrimitives) o;

            if (value1 != that.value1) return false;
            if (value3 != that.value3) return false;
            return Objects.equals(value2, that.value2);
        }

        @Override
        public String toString() {
            return "BagOfPrimitives{" +
                    "isSmth=" + isSmth +
                    ", value1=" + value1 +
                    ", value2='" + value2 + '\'' +
                    ", value3=" + value3 +
                    ", val4IntArr=" + Arrays.toString(val4IntArr) +
                    ", val5StrArr=" + Arrays.toString(val5StrArr) +
                    ", val6ObjArr=" + Arrays.toString(val6ObjArr) +
                    ", val7List=" + val7List +
                    ", val8Map=" + val8Map +
                    '}';
        }
    }
}
