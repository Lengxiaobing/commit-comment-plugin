package com.marmot.commit;

import com.intellij.dvcs.repo.Repository;
import com.intellij.dvcs.repo.VcsRepositoryManager;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * @author Damien Arrachequesne
 */
public class CommitPanel {
    private final static String SEPARATOR = "-";

    private JPanel mainPanel;
    private JComboBox<String> changeScope;
    private JTextField shortDescription;
    private JTextArea longDescription;
    private JTextArea breakingChanges;
    private JTextField closedIssues;
    private JCheckBox wrapTextCheckBox;
    private JCheckBox skipCICheckBox;
    private JComboBox<String> commitType;

    CommitPanel(Project project, CommitMessage commitMessage) {
        //获取当前分支名
        Collection<Repository> repositories = VcsRepositoryManager.getInstance(project).getRepositories();
        for (Repository repository : repositories) {
            String branchName = repository.getCurrentBranchName();
            changeScope.addItem(branchName);
        }

        Map<String, String> typeMap = DataSettings.getCommitTypeMap();
        for (Map.Entry<String, String> entry : typeMap.entrySet()) {
            commitType.addItem(entry.getKey() + SEPARATOR + entry.getValue());
        }

        if (commitMessage != null) {
            restoreValuesFromParsedCommitMessage(commitMessage);
        }
    }

    JPanel getMainPanel() {
        return mainPanel;
    }

    CommitMessage getCommitMessage() {
        return new CommitMessage(
                getSelectedChangeType(),
                (String) changeScope.getSelectedItem(),
                shortDescription.getText().trim(),
                longDescription.getText().trim(),
                breakingChanges.getText().trim(),
                closedIssues.getText().trim(),
                wrapTextCheckBox.isSelected(),
                skipCICheckBox.isSelected()
        );
    }

    private String getSelectedChangeType() {
        Object item = commitType.getSelectedItem();
        if (Objects.isNull(item)) {
            return null;
        }

        return item.toString().split(SEPARATOR)[0];
    }

    private void restoreValuesFromParsedCommitMessage(CommitMessage commitMessage) {
        String key = commitMessage.getChangeType();
        String value = DataSettings.getValue(commitMessage.getChangeType());
        if (Objects.nonNull(key) && Objects.nonNull(value)) {
            commitType.setSelectedItem(key + SEPARATOR + value);
        }
        changeScope.setSelectedItem(commitMessage.getChangeScope());
        shortDescription.setText(commitMessage.getShortDescription());
        longDescription.setText(commitMessage.getLongDescription());
        breakingChanges.setText(commitMessage.getBreakingChanges());
        closedIssues.setText(commitMessage.getClosedIssues());
        skipCICheckBox.setSelected(commitMessage.isSkipCI());
    }
}
