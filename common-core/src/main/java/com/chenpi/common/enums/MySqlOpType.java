package com.chenpi.common.enums;

/*
| `op` 值 | 含义 | 对应 SQL 操作 | 说明 |
|--------|------|---------------|------|
| "c"  | Create | `INSERT` | 插入新记录 |
| "u"  | Update | `UPDATE` | 更新已有记录 |
| "d"  | Delete | `DELETE` | 删除记录 |
| "r"  | Read   | `SELECT`（初始快照） | 仅在 快照（snapshot）阶段 出现，表示读取现有数据用于初始化状态 |

 */
public enum MySqlOpType {
    c,
    u,
    r,
    d,
    ;

    public static boolean isDelete(String type) {
        if (type == null) {
            return false;
        }
        return d.name().equals(type);
    }

    public static boolean isNewData(String type) {
        if (type == null) {
            return false;
        }
        return c.name().equals(type) || r.name().equals(type);
    }

    public static boolean isUpdate(String type) {
        if (type == null) {
            return false;
        }
        return u.name().equals(type);
    }
}
