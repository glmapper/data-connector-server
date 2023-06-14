package com.idata.connectors.mysql.page;

/**
 * <p>
 * RowBounds
 * </p>
 *
 * @author mailto:glmapper_2018@163.com glmapper
 * @date 2023/6/13 2:32 PM
 * @since 1.0
 */
public class RowBounds {

    public static int page_size = 1000;

    private final int total;

    public RowBounds(int total) {
        this.total = total;
    }

    public int getOffset(int currentPage) {
        return (currentPage - 1) * page_size;
    }

    public int getTotalPage() {
        return (this.total + page_size - 1) / page_size;
    }
}
