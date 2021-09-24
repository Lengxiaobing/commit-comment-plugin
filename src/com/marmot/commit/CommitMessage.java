package com.marmot.commit;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * @author Damien Arrachequesne <damien.arrachequesne@gmail.com>
 */
class CommitMessage {
    private static final int MAX_LINE_LENGTH = 72; // https://stackoverflow.com/a/2120040/5138796

    public static final Pattern COMMIT_FIRST_LINE_FORMAT = Pattern.compile("^(\\[(.+)])(\\((.+)\\))?: (.+)");
    public static final Pattern COMMIT_CLOSES_FORMAT = Pattern.compile("关闭问题: (.+)");

    private String changeType;
    private String changeScope, shortDescription, longDescription, breakingChanges, closedIssues;
    private boolean wrapText = true;
    private boolean skipCI = false;

    private CommitMessage() {
        this.longDescription = "";
        this.breakingChanges = "";
        this.closedIssues = "";
    }

    public CommitMessage(String changeType, String changeScope, String shortDescription, String longDescription,
                         String breakingChanges, String closedIssues, boolean wrapText, boolean skipCI) {
        this.changeType = changeType;
        this.changeScope = changeScope;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.breakingChanges = breakingChanges;
        this.closedIssues = closedIssues;
        this.wrapText = wrapText;
        this.skipCI = skipCI;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append(changeType);
        builder.append("]");
        if (isNotBlank(changeScope)) {
            builder
                    .append('(')
                    .append(changeScope)
                    .append(')');
        }
        builder
                .append(": ")
                .append(shortDescription);

        if (isNotBlank(longDescription)) {
            builder
                    .append(System.lineSeparator())
                    .append(System.lineSeparator())
                    .append(wrapText ? WordUtils.wrap(longDescription, MAX_LINE_LENGTH) : longDescription);
        }

        if (isNotBlank(breakingChanges)) {
            String content = "重大改进: " + breakingChanges;
            builder
                    .append(System.lineSeparator())
                    .append(System.lineSeparator())
                    .append(wrapText ? WordUtils.wrap(content, MAX_LINE_LENGTH) : content);
        }

        if (isNotBlank(closedIssues)) {
            builder.append(System.lineSeparator());
            for (String closedIssue : closedIssues.split(",")) {
                builder
                        .append(System.lineSeparator())
                        .append("关闭问题: ")
                        .append(formatClosedIssue(closedIssue));
            }
        }

        if (skipCI) {
            builder
                    .append(System.lineSeparator())
                    .append(System.lineSeparator())
                    .append("[skip ci]");
        }

        return builder.toString();
    }

    private String formatClosedIssue(String closedIssue) {
        String trimmed = closedIssue.trim();
        return (StringUtils.isNumeric(trimmed) ? "#" : "") + trimmed;
    }

    public static CommitMessage parse(String message) {
        CommitMessage commitMessage = new CommitMessage();

        try {
            Matcher matcher = COMMIT_FIRST_LINE_FORMAT.matcher(message);
            if (!matcher.find()) {
                return commitMessage;
            }

            commitMessage.changeType = matcher.group(1).replace("[","").replace("]","");
            commitMessage.changeScope = matcher.group(3).replace("(","").replace(")","");
            commitMessage.shortDescription = matcher.group(5);

            String[] strings = message.split("\n");
            if (strings.length < 2) {
                return commitMessage;
            }

            int pos = 1;
            StringBuilder stringBuilder;

            stringBuilder = new StringBuilder();
            for (; pos < strings.length; pos++) {
                String lineString = strings[pos];
                if (lineString.startsWith("重大改进") || lineString.startsWith("关闭问题") || lineString.equalsIgnoreCase("[skip ci]")) {
                    break;
                }
                stringBuilder.append(lineString).append('\n');
            }
            commitMessage.longDescription = stringBuilder.toString().trim();

            stringBuilder = new StringBuilder();
            for (; pos < strings.length; pos++) {
                String lineString = strings[pos];
                if (lineString.startsWith("关闭问题") || lineString.equalsIgnoreCase("[skip ci]")) {
                    break;
                }
                stringBuilder.append(lineString).append('\n');
            }
            commitMessage.breakingChanges = stringBuilder.toString().trim().replace("重大改进: ", "");

            matcher = COMMIT_CLOSES_FORMAT.matcher(message);
            stringBuilder = new StringBuilder();
            while (matcher.find()) {
                stringBuilder.append(matcher.group(1)).append(',');
            }
            if (stringBuilder.length() > 0) {
                stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
            }
            commitMessage.closedIssues = stringBuilder.toString();

            commitMessage.skipCI = message.contains("[skip ci]");
        } catch (RuntimeException e) {
        }

        return commitMessage;
    }

    public String getChangeType() {
        return changeType;
    }

    public String getChangeScope() {
        return changeScope;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public String getBreakingChanges() {
        return breakingChanges;
    }

    public String getClosedIssues() {
        return closedIssues;
    }

    public boolean isSkipCI() {
        return skipCI;
    }
}