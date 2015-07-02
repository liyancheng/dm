package com.tbc.framework.dm.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Ztian
 * Date: 13-2-21
 * Time: 下午1:59
 * To change this template use File | Settings | File Templates.
 */
public class Page<T> {
    private long total = -1;
    private int size = 15;
    private int index = 1;
    private Date timestamp;

    private List<T> data;

    public Page() {
    }

    public Page(Page<T> p) {
        if (p == null) {
            return;
        }

        this.total = p.total;
        this.size = p.size;
        this.index = p.index;
        this.timestamp = p.timestamp;
    }

    public Page(int size) {
        this.size = size;
    }


    public boolean isFirst() {
        return index == 1;
    }

    public boolean isLast() {
        return index == getCount();
    }

    public List<Integer> nearPages() {
        int count = 7;
        int before = count / 2;
        int start = index > before ? index - before : 1;
        int after = count - (index - start) - 1;
        int end = getCount() - index > after ? index + after : (int) getCount();
        if (end - start < count && end == getCount()) {
            int expectedStart = end - count + 1;
            start = expectedStart >= 1 ? expectedStart : 1;
        }

        List<Integer> nearPages = new ArrayList<Integer>(count);
        for (int i = start; i <= end; i++) {
            nearPages.add(i);
        }


        return nearPages;
    }

    public boolean hasNext() {
        return index < getCount();
    }

    public boolean isTotalSet() {
        return total >= 0;
    }

    /**
     * 第一条记录的位置。用户offset的参数。
     *
     * @return 起始位置
     */
    public int getStart() {
        return size * (index - 1);
    }

    public int next() {
        if (hasNext()) {
            return index + 1;
        }
        data = null;
        return index;
    }


    public void first() {
        index = 1;
        timestamp = null;
        data = null;
    }

    public int previous() {
        if (index > 1) {
            return index - 1;
        }
        data = null;
        return index;
    }

    /**
     * 计算总共有多少页。
     *
     * @return 总页数
     */
    public long getCount() {
        return total % size != 0 ? total / size + 1 : total / size;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        if (total < 0) {
            total = 0;
        }
        this.total = total;
    }

    /**
     * 获取每页的大小。
     *
     * @return
     */
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        if (size < 1) {
            size = 1;
        }
        this.size = size;
    }

    /**
     * 获取当前页中实际数据的条数。
     *
     * @return 数据条数。
     */
    public int getNumber() {
        return data == null ? 0 : data.size();
    }

    /**
     * 获取当前页码
     *
     * @return
     */
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        if (index < 0) {
            index = 0;
        }

        this.index = index;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public boolean hasTotal() {
        return total != -1;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
