package io.unthrottled.amii.config.ui;

import com.intellij.ide.util.BrowseFilesListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.BrowseFolderRunnable;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.ui.components.fields.ExtendableTextField;
import io.unthrottled.doki.promotions.MessageBundle;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CustomAssetDirectoryChooser extends DialogWrapper {
  private JPanel contentPane;
  private JTextField textField1;

  public CustomAssetDirectoryChooser(Project project, String path) {
    super(project, true);

    setTitle(MessageBundle.message("settings.general.content.custom.sticker.modal.title"));
    textField1.setText(path);

    init();
    pack();
  }


  @Override
  protected @Nullable JComponent createCenterPanel() {
    return contentPane;
  }

  private void createUIComponents() {
    ExtendableTextField extendableTextField = new ExtendableTextField();
    textField1 = extendableTextField.addBrowseExtension(
      new BrowseFolderRunnable<>(
        MessageBundle.message("settings.general.content.custom.sticker.modal.chooser.title"),
        MessageBundle.message("settings.general.content.custom.sticker.modal.chooser.description"),
        null,
        BrowseFilesListener.SINGLE_DIRECTORY_DESCRIPTOR,
        extendableTextField,
        TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT
      ), null
    );
  }

  public String getPath() {
    return textField1.getText();
  }
}
