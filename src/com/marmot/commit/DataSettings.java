package com.marmot.commit;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 数据常量
 *
 * @author zhuxiang
 * @date 2021/9/9 10:28
 */
public class DataSettings {

    private static final Map<String, String> COMMIT_TYPE_MAP = initMap();

    private static Map<String, String> initMap() {
        Map<String, String> map = new LinkedHashMap<>(16);
        map.put("新增功能", "新的功能、新的需求");
        map.put("Bug修复", "修复代码Bug(测试、验收等阶段的Bug)");
        map.put("文档修改", "仅文档更改(代码注释、README等)");
        map.put("样式修改", "不影响代码功能的修改 (CSS样式、代码格式化等)");
        map.put("代码重构", "既不修复错误也不添加功能的代码更改");
        map.put("性能优化", "提高性能的代码更改");
        map.put("测试代码", "添加缺失的测试或更正现有的测试");
        map.put("编译代码", "影响构建系统或外部依赖项的更改(示例：yarn、npm)");
        map.put("持续集成", "对 CI 配置文件和脚本的更改(示例：Jenkinsfile、gitlab-ci、Dockerfile)");
        map.put("代码回退", "恢复之前的提交，代码回滚");
        map.put("其他更改", "不修改代码或测试文件的其他更改");
        return map;
    }

    public static Map<String, String> getCommitTypeMap() {
        return COMMIT_TYPE_MAP;
    }

    public static String getValue(String key){
        return COMMIT_TYPE_MAP.get(key);
    }
}
