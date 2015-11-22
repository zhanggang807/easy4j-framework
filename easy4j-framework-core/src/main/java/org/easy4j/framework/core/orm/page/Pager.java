package org.easy4j.framework.core.orm.page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author: liuyong
 * @since 1.0
 */
public class Pager<E> extends ArrayList<E> implements List<E> {

    private int pageNumber ;

    private int total ;

    private int pageSize ;

    public Pager(Collection<? extends E> c,int pageNumber , int pageSize ,int total ){
        super(c);
        this.pageNumber = pageNumber ;
        this.pageSize = pageSize ;
        this.total = total ;
    }

    public boolean isFirst(){
        return pageNumber == 0 ;
    }

    public boolean isLast(){
        return pageNumber == total / pageSize ;
    }

    public int getStartRowNumber(){
        return (pageNumber - 1) * pageSize + 1;
    }

    public int getEndRowNumber(){
        return (pageNumber - 1) * pageSize + size()   ;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage(){
        return total / pageSize ;
    }

    public int getDisplayStartPage(int midNumber){
        return pageNumber > midNumber ? pageNumber - midNumber : 1 ;
    }

    public int getDisplayEndPage(int midNumber){

        int end = midNumber * 2 ;

        if(getTotalPage() < end){
            return getTotalPage() ;
        } else if (pageNumber < midNumber ) {
            return  end ;
        }  else {
            return pageNumber + 3 ;
        }
    }
}
