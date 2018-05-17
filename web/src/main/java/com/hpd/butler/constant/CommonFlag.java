package com.hpd.butler.constant;

/**
 * Created by zy on 7/15/2017.
 */
public enum CommonFlag {
    INVALID(0, "无效"),
    VALID(1, "有效"),
    DELETED(99, "删除");

    private Integer value;
    private String label;

    CommonFlag(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    public Integer getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public static CommonFlag get(String value) {
        CommonFlag[] types = CommonFlag.values();

        for (CommonFlag type : types) {
            if (value.equals(type.getValue())) {
                return type;
            }
        }
        return null;
    }
}
