package com.marmot.commit;

/**
 * From https://github.com/commitizen/conventional-commit-types
 *
 * @author Damien Arrachequesne
 */
public enum ChangeType {

    FEAT("新增功能", "新的功能、新的需求"),
    FIX("Bug修复", "修复Bug (现网发散Bug、测试阶段的Bug、验收阶段的Bug)"),
    DOCS("文档修改", "仅文档更改(代码注释、README等)"),
    STYLE("样式修改", "不影响代码功能的修改 (CSS样式、代码格式化等)"),
    REFACTOR("代码重构", "既不修复错误也不添加功能的代码更改"),
    PERF("性能优化", "提高性能的代码更改"),
    TEST("测试代码", "添加缺失的测试或更正现有的测试"),
    BUILD("编译代码", "影响构建系统或外部依赖项的更改（示例范围：gulp、broccoli、npm）"),
    CI("持续集成", "对 CI 配置文件和脚本的更改（示例范围：Travis、Circle、BrowserStack、SauceLabs）"),
    REVERT("代码回退", "恢复之前的提交"),
    CHORE("其他更改", "不修改 src 或测试文件的其他更改");

    public final String title;
    public final String description;

    ChangeType(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String label() {
        return this.name().toLowerCase();
    }

    @Override
    public String toString() {
        return String.format("%s - %s", this.label(), this.description);
    }
}
