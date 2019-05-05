import ru.otus.generics.bounds.entries.Animal;
import ru.otus.generics.bounds.entries.Cat;
import ru.otus.generics.bounds.entries.DIYarrayList;
import ru.otus.generics.bounds.entries.HomeCat;

import java.util.*;

  public class MyArrList  {
    ArrayList alist = new ArrayList();

    static <T> void printList(String  msg, List<T> list){
        System.out.println();
        System.out.println(msg+":");
        int i =1;
        for ( T el: list ){ System.out.println( (i++)+"."+el); }
    }

    public static void main(String[] args) {

        Cat[] animalArr = new Cat[]{new HomeCat("Мурзик"), new HomeCat("Васька")};

        DIYarrayList <Cat> dlist = new DIYarrayList<>();
        DIYarrayList <Cat>  dListSrc = new DIYarrayList<>(2);

        Comparator<Cat> comparator = (o1, o2) ->{ int x= o1.getId(), y = o2.getId(); return  (x < y) ? -1 : ((x == y) ? 0 : 1);};



        dListSrc.add(new HomeCat("uasya"));
        dListSrc.add(new Cat());
        for (int i = 0; i < 20; i++) {
            dListSrc.add(new HomeCat("hcSrc"+i));
        }


        dlist.add(new Cat());
        for (int i = 0; i < 30; i++) {
            dlist.add(new HomeCat("CatL"+i));
        }

        java.util.Collections.addAll(dlist,new HomeCat("cal1"), new HomeCat("cal2"),new HomeCat("cal3"));
        printList("List afer addAll",dlist);

        java.util.Collections.copy(dlist,dListSrc);
        printList("List afer Copy",dlist);

        java.util.Collections.sort(dlist,comparator);
        printList("List afer Sort",dlist);


    }

}
